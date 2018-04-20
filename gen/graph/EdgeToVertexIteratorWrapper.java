

package graph;


class EdgeToVertexIteratorWrapper<V extends graph.Vertex> implements java.util.Iterator<V> {
    private java.util.Iterator<? extends graph.Edge<V>> wrapped;

    private java.util.Iterator<V> vertIt;

    private V v;

    private V next;

    protected EdgeToVertexIteratorWrapper(java.util.Iterator<? extends graph.Edge<V>> wrapped, V v) {
        graph.EdgeToVertexIteratorWrapper.this.wrapped = wrapped;
        graph.EdgeToVertexIteratorWrapper.this.v = v;
        graph.EdgeToVertexIteratorWrapper.this.vertIt = (wrapped.hasNext()) ? wrapped.next().incidentVertices() : null;
        graph.EdgeToVertexIteratorWrapper.this.hasNext();
    }

    public boolean hasNext() {
        if ((vertIt) == null) {
            return false;
        }
        while (((wrapped.hasNext()) || (vertIt.hasNext())) && ((next) == null)) {
            if (vertIt.hasNext()) {
                next = vertIt.next();
                if (next.equals(v))
                    next = null;
                
            }else {
                vertIt = wrapped.next().incidentVertices();
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
}

