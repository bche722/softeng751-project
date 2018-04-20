

package bbfs;


public class SinkTask<V extends graph.Vertex, E extends graph.DirectedEdge<V>> implements bbfs.Task , java.lang.Runnable {
    private graph.BasicDirectedGraph<V, E> graph;

    private bbfs.SourceTask<V, E> peer;

    void setPeer(bbfs.SourceTask<V, E> peer) {
        bbfs.SinkTask.this.peer = peer;
    }

    bbfs.SourceTask<V, E> getPeer() {
        return bbfs.SinkTask.this.peer;
    }

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSink;

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sinkRouteCost;

    private bbfs.MutableInt sinkDump;

    private java.util.ArrayList<V> sinkFrontier;

    private java.util.HashSet<V> sinkClose;

    private pu.RedLib.Reducible<bbfs.CostNamePair<V>> sinkLocalMin;

    private utils.CostComparatorForVertices<V> sinkComparator;

    SinkTask(graph.BasicDirectedGraph<V, E> graph) {
        bbfs.SinkTask.this.graph = graph;
    }

    java.util.concurrent.ConcurrentHashMap<V, V> getRouteMapFromSink() {
        return routeMapFromSink;
    }

    java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> getSinkRouteCost() {
        return sinkRouteCost;
    }

    bbfs.MutableInt getSinkDump() {
        return sinkDump;
    }

    java.util.concurrent.atomic.AtomicBoolean stopFlag = new java.util.concurrent.atomic.AtomicBoolean();

    void setRouteMapFromSink(java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSink) {
        bbfs.SinkTask.this.routeMapFromSink = routeMapFromSink;
    }

    void setSinkRouteCost(java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> sinkRouteCost) {
        bbfs.SinkTask.this.sinkRouteCost = sinkRouteCost;
        bbfs.SinkTask.this.sinkComparator = new utils.CostComparatorForVertices<>(bbfs.SinkTask.this.sinkRouteCost);
    }

    void setSinkDump(bbfs.MutableInt sinkDump) {
        bbfs.SinkTask.this.sinkDump = sinkDump;
    }

    void setSinkLocalMin(pu.RedLib.Reducible<bbfs.CostNamePair<V>> sinkLocalMin) {
        bbfs.SinkTask.this.sinkLocalMin = sinkLocalMin;
    }

    void setSinkFrontier(java.util.ArrayList<V> sinkFrontier) {
        bbfs.SinkTask.this.sinkFrontier = sinkFrontier;
    }

    void setSinkClose(java.util.HashSet<V> sinkClose) {
        bbfs.SinkTask.this.sinkClose = sinkClose;
    }

    @java.lang.Override
    public bbfs.CostNamePair<V> execute() {
        java.lang.System.out.println(("thread id from sink task : " + (java.lang.Thread.currentThread().getId())));
        int numOfNodes = graph.verticesSet().size();
        int loopCounter = 0;
        while (!(stopFlag.get())) {
            sinkFrontier.sort(sinkComparator);
            V bar = sinkFrontier.get(0);
            sinkFrontier.remove(bar);
            sinkClose.add(bar);
            sinkDump.set(sinkRouteCost.get(bar));
            int dump0 = peer.getSourceDump().get();
            int dump1 = sinkDump.get();
            for (V parent : graph.parents(bar)) {
                int newCost = ((sinkRouteCost.get(bar)) + (parent.weight())) + (graph.edgeBetween(parent, bar).weight());
                if (newCost < (sinkRouteCost.get(parent))) {
                    sinkRouteCost.replace(parent, newCost);
                    routeMapFromSink.put(parent, bar);
                }
                int sourceRouteCostForThisVisitedNode = peer.getSourceRouteCost().get(parent);
                if (sourceRouteCostForThisVisitedNode != (java.lang.Integer.MAX_VALUE)) {
                    int temp = ((sinkRouteCost.get(parent)) + sourceRouteCostForThisVisitedNode) - (parent.weight());
                    if (temp < (sinkLocalMin.get().getCost())) {
                        sinkLocalMin.set(new bbfs.CostNamePair<>(temp, parent, peer.getRouteMapFromSource(), routeMapFromSink));
                    }
                }
                if ((dump0 != 0) && ((dump1 != 0) & ((dump0 + dump1) >= (sinkLocalMin.get().getCost())))) {
                    java.lang.System.out.println((("Sink thread " + (java.lang.Thread.currentThread().getId())) + " found a solution"));
                    peer.stopFlag.set(true);
                    java.lang.System.out.println((((("Thread " + (java.lang.Thread.currentThread().getId())) + " took : ") + loopCounter) + " iterations"));
                    return sinkLocalMin.get();
                }
            }
            loopCounter++;
        } 
        java.lang.System.out.println((((("Thread " + (java.lang.Thread.currentThread().getId())) + " took : ") + loopCounter) + " iterations"));
        return sinkLocalMin.get();
    }

    @java.lang.Override
    public void run() {
        execute();
    }
}

