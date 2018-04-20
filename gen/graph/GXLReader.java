

package graph;


public interface GXLReader {
    public abstract java.util.Iterator<org.w3c.dom.Node> getEdges();

    public abstract java.lang.String getName();

    public abstract java.lang.String getType();

    public abstract java.util.Iterator<org.w3c.dom.Node> getVertices();

    public abstract java.util.Iterator<org.w3c.dom.Node> getHypers();
}

