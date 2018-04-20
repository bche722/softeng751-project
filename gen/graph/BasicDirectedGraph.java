

package graph;


public class BasicDirectedGraph<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends graph.AbstractGraph<V, E> implements graph.DirectedGraph<V, E> {
    protected java.util.Map<V, java.util.Set<E>> sourceVerts;

    protected java.util.Map<V, java.util.Set<E>> sourceEdges;

    public BasicDirectedGraph(java.lang.String name) {
        super(name, "DirectedSimple", true, true);
        graph.BasicDirectedGraph.this.sourceVerts = new java.util.HashMap<V, java.util.Set<E>>();
        graph.BasicDirectedGraph.this.sourceEdges = new java.util.HashMap<V, java.util.Set<E>>();
    }

    public BasicDirectedGraph(java.lang.String name, java.lang.String type) {
        super(name, type, false, true);
        graph.BasicDirectedGraph.this.sourceVerts = new java.util.HashMap<V, java.util.Set<E>>();
        graph.BasicDirectedGraph.this.sourceEdges = new java.util.HashMap<V, java.util.Set<E>>();
    }

    @java.lang.Override
    public java.util.Iterator<V> adjacentVerticesIterator(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeToVertexIteratorWrapper<V>(graph.BasicDirectedGraph.this.sourceVerts.get(v).iterator(), v);
    }

    @java.lang.Override
    public int degree(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return graph.BasicDirectedGraph.this.map.get(v).size();
    }

    @java.lang.Override
    public java.util.Iterator<E> incidentEdgesIterator(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeIteratorWrapper<E>(graph.BasicDirectedGraph.this.map.get(v).iterator());
    }

    public boolean add(E edge) {
        if ((((((edge != null) && (!(graph.BasicDirectedGraph.this.containsCongruent(edge)))) && (graph.BasicDirectedGraph.this.contains(edge.from()))) && (graph.BasicDirectedGraph.this.contains(edge.to()))) && ((graph.BasicDirectedGraph.this.edgeForName(edge.name())) == null)) && (edge.isDirected())) {
            graph.BasicDirectedGraph.this.edges.add(edge);
            V source = edge.from();
            V dest = edge.to();
            graph.BasicDirectedGraph.this.map.get(source).add(edge);
            graph.BasicDirectedGraph.this.map.get(dest).add(edge);
            graph.BasicDirectedGraph.this.sourceEdges.get(dest).add(edge);
            graph.BasicDirectedGraph.this.sourceVerts.get(source).add(edge);
            graph.BasicDirectedGraph.this.edgeForName.put(edge.name(), edge);
            return true;
        }else
            return false;
        
    }

    public int inDegree(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return graph.BasicDirectedGraph.this.sourceEdges.get(v).size();
    }

    public java.util.Iterator<E> inEdgesIterator(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeIteratorWrapper<E>(graph.BasicDirectedGraph.this.sourceEdges.get(v).iterator());
    }

    public int outDegree(V v) {
        try {
            return graph.BasicDirectedGraph.this.sourceVerts.get(v).size();
        } catch (java.lang.NullPointerException p) {
            return -1;
        }
    }

    public java.util.Iterator<E> outEdgesIterator(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeIteratorWrapper<E>(graph.BasicDirectedGraph.this.sourceVerts.get(v).iterator());
    }

    @java.lang.Override
    public boolean add(V v) {
        if (((v != null) && (!(graph.BasicDirectedGraph.this.contains(v)))) && ((graph.BasicDirectedGraph.this.vertexForName(v.name())) == null)) {
            graph.BasicDirectedGraph.this.vertices.add(v);
            graph.BasicDirectedGraph.this.vertexForName.put(v.name(), v);
            graph.BasicDirectedGraph.this.map.put(v, new java.util.HashSet<E>());
            graph.BasicDirectedGraph.this.sourceEdges.put(v, new java.util.LinkedHashSet<E>());
            graph.BasicDirectedGraph.this.sourceVerts.put(v, new java.util.LinkedHashSet<E>());
            return true;
        }else
            return false;
        
    }

    public E edgeBetween(V v, V w) {
        if ((!(graph.BasicDirectedGraph.this.contains(v))) || (!(graph.BasicDirectedGraph.this.contains(w)))) {
            return null;
        }
        java.util.Iterator<E> it = graph.BasicDirectedGraph.this.sourceVerts.get(v).iterator();
        while (it.hasNext()) {
            E e = it.next();
            if (e.second().equals(w)) {
                return e;
            }
        } 
        return null;
    }

    @java.lang.Override
    public boolean remove(E e) {
        if (!(graph.BasicDirectedGraph.this.contains(e))) {
            return false;
        }else {
            E edge = graph.BasicDirectedGraph.this.edgeBetween(e.first(), e.second());
            graph.Vertex a = edge.first();
            graph.BasicDirectedGraph.this.map.get(a).remove(edge);
            graph.BasicDirectedGraph.this.sourceVerts.get(a).remove(edge);
            graph.Vertex b = edge.second();
            graph.BasicDirectedGraph.this.map.get(b).remove(edge);
            graph.BasicDirectedGraph.this.sourceEdges.get(b).remove(edge);
            graph.BasicDirectedGraph.this.edges.remove(edge);
            graph.BasicDirectedGraph.this.edgeForName.remove(edge.name());
            return true;
        }
    }

    @java.lang.Override
    public graph.Graph<V, E> copy() {
        graph.BasicDirectedGraph<V, E> that = new graph.BasicDirectedGraph<V, E>(graph.BasicDirectedGraph.this.name());
        java.util.Iterator<V> thisVert = graph.BasicDirectedGraph.this.verticesIterator();
        while (thisVert.hasNext()) {
            that.add(thisVert.next());
        } 
        java.util.Iterator<E> thisEdge = graph.BasicDirectedGraph.this.edgesIterator();
        while (thisEdge.hasNext()) {
            that.add(thisEdge.next());
        } 
        return that;
    }

    public boolean containsCongruent(graph.Edge<V> e) {
        if (e == null) {
            return false;
        }
        if ((e.isSimple()) && (e.isDirected())) {
            if ((graph.BasicDirectedGraph.this.edgeBetween(e.first(), e.second())) != null)
                return true;
            
            return false;
        }else
            return false;
        
    }

    @java.lang.Override
    public java.lang.String toXML() {
        return ("<graph id=\"" + (graph.BasicDirectedGraph.this.name())) + "\" edgeids=\" true\" edgemode=\" directed\" hypergraph=\" false\">";
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public static graph.Graph doLoad(graph.GXLReader input) {
        graph.BasicDirectedGraph returned = new graph.BasicDirectedGraph(input.getName());
        java.util.Iterator<org.w3c.dom.Node> verts = input.getVertices();
        java.util.HashMap<java.lang.String, graph.Vertex> vertmap = new java.util.HashMap<java.lang.String, graph.Vertex>();
        while (verts.hasNext()) {
            org.w3c.dom.NodeList p = verts.next().getChildNodes();
            graph.Vertex temp = null;
            for (int i = 0; i < (p.getLength()); i++) {
                org.w3c.dom.Node child = p.item(i);
                java.lang.String attributeName = (child.getAttributes()) != null ? child.getAttributes().getNamedItem("name").getNodeValue() : "";
                if (attributeName.equals("name")) {
                    temp = new graph.BasicVertex(child.getFirstChild().getTextContent());
                    vertmap.put(child.getFirstChild().getTextContent(), temp);
                }else
                    if (attributeName.equals("weight")) {
                        java.lang.String type = child.getAttributes().getNamedItem("type").getNodeValue();
                        if ("int".equals(type)) {
                            temp.setWeight(java.lang.Integer.parseInt(child.getTextContent()));
                        }
                    }
                
            }
            returned.add(temp);
        } 
        java.util.Iterator<org.w3c.dom.Node> edges = input.getEdges();
        while (edges.hasNext()) {
            org.w3c.dom.Node e = edges.next();
            org.w3c.dom.NodeList p = e.getChildNodes();
            graph.DirectedEdge<graph.Vertex> temp = new graph.BasicSimpleEdge<graph.Vertex>(e.getAttributes().getNamedItem("id").getNodeValue(), vertmap.get(e.getAttributes().getNamedItem("from").getNodeValue()), vertmap.get(e.getAttributes().getNamedItem("to").getNodeValue()), true);
            for (int i = p.getLength(); i > 0; i--) {
                org.w3c.dom.Node child = p.item((i - 1));
                java.lang.String attributeName = (child.getAttributes()) != null ? child.getAttributes().getNamedItem("name").getNodeValue() : "";
                if (attributeName.equals("weight")) {
                    java.lang.String type = child.getAttributes().getNamedItem("type").getNodeValue();
                    if ("int".equals(type)) {
                        temp.setWeight(java.lang.Integer.parseInt(child.getTextContent()));
                    }
                }
            }
            returned.add(temp);
        } 
        return returned;
    }

    public java.util.Iterator<V> sinksIterator() {
        final java.util.Iterator<V> wrapped = graph.BasicDirectedGraph.this.vertices.iterator();
        final graph.DirectedGraph<V, E> sic = graph.BasicDirectedGraph.this;
        return new graph.VertexIteratorWrapper<V>(new java.util.Iterator<V>() {
            private V next;

            public boolean hasNext() {
                while ((wrapped.hasNext()) && ((next) == null)) {
                    V v = wrapped.next();
                    if (!(sic.outEdgesIterator(v).hasNext())) {
                        next = v;
                    }
                } 
                return (next) != null;
            }

            public V next() {
                V temp = next;
                next = null;
                return temp;
            }

            public void remove() {
                throw new java.lang.UnsupportedOperationException();
            }
        }, graph.BasicDirectedGraph.this);
    }

    public java.util.Iterator<V> sourcesIterator() {
        final java.util.Iterator<V> wrapped = graph.BasicDirectedGraph.this.vertices.iterator();
        final graph.DirectedGraph<V, E> sic = graph.BasicDirectedGraph.this;
        return new graph.VertexIteratorWrapper<V>(new java.util.Iterator<V>() {
            private V next;

            public boolean hasNext() {
                while ((wrapped.hasNext()) && ((next) == null)) {
                    V v = wrapped.next();
                    if (!(sic.inEdgesIterator(v).hasNext())) {
                        next = v;
                    }
                } 
                return (next) != null;
            }

            public V next() {
                V temp = next;
                next = null;
                return temp;
            }

            public void remove() {
                throw new java.lang.UnsupportedOperationException();
            }
        }, graph.BasicDirectedGraph.this);
    }

    public V vertexForName(java.lang.String s) {
        return graph.BasicDirectedGraph.this.vertexForName.get(s);
    }

    public java.lang.Iterable<E> inEdges(V v) {
        return new graph.IteratorToIterableWrapper<E>(graph.BasicDirectedGraph.this.inEdgesIterator(v));
    }

    public java.lang.Iterable<E> outEdges(V v) {
        return new graph.IteratorToIterableWrapper<E>(graph.BasicDirectedGraph.this.outEdgesIterator(v));
    }

    public java.lang.Iterable<V> sinks() {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedGraph.this.sinksIterator());
    }

    public java.lang.Iterable<V> sources() {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedGraph.this.sourcesIterator());
    }

    public java.lang.Iterable<V> children(V v) {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedGraph.this.childrenIterator(v));
    }

    public java.util.Iterator<V> childrenIterator(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        return new graph.EdgeToVertexIteratorWrapper<V>(graph.BasicDirectedGraph.this.sourceVerts.get(v).iterator(), v);
    }

    public java.lang.Iterable<V> parents(V v) {
        return new graph.IteratorToIterableWrapper<V>(graph.BasicDirectedGraph.this.parentsIterator(v));
    }

    public java.util.Iterator<V> parentsIterator(V v) {
        if (!(graph.BasicDirectedGraph.this.contains(v))) {
            throw new java.util.NoSuchElementException("The specified vertex is not part of this graph.");
        }
        java.util.Iterator<V> a = new graph.EdgeToVertexIteratorWrapper<V>(graph.BasicDirectedGraph.this.sourceEdges.get(v).iterator(), v);
        return new graph.EdgeToVertexIteratorWrapper<V>(graph.BasicDirectedGraph.this.sourceEdges.get(v).iterator(), v);
    }

    public java.util.Iterator<V> getParentsIterator(V v) {
        return getParents(v).iterator();
    }

    public java.util.ArrayList<V> getParents(V v) {
        java.util.ArrayList<V> c = new java.util.ArrayList<V>();
        java.util.Iterator<E> iter = graph.BasicDirectedGraph.this.inEdgesIterator(v);
        while (iter.hasNext()) {
            c.add(iter.next().from());
        } 
        return c;
    }

    public java.util.Iterator<V> getChildsIterator(V v) {
        return getChilds(v).iterator();
    }

    public java.util.ArrayList<V> getChilds(V v) {
        java.util.ArrayList<V> c = new java.util.ArrayList<V>();
        java.util.Iterator<E> iter = graph.BasicDirectedGraph.this.outEdgesIterator(v);
        while (iter.hasNext()) {
            c.add(iter.next().to());
        } 
        return c;
    }

    public graph.BasicPath<E> longestPathByWeight(V v, V w) {
        graph.BasicPath<E> paths = new graph.BasicPath<E>();
        graph.BasicPath<E> tempPaths = new graph.BasicPath<E>();
        if (v.equals(w)) {
            return paths;
        }else {
            java.util.Iterator<E> it = graph.BasicDirectedGraph.this.outEdgesIterator(v);
            while (it.hasNext()) {
                E e = it.next();
                if (!(graph.BasicDirectedGraph.this.isConnected(e.to(), w))) {
                    continue;
                }else {
                    tempPaths = longestPathByWeight(e.to(), w);
                    tempPaths.addToPath(e);
                    if ((tempPaths.getLength()) >= (paths.getLength())) {
                        paths = tempPaths;
                    }
                }
            } 
            return paths;
        }
    }
}

