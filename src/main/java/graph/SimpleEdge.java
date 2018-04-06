package graph;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface SimpleEdge<V extends Vertex> extends Edge<V> {

	public Vertex other(V v);
	
}
