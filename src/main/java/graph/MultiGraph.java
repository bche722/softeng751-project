/**
 * 
 */
package graph;

import java.util.Iterator;



/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface MultiGraph<V extends Vertex, E extends Edge<V>> extends Graph<V, E> {
	
	public Iterator<E> edgesBetweenIterator(Vertex source, Vertex dest);

}
