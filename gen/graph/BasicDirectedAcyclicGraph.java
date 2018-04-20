

package graph;


public class BasicDirectedAcyclicGraph<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends graph.BasicDirectedGraph<V, E> implements graph.DirectedAcyclicGraph<V, E> {
    public BasicDirectedAcyclicGraph(java.lang.String name) {
        super(name);
    }

    public java.lang.Iterable<V> ancestors(final V v) {
        return new java.lang.Iterable<V>() {
            @java.lang.Override
            public java.util.Iterator<V> iterator() {
                return new AncestorsIterator(v);
            }
        };
    }

    public java.util.Iterator<V> ancestorsIterator(V v) {
        return new AncestorsIterator(v);
    }

    public java.lang.Iterable<V> descendents(final V v) {
        return new java.lang.Iterable<V>() {
            @java.lang.Override
            public java.util.Iterator<V> iterator() {
                return new DescendentsIterator(v);
            }
        };
    }

    public java.util.Iterator<V> descendentsIterator(V v) {
        return new DescendentsIterator(v);
    }

    public java.lang.Iterable<V> inverseTopologicalOrder() {
        return new TopologicalOrderIterable(true);
    }

    public java.util.Iterator<V> inverseTopologicalOrderIterator() {
        return new TopologicalOrderIterable(true).iterator();
    }

    public java.lang.Iterable<V> topologicalOrder() {
        return new TopologicalOrderIterable(false);
    }

    public java.util.Iterator<V> topologicalOrderIterator() {
        return new TopologicalOrderIterable(false).iterator();
    }

    class AncestorsIterator implements java.util.Iterator<V> {
        java.util.Stack<V> ancestors = new java.util.Stack<>();

        java.util.Set<V> stacked = new java.util.HashSet<>();

        public AncestorsIterator(V vertex) {
            for (V parent : parents(vertex)) {
                ancestors.push(parent);
            }
        }

        @java.lang.Override
        public boolean hasNext() {
            return !(ancestors.isEmpty());
        }

        @java.lang.Override
        public V next() {
            V next = ancestors.pop();
            for (V parent : parents(next)) {
                if (!(stacked.contains(parent))) {
                    ancestors.push(parent);
                    stacked.add(parent);
                }
            }
            return next;
        }
    }

    class DescendentsIterator implements java.util.Iterator<V> {
        java.util.Stack<V> descendents = new java.util.Stack<>();

        java.util.Set<V> stacked = new java.util.HashSet<>();

        public DescendentsIterator(V vertex) {
            for (V child : children(vertex)) {
                descendents.push(child);
            }
        }

        @java.lang.Override
        public boolean hasNext() {
            return !(descendents.isEmpty());
        }

        @java.lang.Override
        public V next() {
            V next = descendents.pop();
            for (V child : children(next)) {
                if (!(stacked.contains(child))) {
                    descendents.push(child);
                    stacked.add(child);
                }
            }
            return next;
        }
    }

    class TopologicalOrderIterable implements java.lang.Iterable<V> {
        boolean inverseOrder;

        java.util.Set<V> expandedVertices = new java.util.HashSet<>();

        java.util.Set<V> finishedVertices = new java.util.HashSet<>();

        java.util.LinkedList<V> orderedVertices = new java.util.LinkedList<>();

        public TopologicalOrderIterable(boolean inverseTopologicalOrder) {
            inverseOrder = inverseTopologicalOrder;
            java.util.Stack<V> stack = new java.util.Stack<>();
            for (V vertex : vertices()) {
                if (!(finishedVertices.contains(vertex))) {
                    stack.add(vertex);
                }
                while (!(stack.empty())) {
                    V top = stack.peek();
                    if (expandedVertices.contains(top)) {
                        if (!(finishedVertices.contains(top))) {
                            orderedVertices.add(top);
                            finishedVertices.add(top);
                        }
                        stack.pop();
                    }else {
                        expandedVertices.add(top);
                        for (V child : children(top)) {
                            if (!(finishedVertices.contains(child))) {
                                stack.add(child);
                            }
                        }
                        continue;
                    }
                } 
            }
        }

        @java.lang.Override
        public java.util.Iterator<V> iterator() {
            return inverseOrder ? orderedVertices.iterator() : orderedVertices.descendingIterator();
        }
    }
}

