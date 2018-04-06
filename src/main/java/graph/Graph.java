package graph;


import java.io.IOException;
import java.util.Iterator;



/**
 * Graph superinterface.  Provides common methods for dealing with Graphs.
 * Not intended to be implemented by itself.  Instead,
 * implement one of the subinterfaces of this Graph: DirectedGraph,
 * UndirectedGraph, MultiGraph, HyperGraph etc.
 * 
 * All Vertices in a Graph must have unique names, and appear only once.
 * All Edges in a Graph must have unique names.
 *  
 * @author Danver Braganza, Oliver Sinnen
 *
 * @param <E> The Edge type that this Graph uses.
 * @param <W> The weight type this Graph uses.
 */
public abstract interface Graph<V extends Vertex, E extends Edge<V>> {
	
	/**
	 * Get the name of this graph
	 * @return String the String of this Graph's name.
	 */
	public String name();
	
	/**
	 * Set the name of this graph
	 * @param name String the new name of this Graph.
	 */
	public void setName(String name);

	/**
	 * The size of the graph is the number of edges plus the number of vertices.
	 * @return int the total number of edges and vertices in this graph.
	 */
	public int size();
	
	/**
	 * Get the number of Vertices in this graph.
	 * @return int the number of vertices.
	 */
	public int sizeVertices();

	/**
	 * Get the number of Vertices in this graph.
	 * @return int the number of vertices.
	 */
	public int sizeEdges();
	
	/**
	 * Get the type of this graph 
	 * A String with the full type of the graph, with the first word being either 
	 * "Directed", "Undirected" or "Hybrid" 
	 * and the second word represents the more specific type of the graph.  At present, the only types that exist are
	 * "Simple", "Multi" or "Hyper".
	 * The rest of the graph's type may be used to represent other features of the graph, as desired.
	 * @return String representing the specific type of this graph.
	 */
	public String type();
	
	/**
	 * Query whether the graph is directed
	 * @return true if the only edges in this graph are implement DirectedEdge
	 */
	public boolean isDirected();
	
	/**
	 * Query whether the graph is simple
	 * @return true if and only if all the edges in this graph implement SimpleEdge;
	 */
	public boolean isSimple();

	/**
	 * Query whether the graph contains the specified vertex.
	 * @param v A Vertex.
	 * @return true if and only if v is a vertex that is part of this graph. False if v is null.  
	 */
	public boolean contains(V v);

	/**
	 * Query whether the graph contains the specified edge.
	 * NB: This checks for the exact edge itself, and not a different 
	 * edge object that is congruent to it. 
	 * 
	 * @param e An Edge
	 * @return true if and only if e itself is part of the graph.
	 * @see containsCongruent
	 */
	public boolean contains(E e);
	
	/**
	 * Query whether the graph already contains an edge parallel to the the given edge.
	 * An edge is parallel to another if they have the same source vertices and the same 
	 * destination vertices.
	 * 
	 * @param e an Edge
	 * @return true if and only if the graph contains an edge that is parallel to e.
	 */
	public boolean containsCongruent(Edge<V> e);

	/**
	 * Query whether a graph g is a subgraph of this one.  A graph g is a subgraph
	 * of f if every vertex in g is also present in f, and every edge in g is 
	 * present in f.
	 * 
	 * @param g A graph.
	 * @return true if g is a subgraph of this.
	 */
	public boolean contains(Graph<V, E> g);

	/**
	 * Gets an iterator over every vertex that is currently in the graph.
	 * 
	 * @return Iterator<Vertex<W>> over the set of vertices currently in the graph.  No vertex is repeated twice.
	 */
	public Iterator<V> verticesIterator();
	
	/**
	 * 
	 * @return an iterable object whose iterator() method returns this.verticesIterator().
	 */
	public Iterable<V> vertices();
	
	/**
	 * Gets an iterator over every edge that is currently in the graph
	 * 
	 * @return Iterator over the set of edges in the graph.  No edge is repeated twice.
	 */
	public Iterator<E> edgesIterator();
	
	/**
	 * 
	 * @return an iterable object whose iterator() method returns this.edgesIterator().
	 */
	public Iterable<E> edges();
	
	/**
	 * This method returns an ordering of the vertices according to their discovery
	 * in depth-first search order.  Every vertex is discovered. 
	 * 
	 * @return an iterator over the vertices of this graph, ordered according to
	 * Depth First search order. 
	 */
	public Iterator<V> DFSIterator();
	
	/**
	 * 
	 * @return an iterable object whose iterator() method returns this.DFSIterator().
	 */
	public Iterable<V> DFS();
	
	/**
	 * This method returns an ordering of all the vertices reachable from
	 * a given Vertex, in the order that they are discovered during a breadth-first
	 * search
	 * 
	 * @param start Vertex in this graph that will be the first discovered.
	 * @return an Iterator over all the vertices reachable from start, beginning with start
	 * and in breadth-first search order.
	 */
	public Iterator<V> BFSIterator(V start);
	
	/**
	 * 
	 * @return an iterable object whose iterator() method returns this.BFSIterator().
	 */
	public Iterable<V> BFS(V start);

	/**
	 * This method returns an iterator over ALL edges that are either ingoing or outcoming from Vertex v.
	 * 
	 * @param v A Vertex in this Graph.
	 * @return
	 */
	public Iterator<E> incidentEdgesIterator(V v);
	
	/**
	 * @param v A Vertex in this Graph. 
	 * @return an iterable object whose iterator() method returns this.incidentEdgesIterator(v).
	 */
	public Iterable<E> incidentEdges(V v);
	
	/**
	 * This method returns an iterator over the vertices in this graph that are reachable from v 
	 * in a path of length 1.  In other words, every vertex that is on the other end of an
	 * outgoing edge from v.  A vertex appears only once in this iterator.
	 * 
	 * @param v A vertex in this graph.
	 * @return An iterator over every vertex that is adjacent to v.
	 */
	public Iterator<V> adjacentVerticesIterator(V v);
	
	/**
	 * @param v A Vertex in this Graph. 
	 * @return an iterable object whose iterator() method returns this.adjacentVerticesIterator(v).
	 */
	public Iterable<V> adjacentVertices(V v);

	/**
	 * Method to add a Vertex to this graph.  Attempting to add a vertex already
	 * in this graph does nothing.
	 * 
	 * @param v a Vertex.
	 * @return true iff the graph has been modified.
	 */
	public boolean add(V v);
	
	/**
	 * Adds a given edge to this graph.  Certain types of graph may have 
	 * restrictions on the edges that may be added to this graph; but in 
	 * general if this graph contains an edge, adding that same edge again 
	 * will not change this graph.
	 * 
	 * Adding an edge parallel to an edge already in the graph is defined 
	 * in the extending interfaces.
	 * 
	 * The Implementation must check that no two edges have the same name in the graph,
	 * so that calling toXML() on the edges does not create clashes.  To ensure this,
	 * ideal implementations will check 
	 * <tt>this.getEdgeForName(edge.name()) == null;</tt>
	 * before adding the edge.
	 * 
	 * @param edge An edge that needs to be added to this graph.
	 * @return true iff the graph has been modified.
	 */
	public boolean add(E edge);
	
	/**
	 * Method to remove a vertex from this graph.  This also removes all edges 
	 * that are incident to this vertex, either outgoing or incoming.
	 * 
	 * @param v the Vertex to be removed.
	 */
	public boolean remove(V v);

	/**
	 * Method to get the degree of a vertex, which is defined as the number of 
	 * edges outgoing and incoming.
	 * 
	 * @param v A vertex in the graph g.
	 * @return int the number of edges that are incident to v.
	 */
	public int degree(V v);
	
	/**
	 * Method to remove a given edge from this graph.  If this graph does not
	 * contain the exact edge, the graph is unchanged.
	 * 
	 * @param e the Edge to be removed.
	 */
	public boolean remove(E e);

	/**
	 * Returns true if there exists a path in this graph connecting vertex primus
	 * to secundus.  In a directed graph, this does not imply that secundus is
	 * connected to primus. 
	 * 
	 * @param primus  The first Vertex: the start of the path.
	 * @param secundus The second Vertex: the termination of the path.
	 * @return true if secundus is reachable from primus
	 */
	public boolean isConnected(V primus, V secundus);
	
	/**
	 * Returns the shortest Path in Graph g that connects Vertex primus to Vertex
	 * secundus.  If no such Path exists, it returns an empty Path.
	 *  
	 * @param primus  The first Vertex: the start of the path.
	 * @param secundus The second Vertex: the termination of the path.
	 * @return the Path connecting them in the shortest number of steps, or an 
	 * empty Path, if secundus is not unreachable from primus.
	 */
	public Path shortestPathBySteps(V primus, V secundus);
	
	/**
	 * Returns the  Path in Graph g that connects Vertex primus to Vertex
	 * secundus with the least weight.  If no such Path exists, it returns
	 * an empty Path.
	 *  
	 * @param primus  The first Vertex: the start of the path.
	 * @param secundus The second Vertex: the termination of the path.
	 * @return the Path connecting them in the shortest number of steps, or an 
	 * empty Path, if secundus is not unreachable from primus.
	 */
	public Path shortestPathByWeight(V primus, V secundus);
	
	/**
	 * Creates a copy of this graph.  A new Graph is created, and every element of this graph
	 * is added to the new one.  No edges or vertices are copied.
	 * 
	 * @return the new copy of the graph.
	 */
	public Graph copy();

	/**
	 * Reads a graph from an GXLReader
	 * @param input a GXLReader that has been initialised with the file that the 
	 * graph to be loaded has been saved to.
	 * @return a graph of the type that was saved, containing all the nodes and 
	 * vertices.
	 */
	public Graph load(GXLReader input);

	/**
	 * Saves this graph to a GXLWriter
	 * @param output a GXLWriter that has been initialised to send the gxl output to the right place.
	 */
	public void save(GXLWriter writer) throws IOException;
	
	/**
	 * Returns the description of the graph as an xml top level node.
	 * The format of the string is as follows:
	 * &lt;graph id="NAMEHERE" edgeids="false" edgemode="directed | undirected"
	 *  hypergraph="true|false"&gt;
	 *  
	 *  The corresponding &lt;/graph&gt; tag need not be supplied.
	 * @return 
	 */
	public String toXML();
	
	/**
	 * Returns the single Edge in this graph that is identifiable by the name given in s.
	 * @param s the name of the Edge to be looked for.
	 * @return the unique edge that matches s in name, or null if no such edge exists.
	 */
	public E edgeForName(String s);
	
	/**
	 * Returns the single Vertex in this graph that is identifiable by the name given in s.
	 * @param s the name of the Vertex to be looked for.
	 * @return the unique vertex that matches s in name, or null if no such edge exists.
	 */
	public V vertexForName(String s);
	
	public double density();

}

