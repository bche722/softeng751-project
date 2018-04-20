

package bbfs;


public class SourceTask<V extends graph.Vertex, E extends graph.DirectedEdge<V>> implements bbfs.Task , java.lang.Runnable {
    private graph.BasicDirectedGraph<V, E> graph;

    private bbfs.SinkTask<V, E> peer;

    void setPeer(bbfs.SinkTask<V, E> peer) {
        bbfs.SourceTask.this.peer = peer;
    }

    bbfs.SinkTask<V, E> getPeer() {
        return bbfs.SourceTask.this.peer;
    }

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSource;

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sourceRouteCost;

    private bbfs.MutableInt sourceDump;

    private java.util.ArrayList<V> sourceFrontier;

    private java.util.HashSet<V> sourceClose;

    private pu.RedLib.Reducible<bbfs.CostNamePair<V>> sourceLocalMin;

    private utils.CostComparatorForVertices<V> sourceComparator;

    SourceTask(graph.BasicDirectedGraph<V, E> graph) {
        bbfs.SourceTask.this.graph = graph;
    }

    java.util.concurrent.ConcurrentHashMap<V, V> getRouteMapFromSource() {
        return routeMapFromSource;
    }

    java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> getSourceRouteCost() {
        return sourceRouteCost;
    }

    bbfs.MutableInt getSourceDump() {
        return sourceDump;
    }

    java.util.concurrent.atomic.AtomicBoolean stopFlag = new java.util.concurrent.atomic.AtomicBoolean();

    void setRouteMapFromSource(java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSource) {
        bbfs.SourceTask.this.routeMapFromSource = routeMapFromSource;
    }

    void setSourceRouteCost(java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sourceRouteCost) {
        bbfs.SourceTask.this.sourceRouteCost = sourceRouteCost;
        bbfs.SourceTask.this.sourceComparator = new utils.CostComparatorForVertices<>(bbfs.SourceTask.this.sourceRouteCost);
    }

    void setSourceDump(bbfs.MutableInt sourceDump) {
        bbfs.SourceTask.this.sourceDump = sourceDump;
    }

    void setSourceLocalMin(pu.RedLib.Reducible<bbfs.CostNamePair<V>> sourceLocalMin) {
        bbfs.SourceTask.this.sourceLocalMin = sourceLocalMin;
    }

    void setSourceFrontier(java.util.ArrayList<V> sourceFrontier) {
        bbfs.SourceTask.this.sourceFrontier = sourceFrontier;
    }

    void setSourceClose(java.util.HashSet<V> sourceClose) {
        bbfs.SourceTask.this.sourceClose = sourceClose;
    }

    @java.lang.Override
    public bbfs.CostNamePair<V> execute() {
        java.lang.System.out.println(("thread id from source task : " + (java.lang.Thread.currentThread().getId())));
        int numOfNodes = graph.verticesSet().size();
        int loopCounter = 0;
        while (!(stopFlag.get())) {
            sourceFrontier.sort(sourceComparator);
            V foo = sourceFrontier.get(0);
            sourceFrontier.remove(foo);
            sourceClose.add(foo);
            sourceDump.set(sourceRouteCost.get(foo));
            int dump0 = sourceDump.get();
            int dump1 = peer.getSinkDump().get();
            for (V child : graph.children(foo)) {
                int newCost = ((sourceRouteCost.get(foo)) + (child.weight())) + (graph.edgeBetween(foo, child).weight());
                if (newCost < (sourceRouteCost.get(child))) {
                    sourceRouteCost.replace(child, newCost);
                    routeMapFromSource.put(child, foo);
                }
                int sinkRouteCostForThisVisitedNode = peer.getSinkRouteCost().get(child);
                if (sinkRouteCostForThisVisitedNode != (java.lang.Integer.MAX_VALUE)) {
                    int temp = ((sourceRouteCost.get(child)) + sinkRouteCostForThisVisitedNode) - (child.weight());
                    if (temp < (sourceLocalMin.get().getCost())) {
                        sourceLocalMin.set(new bbfs.CostNamePair<>(temp, child, routeMapFromSource, peer.getRouteMapFromSink()));
                    }
                }
                if ((dump0 != 0) && ((dump1 != 0) & ((dump0 + dump1) >= (sourceLocalMin.get().getCost())))) {
                    java.lang.System.out.println((("Source thread " + (java.lang.Thread.currentThread().getId())) + " found a solution"));
                    peer.stopFlag.set(true);
                    java.lang.System.out.println((((("Thread " + (java.lang.Thread.currentThread().getId())) + " took : ") + loopCounter) + " iterations"));
                    return sourceLocalMin.get();
                }
            }
            loopCounter++;
        } 
        java.lang.System.out.println((((("Thread " + (java.lang.Thread.currentThread().getId())) + " took : ") + loopCounter) + " iterations"));
        return sourceLocalMin.get();
    }

    @java.lang.Override
    public void run() {
        execute();
    }
}

