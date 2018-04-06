/**
 * 
 */
package graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;


/**
 * @author Danver Braganza
 *
 */
public class GXLFileReader implements GXLReader {

	private Node graph;

	public GXLFileReader(File f) {

		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
			doc.getDocumentElement().normalize();
			graph =  doc.getElementsByTagName("graph").item(0);
		} catch (ParserConfigurationException e) {
			graph = null;
		} catch (SAXException e) {
			graph = null;
		} catch (IOException e) {
			graph = null;
		}

	}

	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getEdges()
	 */
	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getEdges()
	 */
	public Iterator<Node> getEdges() {
		if (graph == null) {
			return null;
		}
		ArrayList<Node> edges = new ArrayList<Node>();
		for (int j = graph.getChildNodes().getLength(); j != 0; j--) {
			Node e = graph.getChildNodes().item(j - 1);
			if (e.getNodeName().equals("edge")) {
				edges.add(e);
			}
		}
		return edges.iterator();
	}

	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getName()
	 */
	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getName()
	 */
	public String getName() {
		if (graph == null) {
			return null;
		}
		return graph.getAttributes().getNamedItem("id").getTextContent();
	}

	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getType()
	 */
	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getType()
	 */
	public String getType() {
		StringBuffer b = new StringBuffer();
		if (graph == null) {
			return null;
		}
		String edgeMode;
		try {
			edgeMode = graph.getAttributes().getNamedItem("edgemode").getTextContent();
		} catch (NullPointerException p) {
			throw new IllegalStateException();
		}
		if (edgeMode.equals("directed")) {
			b.append("Directed");
		} else if (edgeMode.equals("undirected")) {
			b.append("Undirected");
		} else if (edgeMode.equals("defaultdirected")) {
			b.append("Hybrid");
		}
		boolean isHyper, isMulti;
		try {
			isHyper = graph.getAttributes().getNamedItem("hypergraph").getTextContent().equals("true");
		} catch (NullPointerException p) {
			isHyper = false;
		}
		try {
			isMulti = graph.getAttributes().getNamedItem("multigraph").getTextContent().equals("true");
		} catch (NullPointerException p) {
			isMulti = false;
		}
		
		if (!isHyper && !isMulti) {
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

	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getVertices()
	 */
	public Iterator<Node> getVertices() {
		if (graph == null) {
			return null;
		}
		ArrayList<Node> vertices = new ArrayList<Node>();
		for (int j = graph.getChildNodes().getLength(); j != 0; j--) {
			Node v = graph.getChildNodes().item(j - 1);
			if (v.getNodeName().equals("node")) {
				vertices.add(v);
			}
		}
		return vertices.iterator();
	}

	/* (non-Javadoc)
	 * @see graph.utils.GXLReader#getHypers()
	 */
	public Iterator<Node> getHypers() {
		if (graph == null) {
			return null;
		}
		ArrayList<Node> hypers = new ArrayList<Node>();
		for (int j = graph.getChildNodes().getLength(); j != 0; j--) {
			Node v = graph.getChildNodes().item(j - 1);
			if (v.getNodeName().equals("rel")) {
				hypers.add(v);
			}
		}
		return hypers.iterator();
	}
}
