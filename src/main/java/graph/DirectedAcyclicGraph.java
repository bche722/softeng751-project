package graph;

import java.util.Iterator;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface DirectedAcyclicGraph<V extends Vertex, E extends DirectedEdge<V>> extends DirectedGraph<V,E> {
	
//	public void ensureAcyclicity(V root);
	
	public Iterator<V> topologicalOrderIterator();
	
	public Iterable<V> topologicalOrder();

	public Iterator<V> inverseTopologicalOrderIterator();
	
	public Iterable<V> inverseTopologicalOrder();

	public Iterator<V> descendentsIterator(V v);
	//Synonym for Graph.adjacentVertices

	public Iterable<V> descendents(V v);
	//Synonym for Graph.adjacentVertices

	public Iterator<V> ancestorsIterator(V v);

	public Iterable<V> ancestors(V v);
}
