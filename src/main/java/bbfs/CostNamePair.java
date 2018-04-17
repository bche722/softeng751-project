package bbfs;

import graph.Vertex;

import java.util.concurrent.ConcurrentHashMap;

class CostNamePair<V extends Vertex> {

    private int cost;
    private V vertex;
    private ConcurrentHashMap<V, V> routeMapFromSource;
    private ConcurrentHashMap<V, V> routeMapFromSink;

    CostNamePair(int cost, V vertex, ConcurrentHashMap<V, V> routeMapFromSource, ConcurrentHashMap<V, V>
            routeMapFromSink) {
        this.cost = cost;
        this.vertex = vertex;
        this.routeMapFromSource = routeMapFromSource;
        this.routeMapFromSink = routeMapFromSink;
    }

    public int getCost() {
        return cost;
    }

    public V getVertex() {
        return vertex;
    }

    public ConcurrentHashMap<V, V> getRouteMapFromSource() {
        return routeMapFromSource;
    }

    public ConcurrentHashMap<V, V> getRouteMapFromSink() {
        return routeMapFromSink;
    }

}
