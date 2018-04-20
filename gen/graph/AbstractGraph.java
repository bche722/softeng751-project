

package graph;


public abstract class AbstractGraph<V extends graph.Vertex, E extends graph.Edge<V>> implements graph.Graph<V, E> {
    protected java.util.Set<V> vertices;

    private java.lang.String name;

    private final java.lang.String type;

    private final boolean simple;

    private final boolean directed;

    protected java.util.Set<E> edges;

    protected java.util.Map<java.lang.String, E> edgeForName;

    protected java.util.Map<java.lang.String, V> vertexForName;

    protected java.util.Map<V, java.util.Set<E>> map;

    protected AbstractGraph(java.lang.String name, java.lang.String type, boolean simple, boolean directed) {
        if ((name == null) || (name == "")) {
            graph.AbstractGraph.this.name = "Unnamed";
        }else
            graph.AbstractGraph.this.name = name;
        
        if ((type == null) || (type == "")) {
            this.type = "Hybrid Untyped";
        }else
            this.type = type;
        
        this.simple = simple;
        this.directed = directed;
        graph.AbstractGraph.this.vertices = new java.util.LinkedHashSet<V>();
        graph.AbstractGraph.this.map = new java.util.HashMap<V, java.util.Set<E>>();
        graph.AbstractGraph.this.edges = new java.util.HashSet<E>();
        graph.AbstractGraph.this.edgeForName = new java.util.HashMap<java.lang.String, E>();
        graph.AbstractGraph.this.vertexForName = new java.util.HashMap<java.lang.String, V>();
    }

    public java.util.Iterator<V> BFSIterator(V start) {
        return new graph.BFSIterator<V, E>(graph.AbstractGraph.this, start);
    }

    public java.util.Iterator<V> DFSIterator() {
        return new graph.DFSIterator<V, E>(graph.AbstractGraph.this);
    }

    public abstract boolean add(V v);

    public abstract boolean add(E e);

    public abstract java.util.Iterator<V> adjacentVerticesIterator(V v);

    public boolean contains(V v) {
        return v == null ? false : vertices.contains(v);
    }

    public boolean contains(E e) {
        return e == null ? false : graph.AbstractGraph.this.edges.contains(e);
    }

    public boolean contains(graph.Graph<V, E> g) {
        boolean retval = true;
        java.util.Iterator<V> vI = g.verticesIterator();
        while (retval && (vI.hasNext())) {
            retval = graph.AbstractGraph.this.contains(vI.next());
        } 
        java.util.Iterator<E> vE = g.edgesIterator();
        while (retval && (vE.hasNext())) {
            retval = graph.AbstractGraph.this.contains(vE.next());
        } 
        return retval;
    }

    public E edgeForName(java.lang.String s) {
        return graph.AbstractGraph.this.edgeForName.get(s);
    }

    public abstract graph.Graph<V, E> copy();

    public abstract int degree(V v);

    public java.lang.String name() {
        return graph.AbstractGraph.this.name;
    }

    public java.lang.String type() {
        return graph.AbstractGraph.this.type;
    }

    public java.util.Iterator<V> verticesIterator() {
        return new graph.VertexIteratorWrapper<V>(graph.AbstractGraph.this.vertices.iterator(), graph.AbstractGraph.this);
    }

    public java.util.Collection<V> verticesSet() {
        return new java.util.ArrayList<V>(graph.AbstractGraph.this.vertices);
    }

    public java.util.ArrayList<E> edgesSet() {
        return new java.util.ArrayList<E>(graph.AbstractGraph.this.edges);
    }

    public abstract java.util.Iterator<E> incidentEdgesIterator(V v);

    public boolean isConnected(V primus, V secundus) {
        boolean found = false;
        java.util.Iterator<V> it = graph.AbstractGraph.this.BFSIterator(primus);
        while ((it.hasNext()) && (!found)) {
            found = it.next().equals(secundus);
        } 
        return found;
    }

    public final boolean isDirected() {
        return graph.AbstractGraph.this.directed;
    }

    public final boolean isSimple() {
        return graph.AbstractGraph.this.simple;
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public graph.Graph load(graph.GXLReader input) {
        graph.Graph returned = null;
        java.lang.String type = input.getType();
        if (type.equals("Directed Simple")) {
            returned = graph.BasicDirectedGraph.doLoad(input);
        }else
            if (type.equals("Undirected Simple")) {
                returned = graph.BasicUndirectedGraph.doLoad(input);
            }else
                if (type.equals("Undirected Multi")) {
                    returned = graph.UndirectedMultiGraph.doLoad(input);
                }else
                    if (type.equals("Undirected Hyper")) {
                        returned = graph.UndirectedHyperGraph.doLoad(input);
                    }
                
            
        
        return returned;
    }

    public boolean remove(V v) {
        if (!(graph.AbstractGraph.this.contains(v))) {
            return false;
        }
        java.util.Collection<E> edgesInvolved = graph.AbstractGraph.this.map.get(v);
        java.util.Iterator<E> iE = edgesInvolved.iterator();
        java.util.ArrayList<E> temp = new java.util.ArrayList<E>();
        while (iE.hasNext()) {
            temp.add(iE.next());
        } 
        for (E e : temp) {
            graph.AbstractGraph.this.remove(e);
        }
        graph.AbstractGraph.this.map.remove(v);
        graph.AbstractGraph.this.vertexForName.remove(v.name());
        graph.AbstractGraph.this.vertices.remove(v);
        return true;
    }

    public final void save(graph.GXLWriter writer) throws java.io.IOException {
        writer.write(graph.AbstractGraph.this);
    }

    public final void setName(java.lang.String name) {
        graph.AbstractGraph.this.name = name;
    }

    public graph.Path shortestPathBySteps(V primus, V secundus) {
        return null;
    }

    public graph.Path shortestPathByWeight(V primus, V secundus) {
        return null;
    }

    public int size() {
        return (graph.AbstractGraph.this.edges.size()) + (graph.AbstractGraph.this.vertices.size());
    }

    public int sizeEdges() {
        return graph.AbstractGraph.this.edges.size();
    }

    public int sizeVertices() {
        return graph.AbstractGraph.this.vertices.size();
    }

    public double density() {
        return ((graph.AbstractGraph.this.edges.size()) * 1.0) / (graph.AbstractGraph.this.vertices.size());
    }

    public abstract java.lang.String toXML();

    public abstract boolean remove(E e);

    public java.util.Iterator<E> edgesIterator() {
        return new graph.EdgeIteratorWrapper<E>(graph.AbstractGraph.this.edges.iterator());
    }

    public java.lang.Iterable<V> BFS(V start) {
        return new graph.IteratorToIterableWrapper<V>(graph.AbstractGraph.this.BFSIterator(start));
    }

    public java.lang.Iterable<V> DFS() {
        return new graph.IteratorToIterableWrapper<V>(graph.AbstractGraph.this.DFSIterator());
    }

    public java.lang.Iterable<V> adjacentVertices(V v) {
        return new graph.IteratorToIterableWrapper<V>(graph.AbstractGraph.this.adjacentVerticesIterator(v));
    }

    public abstract boolean containsCongruent(graph.Edge<V> e);

    public java.lang.Iterable<E> edges() {
        return new graph.IteratorToIterableWrapper<E>(graph.AbstractGraph.this.edgesIterator());
    }

    public abstract V vertexForName(java.lang.String s);

    public java.lang.Iterable<E> incidentEdges(V v) {
        return new graph.IteratorToIterableWrapper<E>(graph.AbstractGraph.this.incidentEdgesIterator(v));
    }

    public java.lang.Iterable<V> vertices() {
        return new graph.IteratorToIterableWrapper<V>(graph.AbstractGraph.this.verticesIterator());
    }
}

