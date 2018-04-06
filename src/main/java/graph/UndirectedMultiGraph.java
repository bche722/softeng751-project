/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Danver Braganza
 *
 */
public class UndirectedMultiGraph<V extends Vertex> extends AbstractGraph<V, UndirectedEdge<V>> implements MultiGraph<V, UndirectedEdge<V>> {
	
	
	public UndirectedMultiGraph(String name) {
		super(name, "MultiGraph", false, false);
		this.vertices = new HashSet<V>();
	}

	/* (non-Javadoc)
	 * @see graph.Graph#add(graph.Vertex)
	 */
	public boolean add(V v) {
		if (v != null && !this.contains(v)  && (this.vertexForName(v.name()) == null)) {
			this.vertices.add(v);
			this.map.put(v, new HashSet<UndirectedEdge<V>>());
			this.vertexForName.put(v.name(), v);
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#adjacentVerticesIterator(graph.Vertex)
	 */
	public Iterator<V> adjacentVerticesIterator(V v) {
		try {
			return new UniqueVertexIteratorWrapper<V>(new EdgeToVertexIteratorWrapper<V>(this.map.get(v).iterator(), v));
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#copy()
	 */
	public Graph copy() {
		UndirectedMultiGraph<V> that = new UndirectedMultiGraph<V>(this.name());
		Iterator<V> thisVert = this.verticesIterator();
		while (thisVert.hasNext()) {
			that.add(thisVert.next());
		}
		Iterator<UndirectedEdge<V>> thisEdge = this.edgesIterator();
		while(thisEdge.hasNext()) {
			that.add(thisEdge.next());
		}
		return that;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#degree(graph.Vertex)
	 */
	public int degree(V v) {
		return map.get(v).size();
	}

	/* (non-Javadoc)
	 * @see graph.Graph#delete(graph.Edge)
	 */
	public boolean remove(UndirectedEdge<V> e) {
		if (this.contains(e)) {
			this.edges.remove(e);
			this.map.get(e.first()).remove(e);
			this.map.get(e.second()).remove(e);
			this.edgeForName.remove(e.name());
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see graph.Graph#incidentEdgesIterator(graph.Vertex)
	 */
	public Iterator<UndirectedEdge<V>> incidentEdgesIterator(V v) {
		return new EdgeIteratorWrapper<UndirectedEdge<V>>(map.get(v).iterator());
	}

	/* (non-Javadoc)
	 * @see graph.Graph#sizeVertices()
	 */

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.MultiGraph#edgesBetweenIterator(graph.Vertex, graph.Vertex)
	 */
	public Iterator<UndirectedEdge<V>> edgesBetweenIterator(Vertex source, Vertex dest) {
		return new EdgeBetweenIteratorWrapper<UndirectedEdge<V>>(this.map.get(source).iterator(), dest);
	}
	

	/* (non-Javadoc)
	 * @see graph.Graph#add(java.lang.Object)
	 */
	public boolean add(UndirectedEdge<V> edge) {
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
	 * @see graph.Graph#containsCongruent(graph.Edge)
	 */
	public boolean containsCongruent(Edge e) {
		if (e.isDirected()) return false;
		try {
			return this.edgesBetweenIterator(e.first(), e.second()).hasNext();
		} catch (NullPointerException noSucVertex) {
			return false;
		}
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
	public static MultiGraph doLoad(GXLReader input) {
		UndirectedMultiGraph returned = new UndirectedMultiGraph(input.getName());
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
			returned.add((Vertex) temp);
		}
		return returned;

	}

	/* (non-Javadoc)
	 * @see graph.Graph#getVertexForName(java.lang.String)
	 */
	public V vertexForName(String s) {
		return this.vertexForName.get(s);
	}

}
