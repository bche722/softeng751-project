package bbfs;

import apt.annotations.Future;
import apt.annotations.InitParaTask;
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
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An implementation of bidirectional breadth first search with heuristic h(x) = 0.
 * Essentially a bidirectional dijkstra.
 * This implementation is tailored to handle single source and single sink DAG only.
 * @param <V> extends Vertex
 * @param <E> extends DirectedEdge<Vertex>
 */
public class BidirectionalBreadthFirstSearch<V extends Vertex, E extends DirectedEdge<V>> extends
        Algorithm<Vertex, DirectedEdge<Vertex>> {

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

        long start_time = System.nanoTime();

        if (isParallel){

            parallelSearch();

        } else {

            sequentialSearch();

            System.out.println("Process took : " + loopCounter.get() + " iterations");

        }

        long end_time = System.nanoTime();

        double difference = (end_time - start_time) / 1e6;

        System.out.println("Program finished in: " + difference + " milliseconds");

    }

    private void sequentialSearch(){

        CostComparatorForVertices<V> sourceComparator = new CostComparatorForVertices<>(sourceRouteCost);
        CostComparatorForVertices<V> sinkComparator = new CostComparatorForVertices<>(sinkRouteCost);

        ArrayList<V> sourceFrontier = new ArrayList<>();
        ArrayList<V> sinkFrontier = new ArrayList<>();

        HashSet<V> sourceClose = new HashSet<>();
        HashSet<V> sinkClose = new HashSet<>();

        // preparation
        long start_time = System.nanoTime();
        graph.vertices().forEach(v -> {

            prepareMaps(v);
            sourceFrontier.add(v);
            sinkFrontier.add(v);

        });
        long end_time = System.nanoTime();

        double difference = (end_time - start_time) / 1e6;

        System.out.println("Data preparation finished in: " + difference + " milliseconds");

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


    private Reducible<CostNamePair<V>> sourceLocalMin =
            new Reducible<>(new CostNamePair<>(Integer.MAX_VALUE, null, null, null));
    private Reducible<CostNamePair<V>> sinkLocalMin =
            new Reducible<>(new CostNamePair<>(Integer.MAX_VALUE, null, null, null));

    private Void populateDataStructures(ArrayList<V> sourceFrontier, ArrayList<V> sinkFrontier, V v){

        prepareMaps(v);
        sourceFrontier.add(v);
        sinkFrontier.add(v);
        return null;

    }

    @InitParaTask
    private void parallelSearch(){

        // make sure both side will have equal number of tasks to be paired up
        int numOfProcessors = Runtime.getRuntime().availableProcessors();
        int maxNumOfSourceTasks = numOfProcessors/2;
        int maxNumOfSinkTasks = numOfProcessors - maxNumOfSourceTasks;

        int numOfSourceBranches = graph.getChilds(source).size();
        int numOfSinkBranches = graph.getParents(sink).size();

        int minNumOfTasksFromSource;
        if (numOfSourceBranches < maxNumOfSourceTasks) {
            minNumOfTasksFromSource = numOfSourceBranches;
        } else {
            minNumOfTasksFromSource = maxNumOfSourceTasks;
        }
        int minNumOfTasksFromSink;
        if (numOfSinkBranches < maxNumOfSinkTasks) {
            minNumOfTasksFromSink = numOfSinkBranches;
        } else {
            minNumOfTasksFromSink = maxNumOfSinkTasks;
        }
        int finalNumOfTasksForBothSide;
        if (minNumOfTasksFromSource < minNumOfTasksFromSink) {
            finalNumOfTasksForBothSide = minNumOfTasksFromSource;
        } else {
            finalNumOfTasksForBothSide = minNumOfTasksFromSink;
        }
        finalNumOfTasksForBothSide = 1; // todo - ensure a strict two-cores mode for testing purpose

        System.out.println("finalNumOfTasksForBothSide : " + finalNumOfTasksForBothSide);

        // initialize tasks for both side
        ArrayList<SourceTask<V, E>> sourceTasks = new ArrayList<>();
        ArrayList<SinkTask<V, E>> sinkTasks = new ArrayList<>();
        for (int i = 0; i < finalNumOfTasksForBothSide; i++) {
            sourceTasks.add(new SourceTask<>(graph));
            sinkTasks.add(new SinkTask<>(graph));
        }

        // prepare other essential objects
        CostComparatorForVertices<V> sourceComparator = new CostComparatorForVertices<>(sourceRouteCost);
        CostComparatorForVertices<V> sinkComparator = new CostComparatorForVertices<>(sinkRouteCost);

        ArrayList<V> sourceFrontier = new ArrayList<>();
        ArrayList<V> sinkFrontier = new ArrayList<>();

        // every insertion and removal of element must be synchronized
        HashSet<V> sourceClose = new HashSet<>(); // avoid duplicates induced by concurrent access
        HashSet<V> sinkClose = new HashSet<>(); // avoid duplicates induced by concurrent access

        // preparation
//        @Future // data preparation is not parallelizable, see BidirectionalBreathFirstSearchOld
//        Void[] populatorPromises = new Void[graph.verticesSet().size()];
        long start_time = System.nanoTime();
//        Iterator<V> vertexIterator = graph.vertices().iterator();
//        for (int i = 0; i < graph.verticesSet().size(); i++) {  // so SLOW!!!!!!!!!!!!!!!!!!!!!!!!!!!
////            populatorPromises[i] = populateDataStructures(sourceFrontier, sinkFrontier, vertexIterator.next());
//            populateDataStructures(sourceFrontier, sinkFrontier, vertexIterator.next());
//        }
        graph.vertices().forEach(v -> {

            prepareMaps(v);
            sourceFrontier.add(v);
            sinkFrontier.add(v);

        });
        long end_time = System.nanoTime();

        double difference = (end_time - start_time) / 1e6;

        System.out.println("Data preparation finished in: " + difference + " milliseconds");

        // initialize dumps (heap tops) for source frontiers and sink frontiers, respectively
        ArrayList<AtomicInteger> sourceDumps = new ArrayList<>();
        ArrayList<AtomicInteger> sinkDumps = new ArrayList<>();
        for (int i = 0; i < finalNumOfTasksForBothSide; i++) {
            sourceDumps.add(new AtomicInteger());
        }
        for (int i = 0; i < finalNumOfTasksForBothSide; i++) {
            sinkDumps.add(new AtomicInteger());
        }

        // experiment : what if all threads share the same dumps? - lose optimality and slower
//        MutableInt dump0 = new MutableInt(sourceDumps.get(0));
//        MutableInt dump1 = new MutableInt(sinkDumps.get(0));

        // strict two cores mode
        if (finalNumOfTasksForBothSide == 1) {

            System.out.println("strict two threads mode");

            // sort both frontier for final setup
            sourceFrontier.sort(sourceComparator);
            sinkFrontier.sort(sinkComparator);

            // prepare source task
            SourceTask<V, E> task = sourceTasks.get(0);
            SinkTask<V, E> peer = sinkTasks.get(0);

            task.setRouteMapFromSource(routeMapFromSource);
            task.setSourceRouteCost(sourceRouteCost);
            task.setSourceDump(new MutableInt(sourceDumps.get(0))); //dump0
            //task.setSourceDump(dump0);

            task.setPeer(peer);

            task.setSourceFrontier(new ArrayList<>(sourceFrontier));
            task.setSourceClose(new HashSet<>(sourceClose));

            task.setSourceLocalMin(sourceLocalMin);

            // prepare sink task
            SinkTask<V, E> task1 = sinkTasks.get(0);
            SourceTask<V, E> peer1 = sourceTasks.get(0);

            task1.setRouteMapFromSink(routeMapFromSink);
            task1.setSinkRouteCost(sinkRouteCost);
            task1.setSinkDump(new MutableInt(sinkDumps.get(0))); //dump1
            //task1.setSinkDump(dump1);

            task1.setPeer(peer1);

            task1.setSinkFrontier(new ArrayList<>(sinkFrontier));
            task1.setSinkClose(new HashSet<>(sinkClose));

            task1.setSinkLocalMin(sinkLocalMin);

        } else {

            System.out.println("normal multi threads mode");

            // initialize a local least cost path for pre-dispatching-stages
            int initialLeastCostPath = Integer.MAX_VALUE;

            // pre-dispatching-stage, source side
            sourceFrontier.sort(sourceComparator);
            V foo = sourceFrontier.get(0);
            sourceFrontier.remove(foo);

            sourceClose.add(foo);
            int sourceDump = sourceRouteCost.get(foo);
            sourceDumps.forEach(dump -> dump.set(sourceDump));

            for (V child : graph.children(foo)) {
                int newCost = sourceRouteCost.get(foo) + child.weight() + graph.edgeBetween(foo, child).weight();

                if (newCost < sourceRouteCost.get(child)) {
                    sourceRouteCost.replace(child, newCost);
                    routeMapFromSource.put(child, foo); // frontier -> close
                }

                // try to update the global least cost path so far
                if (sinkRouteCost.get(child) != Integer.MAX_VALUE) {
                    int temp = newCost + sinkRouteCost.get(child) - child.weight();
                    if (temp < initialLeastCostPath) {
                        initialLeastCostPath = temp;
                    }
                }
            }

            // pre-dispatching-stage, sink side
            sinkFrontier.sort(sinkComparator);
            V bar = sinkFrontier.get(0);
            sinkFrontier.remove(bar);

            sinkClose.add(bar);
            int sinkDump = sinkRouteCost.get(bar);
            sinkDumps.forEach(dump -> dump.set(sinkDump));

            for (V parent : graph.parents(bar)) {
                int newCost = sinkRouteCost.get(bar) + parent.weight() + graph.edgeBetween(parent, bar).weight();
                if (newCost < sinkRouteCost.get(parent)) {
                    sinkRouteCost.replace(parent, newCost);
                    routeMapFromSink.put(parent, bar); // frontier -> close
                }

                // try to update the global least cost path so far
                if (sourceRouteCost.get(parent) != Integer.MAX_VALUE) {
                    int temp = newCost + sourceRouteCost.get(parent) - parent.weight();
                    if (temp < initialLeastCostPath) {
                        initialLeastCostPath = temp;
                    }
                }
            }
            // pre-dispatching-stages finished


            // start dispatching tasks (cutting both frontiers and supplying tasks with all required data structure)
            int cuttingIntervalForSourceSide = numOfSourceBranches/finalNumOfTasksForBothSide;
            int cuttingIntervalForSinkSide = numOfSinkBranches/finalNumOfTasksForBothSide;

            // last check on initialLeastCostPath, for graphs with single nodes or there is a direct edge between source
            // and sink
            if (initialLeastCostPath < Integer.MAX_VALUE) {
                System.out.println("shortest path from source to sink costs: " + initialLeastCostPath +
                        " units");
                return;
            }

            // sort both frontier for final setup
            sourceFrontier.sort(sourceComparator);
            sinkFrontier.sort(sinkComparator);

            // record how many tasks from source have been set up
            int numOfReadyTasksFromSource = 0;

            // have a temporary placeholder for partitioned source frontier
            ArrayDeque<V> sourceFrontierPlaceholder = new ArrayDeque<>();

            // complete the setup of source tasks
            for (int i = 0; i < sourceFrontier.size(); i++) {
                if (numOfReadyTasksFromSource < finalNumOfTasksForBothSide - 1) {
                    if (loopCounter.get() != 0 && loopCounter.get() % cuttingIntervalForSourceSide == 0) {

                    /*
                    // get maps and dumps, and a reference to the Reducible
                    // following are SHARED data structures, passed by setter, shared with the sink peer, make sure
                    // maps are copied
                    private ConcurrentHashMap<V, V> routeMapFromSource;
                    private ConcurrentHashMap<V, Integer> sourceRouteCost;
                    private AtomicInteger sourceDump;

                    // following are private data structures, passed by setter, make sure they are copied
                    private ArrayList<V> sourceFrontier;
                    private HashSet<V> sourceClose;

                    // following are SHARED data structures for source side, passed by setter, make sure they are passed
                    // by reference
                    private Reducible<CostNamePair<V>> sourceLocalMin;
                     */

                        SourceTask<V, E> task = sourceTasks.get(numOfReadyTasksFromSource);
                        SinkTask<V, E> peer = sinkTasks.get(numOfReadyTasksFromSource);

                        task.setRouteMapFromSource(new ConcurrentHashMap<>(routeMapFromSource));
                        task.setSourceRouteCost(new ConcurrentHashMap<>(sourceRouteCost));
                        task.setSourceDump(new MutableInt(sourceDumps.get(numOfReadyTasksFromSource)));
                        //task.setSourceDump(dump0);

                        task.setPeer(peer);

                        task.setSourceFrontier(new ArrayList<>(sourceFrontier));
                        task.setSourceClose(new HashSet<>(sourceClose));

                        task.setSourceLocalMin(sourceLocalMin);


                        numOfReadyTasksFromSource++;
                        while (!sourceFrontierPlaceholder.isEmpty()) {
                            V temp = sourceFrontierPlaceholder.poll();
                            sourceFrontier.remove(temp);
                        }
                    }

                    sourceFrontierPlaceholder.push(sourceFrontier.get(i));
                    loopCounter.incrementAndGet();

                } else {

                    // setup the last task
                    SourceTask<V, E> task = sourceTasks.get(numOfReadyTasksFromSource);
                    SinkTask<V, E> peer = sinkTasks.get(numOfReadyTasksFromSource);

                    task.setRouteMapFromSource(new ConcurrentHashMap<>(routeMapFromSource));
                    task.setSourceRouteCost(new ConcurrentHashMap<>(sourceRouteCost));
                    task.setSourceDump(new MutableInt(sourceDumps.get(numOfReadyTasksFromSource)));
                    //task.setSourceDump(dump0);

                    task.setPeer(peer);

                    task.setSourceFrontier(new ArrayList<>(sourceFrontier));
                    task.setSourceClose(new HashSet<>(sourceClose));

                    task.setSourceLocalMin(sourceLocalMin);

                    break;
                }
            }

            // use the loopCounter to detect the intervals, reset for sink side
            loopCounter.set(0);

            // record how many tasks from sink have been set up
            int numOfReadyTasksFromSink = 0;

            // have a temporary placeholder for partitioned sink frontier
            ArrayDeque<V> sinkFrontierPlaceholder = new ArrayDeque<>();

            // complete the setup of sink tasks
            for (int i = 0; i < sinkFrontier.size(); i++) {
                if (numOfReadyTasksFromSink < finalNumOfTasksForBothSide - 1) {
                    if (loopCounter.get() != 0 && loopCounter.get() % cuttingIntervalForSinkSide == 0) {

                    /*
                    // get maps and dumps, and a reference to the Reducible
                    // following are SHARED data structures, passed by setter, shared with the source peer, make sure
                    // maps are copied
                    private ConcurrentHashMap<V, V> routeMapFromSink;
                    private ConcurrentHashMap<V, Integer> sinkRouteCost;
                    private AtomicInteger sinkDump;

                    // following are private data structures, passed by setter, make sure they are copied
                    private ArrayList<V> sinkFrontier;
                    private HashSet<V> sinkClose;

                    // following are SHARED data structures for sink side, passed by setter, make sure they are passed
                    // by reference
                    private Reducible<CostNamePair<V>> sinkLocalMin;
                     */

                        SinkTask<V, E> task = sinkTasks.get(numOfReadyTasksFromSink);
                        SourceTask<V, E> peer = sourceTasks.get(numOfReadyTasksFromSink);

                        task.setRouteMapFromSink(new ConcurrentHashMap<>(routeMapFromSink));
                        task.setSinkRouteCost(new ConcurrentHashMap<>(sinkRouteCost));
                        task.setSinkDump(new MutableInt(sinkDumps.get(numOfReadyTasksFromSink)));
                        //task.setSinkDump(dump1);

                        task.setPeer(peer);

                        task.setSinkFrontier(new ArrayList<>(sinkFrontier));
                        task.setSinkClose(new HashSet<>(sinkClose));

                        task.setSinkLocalMin(sinkLocalMin);


                        numOfReadyTasksFromSink++;
                        while (!sinkFrontierPlaceholder.isEmpty()) {
                            V temp = sinkFrontierPlaceholder.poll();
                            sinkFrontier.remove(temp);
                        }
                    }

                    sinkFrontierPlaceholder.push(sinkFrontier.get(i));
                    loopCounter.incrementAndGet();

                } else {

                    // setup the last task
                    SinkTask<V, E> task = sinkTasks.get(numOfReadyTasksFromSink);
                    SourceTask<V, E> peer = sourceTasks.get(numOfReadyTasksFromSink);

                    task.setRouteMapFromSink(new ConcurrentHashMap<>(routeMapFromSink));
                    task.setSinkRouteCost(new ConcurrentHashMap<>(sinkRouteCost));
                    task.setSinkDump(new MutableInt(sinkDumps.get(numOfReadyTasksFromSink)));
                    //task.setSinkDump(dump1);

                    task.setPeer(peer);

                    task.setSinkFrontier(new ArrayList<>(sinkFrontier));
                    task.setSinkClose(new HashSet<>(sinkClose));

                    task.setSinkLocalMin(sinkLocalMin);

                    break;

                }
            }
        }


        // explicit barrier for populatorPromises, make sure every data structure is ready
//        while (populatorPromises[0] != null) {}; // data preparation is not parallelizable, don't do it

        // initialized future group, raw typed, cast the element before using it if needed
        @Future
        CostNamePair[] leastCostPathPromises = new CostNamePair[finalNumOfTasksForBothSide * 2];

//        sourceTasks.forEach(veSourceTask -> System.out.println("peer dump: " + veSourceTask.getPeer()));
//        sinkTasks.forEach(veSinkTask -> System.out.println("peer dump: " + veSinkTask.getPeer()));

        // just for testing purpose
//        ExecutorService pool = Executors.newFixedThreadPool(finalNumOfTasksForBothSide * 2);
//
//        List<Callable<Object>> calls = new ArrayList<>();

        // return type of executor.invokeAll : List<Future<Object>> futures = executor.invokeAll(calls);

        // finalize dispatching - use leastCostPathPromises array as future group
        for (int i = 0; i < leastCostPathPromises.length; i++) {

            if (i < finalNumOfTasksForBothSide) {

                // dispatching source tasks
                int index = i; // must be effectively final, @PT covert the following code to lambda internally
                leastCostPathPromises[i] = sourceTasks.get(index).execute();
                //calls.add(Executors.callable(sourceTasks.get(i)));
                System.out.println("dispatching : " + i);

            } else {

                // dispatching sink tasks
                int index = i; // must be effectively final, @PT covert the following code to lambda internally
                leastCostPathPromises[i] = sinkTasks.get(index - sinkTasks.size()).execute();
                //calls.add(Executors.callable(sinkTasks.get(i - sinkTasks.size())));
                System.out.println("dispatching : " + i);

            }

        }

        // explicit barrier for @PT future group
        CostNamePair temp = leastCostPathPromises[0];

        // use executor service for testing purpose
//        try {
//            pool.invokeAll(calls);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            pool.shutdown();
//        }

        CostNamePair<V> sourceSideResult = sourceLocalMin.reduce(new Reducer<>());
        CostNamePair<V> sinkSideResult = sinkLocalMin.reduce(new Reducer<>());

        if (sourceSideResult.getCost() < sinkSideResult.getCost()) {

            System.out.println("shortest path from source to sink costs: " + sourceSideResult.getCost() + " units");

        } else {

            System.out.println("shortest path from source to sink costs: " + sinkSideResult.getCost() + " units");

        }

    }
}