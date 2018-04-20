

package graph;


class VerticesIterator<V extends graph.Vertex> implements java.util.Iterator<V> {
    private int j = 0;

    private graph.SimpleEdge<V> edge;

    public VerticesIterator(graph.BasicSimpleEdge<V> theEdge) {
        graph.VerticesIterator.this.edge = theEdge;
    }

    public boolean hasNext() {
        return (graph.VerticesIterator.this.j) < 2;
    }

    public V next() {
        switch (graph.VerticesIterator.this.j) {
            case 0 :
                (j)++;
                return edge.first();
            case 1 :
                (j)++;
                return edge.second();
            default :
                throw new java.util.NoSuchElementException();
        }
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException();
    }
}

