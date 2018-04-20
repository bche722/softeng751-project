

package graph;


class DFSIterator<V extends graph.Vertex, E extends graph.Edge<V>> implements java.util.Iterator<V> {
    private java.util.Set<V> whiteList;

    private graph.Graph<V, E> g;

    private java.util.Deque<V> blackList;

    public DFSIterator(graph.Graph<V, E> g) {
        whiteList = new java.util.HashSet<V>();
        blackList = new java.util.LinkedList<V>();
        java.util.Iterator<V> vI = g.verticesIterator();
        while (vI.hasNext()) {
            whiteList.add(vI.next());
        } 
    }

    public boolean hasNext() {
        return (!(whiteList.isEmpty())) || (!(blackList.isEmpty()));
    }

    public V next() {
        V next = blackList.pollFirst();
        if (next != null) {
            java.util.Iterator<V> vI = g.adjacentVerticesIterator(next);
            while (vI.hasNext()) {
                if (whiteList.remove(vI.next())) {
                    blackList.offerFirst(vI.next());
                }
            } 
        }else {
            next = whiteList.iterator().next();
            java.util.Iterator<V> vI = g.adjacentVerticesIterator(next);
            while (vI.hasNext()) {
                blackList.offerFirst(vI.next());
            } 
        }
        return next;
    }

    public void remove() {
        throw new java.lang.UnsupportedOperationException();
    }
}

