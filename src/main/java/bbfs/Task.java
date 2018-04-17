package bbfs;

import graph.Vertex;

public interface Task<V extends Vertex> {

    CostNamePair<V> execute();

}
