package fw;

import java.util.HashMap;
import java.util.*;

import apt.annotations.*;
import com.sun.org.apache.bcel.internal.generic.MULTIANEWARRAY;
import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.Edge;
import graph.Vertex;
import interfaces.Algorithm;
import pu.RedLib.Reducible;
import pu.loopScheduler.LoopScheduler;
import pu.loopScheduler.LoopSchedulerFactory;
import pu.loopScheduler.AbstractLoopScheduler;
import pu.loopScheduler.DynamicLoopScheduler;
import pu.loopScheduler.*;


import static pt.runtime.ParaTask.TaskType.MULTI;


public class FloydWarshall<V extends Vertex, E extends DirectedEdge<V>> extends Algorithm<Vertex, DirectedEdge<Vertex>> {

    private BasicDirectedGraph<V, E> graph;
    private int [][] matrix = null;
    private HashMap<V,Integer> map= new HashMap<V,Integer>();
    private int p =0;
    private long startTime;
    private long endTime;
    private long totalTime;
    private long difference;
    private int id1;
    private int id2;
    private int iteration = 0;


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
        //This will initialize the matrix[i][j]
        long initialStart = System.currentTimeMillis();
        init();
        long initialFinish = System.currentTimeMillis();
        long differenceInit = initialFinish - initialStart;
        System.out.println("Time taken to initialize the matrix is: " + differenceInit + " milliseconds");
         startTime = System.currentTimeMillis();
        if (isParallel){
            parallelSearch();
            System.out.println("Parallel version done!");
        } else {
            sequentialSearch();
            System.out.println("Sequential version done!");
        }
        endTime = System.currentTimeMillis();
        difference = endTime - startTime;

        System.out.println("The time taken for searching is: " + difference + " milliseconds");

        //print matrix to check if the result is correct
        //printMatrix();

    }

    //Initialize the matrix[i][j]
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

    private Void sequentialSearch(){
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
        return null;
    }

    //Print the matrix to console
    private void printMatrix(){
        for(int p=0; p<graph.sizeVertices(); p++){
            System.out.println();
            for(int q=0; q<graph.sizeVertices(); q++){
                //System.out.println();
                if(p==q){
                    System.out.print("0   ");
                }else{
                    if(matrix[p][q]<99999){
                        System.out.print(matrix[p][q]+"   ");
                    }else{
                        System.out.print("INF"+ " ");
                    }
                }

            }
        }

    }

    @InitParaTask
    private void parallelSearch(){
        @Future(taskType = TaskInfoType.MULTI, taskCount = 2)
        List<Integer>  list = new ArrayList<Integer>();

        list.add(processSearch());
        System.out.println(list);

    }
    private int processSearch(){
        for(int k=0; k<graph.sizeVertices(); k++) {
            for (int i = 0; i < graph.sizeVertices(); i++) {
                for (int j = 0; j < graph.sizeVertices(); j++) {

                    task(i,j,k);
                }
            }
        }
        return 0;
    }


    private Void task(int i, int j, int k){
        iteration++;
        //System.out.println("Current thread: "+ Thread.currentThread().getId());
            if(matrix[i][k]!= Integer.MAX_VALUE&&
                    matrix[k][j]!= Integer.MAX_VALUE&&
                    matrix[i][k]+matrix[k][j]<matrix[i][j]){
                matrix[i][j]=matrix[i][k]+matrix[k][j];
            }

        return null;
        //System.out.println("iteration: " + iteration);

    }

}
