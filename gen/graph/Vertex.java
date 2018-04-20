

package graph;


public interface Vertex {
    public java.lang.String name();

    public void setName(java.lang.String name);

    public java.lang.String toXML();

    public graph.Vertex clone();

    public boolean equals(java.lang.Object o);

    public int hashCode();

    public int weight();

    public void setWeight(int w);

    public int compareWeight(graph.Vertex c);
}

