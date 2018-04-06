/**
 * 
 */
package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * @author Danver Braganza, Oliver Sinnen
 *
 */
public class UndirectedHyperEdge<V extends Vertex> implements HyperEdge<V> {
	
	private Set<V> vertices;
	private int weight;
	protected String type; 
	private String name;
	
	public UndirectedHyperEdge (String s, V ...vertices) {
		this.vertices = new HashSet<V>();
		for (V v : vertices) {
			if (v == null) 
				throw new IllegalArgumentException();
			this.vertices.add(v);
		}
		this.type = "Undirected Hyper";
		this.name = s;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#first()
	 */
	public V first() {
		Iterator<V> temp = this.vertices.iterator();
		if (temp.hasNext()) { 
			return temp.next(); 
		} else return null;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#getType()
	 */
	public String type() {
		return this.type;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#getVertices()
	 */
	public Iterator<V> incidentVertices() {
		return new VertexIteratorWrapper<V>(this.vertices.iterator());
	}

	/* (non-Javadoc)
	 * @see graph.Edge#isDirected()
	 */
	public boolean isDirected() {
		return false;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#isSimple()
	 */
	public boolean isSimple() {
		return false;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#second()
	 */
	public V second() {
		Iterator<V> temp = this.vertices.iterator();
		if (temp.hasNext()) { 
			temp.next();
			if (temp.hasNext()) { 
				return temp.next();
			}
		}
		return null;
	}


	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.HyperEdge#otherVerticesIterator(graph.Vertex)
	 */
	/* (non-Javadoc)
	 * @see graph.UndirectedHyperEdge#otherVerticesIterator(graph.Vertex)
	 */
	public Iterator<V> otherVerticesIterator(V v) {
		if (this.contains(v)) {
			Set<V> temp = new HashSet<V>(this.vertices);
			temp.remove(v);
			return new VertexIteratorWrapper<V>(temp.iterator());
		} else return null;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#copy()
	 */
	/* (non-Javadoc)
	 * @see graph.UndirectedHyperEdge#clone()
	 */
	@SuppressWarnings("unchecked")
	public Edge<V> clone() {
		return new UndirectedHyperEdge<V>(this.name, (V[])this.vertices.toArray());
	}


	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.HyperEdge#degree()
	 */
	public int size() {
		return this.vertices.size();
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.HyperEdge#contains(graph.Vertex)
	 */
	/* (non-Javadoc)
	 * @see graph.UndirectedHyperEdge#contains(graph.Vertex)
	 */
	public boolean contains(V v) {
		return v == null ? false : this.vertices.contains(v);
	}

	/* (non-Javadoc)
	 * @see graph.Edge#toXML()
	 */
	public String toXML() {
		StringBuffer b = new StringBuffer();
		b.append("<rel id=\"" + this.name + "\" isdirected = \"false\">");
		for (Vertex v : this.vertices) {
			b.append("<relend target = \""+ v.name() + "\"/>");
		}
		b.append("</rel>");
		return b.toString();
	}

	/**
	 * @return the name
	 */
	public String name() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#toXML(java.util.Map)
	 */
	public String toXML(Map<Vertex, Integer> idMap) {
		StringBuffer b = new StringBuffer();
		b.append("<rel id=\"" + this.name + "\" isdirected=\" false\">");
		Iterator<V> it = this.incidentVertices();
		while (it.hasNext()) {
			b.append("<rel target = \""+ idMap.get(it.next()) +"\">");
		}
		b.append("</rel>");
		return b.toString();
	}

	/* (non-Javadoc)
	 * @see graph.HyperEdge#otherVertices(graph.Vertex)
	 */
	/* (non-Javadoc)
	 * @see graph.UndirectedHyperEdge#otherVertices(graph.Vertex)
	 */
	public Iterable<V> otherVertices(V v) {
		return new IteratorToIterableWrapper<V>(this.otherVerticesIterator(v));
	}
	
	public int weight() {
		return weight;
	}

	public void setWeight(int w) {
		this.weight = w;
	}

	
//UNWANTED
//	/* (non-Javadoc)
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode() {
//		final int PRIME = 31;
//		int result = 1;
//		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
//		result = PRIME * result + ((this.type == null) ? 0 : this.type.hashCode());
//		result = PRIME * result + ((this.vertices == null) ? 0 : this.vertices.hashCode());
//		return result;
//	}
//
//	/* (non-Javadoc)
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		final HyperEdgeImplementation other = (HyperEdgeImplementation) obj;
//		if (this.name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!this.name.equals(other.name))
//			return false;
//		if (this.type == null) {
//			if (other.type != null)
//				return false;
//		} else if (!this.type.equals(other.type))
//			return false;
//		if (this.vertices == null) {
//			if (other.vertices != null)
//				return false;
//		} else if (!this.vertices.equals(other.vertices))
//			return false;
//		return true;
//	}

}
