

package graph;


public interface DirectedAcyclicGraph<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends graph.DirectedGraph<V, E> {
    public java.util.Iterator<V> topologicalOrderIterator();

    public java.lang.Iterable<V> topologicalOrder();

    public java.util.Iterator<V> inverseTopologicalOrderIterator();

    public java.lang.Iterable<V> inverseTopologicalOrder();

    public java.util.Iterator<V> descendentsIterator(V v);

    public java.lang.Iterable<V> descendents(V v);

    public java.util.Iterator<V> ancestorsIterator(V v);

    public java.lang.Iterable<V> ancestors(V v);
}

