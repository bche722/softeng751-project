

package graph;


public interface DirectedHyperEdge<V extends graph.Vertex> extends graph.HyperEdge<V> {
    public java.util.Iterator<V> fromVerticesIterator();

    public java.lang.Iterable<V> fromVertices();

    public java.util.Iterator<V> toVerticesIterator();

    public java.lang.Iterable<V> toVertices();
}

