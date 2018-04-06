package graph;

import java.util.Iterator;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public interface DirectedGraph<V extends Vertex, E extends DirectedEdge<V>> extends Graph<V,E> {
	
//	public boolean isAcyclic(V root);

	public int inDegree(V v);
	
	public int outDegree(V v);

	public Iterator<E> inEdgesIterator(V v);
	
	public Iterable<E> inEdges(V v);
	
	public Iterator<E> outEdgesIterator(V v);
	
	public Iterable<E> outEdges(V v);
		
	public Iterator<V> parentsIterator(V v);

	public Iterable<V> parents(V v);

	public Iterator<V> childrenIterator(V v);

	public Iterable<V> children(V v);
	
	public Iterator<V> sourcesIterator();
	
	public Iterable<V> sources();
	
	public Iterator<V> sinksIterator();
	
	public Iterable<V> sinks();
	
	public E edgeBetween(V source, V dest);
	
}
