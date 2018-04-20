

package bbfs;


public class BidirectionalBreadthFirstSearch<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends interfaces.Algorithm<graph.Vertex, graph.DirectedEdge<graph.Vertex>> {
    private graph.BasicDirectedGraph<V, E> graph;

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSource = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSink = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sourceRouteCost = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sinkRouteCost = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.atomic.AtomicInteger loopCounter = new java.util.concurrent.atomic.AtomicInteger();

    private V source;

    private V sink;

    public BidirectionalBreadthFirstSearch(graph.BasicDirectedGraph<V, E> graph, boolean isParallel) {
        bbfs.BidirectionalBreadthFirstSearch.this.graph = graph;
        bbfs.BidirectionalBreadthFirstSearch.this.isParallel = isParallel;
    }

    @java.lang.Override
    public void doTheJob() {
        V[] source_and_sink = ((V[]) (new graph.Vertex[]{ null , null }));
        graph.sources().forEach(( v) -> source_and_sink[0] = v);
        graph.sinks().forEach(( v) -> source_and_sink[1] = v);
        source = source_and_sink[0];
        sink = source_and_sink[1];
        long start_time = java.lang.System.nanoTime();
        if (isParallel) {
            parallelSearch();
        }else {
            sequentialSearch();
            java.lang.System.out.println((("Process took : " + (loopCounter.get())) + " iterations"));
        }
        long end_time = java.lang.System.nanoTime();
        double difference = (end_time - start_time) / 1000000.0;
        java.lang.System.out.println((("Program finished in: " + difference) + " milliseconds"));
    }

    private void sequentialSearch() {
        utils.CostComparatorForVertices<V> sourceComparator = new utils.CostComparatorForVertices<>(sourceRouteCost);
        utils.CostComparatorForVertices<V> sinkComparator = new utils.CostComparatorForVertices<>(sinkRouteCost);
        java.util.ArrayList<V> sourceFrontier = new java.util.ArrayList<>();
        java.util.ArrayList<V> sinkFrontier = new java.util.ArrayList<>();
        java.util.HashSet<V> sourceClose = new java.util.HashSet<>();
        java.util.HashSet<V> sinkClose = new java.util.HashSet<>();
        long start_time = java.lang.System.nanoTime();
        graph.vertices().forEach(( v) -> {
            prepareMaps(v);
            sourceFrontier.add(v);
            sinkFrontier.add(v);
        });
        long end_time = java.lang.System.nanoTime();
        double difference = (end_time - start_time) / 1000000.0;
        java.lang.System.out.println((("Data preparation finished in: " + difference) + " milliseconds"));
        int[] leastCostPathSoFar = new int[]{ java.lang.Integer.MAX_VALUE };
        int[] dump = new int[]{ 0 , 0 };
        while ((loopCounter.get()) >= 0) {
            if (((loopCounter.get()) % 2) == 0) {
                if (sourceFrontier.isEmpty()) {
                    java.lang.System.out.println((("shortest path from source to sink costs: " + (leastCostPathSoFar[0])) + " units"));
                    break;
                }
                sourceFrontier.sort(sourceComparator);
                V foo = sourceFrontier.get(0);
                sourceFrontier.remove(foo);
                graph.childrenIterator(foo).forEachRemaining(( child) -> {
                    int newCost = ((sourceRouteCost.get(foo)) + (child.weight())) + (graph.edgeBetween(foo, child).weight());
                    if (newCost < (sourceRouteCost.get(child))) {
                        sourceRouteCost.replace(child, newCost);
                        routeMapFromSource.put(child, foo);
                    }
                    if ((sinkRouteCost.get(child)) != (java.lang.Integer.MAX_VALUE)) {
                        int temp = (newCost + (sinkRouteCost.get(child))) - (child.weight());
                        if (temp < (leastCostPathSoFar[0])) {
                            leastCostPathSoFar[0] = temp;
                        }
                    }
                });
                sourceClose.add(foo);
                dump[0] = sourceRouteCost.get(foo);
            }else {
                if (sinkFrontier.isEmpty()) {
                    java.lang.System.out.println((("shortest path from source to sink costs: " + (leastCostPathSoFar[0])) + " units"));
                    break;
                }
                sinkFrontier.sort(sinkComparator);
                V bar = sinkFrontier.get(0);
                sinkFrontier.remove(bar);
                graph.parentsIterator(bar).forEachRemaining(( parent) -> {
                    int newCost = ((sinkRouteCost.get(bar)) + (parent.weight())) + (graph.edgeBetween(parent, bar).weight());
                    if (newCost < (sinkRouteCost.get(parent))) {
                        sinkRouteCost.replace(parent, newCost);
                        routeMapFromSink.put(parent, bar);
                    }
                    if ((sourceRouteCost.get(parent)) != (java.lang.Integer.MAX_VALUE)) {
                        int temp = (newCost + (sourceRouteCost.get(parent))) - (parent.weight());
                        if (temp < (leastCostPathSoFar[0])) {
                            leastCostPathSoFar[0] = temp;
                        }
                    }
                });
                sinkClose.add(bar);
                dump[1] = sinkRouteCost.get(bar);
            }
            if (((dump[0]) != 0) && ((dump[1]) != 0)) {
                if (((dump[0]) + (dump[1])) >= (leastCostPathSoFar[0])) {
                    java.lang.System.out.println((("shortest path from source to sink costs: " + (leastCostPathSoFar[0])) + " units"));
                    loopCounter.addAndGet(1);
                    break;
                }
            }
            loopCounter.addAndGet(1);
        } 
    }

    private void prepareMaps(V v) {
        if (v.name().equals(source.name())) {
            sourceRouteCost.put(v, v.weight());
            sinkRouteCost.put(v, java.lang.Integer.MAX_VALUE);
        }else
            if (v.name().equals(sink.name())) {
                sourceRouteCost.put(v, java.lang.Integer.MAX_VALUE);
                sinkRouteCost.put(v, v.weight());
            }else {
                sourceRouteCost.put(v, java.lang.Integer.MAX_VALUE);
                sinkRouteCost.put(v, java.lang.Integer.MAX_VALUE);
            }
        
    }

    private pu.RedLib.Reducible<bbfs.CostNamePair<V>> sourceLocalMin = new pu.RedLib.Reducible<>(new bbfs.CostNamePair<>(java.lang.Integer.MAX_VALUE, null, null, null));

    private pu.RedLib.Reducible<bbfs.CostNamePair<V>> sinkLocalMin = new pu.RedLib.Reducible<>(new bbfs.CostNamePair<>(java.lang.Integer.MAX_VALUE, null, null, null));

    private java.lang.Void populateDataStructures(java.util.ArrayList<V> sourceFrontier, java.util.ArrayList<V> sinkFrontier, V v) {
        prepareMaps(v);
        sourceFrontier.add(v);
        sinkFrontier.add(v);
        return null;
    }

    private void parallelSearch() {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        int numOfProcessors = java.lang.Runtime.getRuntime().availableProcessors();
        int maxNumOfSourceTasks = numOfProcessors / 2;
        int maxNumOfSinkTasks = numOfProcessors - maxNumOfSourceTasks;
        int numOfSourceBranches = graph.getChilds(source).size();
        int numOfSinkBranches = graph.getParents(sink).size();
        int minNumOfTasksFromSource;
        if (numOfSourceBranches < maxNumOfSourceTasks) {
            minNumOfTasksFromSource = numOfSourceBranches;
        }else {
            minNumOfTasksFromSource = maxNumOfSourceTasks;
        }
        int minNumOfTasksFromSink;
        if (numOfSinkBranches < maxNumOfSinkTasks) {
            minNumOfTasksFromSink = numOfSinkBranches;
        }else {
            minNumOfTasksFromSink = maxNumOfSinkTasks;
        }
        int finalNumOfTasksForBothSide;
        if (minNumOfTasksFromSource < minNumOfTasksFromSink) {
            finalNumOfTasksForBothSide = minNumOfTasksFromSource;
        }else {
            finalNumOfTasksForBothSide = minNumOfTasksFromSink;
        }
        finalNumOfTasksForBothSide = 1;
        java.lang.System.out.println(("finalNumOfTasksForBothSide : " + finalNumOfTasksForBothSide));
        java.util.ArrayList<bbfs.SourceTask<V, E>> sourceTasks = new java.util.ArrayList<>();
        java.util.ArrayList<bbfs.SinkTask<V, E>> sinkTasks = new java.util.ArrayList<>();
        for (int i = 0; i < finalNumOfTasksForBothSide; i++) {
            sourceTasks.add(new bbfs.SourceTask<>(graph));
            sinkTasks.add(new bbfs.SinkTask<>(graph));
        }
        utils.CostComparatorForVertices<V> sourceComparator = new utils.CostComparatorForVertices<>(sourceRouteCost);
        utils.CostComparatorForVertices<V> sinkComparator = new utils.CostComparatorForVertices<>(sinkRouteCost);
        java.util.ArrayList<V> sourceFrontier = new java.util.ArrayList<>();
        java.util.ArrayList<V> sinkFrontier = new java.util.ArrayList<>();
        java.util.HashSet<V> sourceClose = new java.util.HashSet<>();
        java.util.HashSet<V> sinkClose = new java.util.HashSet<>();
        long start_time = java.lang.System.nanoTime();
        graph.vertices().forEach(( v) -> {
            prepareMaps(v);
            sourceFrontier.add(v);
            sinkFrontier.add(v);
        });
        long end_time = java.lang.System.nanoTime();
        double difference = (end_time - start_time) / 1000000.0;
        java.lang.System.out.println((("Data preparation finished in: " + difference) + " milliseconds"));
        java.util.ArrayList<java.util.concurrent.atomic.AtomicInteger> sourceDumps = new java.util.ArrayList<>();
        java.util.ArrayList<java.util.concurrent.atomic.AtomicInteger> sinkDumps = new java.util.ArrayList<>();
        for (int i = 0; i < finalNumOfTasksForBothSide; i++) {
            sourceDumps.add(new java.util.concurrent.atomic.AtomicInteger());
        }
        for (int i = 0; i < finalNumOfTasksForBothSide; i++) {
            sinkDumps.add(new java.util.concurrent.atomic.AtomicInteger());
        }
        if (finalNumOfTasksForBothSide == 1) {
            java.lang.System.out.println("strict two threads mode");
            sourceFrontier.sort(sourceComparator);
            sinkFrontier.sort(sinkComparator);
            bbfs.SourceTask<V, E> task = sourceTasks.get(0);
            bbfs.SinkTask<V, E> peer = sinkTasks.get(0);
            task.setRouteMapFromSource(routeMapFromSource);
            task.setSourceRouteCost(sourceRouteCost);
            task.setSourceDump(new bbfs.MutableInt(sourceDumps.get(0)));
            task.setPeer(peer);
            task.setSourceFrontier(new java.util.ArrayList<>(sourceFrontier));
            task.setSourceClose(new java.util.HashSet<>(sourceClose));
            task.setSourceLocalMin(sourceLocalMin);
            bbfs.SinkTask<V, E> task1 = sinkTasks.get(0);
            bbfs.SourceTask<V, E> peer1 = sourceTasks.get(0);
            task1.setRouteMapFromSink(routeMapFromSink);
            task1.setSinkRouteCost(sinkRouteCost);
            task1.setSinkDump(new bbfs.MutableInt(sinkDumps.get(0)));
            task1.setPeer(peer1);
            task1.setSinkFrontier(new java.util.ArrayList<>(sinkFrontier));
            task1.setSinkClose(new java.util.HashSet<>(sinkClose));
            task1.setSinkLocalMin(sinkLocalMin);
        }else {
            java.lang.System.out.println("normal multi threads mode");
            int initialLeastCostPath = java.lang.Integer.MAX_VALUE;
            sourceFrontier.sort(sourceComparator);
            V foo = sourceFrontier.get(0);
            sourceFrontier.remove(foo);
            sourceClose.add(foo);
            int sourceDump = sourceRouteCost.get(foo);
            sourceDumps.forEach(( dump) -> dump.set(sourceDump));
            for (V child : graph.children(foo)) {
                int newCost = ((sourceRouteCost.get(foo)) + (child.weight())) + (graph.edgeBetween(foo, child).weight());
                if (newCost < (sourceRouteCost.get(child))) {
                    sourceRouteCost.replace(child, newCost);
                    routeMapFromSource.put(child, foo);
                }
                if ((sinkRouteCost.get(child)) != (java.lang.Integer.MAX_VALUE)) {
                    int temp = (newCost + (sinkRouteCost.get(child))) - (child.weight());
                    if (temp < initialLeastCostPath) {
                        initialLeastCostPath = temp;
                    }
                }
            }
            sinkFrontier.sort(sinkComparator);
            V bar = sinkFrontier.get(0);
            sinkFrontier.remove(bar);
            sinkClose.add(bar);
            int sinkDump = sinkRouteCost.get(bar);
            sinkDumps.forEach(( dump) -> dump.set(sinkDump));
            for (V parent : graph.parents(bar)) {
                int newCost = ((sinkRouteCost.get(bar)) + (parent.weight())) + (graph.edgeBetween(parent, bar).weight());
                if (newCost < (sinkRouteCost.get(parent))) {
                    sinkRouteCost.replace(parent, newCost);
                    routeMapFromSink.put(parent, bar);
                }
                if ((sourceRouteCost.get(parent)) != (java.lang.Integer.MAX_VALUE)) {
                    int temp = (newCost + (sourceRouteCost.get(parent))) - (parent.weight());
                    if (temp < initialLeastCostPath) {
                        initialLeastCostPath = temp;
                    }
                }
            }
            int cuttingIntervalForSourceSide = numOfSourceBranches / finalNumOfTasksForBothSide;
            int cuttingIntervalForSinkSide = numOfSinkBranches / finalNumOfTasksForBothSide;
            if (initialLeastCostPath < (java.lang.Integer.MAX_VALUE)) {
                java.lang.System.out.println((("shortest path from source to sink costs: " + initialLeastCostPath) + " units"));
                return ;
            }
            sourceFrontier.sort(sourceComparator);
            sinkFrontier.sort(sinkComparator);
            int numOfReadyTasksFromSource = 0;
            java.util.ArrayDeque<V> sourceFrontierPlaceholder = new java.util.ArrayDeque<>();
            for (int i = 0; i < (sourceFrontier.size()); i++) {
                if (numOfReadyTasksFromSource < (finalNumOfTasksForBothSide - 1)) {
                    if (((loopCounter.get()) != 0) && (((loopCounter.get()) % cuttingIntervalForSourceSide) == 0)) {
                        bbfs.SourceTask<V, E> task = sourceTasks.get(numOfReadyTasksFromSource);
                        bbfs.SinkTask<V, E> peer = sinkTasks.get(numOfReadyTasksFromSource);
                        task.setRouteMapFromSource(new java.util.concurrent.ConcurrentHashMap<>(routeMapFromSource));
                        task.setSourceRouteCost(new java.util.concurrent.ConcurrentHashMap<>(sourceRouteCost));
                        task.setSourceDump(new bbfs.MutableInt(sourceDumps.get(numOfReadyTasksFromSource)));
                        task.setPeer(peer);
                        task.setSourceFrontier(new java.util.ArrayList<>(sourceFrontier));
                        task.setSourceClose(new java.util.HashSet<>(sourceClose));
                        task.setSourceLocalMin(sourceLocalMin);
                        numOfReadyTasksFromSource++;
                        while (!(sourceFrontierPlaceholder.isEmpty())) {
                            V temp = sourceFrontierPlaceholder.poll();
                            sourceFrontier.remove(temp);
                        } 
                    }
                    sourceFrontierPlaceholder.push(sourceFrontier.get(i));
                    loopCounter.incrementAndGet();
                }else {
                    bbfs.SourceTask<V, E> task = sourceTasks.get(numOfReadyTasksFromSource);
                    bbfs.SinkTask<V, E> peer = sinkTasks.get(numOfReadyTasksFromSource);
                    task.setRouteMapFromSource(new java.util.concurrent.ConcurrentHashMap<>(routeMapFromSource));
                    task.setSourceRouteCost(new java.util.concurrent.ConcurrentHashMap<>(sourceRouteCost));
                    task.setSourceDump(new bbfs.MutableInt(sourceDumps.get(numOfReadyTasksFromSource)));
                    task.setPeer(peer);
                    task.setSourceFrontier(new java.util.ArrayList<>(sourceFrontier));
                    task.setSourceClose(new java.util.HashSet<>(sourceClose));
                    task.setSourceLocalMin(sourceLocalMin);
                    break;
                }
            }
            loopCounter.set(0);
            int numOfReadyTasksFromSink = 0;
            java.util.ArrayDeque<V> sinkFrontierPlaceholder = new java.util.ArrayDeque<>();
            for (int i = 0; i < (sinkFrontier.size()); i++) {
                if (numOfReadyTasksFromSink < (finalNumOfTasksForBothSide - 1)) {
                    if (((loopCounter.get()) != 0) && (((loopCounter.get()) % cuttingIntervalForSinkSide) == 0)) {
                        bbfs.SinkTask<V, E> task = sinkTasks.get(numOfReadyTasksFromSink);
                        bbfs.SourceTask<V, E> peer = sourceTasks.get(numOfReadyTasksFromSink);
                        task.setRouteMapFromSink(new java.util.concurrent.ConcurrentHashMap<>(routeMapFromSink));
                        task.setSinkRouteCost(new java.util.concurrent.ConcurrentHashMap<>(sinkRouteCost));
                        task.setSinkDump(new bbfs.MutableInt(sinkDumps.get(numOfReadyTasksFromSink)));
                        task.setPeer(peer);
                        task.setSinkFrontier(new java.util.ArrayList<>(sinkFrontier));
                        task.setSinkClose(new java.util.HashSet<>(sinkClose));
                        task.setSinkLocalMin(sinkLocalMin);
                        numOfReadyTasksFromSink++;
                        while (!(sinkFrontierPlaceholder.isEmpty())) {
                            V temp = sinkFrontierPlaceholder.poll();
                            sinkFrontier.remove(temp);
                        } 
                    }
                    sinkFrontierPlaceholder.push(sinkFrontier.get(i));
                    loopCounter.incrementAndGet();
                }else {
                    bbfs.SinkTask<V, E> task = sinkTasks.get(numOfReadyTasksFromSink);
                    bbfs.SourceTask<V, E> peer = sourceTasks.get(numOfReadyTasksFromSink);
                    task.setRouteMapFromSink(new java.util.concurrent.ConcurrentHashMap<>(routeMapFromSink));
                    task.setSinkRouteCost(new java.util.concurrent.ConcurrentHashMap<>(sinkRouteCost));
                    task.setSinkDump(new bbfs.MutableInt(sinkDumps.get(numOfReadyTasksFromSink)));
                    task.setPeer(peer);
                    task.setSinkFrontier(new java.util.ArrayList<>(sinkFrontier));
                    task.setSinkClose(new java.util.HashSet<>(sinkClose));
                    task.setSinkLocalMin(sinkLocalMin);
                    break;
                }
            }
        }
        bbfs.CostNamePair[] leastCostPathPromises = new bbfs.CostNamePair[finalNumOfTasksForBothSide * 2];
        pt.runtime.TaskIDGroup<bbfs.CostNamePair> __leastCostPathPromisesPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>((finalNumOfTasksForBothSide * 2));
        for (int i = 0; i < (leastCostPathPromises.length); i++) {
            if (i < finalNumOfTasksForBothSide) {
                int index = i;
                pt.runtime.TaskInfoNoArgs<bbfs.CostNamePair> ____leastCostPathPromises_1__PtTask__ = ((pt.runtime.TaskInfoNoArgs<bbfs.CostNamePair>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<bbfs.CostNamePair>) (() -> sourceTasks.get(index).execute())))));
                pt.runtime.TaskID<bbfs.CostNamePair> ____leastCostPathPromises_1__PtTaskID__ = ____leastCostPathPromises_1__PtTask__.start();
                __leastCostPathPromisesPtTaskIDGroup__.setInnerTask(i, ____leastCostPathPromises_1__PtTaskID__);
                java.lang.System.out.println(("dispatching : " + i));
            }else {
                int index = i;
                pt.runtime.TaskInfoNoArgs<bbfs.CostNamePair> ____leastCostPathPromises_2__PtTask__ = ((pt.runtime.TaskInfoNoArgs<bbfs.CostNamePair>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorNoArgsWithReturn<bbfs.CostNamePair>) (() -> sinkTasks.get((index - (sinkTasks.size()))).execute())))));
                pt.runtime.TaskID<bbfs.CostNamePair> ____leastCostPathPromises_2__PtTaskID__ = ____leastCostPathPromises_2__PtTask__.start();
                __leastCostPathPromisesPtTaskIDGroup__.setInnerTask(i, ____leastCostPathPromises_2__PtTaskID__);
                java.lang.System.out.println(("dispatching : " + i));
            }
        }
        try {
            __leastCostPathPromisesPtTaskIDGroup__.waitTillFinished();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        for (int __leastCostPathPromisesLoopIndex1__ = 0; __leastCostPathPromisesLoopIndex1__ < (leastCostPathPromises.length); __leastCostPathPromisesLoopIndex1__++)
            leastCostPathPromises[__leastCostPathPromisesLoopIndex1__] = __leastCostPathPromisesPtTaskIDGroup__.getInnerTaskResult(__leastCostPathPromisesLoopIndex1__);
        
        bbfs.CostNamePair temp = leastCostPathPromises[0];
        bbfs.CostNamePair<V> sourceSideResult = sourceLocalMin.reduce(new bbfs.Reducer<>());
        bbfs.CostNamePair<V> sinkSideResult = sinkLocalMin.reduce(new bbfs.Reducer<>());
        if ((sourceSideResult.getCost()) < (sinkSideResult.getCost())) {
            java.lang.System.out.println((("shortest path from source to sink costs: " + (sourceSideResult.getCost())) + " units"));
        }else {
            java.lang.System.out.println((("shortest path from source to sink costs: " + (sinkSideResult.getCost())) + " units"));
        }
    }
}

