

package graph;


public class DirectedMultiGraph<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends graph.BasicDirectedGraph<V, E> implements graph.DirectedGraph<V, E> , graph.MultiGraph<V, E> {
    public DirectedMultiGraph(java.lang.String name) {
        super(name, "Directed Multi");
    }

    public graph.Graph<V, E> copy() {
        graph.DirectedMultiGraph<V, E> that = new graph.DirectedMultiGraph<V, E>(graph.DirectedMultiGraph.this.name());
        java.util.Iterator<V> thisVert = graph.DirectedMultiGraph.this.verticesIterator();
        while (thisVert.hasNext()) {
            that.add(thisVert.next());
        } 
        java.util.Iterator<E> thisEdge = graph.DirectedMultiGraph.this.edgesIterator();
        while (thisEdge.hasNext()) {
            that.add(thisEdge.next());
        } 
        return that;
    }

    public java.util.Iterator<E> incidentEdgesIterator2(graph.Vertex v) {
        return new graph.EdgeIteratorWrapper<E>(sourceVerts.get(v).iterator());
    }

    public java.util.Iterator<E> edgesBetweenIterator(graph.Vertex source, graph.Vertex dest) {
        return new graph.EdgeBetweenIteratorWrapper<E>(graph.DirectedMultiGraph.this.map.get(source).iterator(), dest);
    }

    public boolean add(E edge) {
        if ((((!(edge.isDirected())) && (graph.DirectedMultiGraph.this.contains(edge.first()))) && (graph.DirectedMultiGraph.this.contains(edge.second()))) && ((graph.DirectedMultiGraph.this.edgeForName(edge.name())) == null)) {
            if (!(graph.DirectedMultiGraph.this.contains(edge))) {
                graph.DirectedMultiGraph.this.edges.add(edge);
                graph.DirectedMultiGraph.this.map.get(edge.first()).add(edge);
                graph.DirectedMultiGraph.this.map.get(edge.second()).add(edge);
                graph.DirectedMultiGraph.this.edgeForName.put(edge.name(), edge);
                return true;
            }else
                return false;
            
        }else
            return false;
        
    }

    @java.lang.Override
    public java.lang.String toXML() {
        return ("<graph id=\"" + (graph.DirectedMultiGraph.this.name())) + "\" edgeids=\" undirected\" edgemode=\" undirected\" hypergraph=\" false\" multigraph = \"true\">";
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public static graph.DirectedMultiGraph doLoad(graph.GXLReader input) {
        graph.DirectedMultiGraph returned = new graph.DirectedMultiGraph(input.getName());
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
            returned.add(((graph.DirectedEdge) (temp)));
        } 
        return returned;
    }
}

