package graph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class BasicDirectedAcyclicGraph<V extends Vertex, E extends DirectedEdge<V>> extends BasicDirectedGraph<V, E>
		implements DirectedAcyclicGraph<V, E> {

	public BasicDirectedAcyclicGraph(String name) {
		super(name);
	}

	public Iterable<V> ancestors(final V v) {
		return new Iterable<V>() {
			@Override
			public Iterator<V> iterator() {
				return new AncestorsIterator(v);
			}
		};
	}

	public Iterator<V> ancestorsIterator(V v) {
		return new AncestorsIterator(v);
	}

	public Iterable<V> descendents(final V v) {
		return new Iterable<V>() {
			@Override
			public Iterator<V> iterator() {
				return new DescendentsIterator(v);
			}
		};
	}

	public Iterator<V> descendentsIterator(V v) {
		return new DescendentsIterator(v);
	}

	public Iterable<V> inverseTopologicalOrder() {
		return new TopologicalOrderIterable(true);
	}

	public Iterator<V> inverseTopologicalOrderIterator() {
		return new TopologicalOrderIterable(true).iterator();
	}

	public Iterable<V> topologicalOrder() {
		return new TopologicalOrderIterable(false);
	}

	public Iterator<V> topologicalOrderIterator() {
		return new TopologicalOrderIterable(false).iterator();
	}
	
	
	class AncestorsIterator implements Iterator<V>
	{
		Stack<V> ancestors = new Stack<>();
		Set<V> stacked = new HashSet<>(); 
		
		public AncestorsIterator(V vertex)
		{
			for (V parent : parents(vertex))
			{
				ancestors.push(parent);
			}
		}

		@Override
		public boolean hasNext()
		{
			return !ancestors.isEmpty();
		}

		@Override
		public V next()
		{
			V next = ancestors.pop();
			for (V parent : parents(next))
			{
				if (!stacked.contains(parent))
				{
					ancestors.push(parent);
					stacked.add(parent);
				}
			}
			return next;
		}
	}

	class DescendentsIterator implements Iterator<V>
	{
		Stack<V> descendents = new Stack<>();
		Set<V> stacked = new HashSet<>(); 
		
		public DescendentsIterator(V vertex)
		{
			for (V child : children(vertex))
			{
				descendents.push(child);
			}
		}

		@Override
		public boolean hasNext()
		{
			return !descendents.isEmpty();
		}

		@Override
		public V next()
		{
			V next = descendents.pop();
			for (V child : children(next))
			{
				if (!stacked.contains(child))
				{
					descendents.push(child);
					stacked.add(child);
				}
			}
			return next;
		}
	}
	
	class TopologicalOrderIterable implements Iterable<V>
	{
		boolean inverseOrder;
		Set<V> expandedVertices = new HashSet<>();
		Set<V> finishedVertices = new HashSet<>();
		LinkedList<V> orderedVertices = new LinkedList<>();
		
		public TopologicalOrderIterable(boolean inverseTopologicalOrder)
		{
			inverseOrder = inverseTopologicalOrder;
			// builds topological order using DFS
			Stack<V> stack = new Stack<>();
			for (V vertex : vertices())
			{
				if (! finishedVertices.contains(vertex))
				{
					stack.add(vertex);
				}
				
				while (!stack.empty())
				{
					V top = stack.peek();
					
					if (expandedVertices.contains(top))
					{
						if (! finishedVertices.contains(top))
						{
							orderedVertices.add(top);
							finishedVertices.add(top);
						}
						stack.pop();
					}
					else
					{
						expandedVertices.add(top);
						
						for (V child : children(top))
						{
							if (! finishedVertices.contains(child))
							{
								stack.add(child);
							}
						}
						continue;
					}
				}
			}
		}

		@Override
		public Iterator<V> iterator()
		{
			// orderedVertices actually has the inverse order because tasks are appended to it during DFS and not prepended
			return inverseOrder ? orderedVertices.iterator() : orderedVertices.descendingIterator();
		}
		
	}
}
