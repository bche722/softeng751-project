

package fw;


public class FloydWarshall<V extends graph.Vertex, E extends graph.DirectedEdge<V>> extends interfaces.Algorithm<graph.Vertex, graph.DirectedEdge<graph.Vertex>> {
    private graph.BasicDirectedGraph<V, E> graph;

    public FloydWarshall(graph.BasicDirectedGraph<V, E> graph, boolean isParallel) {
        fw.FloydWarshall.this.graph = graph;
        fw.FloydWarshall.this.isParallel = isParallel;
    }

    @java.lang.Override
    public void doTheJob() {
        graph.vertices().forEach(( vertex) -> java.lang.System.out.println((((vertex.name()) + " : ") + (vertex.weight()))));
        graph.edges().forEach(( edge) -> java.lang.System.out.println((((edge.name()) + " : ") + (edge.weight()))));
        java.lang.System.out.println(("Parallel Mode: " + (fw.FloydWarshall.this.isParallel)));
        if (isParallel) {
        }else {
        }
    }
}

