package graph;

/**
 * Interface expected to be implemented by any Vertex class used in this graph 
 * package.  Vertices have a name and a weight.
 * @author Danver Braganza, Oliver Sinnen
 */
public interface Vertex {
	
	/**
	 * @return the name of the Vertex, or Unnamed if no name is given.
	 */
	public String name();
	
	/**
	 * Set the name of this Vertex to a given string
	 * @param name the String that this vertex is to be named.
	 */
	public void setName(String name);
	
	/**
	 * @return a string representation of this Vertex in GXL, with "id"
	 * used as this vertex's id.
	 */
	public String toXML();
	
	
	/**
	 * Creates an exact copy of this vertex, with it's own duplicated fields.
	 * @return the cloned Vertex
	 */
	public Vertex clone();
	
	public boolean equals(Object o);
	
	public int hashCode();
	
	public int weight();
	
	public void setWeight(int w);

	public int compareWeight(Vertex c);

}
