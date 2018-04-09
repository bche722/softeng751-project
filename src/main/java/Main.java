import bbfs.BidirectionalBreadthFirstSearch;
import fw.FloydWarshall;
import graph.BasicDirectedGraph;
import graph.DirectedEdge;
import graph.GXLFileReader;
import graph.Vertex;
import iddfs.IterativeDeepeningDepthFirstSearch;
import interfaces.Algorithm;
import org.apache.commons.cli.*;

import java.io.File;

public class Main {

    private static String userDir = System.getProperty("user.dir");

    public static void main(String[] args){

        for (String s : args) {System.out.print(s+" ");}

        // create Options object
        Options options = new Options();

        Option help1 = new Option( "h", "user manual" );
        Option help2 = new Option( "help", "user manual" );
        Option algo_choice = new Option("a", true, "choose the algorithm to be executed");
        Option file_choice = new Option("f", true, "choose the graph to be processed");
        Option isParallelized = new Option("p", false, "whether the execution is parallelized");


        options.addOption(help1);
        options.addOption(help2);
        options.addOption(algo_choice);
        options.addOption(file_choice);
        options.addOption(isParallelized);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            boolean[] flags = {true, true};

            // detect flag 'h'
            if (cmd.hasOption("h") || cmd.hasOption("help")){
                System.out.println("After -a flag, put: <bbfs> - bidirectional BFS, <iddfs> - iterative deepening DFS," +
                        " <fw> - Floyd-Warshall. Example: '-a fw' will set the algorithm to Floyd-Warshall.");
                System.out.println("Full syntax: [ -h | -help ] -a [ bbfs | iddfs | fw ] -f " +
                        "<filename_in_the_current_dir>");
                System.out.println("Precedence of flags is in the same order as above.");
            } else {
                flags[0] = false;
            }

            // detect flag 'a'
            if (cmd.hasOption("a")) {
                // get a option value
                String algo_name = cmd.getOptionValue("a");

                if (algo_name != null) {
                    boolean isParallel;
                    switch (algo_name){
                        case "bbfs":
                            isParallel = cmd.hasOption("p");
                            new Thread(new Dispatcher(new BidirectionalBreadthFirstSearch(fFlagHandler(cmd), isParallel)))
                                    .start();
                            break;
                        case "iddfs":
                            isParallel = cmd.hasOption("p");
                            new Thread(new Dispatcher(new IterativeDeepeningDepthFirstSearch(fFlagHandler(cmd), isParallel)))
                                    .start();
                            break;
                        case "fw":
                            isParallel = cmd.hasOption("p");
                            new Thread(new Dispatcher(new FloydWarshall(fFlagHandler(cmd), isParallel))).start();
                            break;
                        default:
                            throw new ParseException("Execution failed due to unrecognizable argument value for " +
                                    "'-a'. See usage via '-h' or '-help'");
                    }
                }
                else {
                    throw new ParseException("Execution failed due to missing argument value for " +
                            "'-a'. See usage via '-h' or '-help'");
                }
            } else {
                flags[1] = false;
            }

            // check if none of the flags was given
            if (!flags[0] && !flags[1]) {
                throw new ParseException("Execution failed due to the lack of arguments. See usage via '-h' or " +
                        "'-help'");
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private static BasicDirectedGraph fFlagHandler (CommandLine cmd) throws ParseException{
        if (cmd.getOptionValue("f") == null){
            throw new ParseException("Execution failed due to missing '-f' flag and/or missing argument value for " +
                    "'-f'. See usage via '-h' or '-help'");
        }
        String filename = cmd.getOptionValue("f");

        System.out.println("\n" + filename);
        System.out.println(userDir);


        File xml = new File(userDir + File.separator + filename);
        if (!xml.exists()){
            throw new ParseException("Execution failed due to nonexistence of the supplied file. Make " +
                    "sure the file exists under the project root directory. See example file: graph1.xml");
        }

        System.out.println(xml);

        GXLFileReader reader = new GXLFileReader(xml);
        if (reader.getName() == null){
            throw new ParseException("Execution failed due to invalid type/format of the supplied graph. Make " +
                    "sure the supplied file is a valid xml and has a root node called 'graph'. See example file: graph1.xml");
        }

        System.out.println(reader.getName());
        System.out.println(reader.getType());
        reader.getVertices().forEachRemaining(node -> System.out.println(node.getNodeName()+" : "+node.getTextContent()));
        reader.getEdges().forEachRemaining(edge ->
            System.out.println(edge.getNodeName() + " : " + edge.getFirstChild().getNodeName() + " "
                    + edge.getFirstChild().getTextContent() + " " + edge.getLastChild().getNodeName() + " "
                    + edge.getLastChild().getTextContent()));

        BasicDirectedGraph basicDirectedGraph = (BasicDirectedGraph) BasicDirectedGraph.doLoad(reader);

        System.out.println(basicDirectedGraph.verticesSet().size());
        System.out.println(basicDirectedGraph.edgesSet().size());

        return basicDirectedGraph;
    }

    static class Dispatcher<V extends Vertex, E extends DirectedEdge<V>> implements Runnable{

        private Algorithm algorithm;

        public Dispatcher(Algorithm<V, E> algorithm) {
            this.algorithm = algorithm;
        }

        @Override
        public void run() {
            algorithm.doTheJob();
        }
    }
}
