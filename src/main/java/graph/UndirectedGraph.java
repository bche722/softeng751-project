/**
 * 
 */
package graph;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface UndirectedGraph<V extends Vertex, E extends UndirectedEdge<V>> extends Graph<V,E> {
	
	public boolean add(E edge);
	
	public UndirectedEdge<V> edgeBetween(V source, V dest);
	
}