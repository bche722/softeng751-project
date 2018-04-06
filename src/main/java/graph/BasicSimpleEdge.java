/**
 * 
 */
package graph;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @author Danver Braganza
 *
 */
public class BasicSimpleEdge<V extends Vertex> implements DirectedEdge<V>, UndirectedEdge<V> {

	private V primus, secundus;
	private boolean directed;
	private int weight;
	protected String type;
	protected String name;
	
	/**
	 * @param source
	 * @param dest
	 * @param b
	 */
	public BasicSimpleEdge(String name, V primus, V secundus, boolean directed) {
		this.primus = primus;
		this.secundus = secundus;
		this.directed = directed;
		this.name = name == null? "" : name;
		if (directed) {
			this.type = "Directed Simple";
		} else {
			this.type = "Undirected Simple";
		}
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedEdge#from()
	 */
	public V from() {
		return primus;
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedEdge#to()
	 */
	public V to() {
		return secundus;
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.SimpleEdge#other(graph.Vertex)
	 */
	public V other(V v) {
		if (v.equals(primus)) {
			return secundus;
		} else if (v.equals(secundus)) {
			return primus;
		} else throw new IllegalArgumentException("No such Vertex known to edge");
	}

	/* (non-Javadoc)
	 * @see graph.Edge#first()
	 */
	public V first() {
		return primus;
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
		return new VerticesIterator<V>(this);
	}

	/* (non-Javadoc)
	 * @see graph.Edge#isDirected()
	 */
	public boolean isDirected() {
		return this.directed;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#isSimple()
	 */
	public boolean isSimple() {
		return true;
	}

	/* (non-Javadoc)
	 * @see graph.Edge#second()
	 */
	public V second() {
		return this.secundus;
	}




	/* (non-Javadoc)
	 * @see graph.Edge#copy()
	 */
	public Edge<V> clone() {
		return new BasicSimpleEdge<V>(this.name, this.primus, this.secundus, this.directed);
	}

	/* (non-Javadoc)
	 * @see graph.Edge#toXML()
	 */
	public String toXML() {
		StringBuffer b = new StringBuffer();
		b.append("<edge id = \"" + this.name + "\" from = \"" +
				this.from().name() + "\" to = \"" +
				this.to().name() + "\" isdirected=\"" + this.directed + "\" >");
		b.append("</edge>");
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
		
	public int weight() {
		return weight;
	}

	public void setWeight(int w) {
		this.weight = w;
	}

}

class VerticesIterator<V extends Vertex> implements Iterator<V> {

	private int j = 0;
	private SimpleEdge<V> edge;

	/**
	 * @param implementation
	 */
	public VerticesIterator(BasicSimpleEdge<V> theEdge) {
		this.edge = theEdge;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		return this.j < 2;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	public V next() {
		switch (this.j) {
		case 0: j++; return edge.first();
		case 1: j++; return edge.second();
		default: throw new NoSuchElementException();
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	
}