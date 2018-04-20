

package graph;


class UniqueVertexIteratorWrapper<V extends graph.Vertex> implements java.util.Iterator<V> {
    private java.util.Iterator<V> wrapped;

    private java.util.HashSet<V> vertices;

    private V vert;

    protected UniqueVertexIteratorWrapper(java.util.Iterator<V> wrapped) {
        graph.UniqueVertexIteratorWrapper.this.wrapped = wrapped;
        graph.UniqueVertexIteratorWrapper.this.vertices = new java.util.HashSet<V>();
        if (wrapped.hasNext()) {
            vert = wrapped.next();
            vertices.add(vert);
        }
    }

    public boolean hasNext() {
        return (vert) != null;
    }

    public V next() {
        V temp = vert;
        vert = null;
        while ((wrapped.hasNext()) && ((vert) == null)) {
            vert = wrapped.next();
            if (!(vertices.add(vert))) {
                vert = null;
            }
        } 
        return temp;
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException();
    }
}

