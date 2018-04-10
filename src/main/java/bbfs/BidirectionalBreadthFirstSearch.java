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
    // sourceFrontier -> sourceClose
    private ConcurrentHashMap<V, V> routeMapFromSource = new ConcurrentHashMap<>();

    // record the last step from sink, the key is the PARENT, and the value is the CHILD, topological order:
    // sinkFrontier -> sinkClose
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

        // start searching
        while (loopCounter.get() >= 0) {

            if (loopCounter.get()%2 == 0) {

                // source turn
                V foo = sourceFrontier.poll();
                System.out.println("start with " + foo.name());
                graph.childrenIterator(foo).forEachRemaining(child -> {
                    int newCost = sourceRouteCost.get(foo) + child.weight() + graph.edgeBetween(foo, child).weight();
                    System.out.println("child name: " + child.name());
                    sourceRouteCost.keySet().forEach(key -> System.out.println("key name: " + key.name()));

                    if (newCost < sourceRouteCost.get(child)) {
                        sourceRouteCost.replace(child, newCost);
                        routeMapFromSource.put(child, foo); // frontier -> close
                    }
                });
                sourceClose.add(foo);

            } else {

                // sink turn
                V bar = sinkFrontier.poll();
                graph.parentsIterator(bar).forEachRemaining(parent -> {
                    int newCost = sinkRouteCost.get(bar) + parent.weight() + graph.edgeBetween(parent, bar).weight();
                    if (newCost < sinkRouteCost.get(parent)) {
                        sinkRouteCost.replace(parent, newCost);
                        routeMapFromSink.put(parent, bar); // frontier -> close
                    }
                });
                sinkClose.add(bar);

            }

            // stopping criterion: check collision
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

            // increment the counter
            loopCounter.addAndGet(1);

        }
    }

    private V getKeyObjByName(){
        return null;
    }


    private void parallelSearch(){
        //todo
    }
}
