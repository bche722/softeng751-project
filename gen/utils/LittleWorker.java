

package utils;


class LittleWorker {
    private java.io.FileWriter fileWriter;

    private int numberOfNodes;

    private int branchingFactor;

    private static java.util.Deque<utils.LittleWorker.Node> nodeQueue = new java.util.ArrayDeque<>();

    LittleWorker(int numberOfNodes, int branchingFactor, java.io.FileWriter fileWriter) {
        utils.LittleWorker.this.numberOfNodes = numberOfNodes;
        utils.LittleWorker.this.branchingFactor = branchingFactor;
        utils.LittleWorker.this.fileWriter = fileWriter;
        letMeDoItBro();
    }

    private void letMeDoItBro() {
        for (int i = 0; i < (utils.RandomGraphGenerator.numOfSources); i++) {
            utils.LittleWorker.nodeQueue.add(new utils.LittleWorker.Node(("source" + i), java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString()));
        }
        for (int i = 0; i < ((numberOfNodes) - 1); i++) {
            if ((i + (utils.LittleWorker.nodeQueue.size())) >= (numberOfNodes)) {
                java.util.ArrayList<utils.LittleWorker.Node> lastLeaves = new java.util.ArrayList<>(utils.LittleWorker.nodeQueue);
                if (utils.RandomGraphGenerator.isMultiSourcesAndOrSinks) {
                    lastLeaves.forEach(( leaf) -> {
                        try {
                            fileWriter.append(leaf.toString());
                            java.lang.System.out.println("write7");
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    });
                    for (int l = 0; l < (utils.RandomGraphGenerator.numOfSinks); l++) {
                        utils.LittleWorker.Node sink = new utils.LittleWorker.Node(("sink" + l), java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString());
                        lastLeaves.forEach(( leaf) -> {
                            utils.LittleWorker.Edge edge = new utils.LittleWorker.Edge(((("edge" + (leaf.name)) + "ToSink") + (sink.name)), leaf.name, sink.name, java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString());
                            try {
                                fileWriter.append(edge.toString());
                                java.lang.System.out.println("write8");
                            } catch (java.io.IOException e) {
                                e.printStackTrace();
                            }
                        });
                        try {
                            fileWriter.append(sink.toString());
                            java.lang.System.out.println("write9");
                            fileWriter.flush();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        fileWriter.append("<depth type=\"level\">").append(java.lang.String.valueOf((i + 1))).append("</depth>\n");
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    utils.LittleWorker.Node sink = new utils.LittleWorker.Node("sink", java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString());
                    int id = 0;
                    while (!(utils.LittleWorker.nodeQueue.isEmpty())) {
                        utils.LittleWorker.Node leaf = utils.LittleWorker.nodeQueue.pop();
                        try {
                            fileWriter.append(leaf.toString());
                            java.lang.System.out.println("write1");
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                        utils.LittleWorker.Edge edge = new utils.LittleWorker.Edge((("edge" + id) + "ToSink"), leaf.name, sink.name, java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString());
                        try {
                            fileWriter.append(edge.toString());
                            java.lang.System.out.println("write2");
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                        id++;
                    } 
                    try {
                        fileWriter.append(sink.toString());
                        java.lang.System.out.println("write3");
                        fileWriter.append("<depth type=\"level\">").append(java.lang.String.valueOf((i + 1))).append("</depth>\n");
                        fileWriter.flush();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            java.util.ArrayList<utils.LittleWorker.Node> currentJunctures = new java.util.ArrayList<>();
            java.util.ArrayList<utils.LittleWorker.Node> currentLeaves = new java.util.ArrayList<>();
            while (!(utils.LittleWorker.nodeQueue.isEmpty())) {
                utils.LittleWorker.Node node = utils.LittleWorker.nodeQueue.pop();
                try {
                    fileWriter.append(node.toString());
                    java.lang.System.out.println("write4");
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                currentJunctures.add(node);
            } 
            for (int j = 1; j <= (branchingFactor); j++) {
                utils.LittleWorker.Node childNode = new utils.LittleWorker.Node(((((("node" + i) + "") + j) + "") + (java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 1000000)).toString())), java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString());
                currentLeaves.add(childNode);
                currentJunctures.forEach(( juncture) -> {
                    utils.LittleWorker.Edge edge = new utils.LittleWorker.Edge(((((("edge" + (juncture.name)) + "TO") + (childNode.name)) + "") + (java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 1000000)).toString())), juncture.name, childNode.name, java.lang.Integer.valueOf(java.util.concurrent.ThreadLocalRandom.current().nextInt(0, 100)).toString());
                    try {
                        fileWriter.append(edge.toString());
                        java.lang.System.out.println("write5");
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            utils.LittleWorker.nodeQueue.addAll(currentLeaves);
            try {
                fileWriter.flush();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Node {
        java.lang.String name;

        java.lang.String weight;

        Node(java.lang.String name, java.lang.String weight) {
            utils.LittleWorker.Node.this.name = name;
            utils.LittleWorker.Node.this.weight = weight;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((("<node>\n" + "        <Name name=\"name\">") + (name)) + "</Name>\n") + "        <Weight name=\"weight\" type=\"int\">") + (weight)) + "</Weight>\n") + "</node>\n";
        }
    }

    class Edge {
        java.lang.String id;

        java.lang.String from;

        java.lang.String to;

        java.lang.String weight;

        public Edge(java.lang.String id, java.lang.String from, java.lang.String to, java.lang.String weight) {
            utils.LittleWorker.Edge.this.id = id;
            utils.LittleWorker.Edge.this.from = from;
            utils.LittleWorker.Edge.this.to = to;
            utils.LittleWorker.Edge.this.weight = weight;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((("<edge id=\"" + (id)) + "\" from=\"") + (from)) + "\" to=\"") + (to)) + "\">\n") + "        <Weight name=\"weight\" type=\"int\">") + (weight)) + "</Weight>\n") + "</edge>\n";
        }
    }
}

