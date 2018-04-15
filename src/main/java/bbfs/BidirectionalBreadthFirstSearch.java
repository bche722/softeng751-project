package bbfs;

import apt.annotations.Future;
import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import interfaces.Algorithm;
import pu.RedLib.Reducible;
import pu.RedLib.Reduction;
import pu.pi.ParIterator;
import pu.pi.ParIteratorFactory;
import utils.CostComparatorForVertices;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An implementation of bidirectional breadth first search with heuristic h(x) = 0.
 * Essentially a bidirectional dijkstra.
 * This implementation is tailored to handle single source and single sink DAG only.
 * @param <V> extends Vertex
 * @param <E> extends DirectedEdge<Vertex>
 */
public class BidirectionalBreadthFirstSearch<V extends Vertex, E extends DirectedEdge<V>> extends Algorithm<Vertex, DirectedEdge<Vertex>> {

    private BasicDirectedGraph<V, E> graph;

    // record the last step from source, the key is the CHILD, and the value is the PARENT, topological order:
    // sourceFrontier -> sourceClose, it is prepared for route tracing at the end if displaying the full path is
    // required.
    private ConcurrentHashMap<V, V> routeMapFromSource = new ConcurrentHashMap<>();

    // record the last step from sink, the key is the PARENT, and the value is the CHILD, topological order:
    // sinkFrontier -> sinkClose, it is prepared for route tracing at the end if displaying the full path is
    // required.
    private ConcurrentHashMap<V, V> routeMapFromSink = new ConcurrentHashMap<>();

    // record the cost for each node (each sub path FROM SOURCE) later on
    private ConcurrentHashMap<V, Integer> sourceRouteCost = new ConcurrentHashMap<>();

    // record the cost for each node (each sub path FROM SINK) later on
    private ConcurrentHashMap<V, Integer> sinkRouteCost = new ConcurrentHashMap<>();

    private AtomicInteger loopCounter = new AtomicInteger();
    private V source;
    private V sink;



    public BidirectionalBreadthFirstSearch(BasicDirectedGraph<V, E> graph, boolean isParallel) {
        this.graph = graph;
        this.isParallel = isParallel;
    }

    @Override
    public void doTheJob() {

        V[] source_and_sink = (V[]) new Vertex[]{null, null};
        graph.sources().forEach(v -> source_and_sink[0] = v);
        graph.sinks().forEach(v -> source_and_sink[1] = v);
        source = source_and_sink[0];
        sink = source_and_sink[1];

        if (isParallel){

            parallelSearch();

        } else {

            sequentialSearch();

        }

        System.out.println("The process took: " + loopCounter.get() + " iterations");

    }

    private void sequentialSearch(){

        CostComparatorForVertices<V, E> sourceComparator = new CostComparatorForVertices<>(sourceRouteCost);
        CostComparatorForVertices<V, E> sinkComparator = new CostComparatorForVertices<>(sinkRouteCost);

        ArrayList<V> sourceFrontier = new ArrayList<>();
        ArrayList<V> sinkFrontier = new ArrayList<>();

        HashSet<V> sourceClose = new HashSet<>();
        HashSet<V> sinkClose = new HashSet<>();

        // preparation
        graph.vertices().forEach(v -> {

            prepareMaps(v);
            sourceFrontier.add(v);
            sinkFrontier.add(v);

        });

        //System.out.println("is integer max equals? " + (Integer.MAX_VALUE == Integer.MAX_VALUE));
        int[] leastCostPathSoFar = {Integer.MAX_VALUE};

        // initialize dumps (heap tops) for source frontier and sink frontier, respectively
        int[] dump = new int[]{0, 0};

        // start searching
        while (loopCounter.get() >= 0) {

            if (loopCounter.get()%2 == 0) {

                // source turn
                if (sourceFrontier.isEmpty()) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }
                sourceFrontier.sort(sourceComparator);
                V foo = sourceFrontier.get(0);
                sourceFrontier.remove(foo);

                graph.childrenIterator(foo).forEachRemaining(child -> {
                    int newCost = sourceRouteCost.get(foo) + child.weight() + graph.edgeBetween(foo, child).weight();

                    if (newCost < sourceRouteCost.get(child)) {
                        sourceRouteCost.replace(child, newCost);
                        routeMapFromSource.put(child, foo); // frontier -> close
                    }

                    // try to update the global least cost path so far
                    if (sinkRouteCost.get(child) != Integer.MAX_VALUE) {
                        int temp = newCost + sinkRouteCost.get(child) - child.weight();
                        if (temp < leastCostPathSoFar[0]) {
                            leastCostPathSoFar[0] = temp;
                        }
                    }
                });
                sourceClose.add(foo);
                dump[0] = sourceRouteCost.get(foo);

            } else {

                // sink turn
                if (sinkFrontier.isEmpty()) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }
                sinkFrontier.sort(sinkComparator);
                V bar = sinkFrontier.get(0);
                sinkFrontier.remove(bar);

                graph.parentsIterator(bar).forEachRemaining(parent -> {
                    int newCost = sinkRouteCost.get(bar) + parent.weight() + graph.edgeBetween(parent, bar).weight();
                    if (newCost < sinkRouteCost.get(parent)) {
                        sinkRouteCost.replace(parent, newCost);
                        routeMapFromSink.put(parent, bar); // frontier -> close
                    }

                    // try to update the global least cost path so far
                    if (sourceRouteCost.get(parent) != Integer.MAX_VALUE) {
                        int temp = newCost + sourceRouteCost.get(parent) - parent.weight();
                        if (temp < leastCostPathSoFar[0]) {
                            leastCostPathSoFar[0] = temp;
                        }
                    }
                });
                sinkClose.add(bar);
                dump[1] = sinkRouteCost.get(bar);

            }

            // stopping criterion 1 : check collision (keep it as a backup in case something gets screwed up later)
            /*
            ArrayList<V> collidingZone = new ArrayList<>(sourceClose);
            System.out.println("sourceClose size: " + sourceClose.size());
            System.out.println("sinkClose size: " + sinkClose.size());
            if (collidingZone.retainAll(sinkClose) && collidingZone.size() >= graph.verticesSet().size()/2){
                System.out.println("intersection size: " + collidingZone.size());
                System.out.println("testing collision");
                collidingZone.sort((x, y) -> {
                    int xPath = sourceRouteCost.get(x) + sinkRouteCost.get(x) - x.weight();
                    int yPath = sourceRouteCost.get(y) + sinkRouteCost.get(y) - y.weight();
                    return Integer.compare(xPath, yPath);
                });
                V leastCostMeetingPoint = collidingZone.get(0);
                System.out.println("shortest path from source to sink costs: " +
                        (sourceRouteCost.get(leastCostMeetingPoint) + sinkRouteCost.get(leastCostMeetingPoint) -
                                leastCostMeetingPoint.weight()) +
                        " units");
                break;
            }
            */

            // stopping criterion 2 : using a global least cost path (so far) and compare it with the sum of both heap
            // tops (dump[0] and dump[1]) in every iteration
            if (dump[0] != 0 && dump[1] != 0) {
                if (dump[0] + dump[1] >= leastCostPathSoFar[0]) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    // increment the counter, this is indeed not required by the loop semantic, it is purely for the
                    // purpose of statistics, because the parallel version still increments the counter in the very
                    // last iteration. hence, this extra increment in sequential version is just for the sake of
                    // simulating an identical behavior as in the parallel version.
                    loopCounter.addAndGet(1);
                    break;
                }
            }

            // increment the counter
            loopCounter.addAndGet(1);

        }
    }

    private void prepareMaps(V v) {
        if (v.name().equals(source.name())) {

            sourceRouteCost.put(v, v.weight());
            sinkRouteCost.put(v, Integer.MAX_VALUE);

        } else if (v.name().equals(sink.name())){

            sourceRouteCost.put(v, Integer.MAX_VALUE);
            sinkRouteCost.put(v, v.weight());

        } else {

            sourceRouteCost.put(v, Integer.MAX_VALUE);
            sinkRouteCost.put(v, Integer.MAX_VALUE);

        }
    }













    // below is the parallel version


    private Reducible<Integer> sourceLocalMin = new Reducible<>(Integer.MAX_VALUE);
    private Reducible<Integer> sinkLocalMin = new Reducible<>(Integer.MAX_VALUE);

    private Void populateDataStructures(ArrayList<V> sourceFrontier, ArrayList<V> sinkFrontier, V v){

        prepareMaps(v);
        sourceFrontier.add(v);
        sinkFrontier.add(v);
        return null;

    }

    /**
     * a sub-task representing the computation to update the child's cost as well as to calculate the local least cost
     * @param foo - V, the parent node
     * @param child - V, the child node
     * @return int - local least cost
     */
    private int sourceTask(V foo, V child) {

        int newCost = sourceRouteCost.get(foo) + child.weight() + graph.edgeBetween(foo, child).weight();

        // try to update the child's priority
        if (newCost < sourceRouteCost.get(child)) {
            sourceRouteCost.replace(child, newCost);
            routeMapFromSource.put(child, foo); // frontier -> close
        }

        // try to update the local least cost path so far
        int localLeastCost;
        if (sinkRouteCost.get(child) != Integer.MAX_VALUE) {
            localLeastCost = newCost + sinkRouteCost.get(child) - child.weight();
            if (localLeastCost < sourceLocalMin.get()) {
                sourceLocalMin.set(localLeastCost);
            }
        } else {
            localLeastCost = Integer.MAX_VALUE;
        }

        return localLeastCost; // just in case if manual reduction is needed (i.e., if Reducible bugs out)
    }

    /**
     * a sub-task representing the computation to update the child's cost as well as to calculate the local least cost
     * @param bar - V, the parent node
     * @param parent - V, the child node
     * @return int - local least cost
     */
    private int sinkTask(V bar, V parent) {

        int newCost = sinkRouteCost.get(bar) + parent.weight() + graph.edgeBetween(parent, bar).weight();
        if (newCost < sinkRouteCost.get(parent)) {
            sinkRouteCost.replace(parent, newCost);
            routeMapFromSink.put(parent, bar); // frontier -> close
        }

        // try to update the local least cost path so far
        int localLeastCost;
        if (sourceRouteCost.get(parent) != Integer.MAX_VALUE) {
            localLeastCost = newCost + sourceRouteCost.get(parent) - parent.weight();
            if (localLeastCost < sinkLocalMin.get()) {
                sinkLocalMin.set(localLeastCost);
            }
        } else {
            localLeastCost = Integer.MAX_VALUE;
        }

        return localLeastCost; // just in case if manual reduction is needed (i.e., if Reducible bugs out)
    }

    private void parallelSearch(){

        CostComparatorForVertices<V, E> sourceComparator = new CostComparatorForVertices<>(sourceRouteCost);
        CostComparatorForVertices<V, E> sinkComparator = new CostComparatorForVertices<>(sinkRouteCost);

        ArrayList<V> sourceFrontier = new ArrayList<>();
        ArrayList<V> sinkFrontier = new ArrayList<>();

        // every insertion and removal of element must be synchronized
        HashSet<V> sourceClose = new HashSet<>(); // avoid duplicates induced by concurrent access
        HashSet<V> sinkClose = new HashSet<>(); // avoid duplicates induced by concurrent access

        // preparation
        @Future
        Void[] populatorPromises = new Void[graph.verticesSet().size()];
        Iterator<V> vertexIterator = graph.vertices().iterator();
        for (int i = 0; i < graph.verticesSet().size(); i++) {
            populatorPromises[i] = populateDataStructures(sourceFrontier, sinkFrontier, vertexIterator.next());
        }

        //System.out.println("is integer max equals? " + (Integer.MAX_VALUE == Integer.MAX_VALUE));
        int[] leastCostPathSoFar = {Integer.MAX_VALUE};

        // initialize dumps (heap tops) for source frontier and sink frontier, respectively
        int[] dump = new int[]{0, 0};

        // explicit barrier for populatorPromises, make sure every data structure is ready
        while (populatorPromises[0] != null) {}; // busy wait

        // start searching - using stopping criterion 2
        while (!(dump[0] != 0 && dump[1] != 0 && dump[0] + dump[1] >= leastCostPathSoFar[0])) {

            // outer loop has a strong inter-loop dependence, due to frontiers

            // inner loops depends on outer loop iterations, but there is no intra-loop dependence among them

            // parallelizing inner loops can prevent the application suffering from those huge branching factors

            if (loopCounter.get()%2 == 0) {

                // source turn
                if (sourceFrontier.isEmpty()) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }
                sourceFrontier.sort(sourceComparator);
                V foo = sourceFrontier.get(0);
                sourceFrontier.remove(foo);

                ArrayList<V> childrenArray = graph.getChilds(foo);
                int size = childrenArray.size();
                @Future
                int[] costPromises = new int[size];
                // every usage of Iterator/Iterable must be switched to ParIterator
                ParIterator<V> childrenIteratorPar = ParIteratorFactory.createParIterator(childrenArray,
                        Runtime.getRuntime().availableProcessors());
                for (int i = 0; i < size; i++) {
                    costPromises[i] = sourceTask(foo, childrenIteratorPar.next());
                }
                // explicit barrier to make sure all promises have been resolved
                int temp;
                if (costPromises.length != 0) {
                    temp = costPromises[0];
                }
                // perform the reduction
                temp = sourceLocalMin.reduce(Reduction.IntegerMIN);

                // try to update the GLOBAL least cost path so far
                if (temp < leastCostPathSoFar[0]) {
                    leastCostPathSoFar[0] = temp;
                }

                sourceClose.add(foo);
                dump[0] = sourceRouteCost.get(foo);

                // reinitialize sourceLocalMin because it should be scoped to each inner iteration
                sourceLocalMin = new Reducible<>(Integer.MAX_VALUE);


            } else {

                // sink turn
                if (sinkFrontier.isEmpty()) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }
                sinkFrontier.sort(sinkComparator);
                V bar = sinkFrontier.get(0);
                sinkFrontier.remove(bar);

                ArrayList<V> parentsArray = graph.getParents(bar);
                int size = parentsArray.size();
                @Future
                int[] costPromises = new int[size];
                // every usage of Iterator/Iterable must be switched to ParIterator
                ParIterator<V> parentsIteratorPar = ParIteratorFactory.createParIterator(parentsArray, Runtime
                        .getRuntime().availableProcessors());
                for (int i = 0; i < size; i++) {
                    costPromises[i] = sinkTask(bar, parentsIteratorPar.next());
                }
                // explicit barrier to make sure all promises have been resolved
                int temp;
                if (costPromises.length != 0) {
                    temp = costPromises[0];
                }
                // perform the reduction
                temp = sinkLocalMin.reduce(Reduction.IntegerMIN);

                // try to update the GLOBAL least cost path so far
                if (temp < leastCostPathSoFar[0]) {
                    leastCostPathSoFar[0] = temp;
                }

                sinkClose.add(bar);
                dump[1] = sinkRouteCost.get(bar);

                // reinitialize sinkLocalMin because it should be scoped to each inner iteration
                sinkLocalMin = new Reducible<>(Integer.MAX_VALUE);


            }

            // increment the counter
            loopCounter.addAndGet(1);

        }

        System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] + " units");

    }
}