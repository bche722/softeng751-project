package graph;

import java.util.Iterator;


/**
 * @author Danver Braganza, Oliver Sinnen
 */
public interface HyperEdge<V extends Vertex> extends Edge<V> {
	
	public Iterator<V> otherVerticesIterator (V v);
	
	public Iterable<V> otherVertices(V v);
	
	public int size();
	
	public boolean contains(V v);

}
