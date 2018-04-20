

package graph;


public interface DirectedGraph<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends graph.Graph<V, E> {
    public int inDegree(V v);

    public int outDegree(V v);

    public java.util.Iterator<E> inEdgesIterator(V v);

    public java.lang.Iterable<E> inEdges(V v);

    public java.util.Iterator<E> outEdgesIterator(V v);

    public java.lang.Iterable<E> outEdges(V v);

    public java.util.Iterator<V> parentsIterator(V v);

    public java.lang.Iterable<V> parents(V v);

    public java.util.Iterator<V> childrenIterator(V v);

    public java.lang.Iterable<V> children(V v);

    public java.util.Iterator<V> sourcesIterator();

    public java.lang.Iterable<V> sources();

    public java.util.Iterator<V> sinksIterator();

    public java.lang.Iterable<V> sinks();

    public E edgeBetween(V source, V dest);
}

