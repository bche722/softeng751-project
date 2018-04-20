

package utils;


public class RouteMarker implements graph.Vertex {
    private graph.Vertex vertex;

    private graph.Vertex lastVisited;

    public RouteMarker(graph.Vertex vertex) {
        utils.RouteMarker.this.vertex = vertex;
    }

    public void setLastVisited(graph.Vertex updatedLastVisited) {
        utils.RouteMarker.this.lastVisited = updatedLastVisited;
    }

    public graph.Vertex getLastVisited() {
        return lastVisited;
    }

    @java.lang.Override
    public java.lang.String name() {
        return vertex.name();
    }

    @java.lang.Override
    public void setName(java.lang.String name) {
        vertex.setName(name);
    }

    @java.lang.Override
    public java.lang.String toXML() {
        return vertex.toXML();
    }

    @java.lang.Override
    public graph.Vertex clone() {
        try {
            return ((graph.Vertex) (super.clone()));
        } catch (java.lang.CloneNotSupportedException e) {
            return null;
        }
    }

    @java.lang.Override
    public int weight() {
        return vertex.weight();
    }

    @java.lang.Override
    public void setWeight(int w) {
        vertex.setWeight(w);
    }

    @java.lang.Override
    public int compareWeight(graph.Vertex c) {
        return vertex.compareWeight(c);
    }
}

