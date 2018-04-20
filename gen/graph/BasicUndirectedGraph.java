

package graph;


public class BasicUndirectedGraph<V extends graph.Vertex, E extends graph.UndirectedEdge<V>> extends graph.AbstractGraph<V, E> implements graph.UndirectedGraph<V, E> {
    public BasicUndirectedGraph(java.lang.String name) {
        super(name, "Undirected_Simple", true, false);
    }

    public BasicUndirectedGraph(java.lang.String name, java.lang.String type) {
        super(name, type, false, false);
    }

    @java.lang.Override
    public java.util.Iterator<V> adjacentVerticesIterator(V v) {
        if (!(graph.BasicUndirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeToVertexIteratorWrapper<V>(graph.BasicUndirectedGraph.this.map.get(v).iterator(), v);
    }

    @java.lang.Override
    public int degree(V v) {
        try {
            return graph.BasicUndirectedGraph.this.map.get(v).size();
        } catch (java.lang.NullPointerException p) {
            return -1;
        }
    }

    @java.lang.Override
    public java.util.Iterator<E> incidentEdgesIterator(V v) {
        if (!(graph.BasicUndirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeIteratorWrapper<E>(graph.BasicUndirectedGraph.this.map.get(v).iterator());
    }

    public java.util.Collection<E> incidentEdgesSet(V v) {
        if (!(graph.BasicUndirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new java.util.ArrayList<E>(graph.BasicUndirectedGraph.this.map.get(v));
    }

    @java.lang.Override
    public boolean add(V v) {
        if (((v != null) && (!(graph.BasicUndirectedGraph.this.contains(v)))) && ((graph.BasicUndirectedGraph.this.vertexForName(v.name())) == null)) {
            graph.BasicUndirectedGraph.this.vertices.add(v);
            graph.BasicUndirectedGraph.this.vertexForName.put(v.name(), v);
            graph.BasicUndirectedGraph.this.map.put(v, new java.util.HashSet<E>());
            return true;
        }else {
            return false;
        }
    }

    @java.lang.Override
    public boolean remove(E e) {
        if (graph.BasicUndirectedGraph.this.contains(e)) {
            graph.Vertex a = e.first();
            graph.BasicUndirectedGraph.this.map.get(a).remove(e);
            graph.Vertex b = e.second();
            graph.BasicUndirectedGraph.this.map.get(b).remove(e);
            graph.BasicUndirectedGraph.this.edges.remove(e);
            graph.BasicUndirectedGraph.this.edgeForName.remove(e.name());
            return true;
        }else {
            return false;
        }
    }

    public boolean containsCongruent(graph.Edge<V> e) {
        if (e == null)
            return false;
        
        if ((e.isSimple()) && (!(e.isDirected()))) {
            if ((graph.BasicUndirectedGraph.this.edgeBetween(e.first(), e.second())) != null)
                return true;
            
            return false;
        }else
            return false;
        
    }

    @java.lang.Override
    public graph.Graph<V, E> copy() {
        graph.BasicUndirectedGraph<V, E> that = new graph.BasicUndirectedGraph<V, E>(graph.BasicUndirectedGraph.this.name());
        java.util.Iterator<V> thisVert = graph.BasicUndirectedGraph.this.verticesIterator();
        while (thisVert.hasNext()) {
            that.add(thisVert.next());
        } 
        java.util.Iterator<E> thisEdge = graph.BasicUndirectedGraph.this.edgesIterator();
        while (thisEdge.hasNext()) {
            that.add(thisEdge.next());
        } 
        return that;
    }

    @java.lang.Override
    public boolean contains(V v) {
        if (v == null)
            return false;
        
        return graph.BasicUndirectedGraph.this.vertices.contains(v);
    }

    @java.lang.Override
    public boolean add(E edge) {
        if ((((((edge != null) && (graph.BasicUndirectedGraph.this.contains(edge.first()))) && (graph.BasicUndirectedGraph.this.contains(edge.second()))) && (!(graph.BasicUndirectedGraph.this.containsCongruent(edge)))) && ((graph.BasicUndirectedGraph.this.edgeForName(edge.name())) == null)) && (!(edge.isDirected()))) {
            graph.BasicUndirectedGraph.this.edges.add(edge);
            graph.Vertex first = edge.first();
            graph.Vertex second = edge.second();
            graph.BasicUndirectedGraph.this.map.get(first).add(edge);
            graph.BasicUndirectedGraph.this.map.get(second).add(edge);
            graph.BasicUndirectedGraph.this.edgeForName.put(edge.name(), edge);
            return true;
        }else {
            return false;
        }
    }

    public graph.UndirectedEdge<V> edgeBetween(V source, V dest) {
        if ((!(graph.BasicUndirectedGraph.this.contains(source))) || (!(graph.BasicUndirectedGraph.this.contains(dest)))) {
            return null;
        }
        for (graph.UndirectedEdge<V> e : graph.BasicUndirectedGraph.this.map.get(source)) {
            if (e.other(source).equals(dest)) {
                return e;
            }
        }
        return null;
    }

    @java.lang.Override
    public java.lang.String toXML() {
        return ("<graph id=\"" + (graph.BasicUndirectedGraph.this.name())) + "\" edgeids=\" true\" edgemode=\" undirected\" hypergraph=\" false\">";
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public static graph.UndirectedGraph doLoad(graph.GXLReader input) {
        graph.BasicUndirectedGraph returned = new graph.BasicUndirectedGraph(input.getName());
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
            graph.UndirectedEdge<graph.Vertex> temp = new graph.BasicSimpleEdge<graph.Vertex>(e.getAttributes().getNamedItem("id").getNodeValue(), vertmap.get(e.getAttributes().getNamedItem("from").getNodeValue()), vertmap.get(e.getAttributes().getNamedItem("to").getNodeValue()), false);
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
            returned.add(temp);
        } 
        return returned;
    }

    public V vertexForName(java.lang.String s) {
        return graph.BasicUndirectedGraph.this.vertexForName.get(s);
    }
}

