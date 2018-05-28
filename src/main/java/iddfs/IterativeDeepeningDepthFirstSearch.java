package iddfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Vertex;
import interfaces.Algorithm;
import pu.pi.ParIterator;
import pu.pi.ParIteratorFactory;

public class IterativeDeepeningDepthFirstSearch<V extends Vertex, E extends DirectedEdge<V>>
		extends Algorithm<Vertex, DirectedEdge<Vertex>> {

	private BasicDirectedGraph<V, E> graph;

	private IterativeDeepeningDepthFirstSearch<V, E> iddfs = this;
	
	private ParIterator<Integer> pi;
	
	private Thread[] threadPool;
	
	private ConcurrentHashMap<Integer, ConcurrentHashMap<V, V>> routeMapWithHeight = new ConcurrentHashMap<Integer, ConcurrentHashMap<V, V>>();
	
	private V s;

	private V t;

	public IterativeDeepeningDepthFirstSearch(BasicDirectedGraph<V, E> graph, boolean isParallel) {
		this.graph = graph;
		this.isParallel = isParallel;
	}

	@Override
	public void doTheJob() {
		System.out.println("Parallel Mode: " + this.isParallel);
		graph.sources().forEach(v -> s = v);
		graph.sinks().forEach(v -> t = v);
		
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
		ArrayList<V> path = IDDFS(s, t);
		printPath(path);
	}

	public ArrayList<V> IDDFS(V source, V target) {
		for (int depth = 0;; depth++) {
			
			ConcurrentHashMap<V, V> routeMap = DFS(source, target, depth);
			if (routeMap != null) {
				ArrayList<V> path = new ArrayList<V>();
				V found = target;
				while (found != null) {
					path.add(found);
					found = routeMap.get(found);
				}
				Collections.reverse(path);
				return path;
			}
		}
	}

	public ConcurrentHashMap<V, V> DFS(V source, V target, int h) {
		Stack<V> stack = new Stack<V>();
		ConcurrentHashMap<V, Integer> heightMap = new ConcurrentHashMap<V, Integer>();
		ConcurrentHashMap<V, V> routeMap = new ConcurrentHashMap<V, V>();
		stack.push(source);
		heightMap.put(source, 0);
		while (!stack.isEmpty()) {
			V current = stack.pop();
			if (current.equals(target)) {
				return routeMap;
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

	public V getSource(){
		return s; 
	}
	
	public V getTarget(){
		return t;
	}
	
	private void printPath(ArrayList<V> path) {
		System.out.print("The path from source to target is: ");
		path.forEach(v -> {
			System.out.print(v.name() + " ");
		});
		System.out.println();
	}

	private void parallelSearch() {
		int threadCount = Runtime.getRuntime().availableProcessors();
		
		ArrayList<Integer> elements = new ArrayList<Integer>();
		for(int i=0;i<graph.sizeVertices();i++){
			elements.add(i);
		}
		pi = ParIteratorFactory.createParIterator(elements, threadCount);
		threadPool = new Thread[threadCount];
		
		for (int i = 0; i < threadCount; i++) {
			threadPool[i] = new Thread(new Runnable(){
				@Override
				public void run() {
					while (pi.hasNext()&& !Thread.interrupted()) {
						int height = pi.next();
						ConcurrentHashMap<V, V> routeMap = iddfs.DFS(s, t, height);
						if (routeMap != null) {
							routeMapWithHeight.put(height, routeMap);
							for(Thread thread : threadPool){
								thread.interrupt();
							}
							break;
						}
						
					}
					
				}
			});
			threadPool[i].start();
		}
		
		for (int i = 0; i < threadCount; i++) {
			try {
				threadPool[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<Integer> key = new ArrayList<Integer>(routeMapWithHeight.keySet());
		Collections.sort(key);
		ConcurrentHashMap<V, V> routeMap = routeMapWithHeight.get(key.get(0));
		ArrayList<V> path = new ArrayList<V>();
		V found = t;
		while (found != null) {
			path.add(found);
			found = routeMap.get(found);
		}
		Collections.reverse(path);
		printPath(path);
	}
}
