package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class BasicDirectedGraph<V extends Vertex, E extends DirectedEdge<V>> extends AbstractGraph<V,E> implements DirectedGraph<V,E>{

	protected Map<V, Set<E>> sourceVerts;
	protected Map<V, Set<E>> sourceEdges;
	/**
	 * @param name
	 */
	public BasicDirectedGraph(String name) {
		super(name, "DirectedSimple", true, true);
		this.sourceVerts = new HashMap<V, Set<E>>();
		this.sourceEdges = new HashMap<V, Set<E>>();
	}
	
	public BasicDirectedGraph(String name, String type) {
		super(name, type, false, true);
		this.sourceVerts = new HashMap<V, Set<E>>();
		this.sourceEdges = new HashMap<V, Set<E>>();
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#adjacentVerticesIterator(graph.Vertex)
	 */
	@Override
	public Iterator<V> adjacentVerticesIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new EdgeToVertexIteratorWrapper<V>(this.sourceVerts.get(v).iterator(), v);
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#degree(graph.Vertex)
	 */
	@Override
	public int degree(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return this.map.get(v).size();
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

	/* (non-Javadoc)
	 * @see 	 
	 * */

	public boolean add(E edge)  {
		if (edge != null
				&& !this.containsCongruent(edge)
				&& this.contains(edge.from())
				&& this.contains(edge.to())
				&& this.edgeForName(edge.name()) == null
				&& edge.isDirected()) {
			this.edges.add(edge);
			V source = edge.from();
			V dest = edge.to();
			this.map.get(source).add(edge);
			this.map.get(dest).add(edge);
			this.sourceEdges.get(dest).add(edge);
			this.sourceVerts.get(source).add(edge);
			this.edgeForName.put(edge.name(), edge);
			return true;
		} else return false;
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedGraph#inDegree(graph.Vertex)
	 */
	public int inDegree(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return this.sourceEdges.get(v).size();

	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedGraph#inEdges(graph.Vertex)
	 */
	public Iterator<E> inEdgesIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new EdgeIteratorWrapper<E>(this.sourceEdges.get(v).iterator());
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedGraph#outDegree(graph.Vertex)
	 */
	public int outDegree(V v) {
		try {
			return this.sourceVerts.get(v).size();
		} catch (NullPointerException p) {
			return -1;
		}
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedGraph#outEdges(graph.Vertex)
	 */
	public Iterator<E> outEdgesIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new EdgeIteratorWrapper<E>(this.sourceVerts.get(v).iterator());
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#add(graph.Vertex)
	 */
	@Override
	public boolean add(V v) {
		if (v != null && !this.contains(v) && (this.vertexForName(v.name()) == null)){
			this.vertices.add(v);
			this.vertexForName.put(v.name(), v);
			this.map.put(v, new HashSet<E>());
			this.sourceEdges.put(v, new LinkedHashSet<E>());
			this.sourceVerts.put(v, new LinkedHashSet<E>());
			return true;
		}else return false;
	}

	/* (non-Javadoc)
	 * @see graph.impl.SimpleGraphImplementation#contains(graph.Edge)
	 */
	public E edgeBetween(V v, V w) {
		if (!this.contains(v) || !this.contains(w)) {
			return null;
		}
		Iterator<E> it = this.sourceVerts.get(v).iterator();
		while (it.hasNext()) {
			E e = it.next();
			if(e.second().equals(w)) {
				return e;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#delete(java.lang.Object)
	 */
	@Override
	public boolean remove(E e) {
		if (!this.contains(e)) {
			return false;
		} else {
			E edge = this.edgeBetween(e.first(), e.second());
			Vertex a = edge.first();
			this.map.get(a).remove(edge);
			this.sourceVerts.get(a).remove(edge);

			Vertex b = edge.second();
			this.map.get(b).remove(edge);
			this.sourceEdges.get(b).remove(edge);

			this.edges.remove(edge);
			this.edgeForName.remove(edge.name());
			return true;
		}
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#copy()
	 */
	@Override
	public Graph<V,E> copy() {
		BasicDirectedGraph<V,E> that = new BasicDirectedGraph<V,E>(this.name());
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
	 * @see graph.Graph#containsCongruent(graph.Edge)
	 */
	public boolean containsCongruent(Edge<V> e) {
		if (e == null) {
			return false;
		}
		if (e.isSimple() && e.isDirected()) {
			if (this.edgeBetween(e.first(), e.second()) != null)
				return true;
			return false;
		} else return false;
	}

	/* (non-Javadoc)
	 * @see graph.impl.GraphImplementation#toXML()
	 */
	@Override
	public String toXML() {
		return "<graph id=\"" + this.name() + "\" edgeids=\" true\" edgemode=\" directed\" hypergraph=\" false\">";
	}

	@SuppressWarnings("unchecked")
	public static Graph doLoad(GXLReader input) {
		BasicDirectedGraph returned = new BasicDirectedGraph(input.getName());
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
			DirectedEdge<Vertex> temp = new BasicSimpleEdge<Vertex>(
					e.getAttributes().getNamedItem("id").getNodeValue(),
					vertmap.get(
							e.getAttributes().getNamedItem("from").getNodeValue()),
							vertmap.get(
									e.getAttributes().getNamedItem("to").getNodeValue()),
									true);
			for (int i = p.getLength(); i > 0; i--) {
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
	 * @see nz.uoaece2008.special.DirectedGraph#sinksIterator()
	 */
	public Iterator<V> sinksIterator() {
		final Iterator<V> wrapped = this.vertices.iterator();
		final DirectedGraph<V,E> sic = this;
		return new VertexIteratorWrapper<V>(new Iterator<V>() {
		
			private V next;

			public boolean hasNext() {
				while (wrapped.hasNext() && next == null) {
					V v = wrapped.next();
					if (!sic.outEdgesIterator(v).hasNext()) {
						next = v;
					}
				}
				return next != null;
			}

			public V next() {
				V temp = next;
				next = null;
				return temp;
			}

			public void remove() {
				//Shouldn't ever be called.
				throw new UnsupportedOperationException();
			}

		}, this);
	}

	/* (non-Javadoc)
	 * @see nz.uoaece2008.special.DirectedGraph#sourcesIterator()
	 */
	public Iterator<V> sourcesIterator() {
		final Iterator<V> wrapped = this.vertices.iterator();
		final DirectedGraph<V,E> sic = this;
		return new VertexIteratorWrapper<V>(new Iterator<V>() {

			private V next;

			public boolean hasNext() {
				while (wrapped.hasNext() && next == null) {
					V v = wrapped.next();
					if (!sic.inEdgesIterator(v).hasNext()) {
						next = v;
					}
				}
				return next != null;
			}

			public V next() {
				V temp = next;
				next = null;
				return temp;
			}

			public void remove() {
				//Shouldn't ever be called.
				throw new UnsupportedOperationException();
			}

		}, this);
	}

	/* (non-Javadoc)
	 * @see graph.Graph#getVertexForName(java.lang.String)
	 */
	public V vertexForName(String s) {
		return this.vertexForName.get(s);
	}

	/* (non-Javadoc)
	 * @see graph.DirectedGraph#inEdges(graph.Vertex)
	 */
	public Iterable<E> inEdges(V v) {
		return new IteratorToIterableWrapper<E>(this.inEdgesIterator(v));
	}

	/* (non-Javadoc)
	 * @see graph.DirectedGraph#outEdges(graph.Vertex)
	 */
	public Iterable<E> outEdges(V v) {
		return new IteratorToIterableWrapper<E>(this.outEdgesIterator(v));
	}

	/* (non-Javadoc)
	 * @see graph.DirectedGraph#sinks()
	 */
	public Iterable<V> sinks() {
		return new IteratorToIterableWrapper<V>(this.sinksIterator());
	}

	/* (non-Javadoc)
	 * @see graph.DirectedGraph#sources()
	 */
	public Iterable<V> sources() {
		return new IteratorToIterableWrapper<V>(this.sourcesIterator());
	}

	public Iterable<V> children(V v) {
		return new IteratorToIterableWrapper<V>(this.childrenIterator(v));
	}

	public Iterator<V> childrenIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		return new EdgeToVertexIteratorWrapper<V>(this.sourceVerts.get(v).iterator(), v);
	}

	public Iterable<V> parents(V v) {
		return new IteratorToIterableWrapper<V>(this.parentsIterator(v));
	}

	public Iterator<V> parentsIterator(V v) {
		if (!this.contains(v)) {
			throw new NoSuchElementException("The specified vertex is not part of this graph.");
		}
		
		Iterator<V> a = new EdgeToVertexIteratorWrapper<V>(this.sourceEdges.get(v).iterator(), v);
		return new EdgeToVertexIteratorWrapper<V>(this.sourceEdges.get(v).iterator(), v);
	}
	
	public Iterator<V> getParentsIterator(V v){
		return getParents(v).iterator();
		
	}
	
	public ArrayList<V> getParents(V v){
		ArrayList<V> c = new ArrayList<V>();
		Iterator<E> iter = this.inEdgesIterator(v);
		while(iter.hasNext()){
			c.add(iter.next().from());
		}
		return c;
		
	}
	
	public Iterator<V> getChildsIterator(V v){
		return getChilds(v).iterator();
		
	}
	public ArrayList<V> getChilds(V v){
		ArrayList<V> c = new ArrayList<V>();
		Iterator<E> iter = this.outEdgesIterator(v);
		while(iter.hasNext()){
			c.add(iter.next().to());
		}
		return c;
		
	}
	
	//Need revising this currently spits any path
	public BasicPath<E> longestPathByWeight(V v, V w){
		BasicPath<E> paths = new BasicPath<E>();
		BasicPath<E> tempPaths = new BasicPath<E>();;
		if(v.equals(w)){
			return paths;
		}else{
		Iterator<E> it = this.outEdgesIterator(v);
		while (it.hasNext()) {
			E e = it.next();
			if(!this.isConnected(e.to(), w)){	
				continue;
			}else{
				tempPaths = longestPathByWeight(e.to(), w);
				tempPaths.addToPath(e);
				if(tempPaths.getLength() >= paths.getLength()){
					paths = tempPaths;
				}
			}
			
		}
//		Collections.reverse(paths.getPath());
		return paths;
		
		}
	}
}
