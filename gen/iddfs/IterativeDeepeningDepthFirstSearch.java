

package iddfs;


public class IterativeDeepeningDepthFirstSearch<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends interfaces.Algorithm<graph.Vertex, graph.DirectedEdge<graph.Vertex>> {
    private graph.BasicDirectedGraph<V, E> graph;

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMap = new java.util.concurrent.ConcurrentHashMap<>();

    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> RouteCost = new java.util.concurrent.ConcurrentHashMap<>();

    private V source;

    private V target;

    public IterativeDeepeningDepthFirstSearch(graph.BasicDirectedGraph<V, E> graph, boolean isParallel) {
        iddfs.IterativeDeepeningDepthFirstSearch.this.graph = graph;
        iddfs.IterativeDeepeningDepthFirstSearch.this.isParallel = isParallel;
    }

    @java.lang.Override
    public void doTheJob() {
        graph.vertices().forEach(( vertex) -> java.lang.System.out.println((((vertex.name()) + " : ") + (vertex.weight()))));
        graph.edges().forEach(( edge) -> java.lang.System.out.println((((edge.name()) + " : ") + (edge.weight()))));
        java.lang.System.out.println(("Parallel Mode: " + (iddfs.IterativeDeepeningDepthFirstSearch.this.isParallel)));
        if (isParallel) {
            parallelSearch();
        }else {
            sequentialSearch();
        }
    }

    private void sequentialSearch() {
        V found = IDDFS(source);
        java.util.ArrayList<V> path = new java.util.ArrayList<V>();
        while (found != null) {
            path.add(found);
            found = routeMap.get(found);
        } 
        java.util.Collections.reverse(path);
    }

    private V IDDFS(V root) {
        for (int depth = 0; ; depth++) {
            V found = DLS(root, depth);
            if (found != null) {
                return found;
            }
        }
    }

    private V DLS(V node, int depth) {
        if ((depth == 0) && (node.equals(target))) {
            return node;
        }else
            if (depth > 0) {
                java.util.Iterator<V> i = graph.childrenIterator(node);
                while (i.hasNext()) {
                    V child = i.next();
                    int newCost = ((RouteCost.get(node)) + (child.weight())) + (graph.edgeBetween(node, child).weight());
                    if (newCost < (RouteCost.get(child))) {
                        RouteCost.replace(child, newCost);
                        routeMap.put(child, node);
                    }
                    V found = DLS(child, (depth - 1));
                    if (found != null) {
                        return found;
                    }
                } 
                return null;
            }else {
                return null;
            }
        
    }

    private void parallelSearch() {
    }
}

