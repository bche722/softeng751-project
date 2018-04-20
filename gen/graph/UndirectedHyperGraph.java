

package graph;


public class UndirectedHyperGraph<V extends graph.Vertex> extends graph.AbstractGraph<V, graph.HyperEdge<V>> implements graph.HyperGraph<V, graph.HyperEdge<V>> {
    public UndirectedHyperGraph(java.lang.String name) {
        super(name, "HyperGraph", false, false);
    }

    @java.lang.Override
    public boolean add(V v) {
        if (((v != null) && (!(graph.UndirectedHyperGraph.this.contains(v)))) && ((graph.UndirectedHyperGraph.this.vertexForName(v.name())) == null)) {
            graph.UndirectedHyperGraph.this.map.put(v, new java.util.HashSet<graph.HyperEdge<V>>());
            graph.UndirectedHyperGraph.this.vertices.add(v);
            graph.UndirectedHyperGraph.this.vertexForName.put(v.name(), v);
            return true;
        }else
            return false;
        
    }

    @java.lang.Override
    public boolean add(graph.HyperEdge<V> e) {
        if (e == null)
            return false;
        
        try {
            if (((!(graph.UndirectedHyperGraph.this.contains(e))) && (!(e.isDirected()))) && ((graph.UndirectedHyperGraph.this.edgeForName(e.name())) == null)) {
                boolean containsVerts = true;
                java.util.Iterator<V> vertices = e.incidentVertices();
                while ((vertices.hasNext()) && containsVerts) {
                    containsVerts = graph.UndirectedHyperGraph.this.contains(vertices.next());
                } 
                if (containsVerts) {
                    graph.UndirectedHyperGraph.this.edges.add(e);
                    vertices = e.incidentVertices();
                    while (vertices.hasNext()) {
                        graph.UndirectedHyperGraph.this.map.get(vertices.next()).add(e);
                    } 
                    graph.UndirectedHyperGraph.this.edgeForName.put(e.name(), e);
                    return true;
                }
            }
            return false;
        } catch (java.lang.NullPointerException p) {
            p.printStackTrace();
            return false;
        }
    }

    @java.lang.Override
    public java.util.Iterator<V> adjacentVerticesIterator(V v) {
        return new graph.UniqueVertexIteratorWrapper<V>(new graph.EdgeToVertexIteratorWrapper<V>(graph.UndirectedHyperGraph.this.map.get(v).iterator(), v));
    }

    @java.lang.Override
    public graph.Graph copy() {
        graph.HyperGraph<V, graph.HyperEdge<V>> retval = new graph.UndirectedHyperGraph<V>(graph.UndirectedHyperGraph.this.name());
        java.util.Iterator<V> vertIt = graph.UndirectedHyperGraph.this.verticesIterator();
        while (vertIt.hasNext()) {
            retval.add(vertIt.next());
        } 
        java.util.Iterator<graph.HyperEdge<V>> edgeIt = graph.UndirectedHyperGraph.this.edgesIterator();
        while (edgeIt.hasNext()) {
            retval.add(edgeIt.next());
        } 
        return retval;
    }

    @java.lang.Override
    public int degree(V v) {
        if (graph.UndirectedHyperGraph.this.contains(v)) {
            return graph.UndirectedHyperGraph.this.map.get(v).size();
        }else
            return 0;
        
    }

    @java.lang.Override
    public java.util.Iterator<graph.HyperEdge<V>> incidentEdgesIterator(V v) {
        try {
            return new graph.EdgeIteratorWrapper<graph.HyperEdge<V>>(graph.UndirectedHyperGraph.this.map.get(v).iterator());
        } catch (java.lang.NullPointerException p) {
            return null;
        }
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    @java.lang.Override
    public boolean remove(graph.HyperEdge<V> e) {
        if (graph.UndirectedHyperGraph.this.contains(e)) {
            java.util.Iterator<V> vertices = e.incidentVertices();
            while (vertices.hasNext()) {
                graph.UndirectedHyperGraph.this.map.get(vertices.next()).remove(e);
            } 
            graph.UndirectedHyperGraph.this.edges.remove(e);
            graph.UndirectedHyperGraph.this.edgeForName.remove(e.name());
            return true;
        }else {
            return false;
        }
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public boolean containsCongruent(graph.Edge e) {
        try {
            if (e.isSimple())
                return false;
            
            graph.HyperEdge otherEdge = ((graph.HyperEdge) (e));
            java.util.Set<graph.Vertex> thoseVertices = new java.util.HashSet<graph.Vertex>();
            java.util.Iterator<graph.Vertex> thoseVerticesIterator = otherEdge.incidentVertices();
            while (thoseVerticesIterator.hasNext()) {
                thoseVertices.add(thoseVerticesIterator.next());
            } 
            for (graph.HyperEdge thisEdge : graph.UndirectedHyperGraph.this.map.get(otherEdge.first())) {
                if ((otherEdge.size()) == (thisEdge.size())) {
                    boolean looking = true;
                    java.util.Iterator<graph.Vertex> theseVerticesIterator = otherEdge.incidentVertices();
                    while ((theseVerticesIterator.hasNext()) && looking) {
                        looking = thoseVertices.contains(theseVerticesIterator.next());
                    } 
                    if (looking = true)
                        return true;
                    
                }
            }
            return false;
        } catch (java.lang.NullPointerException nullEdge) {
            return false;
        } catch (java.lang.ClassCastException wrongTypeOfEdge) {
            throw new java.lang.IllegalArgumentException("Wrong Edge supplied.");
        }
    }

    @java.lang.Override
    public java.lang.String toXML() {
        return ("<graph id=\"" + (graph.UndirectedHyperGraph.this.name())) + "\" edgeids=\" undirected\" edgemode=\" directed\" hypergraph=\" true\">";
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public static graph.HyperGraph doLoad(graph.GXLReader input) {
        graph.UndirectedHyperGraph returned = new graph.UndirectedHyperGraph(input.getName());
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
        java.util.Iterator<org.w3c.dom.Node> hypers = input.getHypers();
        while (hypers.hasNext()) {
        } 
        return returned;
    }

    public V vertexForName(java.lang.String s) {
        return graph.UndirectedHyperGraph.this.vertexForName.get(s);
    }
}

