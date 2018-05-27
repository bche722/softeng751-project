package iddfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import interfaces.Algorithm;

public class IterativeDeepeningDepthFirstSearch<V extends Vertex, E extends DirectedEdge<V>>
		extends Algorithm<Vertex, DirectedEdge<Vertex>> {

	private BasicDirectedGraph<V, E> graph;

	private ConcurrentHashMap<V, V> routeMap;

	private V s;

	private V t;

	public IterativeDeepeningDepthFirstSearch(BasicDirectedGraph<V, E> graph, boolean isParallel) {
		this.graph = graph;
		this.isParallel = isParallel;
	}

	@Override
	public void doTheJob() {
		System.out.println("Parallel Mode: " + this.isParallel);

		long start_time = System.nanoTime();
		if (isParallel) {
			parallelSearch();
		} else {
			sequentialSearch();
		}
		long end_time = System.nanoTime();
		double difference = (end_time - start_time) / 1e6;
		System.out.println("Program finished in: " + difference + " milliseconds");
	}

	private void sequentialSearch() {
		graph.sources().forEach(v -> s = v);
		graph.sinks().forEach(v -> t = v);
		V found = IDDFS(s, t);
		ArrayList<V> path = new ArrayList<V>();
		while (found != null) {
			path.add(found);
			found = routeMap.get(found);
		}
		Collections.reverse(path);
		printPath(path);
	}

	private V IDDFS(V source, V target) {
		for (int depth = 0;; depth++) {
			routeMap = new ConcurrentHashMap<V, V>();
			V found = DFS(source, target, depth);
			if (found != null) {
				return found;
			}
		}
	}

	private V DFS(V source, V target, int h) {
		Stack<V> stack = new Stack<V>();
		ConcurrentHashMap<V, Integer> heightMap = new ConcurrentHashMap<V, Integer>();
		stack.push(source);
		heightMap.put(source, 0);
		while (!stack.isEmpty()) {
			V current = stack.pop();
			if (current.equals(target)) {
				return current;
			}
			graph.getChilds(current).forEach(v -> {
				if (routeMap.get(v) == null && heightMap.get(current) < h) {
					routeMap.put(v, current);
					heightMap.put(v, heightMap.get(current) + 1);
					stack.push(v);
				}
			});
		}
		return null;
	}

	private void printPath(ArrayList<V> path) {
		System.out.print("The path from source to target is: ");
		path.forEach(v -> {
			System.out.print(v.name() + " ");
		});
		System.out.println();
	}

	private void parallelSearch() {
		// TODO Auto-generated method stub

	}
}
