

package graph;


public class EdgeBetweenIteratorWrapper<E extends graph.Edge> implements java.util.Iterator<E> {
    private java.util.Iterator<E> wrapped;

    private graph.Edge current;

    private graph.Edge temp;

    private graph.Vertex dest;

    protected EdgeBetweenIteratorWrapper(java.util.Iterator<E> wrapped, graph.Vertex dest) {
        graph.EdgeBetweenIteratorWrapper.this.dest = dest;
        graph.EdgeBetweenIteratorWrapper.this.wrapped = wrapped;
        graph.EdgeBetweenIteratorWrapper.this.current = null;
        temp = null;
    }

    public boolean hasNext() {
        while ((graph.EdgeBetweenIteratorWrapper.this.wrapped.hasNext()) && ((current) == null)) {
            temp = graph.EdgeBetweenIteratorWrapper.this.wrapped.next();
            if (((temp.second()) == (dest)) || (((temp.first()) == (dest)) && (!(temp.isDirected())))) {
                current = temp;
            }
        } 
        return (graph.EdgeBetweenIteratorWrapper.this.current) != null;
    }

    @java.lang.SuppressWarnings(value = "unchecked")
    public E next() {
        temp = current;
        current = null;
        return ((E) (graph.EdgeBetweenIteratorWrapper.this.temp));
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException();
    }
}

