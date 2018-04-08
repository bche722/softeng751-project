package interfaces;

import graph.DirectedEdge;
import graph.Vertex;

public interface Algorithm<V extends Vertex, E extends DirectedEdge<V>> {

    public void doTheJob();

}
