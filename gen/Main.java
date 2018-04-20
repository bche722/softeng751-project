

// default package (CtPackage.TOP_LEVEL_PACKAGE_NAME in Spoon= unnamed package)



public class Main {
    private static java.lang.String userDir = java.lang.System.getProperty("user.dir");

    public static void main(java.lang.String[] args) {
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        org.apache.commons.cli.Option help1 = new org.apache.commons.cli.Option("h", "user manual");
        org.apache.commons.cli.Option help2 = new org.apache.commons.cli.Option("help", "user manual");
        org.apache.commons.cli.Option algo_choice = new org.apache.commons.cli.Option("a", true, "choose the algorithm to be executed");
        org.apache.commons.cli.Option file_choice = new org.apache.commons.cli.Option("f", true, "choose the graph to be processed");
        org.apache.commons.cli.Option isParallelized = new org.apache.commons.cli.Option("p", false, "whether the execution is parallelized");
        options.addOption(help1);
        options.addOption(help2);
        options.addOption(algo_choice);
        options.addOption(file_choice);
        options.addOption(isParallelized);
        org.apache.commons.cli.CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
        try {
            org.apache.commons.cli.CommandLine cmd = parser.parse(options, args);
            boolean[] flags = new boolean[]{ true , true };
            if ((cmd.hasOption("h")) || (cmd.hasOption("help"))) {
                java.lang.System.out.println(("After -a flag, put: <bbfs> - bidirectional BFS, <iddfs> - iterative deepening DFS," + " <fw> - Floyd-Warshall. Example: '-a fw' will set the algorithm to Floyd-Warshall."));
                java.lang.System.out.println(("Full syntax: [ -h | -help ] -a [ bbfs | iddfs | fw ] -f " + "<filename_in_the_current_dir>"));
                java.lang.System.out.println("Precedence of flags is in the same order as above.");
            }else {
                flags[0] = false;
            }
            if (cmd.hasOption("a")) {
                java.lang.String algo_name = cmd.getOptionValue("a");
                if (algo_name != null) {
                    java.lang.System.out.println(("thread id : " + (java.lang.Thread.currentThread().getId())));
                    boolean isParallel;
                    switch (algo_name) {
                        case "bbfs" :
                            isParallel = cmd.hasOption("p");
                            new java.lang.Thread(new Main.Dispatcher<>(new bbfs.BidirectionalBreadthFirstSearchOld<>(Main.fFlagHandler(cmd), isParallel))).start();
                            break;
                        case "iddfs" :
                            isParallel = cmd.hasOption("p");
                            new java.lang.Thread(new Main.Dispatcher<>(new iddfs.IterativeDeepeningDepthFirstSearch<>(Main.fFlagHandler(cmd), isParallel))).start();
                            break;
                        case "fw" :
                            isParallel = cmd.hasOption("p");
                            new java.lang.Thread(new Main.Dispatcher<>(new fw.FloydWarshall<>(Main.fFlagHandler(cmd), isParallel))).start();
                            break;
                        default :
                            throw new org.apache.commons.cli.ParseException(("Execution failed due to unrecognizable argument value for " + "'-a'. See usage via '-h' or '-help'"));
                    }
                }else {
                    throw new org.apache.commons.cli.ParseException(("Execution failed due to missing argument value for " + "'-a'. See usage via '-h' or '-help'"));
                }
            }else {
                flags[1] = false;
            }
            if ((!(flags[0])) && (!(flags[1]))) {
                throw new org.apache.commons.cli.ParseException(("Execution failed due to the lack of arguments. See usage via '-h' or " + "'-help'"));
            }
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
        }
    }

    private static graph.BasicDirectedGraph<graph.Vertex, graph.DirectedEdge<graph.Vertex>> fFlagHandler(org.apache.commons.cli.CommandLine cmd) throws org.apache.commons.cli.ParseException {
        if ((cmd.getOptionValue("f")) == null) {
            throw new org.apache.commons.cli.ParseException(("Execution failed due to missing '-f' flag and/or missing argument value for " + "'-f'. See usage via '-h' or '-help'"));
        }
        java.lang.String filename = cmd.getOptionValue("f");
        java.lang.System.out.println(("\n" + filename));
        java.lang.System.out.println(Main.userDir);
        java.io.File xml = new java.io.File((((Main.userDir) + (java.io.File.separator)) + filename));
        if (!(xml.exists())) {
            throw new org.apache.commons.cli.ParseException(("Execution failed due to nonexistence of the supplied file. Make " + "sure the file exists under the project root directory. See example file: graph1.xml"));
        }
        java.lang.System.out.println(xml);
        graph.GXLFileReader reader = new graph.GXLFileReader(xml);
        if ((reader.getName()) == null) {
            throw new org.apache.commons.cli.ParseException(("Execution failed due to invalid type/format of the supplied graph. Make " + "sure the supplied file is a valid xml and has a root node called 'graph'. See example file: graph1.xml"));
        }
        java.lang.System.out.println(reader.getName());
        java.lang.System.out.println(reader.getType());
        graph.BasicDirectedGraph<graph.Vertex, graph.DirectedEdge<graph.Vertex>> basicDirectedGraph = ((graph.BasicDirectedGraph<graph.Vertex, graph.DirectedEdge<graph.Vertex>>) (graph.BasicDirectedGraph.doLoad(reader)));
        java.lang.System.out.println(basicDirectedGraph.verticesSet().size());
        java.lang.System.out.println(basicDirectedGraph.edgesSet().size());
        return basicDirectedGraph;
    }

    static class Dispatcher<V extends graph.Vertex, E extends graph.DirectedEdge<V>> implements java.lang.Runnable {
        private interfaces.Algorithm algorithm;

        Dispatcher(interfaces.Algorithm<V, E> algorithm) {
            Main.Dispatcher.this.algorithm = algorithm;
        }

        @java.lang.Override
        public void run() {
            algorithm.doTheJob();
        }
    }
}

