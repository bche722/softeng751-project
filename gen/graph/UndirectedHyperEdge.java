

package graph;


public class UndirectedHyperEdge<V extends graph.Vertex> implements graph.HyperEdge<V> {
    private java.util.Set<V> vertices;

    private int weight;

    protected java.lang.String type;

    private java.lang.String name;

    public UndirectedHyperEdge(java.lang.String s, V... vertices) {
        graph.UndirectedHyperEdge.this.vertices = new java.util.HashSet<V>();
        for (V v : vertices) {
            if (v == null)
                throw new java.lang.IllegalArgumentException();
            
            graph.UndirectedHyperEdge.this.vertices.add(v);
        }
        graph.UndirectedHyperEdge.this.type = "Undirected Hyper";
        graph.UndirectedHyperEdge.this.name = s;
    }

    public V first() {
        java.util.Iterator<V> temp = graph.UndirectedHyperEdge.this.vertices.iterator();
        if (temp.hasNext()) {
            return temp.next();
        }else
            return null;
        
    }

    public java.lang.String type() {
        return graph.UndirectedHyperEdge.this.type;
    }

    public java.util.Iterator<V> incidentVertices() {
        return new graph.VertexIteratorWrapper<V>(graph.UndirectedHyperEdge.this.vertices.iterator());
    }

    public boolean isDirected() {
        return false;
    }

    public boolean isSimple() {
        return false;
    }

    public V second() {
        java.util.Iterator<V> temp = graph.UndirectedHyperEdge.this.vertices.iterator();
        if (temp.hasNext()) {
            temp.next();
            if (temp.hasNext()) {
                return temp.next();
            }
        }
        return null;
    }

    public java.util.Iterator<V> otherVerticesIterator(V v) {
        if (graph.UndirectedHyperEdge.this.contains(v)) {
            java.util.Set<V> temp = new java.util.HashSet<V>(graph.UndirectedHyperEdge.this.vertices);
            temp.remove(v);
            return new graph.VertexIteratorWrapper<V>(temp.iterator());
        }else
            return null;
        
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public graph.Edge<V> clone() {
        return new graph.UndirectedHyperEdge<V>(graph.UndirectedHyperEdge.this.name, ((V[]) (graph.UndirectedHyperEdge.this.vertices.toArray())));
    }

    public int size() {
        return graph.UndirectedHyperEdge.this.vertices.size();
    }

    public boolean contains(V v) {
        return v == null ? false : graph.UndirectedHyperEdge.this.vertices.contains(v);
    }

    public java.lang.String toXML() {
        java.lang.StringBuffer b = new java.lang.StringBuffer();
        b.append((("<rel id=\"" + (graph.UndirectedHyperEdge.this.name)) + "\" isdirected = \"false\">"));
        for (graph.Vertex v : graph.UndirectedHyperEdge.this.vertices) {
            b.append((("<relend target = \"" + (v.name())) + "\"/>"));
        }
        b.append("</rel>");
        return b.toString();
    }

    public java.lang.String name() {
        return graph.UndirectedHyperEdge.this.name;
    }

    public void setName(java.lang.String name) {
        graph.UndirectedHyperEdge.this.name = name;
    }

    public java.lang.String toXML(java.util.Map<graph.Vertex, java.lang.Integer> idMap) {
        java.lang.StringBuffer b = new java.lang.StringBuffer();
        b.append((("<rel id=\"" + (graph.UndirectedHyperEdge.this.name)) + "\" isdirected=\" false\">"));
        java.util.Iterator<V> it = graph.UndirectedHyperEdge.this.incidentVertices();
        while (it.hasNext()) {
            b.append((("<rel target = \"" + (idMap.get(it.next()))) + "\">"));
        } 
        b.append("</rel>");
        return b.toString();
    }

    public java.lang.Iterable<V> otherVertices(V v) {
        return new graph.IteratorToIterableWrapper<V>(graph.UndirectedHyperEdge.this.otherVerticesIterator(v));
    }

    public int weight() {
        return weight;
    }

    public void setWeight(int w) {
        graph.UndirectedHyperEdge.this.weight = w;
    }
}

