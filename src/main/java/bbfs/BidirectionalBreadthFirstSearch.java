package bbfs;

import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import interfaces.Algorithm;

public class BidirectionalBreadthFirstSearch<V extends Vertex, E extends DirectedEdge<V>> extends Algorithm<Vertex, DirectedEdge<Vertex>> {

    private BasicDirectedGraph<V, E> graph;

    public BidirectionalBreadthFirstSearch(BasicDirectedGraph<V, E> graph, boolean isParallel) {
        this.graph = graph;
        this.isParallel = isParallel;
    }

    @Override
    public void doTheJob() {
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

        //todo - algorithm implementation
        if (isParallel){

            //todo - parallel implementation goes here

        } else {

            //todo - sequential implementation goes here

        }
    }
}
