

package graph;


public class BasicPath<E extends graph.DirectedEdge> {
    private java.util.ArrayList<E> path = new java.util.ArrayList<E>();

    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        graph.BasicPath.this.length = length;
    }

    public void setPath(java.util.ArrayList<E> path) {
        graph.BasicPath.this.path = path;
    }

    public void addToPath(E e) {
        path.add(e);
        length += e.weight();
    }

    public void removeFromPath(E e) {
        path.remove(e);
        length -= e.weight();
    }

    public java.util.Iterator<E> pathIterator() {
        return path.iterator();
    }

    public java.util.ArrayList<E> getPath() {
        return path;
    }
}

