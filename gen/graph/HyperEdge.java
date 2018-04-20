

package graph;


public interface HyperEdge<V extends graph.Vertex> extends graph.Edge<V> {
    public java.util.Iterator<V> otherVerticesIterator(V v);

    public java.lang.Iterable<V> otherVertices(V v);

    public int size();

    public boolean contains(V v);
}

