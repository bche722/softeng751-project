package interfaces;

import graph.DirectedEdge;
import graph.Vertex;

public abstract class Algorithm<V extends Vertex, E extends DirectedEdge<V>> {

    protected boolean isParallel;

    public abstract void doTheJob();

}
