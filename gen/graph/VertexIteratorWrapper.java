

package graph;


class VertexIteratorWrapper<V extends graph.Vertex> implements java.util.Iterator<V> {
    private final java.util.Iterator<V> wrapped;

    private final graph.Graph g;

    private V current;

    protected VertexIteratorWrapper(java.util.Iterator<V> wrapped, graph.Graph g) {
        this.g = g;
        this.wrapped = wrapped;
    }

    protected VertexIteratorWrapper(java.util.Iterator<V> wrapped) {
        this.wrapped = wrapped;
        this.g = null;
    }

    public boolean hasNext() {
        return wrapped.hasNext();
    }

    public V next() {
        return current = wrapped.next();
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public void remove() {
        if ((current) != null) {
            if ((g) != null) {
                g.remove(current);
                current = null;
            }else {
                throw new java.lang.UnsupportedOperationException();
            }
        }else {
            throw new java.lang.IllegalStateException();
        }
    }
}

