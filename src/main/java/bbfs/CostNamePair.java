package bbfs;

import graph.Vertex;

public class CostNamePair<I extends Integer, V extends Vertex> {

    private I cost;
    private V vertex;

    public CostNamePair(I cost, V vertex) {
        this.cost = cost;
        this.vertex = vertex;
    }

    public I getCost() {
        return cost;
    }

    public V getVertex() {
        return vertex;
    }
}
