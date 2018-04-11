package bbfs;

import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import interfaces.Algorithm;
import utils.CostComparatorForVertices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
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
        /*
        graph.vertices().forEach(vertex -> {
            System.out.println(vertex.name() + " : " + vertex.weight());
            graph.adjacentVerticesIterator(vertex).forEachRemaining(vertex1 -> System.out.println
                    ("adjacent: "+vertex1.name()));
            graph.childrenIterator(vertex).forEachRemaining(vertex2 -> System.out.println
                    ("child node: " + vertex2.name()));
            graph.parents(vertex).forEach(vertex3 ->System.out.println
                    ("parent node: " + vertex3.name()));
            graph.outEdges(vertex).forEach(edge -> System.out.println
                    ("outgoing edges: "+edge.name()));
        });
        graph.sources().forEach(v -> System.out.println("Source:" + v.name()));
        graph.sinks().forEach(v -> System.out.println("Sink:" + v.name()));
        graph.edges().forEach(edge -> System.out.println(edge.name() + " : " + edge.weight()));
        System.out.println("Parallel Mode: " + this.isParallel);
         */

        V[] source_and_sink = (V[]) new Vertex[]{null, null};
        graph.sources().forEach(v -> source_and_sink[0] = v);
        graph.sinks().forEach(v -> source_and_sink[1] = v);
        source = source_and_sink[0];
        sink = source_and_sink[1];

        //todo - algorithm implementation
        if (isParallel){

            //todo - parallel implementation goes here

        } else {

            //todo - sequential implementation goes here
            sequentialSearch();

        }
    }

    private void sequentialSearch(){

        PriorityQueue<V> sourceFrontier = new PriorityQueue<>(new CostComparatorForVertices<>(sourceRouteCost));
        PriorityQueue<V> sinkFrontier = new PriorityQueue<>(new CostComparatorForVertices<>(sinkRouteCost));

        ArrayList<V> sourceClose = new ArrayList<>();
        ArrayList<V> sinkClose = new ArrayList<>();

        // preparation
        graph.vertices().forEach(v -> {
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

                System.out.println("source turn");
                sourceFrontier.forEach(v -> System.out.print(v.name() + " " + sourceRouteCost.get(v) + "   "));
                System.out.println("");

                // source turn
                V foo = sourceFrontier.poll();
                if (foo == null) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }

                System.out.println("start with " + foo.name());

                graph.childrenIterator(foo).forEachRemaining(child -> {
                    int newCost = sourceRouteCost.get(foo) + child.weight() + graph.edgeBetween(foo, child).weight();

//                    System.out.println("child name: " + child.name());
//                    sourceRouteCost.keySet().forEach(key -> System.out.println("key name: " + key.name()));

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

                System.out.println("what is the least cost path so far? " + leastCostPathSoFar[0]);

            } else {

                System.out.println("sink turn");
                sinkFrontier.forEach(v -> System.out.print(v.name() + " : " + sinkRouteCost.get(v) + "   "));
                System.out.println("");

                // sink turn
                V bar = sinkFrontier.poll();
                if (bar == null) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }

                System.out.println("start with " + bar.name());

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

                System.out.println("what is the least cost path so far? " + leastCostPathSoFar[0]);
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

            System.out.println("dump0 -> " + dump[0] + " dump1 -> " +dump[1]);

            // stopping criterion 2 : using a global least cost path (so far) and compare it with the sum of both heap
            // tops (dump[0] and dump[1]) in every iteration
            if (dump[0] != 0 && dump[1] != 0) {
                System.out.println("checking criterion now");
                if (dump[0] + dump[1] >= leastCostPathSoFar[0]) {
                    System.out.println("shortest path from source to sink costs: " + leastCostPathSoFar[0] +
                            " units");
                    break;
                }
            }

            // increment the counter
            loopCounter.addAndGet(1);

        }
    }

    /**
     * Addition helper takes care of Integer.MAX_VALUE and null.
     * This method is specialized to calculate least cost path.
     * Do use it for normal additions.
     * @param myCost - int
     * @param edgeCost - int
     * @param yourCost - int
     * @param hisCost - int
     * @return int - the sum
     */
    private int costAdder(int myCost, int edgeCost, int yourCost, int hisCost){ // todo

        if (myCost == Integer.MAX_VALUE || edgeCost == Integer.MAX_VALUE || yourCost == Integer.MAX_VALUE
                || hisCost == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else {
            return myCost + edgeCost + yourCost + hisCost;
        }
    }


    private void parallelSearch(){
        //todo
    }
}