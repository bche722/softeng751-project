

package graph;


public class DirectedHyperGraph<V extends graph.Vertex, E extends graph.DirectedHyperEdge<V>> extends graph.AbstractGraph<V, E> implements graph.HyperGraph<V, E> {
    public DirectedHyperGraph(java.lang.String name) {
        super(name, "HyperGraph", false, false);
    }

    @java.lang.Override
    public boolean add(V v) {
        if (((v != null) && (!(graph.DirectedHyperGraph.this.contains(v)))) && ((graph.DirectedHyperGraph.this.vertexForName(v.name())) == null)) {
            graph.DirectedHyperGraph.this.map.put(v, new java.util.HashSet<E>());
            graph.DirectedHyperGraph.this.vertices.add(v);
            graph.DirectedHyperGraph.this.vertexForName.put(v.name(), v);
            return true;
        }else
            return false;
        
    }

    @java.lang.Override
    public boolean add(E e) {
        if (e == null)
            return false;
        
        try {
            if (((!(graph.DirectedHyperGraph.this.contains(e))) && (!(e.isDirected()))) && ((graph.DirectedHyperGraph.this.edgeForName(e.name())) == null)) {
                boolean containsVerts = true;
                java.util.Iterator<V> vertices = e.incidentVertices();
                while ((vertices.hasNext()) && containsVerts) {
                    containsVerts = graph.DirectedHyperGraph.this.contains(vertices.next());
                } 
                if (containsVerts) {
                    graph.DirectedHyperGraph.this.edges.add(e);
                    vertices = e.incidentVertices();
                    while (vertices.hasNext()) {
                        graph.DirectedHyperGraph.this.map.get(vertices.next()).add(e);
                    } 
                    graph.DirectedHyperGraph.this.edgeForName.put(e.name(), e);
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
        return new graph.UniqueVertexIteratorWrapper<V>(new graph.EdgeToVertexIteratorWrapper<V>(graph.DirectedHyperGraph.this.map.get(v).iterator(), v));
    }

    @java.lang.Override
    public graph.Graph<V, E> copy() {
        graph.HyperGraph<V, E> retval = new graph.DirectedHyperGraph<V, E>(graph.DirectedHyperGraph.this.name());
        for (V v : graph.DirectedHyperGraph.this.vertices()) {
            retval.add(v);
        }
        for (E e : graph.DirectedHyperGraph.this.edges()) {
            retval.add(e);
        }
        return retval;
    }

    @java.lang.Override
    public int degree(V v) {
        if (graph.DirectedHyperGraph.this.contains(v)) {
            return graph.DirectedHyperGraph.this.map.get(v).size();
        }else
            return 0;
        
    }

    @java.lang.Override
    public java.util.Iterator<E> incidentEdgesIterator(V v) {
        try {
            return new graph.EdgeIteratorWrapper<E>(graph.DirectedHyperGraph.this.map.get(v).iterator());
        } catch (java.lang.NullPointerException p) {
            return null;
        }
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public boolean remove(E e) {
        if (graph.DirectedHyperGraph.this.contains(e)) {
            java.util.Iterator<V> vertices = e.incidentVertices();
            while (vertices.hasNext()) {
                graph.DirectedHyperGraph.this.map.get(vertices.next()).remove(e);
            } 
            graph.DirectedHyperGraph.this.edges.remove(e);
            graph.DirectedHyperGraph.this.edgeForName.remove(e.name());
            return true;
        }else
            return false;
        
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public boolean containsCongruent(graph.Edge e) {
        try {
            if (e.isSimple())
                return false;
            
            graph.DirectedHyperEdge otherEdge = ((graph.DirectedHyperEdge) (e));
            java.util.Set<graph.Vertex> thoseVertices = new java.util.HashSet<graph.Vertex>();
            java.util.Iterator<graph.Vertex> thoseVerticesIterator = otherEdge.incidentVertices();
            while (thoseVerticesIterator.hasNext()) {
                thoseVertices.add(thoseVerticesIterator.next());
            } 
            for (graph.DirectedHyperEdge thisEdge : graph.DirectedHyperGraph.this.map.get(otherEdge.first())) {
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
        return ("<graph id=\"" + (graph.DirectedHyperGraph.this.name())) + "\" edgeids=\" undirected\" edgemode=\" directed\" hypergraph=\" true\">";
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public static graph.HyperGraph doLoad(graph.GXLReader input) {
        graph.DirectedHyperGraph returned = new graph.DirectedHyperGraph(input.getName());
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
        return graph.DirectedHyperGraph.this.vertexForName.get(s);
    }
}

