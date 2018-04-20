

package graph;


public interface MultiGraph<V extends graph.Vertex, E extends graph.Edge<V>> extends graph.Graph<V, E> {
    public java.util.Iterator<E> edgesBetweenIterator(graph.Vertex source, graph.Vertex dest);
}

