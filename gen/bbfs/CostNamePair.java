

package bbfs;


class CostNamePair<V extends graph.Vertex> {
    private int cost;

    private V vertex;

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSource;

    private java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSink;

    CostNamePair(int cost, V vertex, java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSource, java.util.concurrent.ConcurrentHashMap<V, V> routeMapFromSink) {
        bbfs.CostNamePair.this.cost = cost;
        bbfs.CostNamePair.this.vertex = vertex;
        bbfs.CostNamePair.this.routeMapFromSource = routeMapFromSource;
        bbfs.CostNamePair.this.routeMapFromSink = routeMapFromSink;
    }

    public int getCost() {
        return cost;
    }

    public V getVertex() {
        return vertex;
    }

    public java.util.concurrent.ConcurrentHashMap<V, V> getRouteMapFromSource() {
        return routeMapFromSource;
    }

    public java.util.concurrent.ConcurrentHashMap<V, V> getRouteMapFromSink() {
        return routeMapFromSink;
    }
}

