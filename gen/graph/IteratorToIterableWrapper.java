

package graph;


public class IteratorToIterableWrapper<T> implements java.lang.Iterable<T> {
    private java.util.Iterator<T> wrapped;

    public IteratorToIterableWrapper(java.util.Iterator<T> wrapped) {
        if (wrapped == null) {
            throw new java.lang.IllegalArgumentException("Null input detected");
        }
        graph.IteratorToIterableWrapper.this.wrapped = wrapped;
    }

    public java.util.Iterator<T> iterator() {
        java.util.Iterator<T> temp = wrapped;
        wrapped = null;
        if (temp == null) {
            throw new java.lang.IllegalStateException("This method may be called once only during the lifetime of this object");
        }
        return temp;
    }
}

