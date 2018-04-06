/**
 * 
 */
package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ting Yu
 * 
 */
public class Utils {

	/**
	 * Returns a sorted iterator from an unsorted one. Use this method as a last
	 * resort, since it is much less efficient then just sorting a collection
	 * that backs the original iterator.
	 */
	public static Iterator sortedIterator(Iterator it, Comparator comparator) {
		List list = new ArrayList();
		while (it.hasNext()) {
			list.add(it.next());
		}

		Collections.sort(list, comparator);
		return list.iterator();
	}

	public static <E> E[] iterableToArray(Iterable<E> iter, E[] a) {
		Collection<E> list = new ArrayList<E>();
		for (E item : iter) {
			list.add(item);
		}
		return (E[])list.toArray(a);
	}
}
