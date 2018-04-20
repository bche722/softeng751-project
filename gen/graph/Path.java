

package graph;


public interface Path<E extends graph.DirectedEdge> {
    public java.util.Iterator<E> edges();

    public java.util.Iterator<graph.Vertex> vertices();

    public void addVertex(graph.Vertex v);

    public void addEdge(E e);

    public graph.Vertex sourceVertex();

    public graph.Vertex destVertex();
}

