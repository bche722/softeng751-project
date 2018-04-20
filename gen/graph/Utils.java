

package graph;


public class Utils {
    public static java.util.Iterator sortedIterator(java.util.Iterator it, java.util.Comparator comparator) {
        java.util.List list = new java.util.ArrayList();
        while (it.hasNext()) {
            list.add(it.next());
        } 
        java.util.Collections.sort(list, comparator);
        return list.iterator();
    }

    public static <E> E[] iterableToArray(java.lang.Iterable<E> iter, E[] a) {
        java.util.Collection<E> list = new java.util.ArrayList<E>();
        for (E item : iter) {
            list.add(item);
        }
        return ((E[]) (list.toArray(a)));
    }
}

