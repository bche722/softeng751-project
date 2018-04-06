/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public class DirectedHyperGraph<V extends Vertex, E extends DirectedHyperEdge<V>> extends AbstractGraph<V,E> implements HyperGraph<V,E> {

	/** 
	 * @param name
	 * @param type
	 * @param simple
	 * @param directed
	 */
	public DirectedHyperGraph(String name) {
		super(name, "HyperGraph", false, false);
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#add(graph.Vertex)
	 */
	@Override
	public boolean add(V v) {
		if (v != null && !this.contains(v) && this.vertexForName(v.name()) == null) {
			this.map.put(v, new HashSet<E>());
			this.vertices.add(v);
			this.vertexForName.put(v.name(), v);
			return true;
		} else return false;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#add(java.lang.Object)
	 */
	@Override
	public boolean add(E e) {
		if (e == null) return false;
		try {
			if (!this.contains(e) && !e.isDirected()
					&& this.edgeForName(e.name()) == null) {
				boolean containsVerts = true;
				Iterator<V> vertices = e.incidentVertices();
				while (vertices.hasNext() && containsVerts) {
					containsVerts = this.contains(vertices.next());
				}
				if (containsVerts) {
					this.edges.add(e);
					vertices = e.incidentVertices();
					while (vertices.hasNext()) {
						this.map.get(vertices.next()).add(e);
					}
					this.edgeForName.put(e.name(), e);
					return true;
				}
			}
			return false;
		} catch (NullPointerException p) {
			p.printStackTrace(); //TODO
			return false;
		}
	}


	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#adjacentVerticesIterator(graph.Vertex)
	 */
	@Override
	public Iterator<V> adjacentVerticesIterator(V v) {
		return new UniqueVertexIteratorWrapper<V>(new EdgeToVertexIteratorWrapper<V>(
				this.map.get(v).iterator(), v));
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#copy()
	 */
	@Override
	public Graph<V,E> copy() {
		HyperGraph<V,E> retval = new DirectedHyperGraph<V,E>(this.name());
		for (V v : this.vertices()) {
			retval.add(v);
		}
		for (E e : this.edges()) {
			retval.add(e);
		}
		return retval;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#degree(graph.Vertex)
	 */
	@Override
	public int degree(V v) {
		if (this.contains(v)) {
			return this.map.get(v).size();
		} else return 0;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#incidentEdgesIterator(graph.Vertex)
	 */
	@Override
	public Iterator<E> incidentEdgesIterator(V v) {
		try {
			return new EdgeIteratorWrapper<E>(this.map.get(v).iterator());
		} catch (NullPointerException p) {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#remove(graph.Edge)
	 */
	@SuppressWarnings("unchecked")
	public boolean remove(E e) {
		if (this.contains(e)) {
			Iterator<V> vertices = e.incidentVertices();
			while (vertices.hasNext()) {
				this.map.get(vertices.next()).remove(e);
			}
			this.edges.remove(e);
			this.edgeForName.remove(e.name());
			return true;
		}
		else return false;
	}

	/* (non-Javadoc)
	 * @see graph.Graph#containsCongruent(graph.Edge)
	 */
	@SuppressWarnings("unchecked")
	public boolean containsCongruent(Edge e) {
		try {
			if (e.isSimple()) return false;
			DirectedHyperEdge otherEdge = (DirectedHyperEdge) e;
			//Create a set of the vertices in the external DirectedHyperEdge: no duplicated Vertices.
			Set<Vertex> thoseVertices = new HashSet<Vertex>();
			Iterator<Vertex> thoseVerticesIterator = otherEdge.incidentVertices();
			while (thoseVerticesIterator.hasNext()) {
				thoseVertices.add(thoseVerticesIterator.next());
			}

			//Iterate over all edges incident in this graph over the external edge's first vertex.
			for (DirectedHyperEdge thisEdge : this.map.get(otherEdge.first())) {
				if ( otherEdge.size() == (thisEdge).size()) {
					boolean looking = true;
					// edge.getVertices() always returns only unique edges.
					Iterator<Vertex> theseVerticesIterator = otherEdge.incidentVertices();
					while (theseVerticesIterator.hasNext() && looking) {
						looking = thoseVertices.contains(theseVerticesIterator.next());
					}
					// If the external DirectedHyperEdge's set of vertices contains every vertex in the edge we are looking at,
					// and the edge we are looking at and the external DirectedHyperEdge have the same degree
					// and neither the external edge nor the edge we are currently on have duplicate elements
					// Then the two vertex sets are equal, and we can return true.
					if (looking = true) return true;
				} 
			} // Not found, keep looking at other vertices.
			return false;
		} catch (NullPointerException nullEdge) {
			return false; // Nothing is parallel to a null edge.
			// All other null pointer exceptions are thrown because a mapping for a vertex doesn't exist in the graph:
			// Therefore the edge mustn't exist.
		} catch (ClassCastException wrongTypeOfEdge) {
			throw new IllegalArgumentException("Wrong Edge supplied.");
		}
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#toXML()
	 */
	@Override
	public String toXML() {
		return "<graph id=\"" + this.name() + "\" edgeids=\" undirected\" edgemode=\" directed\" hypergraph=\" true\">";
	}

	/**
	 * @param input
	 */
	@SuppressWarnings("unchecked")
	public static HyperGraph doLoad(GXLReader input) {
		DirectedHyperGraph returned = new DirectedHyperGraph(input.getName());
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

		Iterator<Node> hypers = input.getHypers();
		while (hypers.hasNext()) {
			//TODO
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
