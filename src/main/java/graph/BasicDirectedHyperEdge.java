/**
 * 
 */
package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;


/**
 * @author Danver Braganza
 *
 */
public class BasicDirectedHyperEdge<V extends Vertex> implements DirectedHyperEdge<V> {
	
	private Set<V> vertices;
	private Set<V> sourceVerts;
	private Set<V> destVerts;
    private int weight;
	protected String type; 
	private String name;
	
	public BasicDirectedHyperEdge (String s, V[] sources, V[] dests){
		this.sourceVerts = new HashSet<V>();
		this.destVerts = new HashSet<V>();
		for (V v : sources) {
			if (v == null) 
				throw new IllegalArgumentException("Null value encountered");
			if (this.vertices.add(v)) {
				this.sourceVerts.add(v);
			} else {
				throw new IllegalArgumentException("The same vertex was supplied twice");
			}
			
		}
		for (V v : dests) {
			if (v == null) 
				throw new IllegalArgumentException("Null value encountered");
			if (this.vertices.add(v)) {
				this.destVerts.add(v);
			} else {
				throw new IllegalArgumentException("The same vertex was supplied twice");
			}
		}
		this.type = "Directed Hyper";
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
		return true;
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
	 * @see graph.DirectedHyperEdge#otherVerticesIterator(graph.Vertex)
	 */
	public Iterator<V> otherVerticesIterator(V v) {
		if (this.contains(v)) {
			Set<V> temp = new HashSet<V>(this.vertices);
			temp.remove(v);
			return new VertexIteratorWrapper<V>(temp.iterator());
		} else throw new NoSuchElementException();
	}

	/* (non-Javadoc)
	 * @see graph.Edge#copy()
	 */
	@SuppressWarnings("unchecked")
	public Edge<V> clone() {
		return new BasicDirectedHyperEdge(this.name, (Vertex[])this.sourceVerts.toArray(), (Vertex[])this.destVerts.toArray());
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
	public boolean contains(V v) {
		return v == null ? false : this.vertices.contains(v);
	}

	/* (non-Javadoc)
	 * @see graph.Edge#toXML()
	 */
	public String toXML() {
		StringBuffer b = new StringBuffer();
		b.append("<rel id=\"" + this.name + "\" isdirected = \"true\">");
		for (V v : this.sourceVerts) {
			b.append("<relend target = \""+ v.name() + " direction=\" in\"/>");
		}
		for (V v : this.destVerts) {
			b.append("<relend target = \""+ v.name() + " direction=\" out\"/>");
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
	 * @see graph.DirectedHyperEdge#otherVertices(graph.Vertex)
	 */
	public Iterable<V> otherVertices(V v) {
		return new IteratorToIterableWrapper<V>(this.otherVerticesIterator(v));
	}
	

	/* (non-Javadoc)
	 * @see graph.DirectedHyperEdge#fromVertices()
	 */
	public Iterable<V> fromVertices() {
		return new IteratorToIterableWrapper<V>(this.fromVerticesIterator());
	}

	/* (non-Javadoc)
	 * @see graph.DirectedHyperEdge#fromVerticesIterator()
	 */
	public Iterator<V> fromVerticesIterator() {
		return new VertexIteratorWrapper<V>(this.sourceVerts.iterator());
	}

	/* (non-Javadoc)
	 * @see graph.DirectedHyperEdge#toVertices()
	 */
	public Iterable<V> toVertices() {
		return new IteratorToIterableWrapper<V>(this.toVerticesIterator());
	}

	/* (non-Javadoc)
	 * @see graph.DirectedHyperEdge#toVerticesIterator()
	 */
	public Iterator<V> toVerticesIterator() {
		return new VertexIteratorWrapper<V>(this.destVerts.iterator());
	}

	
	public int weight() {
		return weight;
	}

	public void setWeight(int w) {
		this.weight = w;
	}

}
