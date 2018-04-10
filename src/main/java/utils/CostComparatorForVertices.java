package utils;

import graph.DirectedEdge;
import graph.Vertex;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;

public class CostComparatorForVertices<V extends Vertex, E extends DirectedEdge<V>> implements Comparator<V> {

    private ConcurrentHashMap<V, Integer> routeCost;

    public CostComparatorForVertices(ConcurrentHashMap<V, Integer> routeCost) {
        this.routeCost = routeCost;
    }

    /**
     * Compare vertices' costs.
     * 1 if first param > second param, -1 if first param < second param, 0 if both equal
     * @param o1 - Vertex
     * @param o2 - Vertex
     * @return int
     */
    @Override
    public int compare(V o1, V o2) {
        return Integer.compare(routeCost.get(o1), routeCost.get(o2));
    }
}
