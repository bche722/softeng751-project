package utils;

import graph.Vertex;

/**
 * A Vertex wrapper
 */
public class RouteMarker implements Vertex {

    private Vertex vertex;
    private Vertex lastVisited;

    public RouteMarker(Vertex vertex){
        this.vertex = vertex;
    }

    /**
     * Update the last step of this RouteMarker
     * @param updatedLastVisited
     */
    public void setLastVisited(Vertex updatedLastVisited){
        this.lastVisited = updatedLastVisited;
    }

    public Vertex getLastVisited(){
        return lastVisited;
    }

    @Override
    public String name() {
        return vertex.name();
    }

    @Override
    public void setName(String name) {
        vertex.setName(name);
    }

    @Override
    public String toXML() {
        return vertex.toXML();
    }

    @Override
    public Vertex clone() {
        try {
            return (Vertex) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public int weight() {
        return vertex.weight();
    }

    @Override
    public void setWeight(int w) {
        vertex.setWeight(w);
    }

    @Override
    public int compareWeight(Vertex c) {
        return vertex.compareWeight(c);
    }
}
