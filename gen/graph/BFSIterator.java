

package graph;


class BFSIterator<V extends graph.Vertex, E extends graph.Edge<V>> implements java.util.Iterator<V> {
    private java.util.Deque<V> queue;

    private graph.Graph<V, E> g;

    private java.util.HashSet<V> vertices;

    public BFSIterator(graph.Graph<V, E> g, V start) {
        queue = new java.util.LinkedList<V>();
        graph.BFSIterator.this.g = g;
        vertices = new java.util.HashSet<V>();
        if (g.contains(start)) {
            queue.add(start);
            vertices.add(start);
        }
    }

    public boolean hasNext() {
        return !(queue.isEmpty());
    }

    public V next() {
        V next = queue.pollFirst();
        if ((g) == null)
            throw new java.lang.NullPointerException("g is null");
        
        java.util.Iterator<V> vI = g.adjacentVerticesIterator(next);
        if (vI != null) {
            while (vI.hasNext()) {
                V temp = vI.next();
                if (vertices.add(temp)) {
                    queue.offerLast(temp);
                }
            } 
        }
        return next;
    }

    public void remove() {
        queue.removeFirst();
    }
}

