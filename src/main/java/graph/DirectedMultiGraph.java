/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public class DirectedMultiGraph<V extends Vertex, E extends DirectedEdge<V>> extends BasicDirectedGraph<V,E> implements MultiGraph<V, E>, DirectedGraph<V,E>{

	
	public DirectedMultiGraph(String name) {
		super(name, "Directed Multi");
	}

	/* (non-Javadoc)
	 * @see graph.Graph#copy()
	 */
	public Graph<V,E> copy() {
		DirectedMultiGraph<V,E> that = new DirectedMultiGraph<V,E>(this.name());
		Iterator<V> thisVert = this.verticesIterator();
		while (thisVert.hasNext()) {
			that.add(thisVert.next());
		}
		Iterator<E> thisEdge = this.edgesIterator();
		while(thisEdge.hasNext()) {
			that.add(thisEdge.next());
		}
		return that;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#incidentEdgesIterator(graph.Vertex)
	 */
	//Error Here
	public Iterator<E> incidentEdgesIterator2(Vertex v) {
		return new EdgeIteratorWrapper<E>(sourceVerts.get(v).iterator());
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.MultiGraph#edgesBetweenIterator(graph.Vertex, graph.Vertex)
	 */
	public Iterator<E> edgesBetweenIterator(Vertex source, Vertex dest) {
		return new EdgeBetweenIteratorWrapper<E>(this.map.get(source).iterator(), dest);
	}
	

	/* (non-Javadoc)
	 * @see graph.Graph#add(java.lang.Object)
	 */
	public boolean add(E edge) {
		if(!edge.isDirected() && this.contains(edge.first()) && this.contains(edge.second())
				&& this.edgeForName(edge.name()) == null) {
			if (!this.contains(edge)) {
				this.edges.add(edge);
				this.map.get(edge.first()).add(edge);
				this.map.get(edge.second()).add(edge);
				this.edgeForName.put(edge.name(), edge);
				return true;
			} else return false;
		} else return false;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#toXML()
	 */
	@Override
	public String toXML() {
		return "<graph id=\"" + this.name() + "\" edgeids=\" undirected\" edgemode=\" undirected\" hypergraph=\" false\" multigraph = \"true\">";
	}

	/**
	 * @param input
	 */
	@SuppressWarnings("unchecked")
	public static DirectedMultiGraph doLoad(GXLReader input) {
		DirectedMultiGraph returned = new DirectedMultiGraph(input.getName());
		Iterator<Node> verts = input.getVertices();
		HashMap<String, Vertex> vertmap = new HashMap<String, Vertex>();
		while (verts.hasNext()) {
			NodeList p = verts.next().getChildNodes();
			Vertex temp = null;
			for (int i = 0; i < p.getLength() ; i++) {
				Node child = p.item(i);
				String attributeName = child.getAttributes() != null ? child.getAttributes().getNamedItem("name").getNodeValue()
						: "";
				if (attributeName.equals("name")) {
					temp = new BasicVertex(child.getFirstChild().getTextContent());
					vertmap.put(child.getFirstChild().getTextContent(), temp);
				} else if (attributeName.equals("weight")) {
					String type = child.getAttributes().getNamedItem("type").getNodeValue();
					if ("int".equals(type)) {
						temp.setWeight(Integer.parseInt(child.getTextContent()));
					}
				}
			}
			returned.add(temp);
		}
		Iterator<Node> edges = input.getEdges();
		while (edges.hasNext()) {
			Node e = edges.next();
			NodeList p = e.getChildNodes();
			Edge temp = new BasicSimpleEdge(
						e.getAttributes().getNamedItem("id").getNodeValue(),
						vertmap.get(
								e.getAttributes().getNamedItem("from").getNodeValue()),
								vertmap.get(
										e.getAttributes().getNamedItem("to").getNodeValue()),
										false);
			for (int i = p.getLength(); i > 0 ;i--) {
				Node child = p.item(i - 1);
				String attributeName = child.getAttributes() != null ? child.getAttributes().getNamedItem("name").getNodeValue()
						: "";
				if (attributeName.equals("weight")) {
					String type = child.getAttributes().getNamedItem("type").getNodeValue();
					if ("int".equals(type)) {
						temp.setWeight(Integer.parseInt(child.getTextContent()));
					}
				}
			}
			returned.add((DirectedEdge) temp);
		}
		return returned;
	}
}






