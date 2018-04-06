/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Danver Braganza
 *
 */
public class BasicUndirectedGraph<V extends Vertex, E extends UndirectedEdge<V>> extends AbstractGraph<V,E> implements UndirectedGraph<V,E> {

	/**
	 * @param name
	 */
	public BasicUndirectedGraph(String name) {
		super(name, "Undirected_Simple", true, false);
	}
	
	public BasicUndirectedGraph(String name, String type) {
		super(name, type, false, false);
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#adjacentVerticesIterator(graph.Vertex)
	 */
	@Override
	public Iterator<V> adjacentVerticesIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new EdgeToVertexIteratorWrapper<V>(this.map.get(v).iterator(), v);

	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#degree(graph.Vertex)
	 */
	@Override
	public int degree(V v) {
		try {
			return this.map.get(v).size();
		} catch (NullPointerException p) {
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#incidentEdgesIterator(graph.Vertex)
	 */
	@Override
	public Iterator<E> incidentEdgesIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new EdgeIteratorWrapper<E>(this.map.get(v).iterator());

	}
	
	public Collection<E> incidentEdgesSet(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new ArrayList<E>(this.map.get(v));
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#add(graph.Vertex)
	 */
	@Override
	public boolean add(V v) {
		if (v != null && !this.contains(v) && this.vertexForName(v.name()) == null) {
			this.vertices.add(v);
			this.vertexForName.put(v.name(), v);
			this.map.put(v, new HashSet<E>());
			return true;
		}else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#delete(graph.Edge)
	 */
	@Override
	public boolean remove(E e) {
		if (this.contains(e)) {
			Vertex a = e.first();
			this.map.get(a).remove(e);
			Vertex b = e.second();
			this.map.get(b).remove(e);
			this.edges.remove(e);
			this.edgeForName.remove(e.name());
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#contains(graph.Edge)
	 */
	public boolean containsCongruent(Edge<V> e) {
		if (e == null) return false;
		if (e.isSimple() && !e.isDirected()) {
			if (this.edgeBetween(e.first(), e.second()) != null)
				return true;
			return false;
		} else return false;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#copy()
	 */
	@Override
	public Graph<V,E> copy() {
		BasicUndirectedGraph<V,E> that = new BasicUndirectedGraph<V,E>(this.name());
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
	 * @see graph.impl.SimpleGraphImplementation#contains(graph.Vertex)
	 */
	@Override
	public boolean contains(V v) {
		if (v == null) return false;
		return this.vertices.contains(v);
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#add(java.lang.Object)
	 */
	@Override
	public boolean add(E edge) {
		if (edge != null
				&& this.contains(edge.first())
				&& this.contains(edge.second())
				&& !this.containsCongruent(edge)
				&& this.edgeForName(edge.name()) == null
				&& !edge.isDirected()) {
			
					this.edges.add(edge);
					Vertex first = edge.first();
					Vertex second = edge.second();
					this.map.get(first).add(edge);
					this.map.get(second).add(edge);
					this.edgeForName.put(edge.name(), edge);
					return true;
			} else{
				return false;
			}
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.UndirectedGraph#edgeBetween(graph.Vertex, graph.Vertex)
	 */
	public UndirectedEdge<V> edgeBetween(V source, V dest) {
		
		if (!this.contains(source) || !this.contains(dest)) {
			return null;
		}
		for (UndirectedEdge<V> e : this.map.get(source)) {
			if(e.other(source).equals(dest)) {
				return e;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#toXML()
	 */
	@Override
	public String toXML() {
		return "<graph id=\"" + this.name() + "\" edgeids=\" true\" edgemode=\" undirected\" hypergraph=\" false\">";
	}

	/**
	 * @param input
	 */
	@SuppressWarnings("unchecked")
	public static UndirectedGraph doLoad(GXLReader input) {
		BasicUndirectedGraph returned = new BasicUndirectedGraph(input.getName());
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
			UndirectedEdge<Vertex> temp = new BasicSimpleEdge<Vertex>(
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
			returned.add(temp);
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
