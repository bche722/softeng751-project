

package graph;


public class BasicVertex implements graph.Vertex {
    private java.lang.String name;

    private int weight;

    public BasicVertex(java.lang.String name) {
        graph.BasicVertex.this.name = name;
    }

    public BasicVertex(java.lang.String name, int weight) {
        this(name);
        graph.BasicVertex.this.weight = weight;
    }

    public java.lang.String name() {
        return graph.BasicVertex.this.name;
    }

    public void setName(java.lang.String name) {
        if ((name == null) || (name.equals(""))) {
            throw new java.lang.IllegalArgumentException("Invalid name supplied");
        }else {
            graph.BasicVertex.this.name = name;
        }
    }

    public java.lang.String toXML() {
        java.lang.StringBuffer b = new java.lang.StringBuffer();
        b.append((("<node id=\"" + (graph.BasicVertex.this.name)) + "\">"));
        b.append("<attr name=\" name\"><string>");
        b.append(graph.BasicVertex.this.name);
        b.append("</string></attr>");
        b.append("</node>");
        return b.toString();
    }

    public graph.Vertex clone() {
        graph.Vertex retval = new graph.BasicVertex(graph.BasicVertex.this.name);
        return retval;
    }

    public int weight() {
        return weight;
    }

    public void setWeight(int w) {
        graph.BasicVertex.this.weight = w;
    }

    public int compareWeight(graph.Vertex c) {
        if ((graph.BasicVertex.this.weight) > (c.weight()))
            return 1;
        else
            if ((graph.BasicVertex.this.weight) == (c.weight()))
                return 0;
            else
                return -1;
            
        
    }
}

