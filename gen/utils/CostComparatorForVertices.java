

package utils;


public class CostComparatorForVertices<V extends graph.Vertex> implements java.util.Comparator<V> {
    private java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> routeCost;

    public CostComparatorForVertices(java.util.concurrent.ConcurrentHashMap<V, java.lang.Integer> routeCost) {
        utils.CostComparatorForVertices.this.routeCost = routeCost;
    }

    @java.lang.Override
    public int compare(V o1, V o2) {
        return java.lang.Integer.compare(routeCost.get(o1), routeCost.get(o2));
    }
}

