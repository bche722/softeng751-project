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

public class SourceTask<V extends Vertex, E extends DirectedEdge<V>> implements Task, Runnable {

    private BasicDirectedGraph<V, E> graph;

    private SinkTask<V, E> peer;

    void setPeer(SinkTask<V, E> peer) {
        this.peer = peer;
    }

    SinkTask<V, E> getPeer() {
        return this.peer;
    }

    // get maps and dumps, and a reference to the Reducible
    // following are SHARED data structures, passed by setter, shared with the sink peer, make sure maps are copied
    private ConcurrentHashMap<V, V> routeMapFromSource;
    private ConcurrentHashMap<V, Integer> sourceRouteCost;
    private MutableInt sourceDump;

    // following are private data structures, passed by setter, make sure they are copied
    private ArrayList<V> sourceFrontier;
    private HashSet<V> sourceClose;

    // following are SHARED data structures for source side, passed by setter, make sure they are passed by reference
    private Reducible<CostNamePair<V>> sourceLocalMin;

    // following are private data structures, initialized internally
    private CostComparatorForVertices<V> sourceComparator;

    SourceTask(BasicDirectedGraph<V, E> graph) {
        this.graph = graph;
    }

    ConcurrentHashMap<V, V> getRouteMapFromSource() {
        return routeMapFromSource;
    }

    ConcurrentHashMap<V, Integer> getSourceRouteCost() {
        return sourceRouteCost;
    }

    MutableInt getSourceDump() {
        return sourceDump;
    }

    AtomicBoolean stopFlag = new AtomicBoolean();

    void setRouteMapFromSource(ConcurrentHashMap<V, V> routeMapFromSource) {
        this.routeMapFromSource = routeMapFromSource;
    }

    void setSourceRouteCost(ConcurrentHashMap<V, Integer> sourceRouteCost) {
        this.sourceRouteCost = sourceRouteCost;
        this.sourceComparator = new CostComparatorForVertices<>(this.sourceRouteCost);
    }

    void setSourceDump(MutableInt sourceDump) {
        this.sourceDump = sourceDump;
    }

    void setSourceLocalMin(Reducible<CostNamePair<V>> sourceLocalMin) {
        this.sourceLocalMin = sourceLocalMin;
    }

    void setSourceFrontier(ArrayList<V> sourceFrontier) {
        this.sourceFrontier = sourceFrontier;
    }

    void setSourceClose(HashSet<V> sourceClose) {
        this.sourceClose = sourceClose;
    }

    @Override
    public CostNamePair<V> execute() {

        System.out.println("thread id from source task : " + Thread.currentThread().getId());


        //System.out.println(sourceDump.get());
        //System.out.println(peer.getSinkDump().get());
        //System.out.println(sourceLocalMin);

        int numOfNodes = graph.verticesSet().size();
        int loopCounter = 0;

//        while (!(sourceDump.get() != 0 && peer.getSinkDump().get() != 0 & sourceDump.get() + peer.getSinkDump()
//                .get() >= sourceLocalMin.get().getCost())) {                 && loopCounter < numOfNodes/2
        while (!stopFlag.get()) {

            sourceFrontier.sort(sourceComparator);
            V foo = sourceFrontier.get(0);
            sourceFrontier.remove(foo);

            sourceClose.add(foo);
            sourceDump.set(sourceRouteCost.get(foo));

            int dump0 = sourceDump.get();
            int dump1 = peer.getSinkDump().get();

//            System.out.println("Thread " + Thread.currentThread().getId() + " source dump : " + dump0 + " sink dump :" +
//                    " " + dump1);

//            graph.getChildsIterator(foo).forEachRemaining(child ->
            for (V child : graph.children(foo)) {
                int newCost = sourceRouteCost.get(foo) + child.weight() + graph.edgeBetween(foo, child).weight();
                if (newCost < sourceRouteCost.get(child)) {
                    sourceRouteCost.replace(child, newCost);
                    routeMapFromSource.put(child, foo); // frontier -> close
                }

                // try to update the local least cost path so far
                // try to update the global least cost path so far
                int sinkRouteCostForThisVisitedNode = peer.getSinkRouteCost().get(child);
                if (sinkRouteCostForThisVisitedNode != Integer.MAX_VALUE) {
                    int temp = sourceRouteCost.get(child) + sinkRouteCostForThisVisitedNode - child.weight();
                    if (temp < sourceLocalMin.get().getCost()) {
                        sourceLocalMin.set(new CostNamePair<>(temp, child, routeMapFromSource, peer.getRouteMapFromSink()));
                    }
                }

                if (dump0 != 0 && dump1 != 0 & dump0 + dump1 >= sourceLocalMin.get().getCost()) {
                    System.out.println("Source thread " + Thread.currentThread().getId() + " found a solution");
                    peer.stopFlag.set(true);
                    System.out.println("Thread " + Thread.currentThread().getId() + " took : " + loopCounter + " iterations");

                    return sourceLocalMin.get(); // just in case if manual reduction is needed (i.e., if Reducible bugs out)

                }

            }

            loopCounter++;

//            sourceClose.forEach(visited -> {
//                // try to update the global least cost path so far
//                int sinkRouteCostForThisVisitedNode = peer.getSinkRouteCost().get(visited);
//                if (sinkRouteCostForThisVisitedNode != Integer.MAX_VALUE) {
//                    int temp = sourceRouteCost.get(visited) + sinkRouteCostForThisVisitedNode - visited.weight();
//                    if (temp < sourceLocalMin.get().getCost()) {
//                        sourceLocalMin.set(new CostNamePair<>(temp, visited, routeMapFromSource, peer.getRouteMapFromSink()));
//                    }
//                }
//            });

        }

        System.out.println("Thread " + Thread.currentThread().getId() + " took : " + loopCounter + " iterations");

        return sourceLocalMin.get(); // just in case if manual reduction is needed (i.e., if Reducible bugs out)
    }

    @Override
    public void run() {
        execute();
    }
}
