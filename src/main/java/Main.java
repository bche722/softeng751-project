import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args){

        for (String s : args) {System.out.println(s);}

        // create Options object
        Options options = new Options();

        Option help1 = new Option( "h", "user manual" );
        Option help2 = new Option( "help", "user manual" );
        Option algo_choice = new Option("a", true, "choose the algorithm to be executed");
        Option file_choice = new Option("f", true, "choose the graph to be processed");


        options.addOption(help1);
        options.addOption(help2);
        options.addOption(algo_choice);
        options.addOption(file_choice);

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
                    switch (algo_name){
                        case "bbfs":
                            // todo - link to bidirectional BFS modules
                            // todo - flag 'f' handler, see the todo method call in the bottom
                            System.out.println("BBFS dummy.");
                            break;
                        case "iddfs":
                            // todo - link to iterative deepening DFS modules
                            // todo - flag 'f' handler, see the todo method call in the bottom
                            System.out.println("IDDFS dummy.");
                            break;
                        case "fw":
                            // todo - link to Floyd-Warshall modules
                            // todo - flag 'f' handler, see the todo method call in the bottom
                            System.out.println("FW dummy.");
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

    // todo - method to check if file arg is provided and if the file indeed exists

}
