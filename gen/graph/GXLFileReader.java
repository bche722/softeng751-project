

package graph;


public class GXLFileReader implements graph.GXLReader {
    private org.w3c.dom.Node graph;

    public GXLFileReader(java.io.File f) {
        try {
            org.w3c.dom.Document doc = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
            doc.getDocumentElement().normalize();
            graph = doc.getElementsByTagName("graph").item(0);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            graph = null;
        } catch (org.xml.sax.SAXException e) {
            graph = null;
        } catch (java.io.IOException e) {
            graph = null;
        }
    }

    public java.util.Iterator<org.w3c.dom.Node> getEdges() {
        if ((graph) == null) {
            return null;
        }
        java.util.ArrayList<org.w3c.dom.Node> edges = new java.util.ArrayList<org.w3c.dom.Node>();
        for (int j = graph.getChildNodes().getLength(); j != 0; j--) {
            org.w3c.dom.Node e = graph.getChildNodes().item((j - 1));
            if (e.getNodeName().equals("edge")) {
                edges.add(e);
            }
        }
        return edges.iterator();
    }

    public java.lang.String getName() {
        if ((graph) == null) {
            return null;
        }
        return graph.getAttributes().getNamedItem("id").getTextContent();
    }

    public java.lang.String getType() {
        java.lang.StringBuffer b = new java.lang.StringBuffer();
        if ((graph) == null) {
            return null;
        }
        java.lang.String edgeMode;
        try {
            edgeMode = graph.getAttributes().getNamedItem("edgemode").getTextContent();
        } catch (java.lang.NullPointerException p) {
            throw new java.lang.IllegalStateException();
        }
        if (edgeMode.equals("directed")) {
            b.append("Directed");
        }else
            if (edgeMode.equals("undirected")) {
                b.append("Undirected");
            }else
                if (edgeMode.equals("defaultdirected")) {
                    b.append("Hybrid");
                }
            
        
        boolean isHyper;
        boolean isMulti;
        try {
            isHyper = graph.getAttributes().getNamedItem("hypergraph").getTextContent().equals("true");
        } catch (java.lang.NullPointerException p) {
            isHyper = false;
        }
        try {
            isMulti = graph.getAttributes().getNamedItem("multigraph").getTextContent().equals("true");
        } catch (java.lang.NullPointerException p) {
            isMulti = false;
        }
        if ((!isHyper) && (!isMulti)) {
            b.append(" Simple");
        }
        if (isHyper) {
            b.append(" Hyper");
        }
        if (isMulti) {
            b.append(" Multi");
        }
        return b.toString();
    }

    public java.util.Iterator<org.w3c.dom.Node> getVertices() {
        if ((graph) == null) {
            return null;
        }
        java.util.ArrayList<org.w3c.dom.Node> vertices = new java.util.ArrayList<org.w3c.dom.Node>();
        for (int j = graph.getChildNodes().getLength(); j != 0; j--) {
            org.w3c.dom.Node v = graph.getChildNodes().item((j - 1));
            if (v.getNodeName().equals("node")) {
                vertices.add(v);
            }
        }
        return vertices.iterator();
    }

    public java.util.Iterator<org.w3c.dom.Node> getHypers() {
        if ((graph) == null) {
            return null;
        }
        java.util.ArrayList<org.w3c.dom.Node> hypers = new java.util.ArrayList<org.w3c.dom.Node>();
        for (int j = graph.getChildNodes().getLength(); j != 0; j--) {
            org.w3c.dom.Node v = graph.getChildNodes().item((j - 1));
            if (v.getNodeName().equals("rel")) {
                hypers.add(v);
            }
        }
        return hypers.iterator();
    }
}

