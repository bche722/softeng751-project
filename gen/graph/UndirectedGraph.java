

package graph;


public interface UndirectedGraph<V extends graph.Vertex, E extends graph.UndirectedEdge<V>> extends graph.Graph<V, E> {
    public boolean add(E edge);

    public graph.UndirectedEdge<V> edgeBetween(V source, V dest);
}

