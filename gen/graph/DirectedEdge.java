

package graph;


public interface DirectedEdge<V extends graph.Vertex> extends graph.SimpleEdge<V> {
    public V from();

    public V to();
}

