package fw;

import java.util.HashMap;

import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Edge;
import graph.Vertex;
import interfaces.Algorithm;

public class FloydWarshall<V extends Vertex, E extends DirectedEdge<V>> extends Algorithm<Vertex, DirectedEdge<Vertex>> {

    private BasicDirectedGraph<V, E> graph;
    private int [][] matrix = null;
    private HashMap<V,Integer> map= new HashMap<V,Integer>();
    private int p =0;
    private long startTime;
    private long endTime;
    private long totalTime;
    public FloydWarshall(BasicDirectedGraph<V, E> graph, boolean isParallel) {
        this.graph = graph;
        this.isParallel = isParallel;
    }

    @Override
    public void doTheJob() {
        graph.vertices().forEach(vertex -> System.out.println(vertex.name() + " : " + vertex.weight()));
        graph.edges().forEach(edge -> System.out.println(edge.name() + " : " + edge.weight()));
        System.out.println("Parallel Mode: " + this.isParallel);

        //todo - algorithm implementation
        if (isParallel){
        	init();
        	parallelSearch();

        } else {
        	init();
        	startTime = System.currentTimeMillis();
        	sequentialSearch();
        	endTime = System.currentTimeMillis();
        	totalTime = endTime - startTime;
        	
        	
        	for(int i=0; i<graph.sizeVertices(); i++){
        			System.out.println();
        		for(int j=0; j<graph.sizeVertices(); j++){
        			//System.out.println();
        			if(i==j){
        				System.out.print("0   ");
        			}else{
        				if(matrix[i][j]<99999){
            				System.out.print(matrix[i][j]+"   ");
            			}else{
            				System.out.print("INF"+ " ");
            			}
        			}
        			
        		}
        	}
        	System.out.println();
        	System.out.println("start time: "+ startTime);
        	System.out.println("end time: "+ endTime);
        	System.out.println("total time: "+ totalTime);

        }
    }
    
    private void init(){
    	matrix=new int [graph.sizeVertices()][graph.sizeVertices()];
    	for(int n=0; n<graph.sizeVertices(); n++){
    		for(int m=0; m<graph.sizeVertices(); m++){
    			matrix[n][m] = Integer.MAX_VALUE;
    		}
    	}
    	graph.vertices().forEach(vertex -> map.put(vertex, p++));
    	graph.edges().forEach(edge -> matrix[map.get(edge.from())][map.get(edge.to())]=edge.weight());
    }
    
    private void sequentialSearch(){
    	for(int k=0; k<graph.sizeVertices(); k++){
    		for(int i=0; i<graph.sizeVertices(); i++){
    			for(int j=0; j<graph.sizeVertices(); j++){
    				if(matrix[i][k]!= Integer.MAX_VALUE&&
    				   matrix[k][j]!= Integer.MAX_VALUE&&
    				   matrix[i][k]+matrix[k][j]<matrix[i][j]){
    						matrix[i][j]=matrix[i][k]+matrix[k][j];
    				}
    			}
    		}
    	}
    }
    

    
    private void parallelSearch(){
    	init();
    	
    }
}
