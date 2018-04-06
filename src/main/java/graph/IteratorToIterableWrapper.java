/**
 * 
 */
package graph;

import java.util.Iterator;

/**
 * @author Danver Braganza
 *
 */
public class IteratorToIterableWrapper<T> implements Iterable<T>{

	private Iterator<T> wrapped;
	
	public IteratorToIterableWrapper(Iterator<T> wrapped) {
		if (wrapped == null) {
			throw new IllegalArgumentException("Null input detected");
		}
		this.wrapped = wrapped;
	}
	
	public Iterator<T> iterator() {
		Iterator<T> temp = wrapped;
		wrapped = null;
		if (temp == null) {
			throw new IllegalStateException("This method may be called once only during the lifetime of this object");
		}
		return temp;
	}

}
