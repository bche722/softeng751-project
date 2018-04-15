package iddfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import interfaces.Algorithm;

public class IterativeDeepeningDepthFirstSearch<V extends Vertex, E extends DirectedEdge<V>>
extends Algorithm<Vertex, DirectedEdge<Vertex>> {

	private BasicDirectedGraph<V, E> graph;

	private ConcurrentHashMap<V, V> routeMap = new ConcurrentHashMap<>();

	private ConcurrentHashMap<V, Integer> RouteCost = new ConcurrentHashMap<>();

	private V source;

	private V target;

	public IterativeDeepeningDepthFirstSearch(BasicDirectedGraph<V, E> graph, boolean isParallel) {
		this.graph = graph;
		this.isParallel = isParallel;
	}

	@Override
	public void doTheJob() {
		graph.vertices().forEach(vertex -> System.out.println(vertex.name() + " : " + vertex.weight()));
		graph.edges().forEach(edge -> System.out.println(edge.name() + " : " + edge.weight()));
		System.out.println("Parallel Mode: " + this.isParallel);

		if (isParallel) {
			parallelSearch();
		} else {
			sequentialSearch();
		}
	}

	private void sequentialSearch() {
		V found = IDDFS(source);
		ArrayList<V> path = new ArrayList<V>();
		while(found != null) {
			path.add(found);
			found = routeMap.get(found);
		}
		Collections.reverse(path);
	}
	
	private V IDDFS(V root) {
		for(int depth=0;;depth++) {
			V found = DLS(root, depth);
			if (found != null) {
				return found;
			}
		}
	}

	private V DLS(V node, int depth) {
		if (depth == 0 && node.equals(target)) {
			return node;
		}else if(depth > 0) {
			Iterator<V> i = graph.childrenIterator(node);
			while(i.hasNext()) {
				V child = i.next();
				int newCost = RouteCost.get(node) + child.weight() + graph.edgeBetween(node, child).weight();
				if (newCost < RouteCost.get(child)) {
                    RouteCost.replace(child, newCost);
                    routeMap.put(child, node);
                }
				V found = DLS(child,depth-1);
				if(found != null) {
					return found;
				}
			}
			return null;
		}else {
			return null;
		}



	}

	private void parallelSearch() {
		// TODO Auto-generated method stub

	}
}
