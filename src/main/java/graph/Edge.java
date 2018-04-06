package graph;

import java.util.Iterator;

/**
 * Interface that edges in this graph package are supposed to implement.
 *An Edge is defined as linking two or more vertices.  Edges can have
 *weights.  The vertices contained in the edge are immutable. 
 *
 * @author Danver Braganza, Oliver Sinnen
 */
public interface Edge<V extends Vertex> {
	
	/**
	 * Query the type of this edge, specifically whether it is directed or not.
	 * @return true if and only if this edge implements nz.uoacec2008.graph.special.DirectedEdge
	 */
	public boolean isDirected();
	
	/**
	 * Query the type of this edge, specifically whether it is simple or not.
	 * In this implementation, a simple edge is defined as an edge that goes between 
	 * just two vertices.
	 * @return true if and only if this edge joins only two vertices.
	 */
	public boolean isSimple();
	
	/**
	 * Query the type of this edge. Returns a String the full type of the edge, with the first word being either 
	 * "Directed" or "Undirected",
	 * and the second word represents the more specific type of the edge.  At present, the only types that exist are
	 * "Simple", "Multi" or "Hyper".
	 * The rest of the edge's type may be used to represent other features of the edge, as desired.
	 * 
	 * @return String representing the type of the edge.
	 */
	public String type();
	
	/**
	 * Returns the first vertex in this edge.  For directed edges, this must be a
	 * source vertex, for undirected edges, any vertex is legal.
	 * @return the first vertex.
	 */
	public V first();
	
	/**
	 * Returns the second vertex in this edge. For directed edges, this must be a destination
	 * vertex, for undirected, any vertex is legal as long as first != second if this edge is not 
	 * a loop.
	 * @return the second vertex
	 */
	public V second();
	
	/**
	 * @return an iterator over every vertex in this edge.  Each Vertex must appear only once.
	 */
	public Iterator<V> incidentVertices();
	
	/**
	 * 
	 * @param o the other object
	 * @return true if and only if o is an edge that is parallel to this one. (ie, has the
	 * same vertices (in any order) and orientation.)
	 */
	public boolean equals(Object o);
	
	/**
	 * Creates a clone of this edge, with it's own data structures, but with references to the
	 * same vertices.  The created edge is equivalent to this one in every way, but may be changed.
	 * @return the copied edge.
	 */
	public Edge<V> clone();
	
	/**
	 * @param idMap TODO
	 * @return A string representation of this edge in GXL, with id used as this
	 * nodes id.
	 */
	public String toXML();
	
	public String name();
	
	public void setName(String name);
	
	public int weight();
	
	public void setWeight(int w);
	
}
