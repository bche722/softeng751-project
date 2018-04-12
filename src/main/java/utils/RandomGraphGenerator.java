package utils;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ThreadLocalRandom;

public class RandomGraphGenerator {

    private static String userDir = System.getProperty("user.dir");

    private static int numberOfNodes;
    private static int branchingFactor;

    public static void main(String[] args){

        // create Options object
        Options options = new Options();

        Option nflag = new Option( "n", true, "number of nodes" );
        Option fflag = new Option( "b", true, "branching factor" );

        options.addOption(nflag);
        options.addOption(fflag);

        CommandLineParser parser = new DefaultParser();

        try{

            CommandLine cmd = parser.parse(options, args);

            String nnodes = cmd.getOptionValue("n");
            if (nnodes == null) {
                throw new ParseException("oops. something wrong here. caused by : numberOfNodes");
            }
            numberOfNodes = Integer.parseInt(nnodes);

            String bfactor = cmd.getOptionValue("b");
            if (bfactor == null) {
                throw new ParseException("oops. something wrong here. caused by : branchingFactor");
            }
            branchingFactor = Integer.parseInt(bfactor);

            // start generating the output xml in the project root dir
            generate();

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private static void generate(){

        int graphID = ThreadLocalRandom.current().nextInt(0, 10000);

        String filename = "graph" + graphID + ".xml";

        File outputXML = new File(userDir + File.separator + filename);
        if (!outputXML.exists()) {
            try {
                if (!outputXML.createNewFile()){
                    System.out.println("the named file already exists, run me again later. :(");
                    System.exit(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(outputXML);

            //write the header
            fileWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");

            fileWriter.append("<graph id=\"graph").append(String.valueOf(graphID)).append("\" edgemode=\"directed\">\n");

            //call the worker
            new LittleWorker(numberOfNodes, branchingFactor, fileWriter);

            //close tag up
            fileWriter.append("</graph>");

            //close the output stream
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class LittleWorker {

    private FileWriter fileWriter;

    private int numberOfNodes;
    private int branchingFactor;

    private static Deque<Node> nodeQueue = new ArrayDeque<>();

    LittleWorker(int numberOfNodes, int branchingFactor, FileWriter fileWriter){
        this.numberOfNodes = numberOfNodes;
        this.branchingFactor = branchingFactor;
        this.fileWriter = fileWriter;

        //get the job done
        letMeDoItBro();
    }

    private void letMeDoItBro(){

        nodeQueue.add(new Node("source",
                Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 100)).toString()));

        for (int i = 0; i < numberOfNodes - 1; i++){

            // add up number of junctures so far and the number of presenting leaves
            if (i + nodeQueue.size() >= numberOfNodes) {

                Node sink = new Node("sink",
                        Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 100)).toString());

                int id = 0;

                while (!nodeQueue.isEmpty()) {

                    Node leaf = nodeQueue.pop();

                    try {
                        fileWriter.append(leaf.toString());
                        System.out.println("write1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Edge edge = new Edge("edge" + id + "ToSink", leaf.name, sink.name,
                            Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 100)).toString());

                    try {
                        fileWriter.append(edge.toString());
                        System.out.println("write2");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    id++;

                }

                try {
                    fileWriter.append(sink.toString());
                    System.out.println("write3");
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }

            ArrayList<Node> currentJunctures = new ArrayList<>();
            ArrayList<Node> currentLeaves = new ArrayList<>();

            while (!nodeQueue.isEmpty()) {

                Node node = nodeQueue.pop();

                try {
                    fileWriter.append(node.toString());
                    System.out.println("write4");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                currentJunctures.add(node);

            }


            for (int j = 1; j <= branchingFactor; j++){

                Node childNode = new Node("node" + i + "" + j + "" +
                        Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)).toString(),
                        Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 100)).toString());

                currentLeaves.add(childNode);

                currentJunctures.forEach(juncture -> {

                    Edge edge = new Edge("edge" + juncture.name + "TO" + childNode.name + "" +
                            Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 1000000)).toString(),
                            juncture.name, childNode.name,
                            Integer.valueOf(ThreadLocalRandom.current().nextInt(0, 100)).toString());

                    try {
                        fileWriter.append(edge.toString());
                        System.out.println("write5");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

            }


            nodeQueue.addAll(currentLeaves);

            try {
                fileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    class Node{

        String name;
        String weight;

        Node(String name, String weight){
            this.name = name;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "<node>\n" +
                    "        <Name name=\"name\">" + name + "</Name>\n" +
                    "        <Weight name=\"weight\" type=\"int\">" + weight + "</Weight>\n" +
                    "</node>\n";
        }
    }

    class Edge{

        String id;
        String from;
        String to;
        String weight;

        public Edge(String id, String from, String to, String weight) {
            this.id = id;
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "<edge id=\""+id+"\" from=\""+from+"\" to=\""+to+"\">\n" +
                    "        <Weight name=\"weight\" type=\"int\">"+weight+"</Weight>\n" +
                    "</edge>\n";
        }

    }

}
