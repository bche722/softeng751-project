

package graph;


public abstract interface Graph<V extends graph.Vertex, E extends graph.Edge<V>> {
    public java.lang.String name();

    public void setName(java.lang.String name);

    public int size();

    public int sizeVertices();

    public int sizeEdges();

    public java.lang.String type();

    public boolean isDirected();

    public boolean isSimple();

    public boolean contains(V v);

    public boolean contains(E e);

    public boolean containsCongruent(graph.Edge<V> e);

    public boolean contains(graph.Graph<V, E> g);

    public java.util.Iterator<V> verticesIterator();

    public java.lang.Iterable<V> vertices();

    public java.util.Iterator<E> edgesIterator();

    public java.lang.Iterable<E> edges();

    public java.util.Iterator<V> DFSIterator();

    public java.lang.Iterable<V> DFS();

    public java.util.Iterator<V> BFSIterator(V start);

    public java.lang.Iterable<V> BFS(V start);

    public java.util.Iterator<E> incidentEdgesIterator(V v);

    public java.lang.Iterable<E> incidentEdges(V v);

    public java.util.Iterator<V> adjacentVerticesIterator(V v);

    public java.lang.Iterable<V> adjacentVertices(V v);

    public boolean add(V v);

    public boolean add(E edge);

    public boolean remove(V v);

    public int degree(V v);

    public boolean remove(E e);

    public boolean isConnected(V primus, V secundus);

    public graph.Path shortestPathBySteps(V primus, V secundus);

    public graph.Path shortestPathByWeight(V primus, V secundus);

    public graph.Graph copy();

    public graph.Graph load(graph.GXLReader input);

    public void save(graph.GXLWriter writer) throws java.io.IOException;

    public java.lang.String toXML();

    public E edgeForName(java.lang.String s);

    public V vertexForName(java.lang.String s);

    public double density();
}

