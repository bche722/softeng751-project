

package graph;


public interface Edge<V extends graph.Vertex> {
    public boolean isDirected();

    public boolean isSimple();

    public java.lang.String type();

    public V first();

    public V second();

    public java.util.Iterator<V> incidentVertices();

    public boolean equals(java.lang.Object o);

    public graph.Edge<V> clone();

    public java.lang.String toXML();

    public java.lang.String name();

    public void setName(java.lang.String name);

    public int weight();

    public void setWeight(int w);
}

