

package graph;


class EdgeIteratorWrapper<E extends graph.Edge<? extends graph.Vertex>> implements java.util.Iterator<E> {
    private java.util.Iterator<E> wrapped;

    protected EdgeIteratorWrapper(java.util.Iterator<E> wrapped) {
        graph.EdgeIteratorWrapper.this.wrapped = wrapped;
    }

    public boolean hasNext() {
        return wrapped.hasNext();
    }

    public E next() {
        return wrapped.next();
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException("Removal not supported.");
    }
}

