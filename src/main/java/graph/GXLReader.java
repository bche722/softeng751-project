/**
 * 
 */
package graph;

import java.util.Iterator;

import org.w3c.dom.Node;

/**
 * @author Danver Braganza
 *
 */
public interface GXLReader {

	/**
	 * @return an Iterator over the Nodes of this graph that contain information about simple Edges.
	 */
	public abstract Iterator<Node> getEdges();

	/**
	 * @return the Graph's name.
	 */
	public abstract String getName();

	/**
	 * @return the graph's type.
	 */
	public abstract String getType();

	/**
	 * @return an Iterator over the Nodes of this graph that contain information about vertices.
	 */
	public abstract Iterator<Node> getVertices();

	/**
	 * @return an Iterator over the Nodes of this graph that contain information about hyperEdges.
	 */
	public abstract Iterator<Node> getHypers();

}