/**
 * 
 */
package graph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;



/**
 * @author Danver Braganza
 *
 */
public class GXLFileWriter implements GXLWriter {
	private File file;
	
	public GXLFileWriter(File f) {
		this.file = f;
	}
	
	/* (non-Javadoc)
	 * @see graph.utils.GXLWriter#write(graph.Graph)
	 */
	@SuppressWarnings("unchecked")
	public void write(Graph g) throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" + 
		"<!DOCTYPE gxl SYSTEM \"http://www.gupro.de/GXL/gxl-1.0.dtd\">");
		pw.write("<gxl>");
		pw.write(g.toXML());
		Iterator<Vertex> verts = g.verticesIterator();
		while (verts.hasNext()) {
			Vertex v  = verts.next();
			pw.write(v.toXML());
		}
		Iterator<Edge> edges = g.edgesIterator();
		while (edges.hasNext()) {
			Edge e  = edges.next();
			pw.write(e.toXML());
		}
		pw.write("</graph>");
		pw.write("</gxl>");
		pw.close();
		
	}

}
