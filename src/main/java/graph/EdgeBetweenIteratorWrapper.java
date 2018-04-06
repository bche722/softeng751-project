/**
 * 
 */
package graph;

import java.util.Iterator;

/**
 * @author Danver Braganza
 *
 */
public class EdgeBetweenIteratorWrapper<E extends Edge> implements Iterator<E>{
	
	private Iterator<E> wrapped;
	private Edge current;
	private Edge temp;
	private Vertex dest;

	protected EdgeBetweenIteratorWrapper(Iterator<E> wrapped, Vertex dest) {
		this.dest = dest;
		this.wrapped = wrapped;
		this.current = null;
		temp = null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		while (this.wrapped.hasNext() && current == null) {
			temp = this.wrapped.next();
			if ((temp.second() == dest)
					|| (temp.first() == dest && !temp.isDirected())) {
				current = temp;
			}
		}
		return this.current != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@SuppressWarnings("unchecked")
	public E next() {
		temp = current;
		current = null;
		return (E)this.temp;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}


	
}