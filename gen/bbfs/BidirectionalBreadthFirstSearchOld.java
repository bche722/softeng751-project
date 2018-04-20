

package bbfs;


public class BidirectionalBreadthFirstSearchOld<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends interfaces.Algorithm<graph.Vertex, graph.DirectedEdge<graph.Vertex>> {
    private graph.BasicDirectedGraph<V, E> graph;

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSource = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSink = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sourceRouteCost = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sinkRouteCost = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.atomic.AtomicInteger loopCounter = new java.util.concurrent.atomic.AtomicInteger();

    private V source;

    private V sink;

    public BidirectionalBreadthFirstSearchOld(graph.BasicDirectedGraph<V, E> graph, boolean isParallel) {
        bbfs.BidirectionalBreadthFirstSearchOld.this.graph = graph;
        bbfs.BidirectionalBreadthFirstSearchOld.this.isParallel = isParallel;
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
        }
        long end_time = java.lang.System.nanoTime();
        double difference = (end_time - start_time) / 1000000.0;
        java.lang.System.out.println((("The process took: " + (loopCounter.get())) + " iterations"));
        java.lang.System.out.println((("Program finished in: " + difference) + " milliseconds"));
    }

    private void sequentialSearch() {
        utils.CostComparatorForVertices<V> sourceComparator = new utils.CostComparatorForVertices<>(sourceRouteCost);
        utils.CostComparatorForVertices<V> sinkComparator = new utils.CostComparatorForVertices<>(sinkRouteCost);
        java.util.ArrayList<V> sourceFrontier = new java.util.ArrayList<>();
        java.util.ArrayList<V> sinkFrontier = new java.util.ArrayList<>();
        java.util.HashSet<V> sourceClose = new java.util.HashSet<>();
        java.util.HashSet<V> sinkClose = new java.util.HashSet<>();
        graph.vertices().forEach(( v) -> {
            prepareMaps(v);
            sourceFrontier.add(v);
            sinkFrontier.add(v);
        });
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

    private pu.RedLib.Reducible<java.lang.Integer> sourceLocalMin = new pu.RedLib.Reducible<>(java.lang.Integer.MAX_VALUE);

    private pu.RedLib.Reducible<java.lang.Integer> sinkLocalMin = new pu.RedLib.Reducible<>(java.lang.Integer.MAX_VALUE);

    private java.lang.Void populateDataStructures(java.util.ArrayList<V> sourceFrontier, java.util.ArrayList<V> sinkFrontier, V v) {
        java.lang.System.out.println(("populating data. thread id : " + (java.lang.Thread.currentThread().getId())));
        prepareMaps(v);
        sourceFrontier.add(v);
        sinkFrontier.add(v);
        return null;
    }

    private int sourceTask(V foo, V child) {
        int newCost = ((sourceRouteCost.get(foo)) + (child.weight())) + (graph.edgeBetween(foo, child).weight());
        if (newCost < (sourceRouteCost.get(child))) {
            sourceRouteCost.replace(child, newCost);
            routeMapFromSource.put(child, foo);
        }
        int localLeastCost;
        if ((sinkRouteCost.get(child)) != (java.lang.Integer.MAX_VALUE)) {
            localLeastCost = (newCost + (sinkRouteCost.get(child))) - (child.weight());
            if (localLeastCost < (sourceLocalMin.get())) {
                sourceLocalMin.set(localLeastCost);
            }
        }else {
            localLeastCost = java.lang.Integer.MAX_VALUE;
        }
        return localLeastCost;
    }

    private int sinkTask(V bar, V parent) {
        int newCost = ((sinkRouteCost.get(bar)) + (parent.weight())) + (graph.edgeBetween(parent, bar).weight());
        if (newCost < (sinkRouteCost.get(parent))) {
            sinkRouteCost.replace(parent, newCost);
            routeMapFromSink.put(parent, bar);
        }
        int localLeastCost;
        if ((sourceRouteCost.get(parent)) != (java.lang.Integer.MAX_VALUE)) {
            localLeastCost = (newCost + (sourceRouteCost.get(parent))) - (parent.weight());
            if (localLeastCost < (sinkLocalMin.get())) {
                sinkLocalMin.set(localLeastCost);
            }
        }else {
            localLeastCost = java.lang.Integer.MAX_VALUE;
        }
        return localLeastCost;
    }

    private void parallelSearch() {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        utils.CostComparatorForVertices<V> sourceComparator = new utils.CostComparatorForVertices<>(sourceRouteCost);
        utils.CostComparatorForVertices<V> sinkComparator = new utils.CostComparatorForVertices<>(sinkRouteCost);
        java.util.ArrayList<V> sourceFrontier = new java.util.ArrayList<>();
        java.util.ArrayList<V> sinkFrontier = new java.util.ArrayList<>();
        java.util.HashSet<V> sourceClose = new java.util.HashSet<>();
        java.util.HashSet<V> sinkClose = new java.util.HashSet<>();
        java.lang.Void[] populatorPromises = new java.lang.Void[graph.verticesSet().size()];
        pt.runtime.TaskIDGroup<java.lang.Void> __populatorPromisesPtTaskIDGroup__ = new pt.runtime.TaskIDGroup<>(graph.verticesSet().size());
        java.util.Iterator<V> vertexIterator = graph.vertices().iterator();
        for (int i = 0; i < (graph.verticesSet().size()); i++) {
            pt.runtime.TaskInfoTwoArgs<java.lang.Void, java.util.ArrayList<V>, java.util.ArrayList<V>> ____populatorPromises_1__PtTask__ = ((pt.runtime.TaskInfoTwoArgs<java.lang.Void, java.util.ArrayList<V>, java.util.ArrayList<V>>) (pt.runtime.ParaTask.asTask(pt.runtime.ParaTask.TaskType.ONEOFF, ((pt.functionalInterfaces.FunctorTwoArgsNoReturn<java.util.ArrayList<V>, java.util.ArrayList<V>>) (( __sinkFrontierPtNonLambdaArg__, __sourceFrontierPtNonLambdaArg__) -> populateDataStructures(__sourceFrontierPtNonLambdaArg__, __sinkFrontierPtNonLambdaArg__, vertexIterator.next()))))));
            pt.runtime.TaskID<java.lang.Void> ____populatorPromises_1__PtTaskID__ = ____populatorPromises_1__PtTask__.start(sinkFrontier, sourceFrontier);
            __populatorPromisesPtTaskIDGroup__.setInnerTask(i, ____populatorPromises_1__PtTaskID__);
        }
        int[] leastCostPathSoFar = new int[]{ java.lang.Integer.MAX_VALUE };
        int[] dump = new int[]{ 0 , 0 };
        try {
            __populatorPromisesPtTaskIDGroup__.waitTillFinished();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        while ((populatorPromises[0]) != null) {
        } 
        while (!((((dump[0]) != 0) && ((dump[1]) != 0)) && (((dump[0]) + (dump[1])) >= (leastCostPathSoFar[0])))) {
            if (((loopCounter.get()) % 2) == 0) {
                if (sourceFrontier.isEmpty()) {
                    java.lang.System.out.println((("shortest path from source to sink costs: " + (leastCostPathSoFar[0])) + " units"));
                    break;
                }
                sourceFrontier.sort(sourceComparator);
                V foo = sourceFrontier.get(0);
                sourceFrontier.remove(foo);
                java.util.ArrayList<V> childrenArray = graph.getChilds(foo);
                int size = childrenArray.size();
                pu.pi.ParIterator<V> childrenIteratorPar = pu.pi.ParIteratorFactory.createParIterator(childrenArray, java.lang.Runtime.getRuntime().availableProcessors());
                java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(java.lang.Runtime.getRuntime().availableProcessors());
                java.util.ArrayList<java.util.concurrent.Callable<java.lang.Object>> callables = new java.util.ArrayList<>();
                for (int i = 0; i < size; i++) {
                    callables.add(java.util.concurrent.Executors.callable(() -> {
                        sourceTask(foo, childrenIteratorPar.next());
                    }));
                }
                try {
                    pool.invokeAll(callables);
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    pool.shutdown();
                }
                int temp;
                temp = sourceLocalMin.reduce(pu.RedLib.Reduction.IntegerMIN);
                if (temp < (leastCostPathSoFar[0])) {
                    leastCostPathSoFar[0] = temp;
                }
                sourceClose.add(foo);
                dump[0] = sourceRouteCost.get(foo);
                sourceLocalMin = new pu.RedLib.Reducible<>(java.lang.Integer.MAX_VALUE);
            }else {
                if (sinkFrontier.isEmpty()) {
                    java.lang.System.out.println((("shortest path from source to sink costs: " + (leastCostPathSoFar[0])) + " units"));
                    break;
                }
                sinkFrontier.sort(sinkComparator);
                V bar = sinkFrontier.get(0);
                sinkFrontier.remove(bar);
                java.util.ArrayList<V> parentsArray = graph.getParents(bar);
                int size = parentsArray.size();
                pu.pi.ParIterator<V> parentsIteratorPar = pu.pi.ParIteratorFactory.createParIterator(parentsArray, java.lang.Runtime.getRuntime().availableProcessors());
                java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(java.lang.Runtime.getRuntime().availableProcessors());
                java.util.ArrayList<java.util.concurrent.Callable<java.lang.Object>> callables = new java.util.ArrayList<>();
                for (int i = 0; i < size; i++) {
                    callables.add(java.util.concurrent.Executors.callable(() -> {
                        sinkTask(bar, parentsIteratorPar.next());
                    }));
                }
                try {
                    pool.invokeAll(callables);
                } catch (java.lang.InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    pool.shutdown();
                }
                int temp;
                temp = sinkLocalMin.reduce(pu.RedLib.Reduction.IntegerMIN);
                if (temp < (leastCostPathSoFar[0])) {
                    leastCostPathSoFar[0] = temp;
                }
                sinkClose.add(bar);
                dump[1] = sinkRouteCost.get(bar);
                sinkLocalMin = new pu.RedLib.Reducible<>(java.lang.Integer.MAX_VALUE);
            }
            loopCounter.addAndGet(1);
        } 
        java.lang.System.out.println((("shortest path from source to sink costs: " + (leastCostPathSoFar[0])) + " units"));
    }
}

