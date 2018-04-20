

package graph;


public class GXLFileWriter implements graph.GXLWriter {
    private java.io.File file;

    public GXLFileWriter(java.io.File f) {
        graph.GXLFileWriter.this.file = f;
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public void write(graph.Graph g) throws java.io.IOException {
        java.io.PrintWriter pw = new java.io.PrintWriter(new java.io.FileWriter(file));
        pw.write(("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + "<!DOCTYPE gxl SYSTEM \"http://www.gupro.de/GXL/gxl-1.0.dtd\">"));
        pw.write("<gxl>");
        pw.write(g.toXML());
        java.util.Iterator<graph.Vertex> verts = g.verticesIterator();
        while (verts.hasNext()) {
            graph.Vertex v = verts.next();
            pw.write(v.toXML());
        } 
        java.util.Iterator<graph.Edge> edges = g.edgesIterator();
        while (edges.hasNext()) {
            graph.Edge e = edges.next();
            pw.write(e.toXML());
        } 
        pw.write("</graph>");
        pw.write("</gxl>");
        pw.close();
    }
}

