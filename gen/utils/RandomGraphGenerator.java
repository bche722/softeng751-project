

package utils;


public class RandomGraphGenerator {
    static boolean isMultiSourcesAndOrSinks = false;

    static int numOfSources = 1;

    static int numOfSinks = 1;

    private static java.lang.String userDir = java.lang.System.getProperty("user.dir");

    private static int numberOfNodes;

    private static int branchingFactor;

    public static void main(java.lang.String[] args) {
        org.apache.commons.cli.Options options = new org.apache.commons.cli.Options();
        org.apache.commons.cli.Option nflag = new org.apache.commons.cli.Option("n", true, "number of nodes");
        org.apache.commons.cli.Option fflag = new org.apache.commons.cli.Option("b", true, "branching factor");
        org.apache.commons.cli.Option nsources = new org.apache.commons.cli.Option("sources", true, "number of sources (default 1)");
        org.apache.commons.cli.Option nsinks = new org.apache.commons.cli.Option("sinks", true, "number of sinks (default 1)");
        options.addOption(nflag);
        options.addOption(fflag);
        options.addOption(nsources);
        options.addOption(nsinks);
        org.apache.commons.cli.CommandLineParser parser = new org.apache.commons.cli.DefaultParser();
        try {
            org.apache.commons.cli.CommandLine cmd = parser.parse(options, args);
            java.lang.String nnodes = cmd.getOptionValue("n");
            if (nnodes == null) {
                throw new org.apache.commons.cli.ParseException("oops. something wrong here. caused by : numberOfNodes");
            }
            utils.RandomGraphGenerator.numberOfNodes = java.lang.Integer.parseInt(nnodes);
            java.lang.String bfactor = cmd.getOptionValue("b");
            if (bfactor == null) {
                throw new org.apache.commons.cli.ParseException("oops. something wrong here. caused by : branchingFactor");
            }
            utils.RandomGraphGenerator.branchingFactor = java.lang.Integer.parseInt(bfactor);
            java.lang.String numberOfSources = cmd.getOptionValue("sources");
            if (numberOfSources != null) {
                utils.RandomGraphGenerator.isMultiSourcesAndOrSinks = true;
                utils.RandomGraphGenerator.numOfSources = java.lang.Integer.parseInt(numberOfSources);
            }
            java.lang.String numberOfSinks = cmd.getOptionValue("sinks");
            if (numberOfSinks != null) {
                utils.RandomGraphGenerator.isMultiSourcesAndOrSinks = true;
                utils.RandomGraphGenerator.numOfSinks = java.lang.Integer.parseInt(numberOfSinks);
            }
            utils.RandomGraphGenerator.generate();
        } catch (org.apache.commons.cli.ParseException e) {
            e.printStackTrace();
        }
    }

    private static void generate() {
        int graphID = java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 10000);
        java.lang.String filename = ((("graph" + graphID) + "--bFactor-") + (utils.RandomGraphGenerator.branchingFactor)) + ".xml";
        if (utils.RandomGraphGenerator.isMultiSourcesAndOrSinks) {
            filename = (((("graph" + graphID) + "multi") + "--bFactor-") + (utils.RandomGraphGenerator.branchingFactor)) + ".xml";
        }
        java.io.File outputXML = new java.io.File((((utils.RandomGraphGenerator.userDir) + (java.io.File.separator)) + filename));
        if (!(outputXML.exists())) {
            try {
                if (!(outputXML.createNewFile())) {
                    java.lang.System.out.println("the named file already exists, run me again later. :(");
                    java.lang.System.exit(0);
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        try {
            java.io.FileWriter fileWriter = new java.io.FileWriter(outputXML);
            fileWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
            fileWriter.append("<graph id=\"graph").append(java.lang.String.valueOf(graphID)).append("\" edgemode=\"directed\">\n");
            new utils.LittleWorker(utils.RandomGraphGenerator.numberOfNodes, utils.RandomGraphGenerator.branchingFactor, fileWriter);
            fileWriter.append("</graph>");
            fileWriter.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}

