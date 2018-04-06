package graph;

import java.util.ArrayList;
import java.util.Iterator;

public class BasicPath<E extends DirectedEdge> {
	private ArrayList<E> path = new ArrayList<E>();
	private int length;
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setPath(ArrayList<E> path) {
		this.path = path;
	}

	public void addToPath(E e){
		path.add(e);
		length += e.weight();
	}
	
	public void removeFromPath(E e){
		path.remove(e);
		length -= e.weight();
	}
	
	public Iterator<E> pathIterator(){
		return path.iterator();
	}
	
	public ArrayList<E> getPath(){
		return path;
	}
}
