package graph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


/**
 * A graph is a set of vertices, and a set of links between those vertices (edges).
 * This class is intended to create an abstract implementation for the functionality of Graphs in Java, and is meant to be subclassed
 * by more specific implementations that make use of more complex edge types and weights.
 * 
 * @author Danver Braganza
 *
 * @param <E> The edge type contained within this graph.
 * @param <W> The weighting of this graph.
 */
public abstract class AbstractGraph<V extends Vertex, E extends Edge<V>> implements Graph<V, E> {

	/**
	 * The set of vertices contained in this graph.  A graph may contain a vertex at most once.
	 */
	protected Set<V> vertices;
	private String name;
	private final String type;
	private final boolean simple;
	private final boolean directed;
	/**
	 * The set of edges contained in this graph.  Each unique edge (defined by reference equality) may only be contained in a graph once.
	 */
	protected Set<E> edges;
	
	protected Map<String, E> edgeForName;
	protected Map<String, V> vertexForName;
	/**
	 * A mapping from each vertex to a set of edges incident to it.
	 */
	protected Map<V, Set<E>> map;

	/**
	 * Constructs a GraphImplementation, that is meant to be subclassed be a more specific
	 * graph type implementation.  A GraphImplementation (as opposed to a Graph) in general is defined as a set of Vertex, a set of Edges, and 
	 * a mapping from each edge to two (or more) vertices.  In addition, the Edges in this GraphImplementation may have a direction.
	 * 
	 * @param name A String with the name of this graph.  If null or empty, the graph will be named "Unnamed".
	 * @param type A String with the full type of the graph, with the first word being either 
	 * "Directed", "Undirected" or "Hybrid" 
	 * and the second word represents the more specific type of the graph.  At present, the only types that exist are
	 * "Simple", "Multi" or "Hyper".
	 * The rest of the graph's type may be used to represent other features of the graph, as desired.
	 * @param simple This is true only if every edge in this graph only goes between two vertices, and all the vertices have the same type of directedness.
	 * ie all directed, or all undirected.
	 * @param directed If this is true, every vertex in this graph must implement DirectedEdge<W>.  If false, and this graph is simple, every vertex must 
	 * implement UndirectedEdge<W>.  If this graph is not simple, and directed is false, the type of edges contained is not gauranteed.
	 */
	protected AbstractGraph(String name, String type, boolean simple, boolean directed) {
		if (name == null || name == "") {
			this.name = "Unnamed";
		} else
			this.name = name;
		if (type == null || type == "") {
			this.type = "Hybrid Untyped";
		} else
			this.type = type;
		this.simple = simple;
		this.directed = directed;
		this.vertices = new LinkedHashSet<V>();
		this.map = new HashMap<V, Set<E>>();
		this.edges = new HashSet<E>();
		this.edgeForName = new HashMap<String, E>();
		this.vertexForName = new HashMap<String, V>();
	}

	/**
	 * Iterates over the vertices of this graph in the order they would be discovered using a breadth
	 * first search starting from the start vertex.
	 * @param start The Vertex that will be returned first, and the starting point for the search
	 */
	public Iterator<V> BFSIterator(V start) {
		return new BFSIterator<V, E>(this, start);
	}

	/**
	 * Iterates over the vertices of this graph in the order they would be discovered using a depth
	 * first search.  The starting vertex is not defined.
	 */
	public Iterator<V> DFSIterator() {
		return new DFSIterator<V, E>(this);
	}

	public abstract boolean add(V v);
	
	public abstract boolean add(E e);

	public abstract Iterator<V> adjacentVerticesIterator(V v);

	public boolean contains(V v) {
		return v == null ? false : vertices.contains(v);
	}

	public boolean contains(E e) {
		return e == null ? false : this.edges.contains(e);
	}

	public boolean contains(Graph<V, E> g) {
		boolean retval = true;
		Iterator<V> vI = g.verticesIterator();
		while (retval && vI.hasNext()) {
			retval = this.contains(vI.next());
		}
		Iterator<E> vE = g.edgesIterator();
		while (retval && vE.hasNext()) {
			retval = this.contains(vE.next());
		}
		return retval;
	}
	
	public E edgeForName(String s) {
		return this.edgeForName.get(s);
	}

	public abstract Graph<V,E> copy();

	public abstract int degree(V v);

	public String name() {
		return this.name;
	}

	public String type() {
		return this.type;
	}

	public Iterator<V> verticesIterator() {
		return new VertexIteratorWrapper<V>(this.vertices.iterator(), this);
	}
	
	public Collection<V> verticesSet() {
		return new ArrayList<V>(this.vertices);
		//ArrayList<UndirectedEdge<V, W>>(this.map.get(v));
	}
	
	public ArrayList<E> edgesSet() {
		return new ArrayList<E>(this.edges);
	}

	public abstract Iterator<E> incidentEdgesIterator(V v); 

	public boolean isConnected(V primus, V secundus) {
		boolean found = false;
		Iterator<V> it = this.BFSIterator(primus);
		while (it.hasNext() && !found) {
			found = it.next().equals(secundus);
		}
		return found;
	}

	public final boolean isDirected() {
		return this.directed;
	}

	public final boolean isSimple() {
		return this.simple;
	}

	/**
	 * Queries the input of this graph, and forwards it to the right subclass to interpret it.
	 * If you wish to use your own graph class and its own interpreter, it has to override load()
	 * to call its own doLoad() method if the type matches, and otherwise forward the call to super.load()
	 */
	@SuppressWarnings("unchecked")
	public Graph load(GXLReader input) {
		Graph returned = null;
		String type = input.getType();
		if (type.equals("Directed Simple")) {
			returned = BasicDirectedGraph.doLoad(input);
		} else if (type.equals("Undirected Simple")) {
			returned = BasicUndirectedGraph.doLoad(input);
		} else if (type.equals("Undirected Multi")) {
			returned = UndirectedMultiGraph.doLoad(input);
		} else if (type.equals("Undirected Hyper")){
			returned = UndirectedHyperGraph.doLoad(input);
		}
		return returned;
	}

	public boolean remove(V v) {
		if (!this.contains(v)) {
			return false;
		}
		Collection<E> edgesInvolved = this.map.get(v);
		Iterator<E> iE = edgesInvolved.iterator();
		ArrayList<E> temp = new ArrayList<E>();
		while (iE.hasNext()) {
			temp.add(iE.next());
		}
		for (E e : temp) {
			this.remove(e);
		}
		this.map.remove(v);
		this.vertexForName.remove(v.name());
		this.vertices.remove(v);
		return true;
	}


	public final void save(GXLWriter writer) throws IOException {
		writer.write(this);
	}

	public final void setName(String name) {
		this.name = name;

	}

	public Path shortestPathBySteps(V primus, V secundus) {
		//TODO
		return null;
	}

	public Path shortestPathByWeight(V primus, V secundus) {
		// TODO Implement later
		return null;
	}

	public int size() {
		return this.edges.size() + this.vertices.size();
	}

	public int sizeEdges() {
		return this.edges.size();
	}

	public int sizeVertices() {
		return this.vertices.size();
	}

	public double density() {
		return this.edges.size()*1.0/this.vertices.size();
	}

	public abstract String toXML();

	/* (non-Javadoc)
	 * @see graph.Graph#delete(graph.Edge)
	 */
	public abstract boolean remove(E e);

	/* (non-Javadoc)
	 * @see graph.Graph#getEdgesIterator()
	 */
	public Iterator<E> edgesIterator() {
		return new EdgeIteratorWrapper<E>(this.edges.iterator());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#BFS()
	 */
	public Iterable<V> BFS(V start) {
		return new IteratorToIterableWrapper<V>(this.BFSIterator(start));
	}

	/* (non-Javadoc)
	 * @see graph.Graph#DFS()
	 */
	public Iterable<V> DFS() {
		return new IteratorToIterableWrapper<V>(this.DFSIterator());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#adjacentVertices(graph.Vertex)
	 */
	public Iterable<V> adjacentVertices(V v) {
		return new IteratorToIterableWrapper<V>(this.adjacentVerticesIterator(v));
	}

	/* (non-Javadoc)
	 * @see graph.Graph#containsCongruent(graph.Edge)
	 */
	public abstract boolean containsCongruent(Edge<V> e);

	/* (non-Javadoc)
	 * @see graph.Graph#edges()
	 */
	public Iterable<E> edges() {
		return new IteratorToIterableWrapper<E>(this.edgesIterator());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getVertexForName(java.lang.String)
	 */
	public abstract V vertexForName(String s);

	/* (non-Javadoc)
	 * @see graph.Graph#incidentEdges(graph.Vertex)
	 */
	public Iterable<E> incidentEdges(V v) {
		return new IteratorToIterableWrapper<E>(this.incidentEdgesIterator(v));
	}

	/* (non-Javadoc)
	 * @see graph.Graph#vertices()
	 */
	public Iterable<V> vertices() {
		return new IteratorToIterableWrapper<V>(this.verticesIterator());
	}
}

class BFSIterator<V extends Vertex, E extends Edge<V>> implements Iterator<V> {

	private Deque<V> queue;
	private Graph<V, E> g;
	private HashSet<V> vertices;

	public BFSIterator(Graph<V, E> g, V start) {
		queue = new LinkedList<V>();
		this.g = g;
		vertices = new HashSet<V>();
		if (g.contains(start)) {
			queue.add(start);
			vertices.add(start);
		}
	}

	public boolean hasNext() {
		return !queue.isEmpty();
	}

	public V next() {
		V next = queue.pollFirst();
		if (g == null)
			throw new NullPointerException("g is null");
		Iterator<V> vI = g.adjacentVerticesIterator(next);
		if (vI != null) {
			while (vI.hasNext()) {
				V temp = vI.next();
				if (vertices.add(temp)) {
					queue.offerLast(temp);
				}
			}
		}
		return next;
	}

	public void remove() {
		queue.removeFirst();
	}
}

class DFSIterator<V extends Vertex, E extends Edge<V>> implements Iterator<V> {

	private Set<V> whiteList;
	private Graph<V, E> g;
	private Deque<V> blackList;

	public DFSIterator(Graph<V, E> g) {
		whiteList = new HashSet<V>();
		blackList = new LinkedList<V>();

		Iterator<V> vI = g.verticesIterator();
		while (vI.hasNext()) {
			whiteList.add(vI.next());
		}
	}

	public boolean hasNext() {
		return !whiteList.isEmpty() || !blackList.isEmpty();
	}

	/**
	 * 
	 * @throws NoSuchElementException
	 */
	public V next() {
		V next = blackList.pollFirst();
		if (next != null) {
			Iterator<V> vI = g.adjacentVerticesIterator(next);
			while (vI.hasNext()) {
				if (whiteList.remove(vI.next())) {
					blackList.offerFirst(vI.next());
				}
			}
		} else {
			next = whiteList.iterator().next();
			Iterator<V> vI = g.adjacentVerticesIterator(next);
			while (vI.hasNext()) {
				blackList.offerFirst(vI.next());
			}
		}
		return next;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}

/**
 * All this class attempts to do is wrap an iterator over vertices so that any attempts to call
 * remove on the underlying data structure of the graph will instead forward remove to the graph itself.
 *
 * @author Danver Braganza
 *
 * @param <W>
 */
class VertexIteratorWrapper<V extends Vertex> implements Iterator<V> {
	private final Iterator<V> wrapped;
	private final Graph g;
	private V current;

	protected VertexIteratorWrapper(Iterator<V> wrapped, Graph g) {
		this.g = g;
		this.wrapped = wrapped;
	}
	
	protected VertexIteratorWrapper(Iterator<V> wrapped) {
		this.wrapped = wrapped;
		this.g = null;
	}

	public boolean hasNext() {
		return wrapped.hasNext();
	}

	public V next() {
		return current = wrapped.next();
	}

	@SuppressWarnings("unchecked")
	public void remove() {
		if (current != null) {
			if (g != null) {
				//Remove from graph g.
				g.remove(current);
				current = null;
			} else {
				//No Graph given, can't remove.
				throw new UnsupportedOperationException();
			}
		} else {
			throw new IllegalStateException();
		}
	}

}

/**
 * This class wraps an edge iterator, and acts as an iterator over all the vertices represented
 * by the edges.  The second parameter allows the user to specify an edge that will not be returned.
 * 
 * This is so the user can wrap all the edges incident on a given vertex v, and by passing in v as an argument,
 * only have the other vertices that are neighbours of v iterated over.
 * 
 * NB: Since each vertex must only be returned once by every iterator, it is important to wrap this iterator
 * with the UniqueVertexIteratorWrapper in MultiGraphs or HyperGraphs.
 * 
 * @author Danver Braganza
 *
 * @param <W>
 */
class EdgeToVertexIteratorWrapper<V extends Vertex> implements Iterator<V> {
	private Iterator<? extends Edge<V>> wrapped;
	private Iterator<V> vertIt;
	private V v, next;

	protected EdgeToVertexIteratorWrapper(Iterator<? extends Edge<V>> wrapped, V v) {
		this.wrapped = wrapped;
		this.v = v;
		this.vertIt = wrapped.hasNext() ? wrapped.next().incidentVertices() : null;
		this.hasNext();
	}

	public boolean hasNext() {
		if (vertIt == null) {
			return false; //Wrapped was empty, so there's no vertexIterator.
		}
		while ((wrapped.hasNext() || vertIt.hasNext()) && next == null) {
			if (vertIt.hasNext()) {
				next = vertIt.next();
				if (next.equals(v))
					next = null;
			} else {
				vertIt = wrapped.next().incidentVertices();
			}
		}
		return next != null;
	}

	public V next() {
		V temp = next;
		next = null;
		return temp;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}

/**
 * All this class attempts to do is wrap an iterator over edges so that any attempts to call
 * remove on the underlying data structure of the graph will instead forward remove to the graph itself.
 * 
 * @author Danver Braganza
 *
 * @param <E>
 */
class EdgeIteratorWrapper<E extends Edge<? extends Vertex>> implements Iterator<E> {
	private Iterator<E> wrapped;

	protected EdgeIteratorWrapper(Iterator<E> wrapped) {
		this.wrapped = wrapped;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return wrapped.hasNext();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public E next() {
		return wrapped.next();
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 * @throws IllegalStateException
	 */
	public void remove() {
		throw new UnsupportedOperationException("Removal not supported.");
	}
}

/**
 * This Wrapper class ensures that the wrapped iterator's duplicate elements are never shown
 * to the outside world.
 * 
 * @author Danver Braganza
 *
 * @param <W>
 */
class UniqueVertexIteratorWrapper<V extends Vertex> implements Iterator<V> {
	
	private Iterator<V> wrapped;
	private HashSet<V> vertices;
	private V vert;

	protected UniqueVertexIteratorWrapper(Iterator<V> wrapped) {
		this.wrapped = wrapped;
		this.vertices = new HashSet<V>();
		if (wrapped.hasNext()) {
			vert = wrapped.next();
			vertices.add(vert);
		}
	}
	

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return vert != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public V next() {
		V temp = vert;
		vert = null;
		while (wrapped.hasNext() && vert == null){
			vert = wrapped.next();
			if (!vertices.add(vert)) {
				vert = null;
			}
		}
		return temp;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
		
}
