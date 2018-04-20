package bbfs;

import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import pu.RedLib.Reducible;
import utils.CostComparatorForVertices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SinkTask<V extends Vertex, E extends DirectedEdge<V>> implements Task, Runnable {

    private BasicDirectedGraph<V, E> graph;

    private SourceTask<V, E> peer;

    void setPeer(SourceTask<V, E> peer) {
        this.peer = peer;
    }

    SourceTask<V, E> getPeer() {
        return this.peer;
    }

    // get maps and dumps, and a reference to the Reducible
    // following are SHARED data structures, passed by setter, shared with the source peer, make sure maps are copied
    private ConcurrentHashMap<V, V> routeMapFromSink;
    private ConcurrentHashMap<V, Integer> sinkRouteCost;
    private MutableInt sinkDump;

    // following are private data structures, passed by setter, make sure they are copied
    private ArrayList<V> sinkFrontier;
    private HashSet<V> sinkClose;

    // following are SHARED data structures for sink side, passed by setter, make sure they are passed by reference
    private Reducible<CostNamePair<V>> sinkLocalMin;

    // following are private data structures, initialized internally
    private CostComparatorForVertices<V> sinkComparator;

    SinkTask(BasicDirectedGraph<V, E> graph) {
        this.graph = graph;
    }

    ConcurrentHashMap<V, V> getRouteMapFromSink() {
        return routeMapFromSink;
    }

    ConcurrentHashMap<V, Integer> getSinkRouteCost() {
        return sinkRouteCost;
    }

    MutableInt getSinkDump() {
        return sinkDump;
    }

    AtomicBoolean stopFlag = new AtomicBoolean();

    void setRouteMapFromSink(ConcurrentHashMap<V, V> routeMapFromSink) {
        this.routeMapFromSink = routeMapFromSink;
    }

    void setSinkRouteCost(ConcurrentHashMap<V, Integer> sinkRouteCost) {
        this.sinkRouteCost = sinkRouteCost;
        this.sinkComparator = new CostComparatorForVertices<>(this.sinkRouteCost);
    }

    void setSinkDump(MutableInt sinkDump) {
        this.sinkDump = sinkDump;
    }

    void setSinkLocalMin(Reducible<CostNamePair<V>> sinkLocalMin) {
        this.sinkLocalMin = sinkLocalMin;
    }

    void setSinkFrontier(ArrayList<V> sinkFrontier) {
        this.sinkFrontier = sinkFrontier;
    }

    void setSinkClose(HashSet<V> sinkClose) {
        this.sinkClose = sinkClose;
    }

    @Override
    public CostNamePair<V> execute() {

        System.out.println("thread id from sink task : " + Thread.currentThread().getId());


        int numOfNodes = graph.verticesSet().size();
        int loopCounter = 0;

//        while (!(peer.getSourceDump().get() != 0 && sinkDump.get() != 0 & peer.getSourceDump().get() + sinkDump
//                .get() >= sinkLocalMin.get().getCost())) {                  && loopCounter < numOfNodes/2
        while (!stopFlag.get()) {

            sinkFrontier.sort(sinkComparator);
            V bar = sinkFrontier.get(0);
            sinkFrontier.remove(bar);

            sinkClose.add(bar);
            sinkDump.set(sinkRouteCost.get(bar));

            int dump0 = peer.getSourceDump().get();
            int dump1 = sinkDump.get();

//            System.out.println("Thread " + Thread.currentThread().getId() + " source dump : " + dump0 + " sink dump :" +
//                    " " + dump1);

//            graph.parentsIterator(bar).forEachRemaining(parent ->
            for (V parent : graph.parents(bar)) {
                int newCost = sinkRouteCost.get(bar) + parent.weight() + graph.edgeBetween(parent, bar).weight();
                if (newCost < sinkRouteCost.get(parent)) {
                    sinkRouteCost.replace(parent, newCost);
                    routeMapFromSink.put(parent, bar); // frontier -> close
                }

                // try to update the global least cost path so far
                int sourceRouteCostForThisVisitedNode = peer.getSourceRouteCost().get(parent);
                if (sourceRouteCostForThisVisitedNode != Integer.MAX_VALUE) {
                    int temp = sinkRouteCost.get(parent) + sourceRouteCostForThisVisitedNode - parent.weight();
                    if (temp < sinkLocalMin.get().getCost()) {
                        sinkLocalMin.set(new CostNamePair<>(temp, parent, peer.getRouteMapFromSource(), routeMapFromSink));
                    }
                }

                if (dump0 != 0 && dump1 != 0 & dump0 + dump1 >= sinkLocalMin.get().getCost()) {
                    System.out.println("Sink thread " + Thread.currentThread().getId() + " found a solution");
                    peer.stopFlag.set(true);
                    System.out.println("Thread " + Thread.currentThread().getId() + " took : " + loopCounter + " iterations");

                    return sinkLocalMin.get(); // just in case if manual reduction is needed (i.e., if Reducible bugs out)

                }

            }

            loopCounter++;

//            for (V visited : sinkClose) {
//                // try to update the global least cost path so far
//                int sourceRouteCostForThisVisitedNode = peer.getSourceRouteCost().get(visited);
//                if (sourceRouteCostForThisVisitedNode != Integer.MAX_VALUE) {
//                    int temp = sinkRouteCost.get(visited) + sourceRouteCostForThisVisitedNode - visited.weight();
//                    if (temp < sinkLocalMin.get().getCost()) {
//                        sinkLocalMin.set(new CostNamePair<>(temp, visited, peer.getRouteMapFromSource(), routeMapFromSink));
//                    }
//                }
//            }

        }

        System.out.println("Thread " + Thread.currentThread().getId() + " took : " + loopCounter + " iterations");

        return sinkLocalMin.get(); // just in case if manual reduction is needed (i.e., if Reducible bugs out)
    }

    @Override
    public void run() {
        execute();
    }
}
