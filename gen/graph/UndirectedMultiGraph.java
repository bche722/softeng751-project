

package graph;


public class UndirectedMultiGraph<V extends graph.Vertex> extends graph.AbstractGraph<V, graph.UndirectedEdge<V>> implements graph.MultiGraph<V, graph.UndirectedEdge<V>> {
    public UndirectedMultiGraph(java.lang.String name) {
        super(name, "MultiGraph", false, false);
        graph.UndirectedMultiGraph.this.vertices = new java.util.HashSet<V>();
    }

    public boolean add(V v) {
        if (((v != null) && (!(graph.UndirectedMultiGraph.this.contains(v)))) && ((graph.UndirectedMultiGraph.this.vertexForName(v.name())) == null)) {
            graph.UndirectedMultiGraph.this.vertices.add(v);
            graph.UndirectedMultiGraph.this.map.put(v, new java.util.HashSet<graph.UndirectedEdge<V>>());
            graph.UndirectedMultiGraph.this.vertexForName.put(v.name(), v);
            return true;
        }
        return false;
    }

    public java.util.Iterator<V> adjacentVerticesIterator(V v) {
        try {
            return new graph.UniqueVertexIteratorWrapper<V>(new graph.EdgeToVertexIteratorWrapper<V>(graph.UndirectedMultiGraph.this.map.get(v).iterator(), v));
        } catch (java.lang.NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public graph.Graph copy() {
        graph.UndirectedMultiGraph<V> that = new graph.UndirectedMultiGraph<V>(graph.UndirectedMultiGraph.this.name());
        java.util.Iterator<V> thisVert = graph.UndirectedMultiGraph.this.verticesIterator();
        while (thisVert.hasNext()) {
            that.add(thisVert.next());
        } 
        java.util.Iterator<graph.UndirectedEdge<V>> thisEdge = graph.UndirectedMultiGraph.this.edgesIterator();
        while (thisEdge.hasNext()) {
            that.add(thisEdge.next());
        } 
        return that;
    }

    public int degree(V v) {
        return map.get(v).size();
    }

    public boolean remove(graph.UndirectedEdge<V> e) {
        if (graph.UndirectedMultiGraph.this.contains(e)) {
            graph.UndirectedMultiGraph.this.edges.remove(e);
            graph.UndirectedMultiGraph.this.map.get(e.first()).remove(e);
            graph.UndirectedMultiGraph.this.map.get(e.second()).remove(e);
            graph.UndirectedMultiGraph.this.edgeForName.remove(e.name());
            return true;
        }else {
            return false;
        }
    }

    public java.util.Iterator<graph.UndirectedEdge<V>> incidentEdgesIterator(V v) {
        return new graph.EdgeIteratorWrapper<graph.UndirectedEdge<V>>(map.get(v).iterator());
    }

    public java.util.Iterator<graph.UndirectedEdge<V>> edgesBetweenIterator(graph.Vertex source, graph.Vertex dest) {
        return new graph.EdgeBetweenIteratorWrapper<graph.UndirectedEdge<V>>(graph.UndirectedMultiGraph.this.map.get(source).iterator(), dest);
    }

    public boolean add(graph.UndirectedEdge<V> edge) {
        if ((((!(edge.isDirected())) && (graph.UndirectedMultiGraph.this.contains(edge.first()))) && (graph.UndirectedMultiGraph.this.contains(edge.second()))) && ((graph.UndirectedMultiGraph.this.edgeForName(edge.name())) == null)) {
            if (!(graph.UndirectedMultiGraph.this.contains(edge))) {
                graph.UndirectedMultiGraph.this.edges.add(edge);
                graph.UndirectedMultiGraph.this.map.get(edge.first()).add(edge);
                graph.UndirectedMultiGraph.this.map.get(edge.second()).add(edge);
                graph.UndirectedMultiGraph.this.edgeForName.put(edge.name(), edge);
                return true;
            }else
                return false;
            
        }else
            return false;
        
    }

    public boolean containsCongruent(graph.Edge e) {
        if (e.isDirected())
            return false;
        
        try {
            return graph.UndirectedMultiGraph.this.edgesBetweenIterator(e.first(), e.second()).hasNext();
        } catch (java.lang.NullPointerException noSucVertex) {
            return false;
        }
    }

    @java.lang.Override
    public java.lang.String toXML() {
        return ("<graph id=\"" + (graph.UndirectedMultiGraph.this.name())) + "\" edgeids=\" undirected\" edgemode=\" undirected\" hypergraph=\" false\" multigraph = \"true\">";
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public static graph.MultiGraph doLoad(graph.GXLReader input) {
        graph.UndirectedMultiGraph returned = new graph.UndirectedMultiGraph(input.getName());
        java.util.Iterator<org.w3c.dom.Node> verts = input.getVertices();
        java.util.HashMap<java.lang.String, graph.Vertex> vertmap = new java.util.HashMap<java.lang.String, graph.Vertex>();
        while (verts.hasNext()) {
            org.w3c.dom.NodeList p = verts.next().getChildNodes();
            graph.Vertex temp = null;
            for (int i = 0; i < (p.getLength()); i++) {
                org.w3c.dom.Node child = p.item(i);
                java.lang.String attributeName = (child.getAttributes()) != null ? child.getAttributes().getNamedItem("name").getNodeValue() : "";
                if (attributeName.equals("name")) {
                    temp = new graph.BasicVertex(child.getFirstChild().getTextContent());
                    vertmap.put(child.getFirstChild().getTextContent(), temp);
                }else
                    if (attributeName.equals("weight")) {
                        java.lang.String type = child.getAttributes().getNamedItem("type").getNodeValue();
                        if ("int".equals(type)) {
                            temp.setWeight(java.lang.Integer.parseInt(child.getTextContent()));
                        }
                    }
                
            }
            returned.add(temp);
        } 
        java.util.Iterator<org.w3c.dom.Node> edges = input.getEdges();
        while (edges.hasNext()) {
            org.w3c.dom.Node e = edges.next();
            org.w3c.dom.NodeList p = e.getChildNodes();
            graph.Edge temp = new graph.BasicSimpleEdge(e.getAttributes().getNamedItem("id").getNodeValue(), vertmap.get(e.getAttributes().getNamedItem("from").getNodeValue()), vertmap.get(e.getAttributes().getNamedItem("to").getNodeValue()), false);
            for (int i = p.getLength(); i > 0; i--) {
                org.w3c.dom.Node child = p.item((i - 1));
                java.lang.String attributeName = (child.getAttributes()) != null ? child.getAttributes().getNamedItem("name").getNodeValue() : "";
                if (attributeName.equals("weight")) {
                    java.lang.String type = child.getAttributes().getNamedItem("type").getNodeValue();
                    if ("int".equals(type)) {
                        temp.setWeight(java.lang.Integer.parseInt(child.getTextContent()));
                    }
                }
            }
            returned.add(((graph.Vertex) (temp)));
        } 
        return returned;
    }

    public V vertexForName(java.lang.String s) {
        return graph.UndirectedMultiGraph.this.vertexForName.get(s);
    }
}

