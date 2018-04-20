

package interfaces;


public abstract class Algorithm<V extends graph.Vertex, E extends graph.DirectedEdge<V>> {
    protected boolean isParallel;

    public abstract void doTheJob();
}

