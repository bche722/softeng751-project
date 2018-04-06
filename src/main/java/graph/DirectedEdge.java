package graph;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface DirectedEdge<V extends Vertex> extends SimpleEdge<V> {
	
	public V from();
	
	public V to();

}
