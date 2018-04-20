

package graph;


public interface SimpleEdge<V extends graph.Vertex> extends graph.Edge<V> {
    public graph.Vertex other(V v);
}

