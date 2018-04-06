package graph;

import java.util.Iterator;

public interface Path<E extends DirectedEdge> {
	
	public Iterator<E> edges();
	
	public Iterator<Vertex> vertices();
	
	public void addVertex(Vertex v);
	
	public void addEdge(E e);
	
	public Vertex sourceVertex();
	
	public Vertex destVertex();
	
}