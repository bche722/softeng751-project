

package graph;


public class BasicDirectedHyperEdge<V extends graph.Vertex> implements graph.DirectedHyperEdge<V> {
    private java.util.Set<V> vertices;

    private java.util.Set<V> sourceVerts;

    private java.util.Set<V> destVerts;

    private int weight;

    protected java.lang.String type;

    private java.lang.String name;

    public BasicDirectedHyperEdge(java.lang.String s, V[] sources, V[] dests) {
        graph.BasicDirectedHyperEdge.this.sourceVerts = new java.util.HashSet<V>();
        graph.BasicDirectedHyperEdge.this.destVerts = new java.util.HashSet<V>();
        for (V v : sources) {
            if (v == null)
                throw new java.lang.IllegalArgumentException("Null value encountered");
            
            if (graph.BasicDirectedHyperEdge.this.vertices.add(v)) {
                graph.BasicDirectedHyperEdge.this.sourceVerts.add(v);
            }else {
                throw new java.lang.IllegalArgumentException("The same vertex was supplied twice");
            }
        }
        for (V v : dests) {
            if (v == null)
                throw new java.lang.IllegalArgumentException("Null value encountered");
            
            if (graph.BasicDirectedHyperEdge.this.vertices.add(v)) {
                graph.BasicDirectedHyperEdge.this.destVerts.add(v);
            }else {
                throw new java.lang.IllegalArgumentException("The same vertex was supplied twice");
            }
        }
        graph.BasicDirectedHyperEdge.this.type = "Directed Hyper";
        graph.BasicDirectedHyperEdge.this.name = s;
    }

    public V first() {
        java.util.Iterator<V> temp = graph.BasicDirectedHyperEdge.this.vertices.iterator();
        if (temp.hasNext()) {
            return temp.next();
        }else
            return null;
        
    }

    public java.lang.String type() {
        return graph.BasicDirectedHyperEdge.this.type;
    }

    public java.util.Iterator<V> incidentVertices() {
        return new graph.VertexIteratorWrapper<V>(graph.BasicDirectedHyperEdge.this.vertices.iterator());
    }

    public boolean isDirected() {
        return true;
    }

    public boolean isSimple() {
        return false;
    }

    public V second() {
        java.util.Iterator<V> temp = graph.BasicDirectedHyperEdge.this.vertices.iterator();
        if (temp.hasNext()) {
            temp.next();
            if (temp.hasNext()) {
                return temp.next();
            }
        }
        return null;
    }

    public java.util.Iterator<V> otherVerticesIterator(V v) {
        if (graph.BasicDirectedHyperEdge.this.contains(v)) {
            java.util.Set<V> temp = new java.util.HashSet<V>(graph.BasicDirectedHyperEdge.this.vertices);
            temp.remove(v);
            return new graph.VertexIteratorWrapper<V>(temp.iterator());
        }else
            throw new java.util.NoSuchElementException();
        
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public graph.Edge<V> clone() {
        return new graph.BasicDirectedHyperEdge(graph.BasicDirectedHyperEdge.this.name, ((graph.Vertex[]) (graph.BasicDirectedHyperEdge.this.sourceVerts.toArray())), ((graph.Vertex[]) (graph.BasicDirectedHyperEdge.this.destVerts.toArray())));
    }

    public int size() {
        return graph.BasicDirectedHyperEdge.this.vertices.size();
    }

    public boolean contains(V v) {
        return v == null ? false : graph.BasicDirectedHyperEdge.this.vertices.contains(v);
    }

    public java.lang.String toXML() {
        java.lang.StringBuffer b = new java.lang.StringBuffer();
        b.append((("<rel id=\"" + (graph.BasicDirectedHyperEdge.this.name)) + "\" isdirected = \"true\">"));
        for (V v : graph.BasicDirectedHyperEdge.this.sourceVerts) {
            b.append((("<relend target = \"" + (v.name())) + " direction=\" in\"/>"));
        }
        for (V v : graph.BasicDirectedHyperEdge.this.destVerts) {
            b.append((("<relend target = \"" + (v.name())) + " direction=\" out\"/>"));
        }
        b.append("</rel>");
        return b.toString();
    }

    public java.lang.String name() {
        return graph.BasicDirectedHyperEdge.this.name;
    }

    public void setName(java.lang.String name) {
        graph.BasicDirectedHyperEdge.this.name = name;
    }

    public java.lang.Iterable<V> otherVertices(V v) {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedHyperEdge.this.otherVerticesIterator(v));
    }

    public java.lang.Iterable<V> fromVertices() {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedHyperEdge.this.fromVerticesIterator());
    }

    public java.util.Iterator<V> fromVerticesIterator() {
        return new graph.VertexIteratorWrapper<V>(graph.BasicDirectedHyperEdge.this.sourceVerts.iterator());
    }

    public java.lang.Iterable<V> toVertices() {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedHyperEdge.this.toVerticesIterator());
    }

    public java.util.Iterator<V> toVerticesIterator() {
        return new graph.VertexIteratorWrapper<V>(graph.BasicDirectedHyperEdge.this.destVerts.iterator());
    }

    public int weight() {
        return weight;
    }

    public void setWeight(int w) {
        graph.BasicDirectedHyperEdge.this.weight = w;
    }
}

