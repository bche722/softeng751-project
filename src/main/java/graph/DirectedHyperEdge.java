/**
 * 
 */
package graph;

import java.util.Iterator;

/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface DirectedHyperEdge<V extends Vertex> extends HyperEdge<V> {
	
	public Iterator<V> fromVerticesIterator();
	
	public Iterable<V> fromVertices();
	
	public Iterator<V> toVerticesIterator();
	
	public Iterable<V> toVertices();

}