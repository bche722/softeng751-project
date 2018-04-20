

package bbfs;


public interface Task<V extends graph.Vertex> {
    bbfs.CostNamePair<V> execute();
}

