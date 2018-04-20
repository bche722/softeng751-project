

package graph;


public class BasicSimpleEdge<V extends graph.Vertex> implements graph.DirectedEdge<V> , graph.UndirectedEdge<V> {
    private V primus;

    private V secundus;

    private boolean directed;

    private int weight;

    protected java.lang.String type;

    protected java.lang.String name;

    public BasicSimpleEdge(java.lang.String name, V primus, V secundus, boolean directed) {
        graph.BasicSimpleEdge.this.primus = primus;
        graph.BasicSimpleEdge.this.secundus = secundus;
        graph.BasicSimpleEdge.this.directed = directed;
        graph.BasicSimpleEdge.this.name = (name == null) ? "" : name;
        if (directed) {
            graph.BasicSimpleEdge.this.type = "Directed Simple";
        }else {
            graph.BasicSimpleEdge.this.type = "Undirected Simple";
        }
    }

    public V from() {
        return primus;
    }

    public V to() {
        return secundus;
    }

    public V other(V v) {
        if (v.equals(primus)) {
            return secundus;
        }else
            if (v.equals(secundus)) {
                return primus;
            }else
                throw new java.lang.IllegalArgumentException("No such Vertex known to edge");
            
        
    }

    public V first() {
        return primus;
    }

    public java.lang.String type() {
        return graph.BasicSimpleEdge.this.type;
    }

    public java.util.Iterator<V> incidentVertices() {
        return new graph.VerticesIterator<V>(graph.BasicSimpleEdge.this);
    }

    public boolean isDirected() {
        return graph.BasicSimpleEdge.this.directed;
    }

    public boolean isSimple() {
        return true;
    }

    public V second() {
        return graph.BasicSimpleEdge.this.secundus;
    }

    public graph.Edge<V> clone() {
        return new graph.BasicSimpleEdge<V>(graph.BasicSimpleEdge.this.name, graph.BasicSimpleEdge.this.primus, graph.BasicSimpleEdge.this.secundus, graph.BasicSimpleEdge.this.directed);
    }

    public java.lang.String toXML() {
        java.lang.StringBuffer b = new java.lang.StringBuffer();
        b.append((((((((("<edge id = \"" + (graph.BasicSimpleEdge.this.name)) + "\" from = \"") + (graph.BasicSimpleEdge.this.from().name())) + "\" to = \"") + (graph.BasicSimpleEdge.this.to().name())) + "\" isdirected=\"") + (graph.BasicSimpleEdge.this.directed)) + "\" >"));
        b.append("</edge>");
        return b.toString();
    }

    public java.lang.String name() {
        return graph.BasicSimpleEdge.this.name;
    }

    public void setName(java.lang.String name) {
        graph.BasicSimpleEdge.this.name = name;
    }

    public int weight() {
        return weight;
    }

    public void setWeight(int w) {
        graph.BasicSimpleEdge.this.weight = w;
    }
}

