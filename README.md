# Softeng751_Project_Group_11
Project 1:  Parallelisation of graph algorithms in Java

# Group members
 - Mason Shi - GitHub : tshi206 - UPI : tshi206 - ID : 313368828 - bidirectional BFS
 - Baiwei Chen - GitHub : bche722 - UPI : bche722 - ID : ?????????? - iterative deepening DFS
 - Tao Ge - GitHub : taotao - UPI : tge676 - ID : ?????????? - Floyd-Warshall

# JDK prerequisite
at least 1.8

# Dependency
This project is relying on the support from @PT (https://github.com/ParallelAndReconfigurableComputing/ParallelTask/tree/ParaTask%40Spoon/WorkingDirectory/AnnotationProcessor). @PT's library and dependency has been included in the project's local maven repository. If the project fails to compile for the first time after the pulling or cloning, see the troubleshooting instructions down below.

# How to build
The build process is handled by ANT. To build the jar, run the following build target:
```
ant -d compressTranspiledJar
```
or alternatively:
```
ant -d clean compressTranspiledJar
```
If using IntelliJ:
```
just double click the target name or right click it and select 'run target'
```
If using Eclipse:
```
should be the same
```
The built file:
```
The resulting jar is called Algo.jar under the 'gen/Algo' folder
```

# How to run
Go to the 'gen' folder or any folder containing the built file:
```
java -jar Algo.jar -a <algo_name> -f <input_file_name>.xml [-p]

-h,-help : user manual
-a       : [bbfs | iddfs | fw], choose an algorithm to run. bbfs - bidirectional BFS; iddfs - iterative deepening DFS; fw - Floyd Warshall.
-f       : <path to the input file/input_file_name>.xml, specify the relative path plus the full name of the input file, the file must be in xml format, the default path of generated graph files is right in the root, see RandomGraphGenerator.
-p       : parallelisation flag, place it at the end of the command if intending to run in parallel mode, don't put it down otherwise.
```

# Input files
All input files are generated using RandomGraphGenerator except graph1.xml, graph2.xml, graph3.xml, these three are manually crafted.

# RandomGraphGenerator
RandomGraphGenerator is a utility class used for generating input files for testing purposes.
To generate a input graph, run the java file RandomGraphGenerator.java under 'utils' folder with the following command line parameters:
```
-n      ：minimum number of nodes, the actual number of nodes and edges is calculated based on this value hence will end up being greater than n by a certain number of percentage
-b      ：branching factor
-sources：number of source nodes, optional, default value 1 if not specified
-sinks  ：same as above, optional, default value 1 if not specified
```
Examples:
```
-n 20 -b 5 -sources 3 -sinks 2           (name of the output file： graphXXXXXXXXmulti--bFactor-5.xml, where XXX...X is the randomly generated ID)
-n 200 -b 10                             (name of the output file： graphXXXXXXXX--bFactor-10.xml, where XXX...X is the randomly generated ID)
```
Destination:
```
All files generated will be in the project root by default
```
Information on the generated graph:
```
All graphs are weighted DAGs, in XML format.
branching factor  : see file name
depth(level)      : see the second line from the bottom, tag name ：<depth type="level">...</depth>
#node             : use those graph lib methods
#edge             : use those graph lib methods
#sources          : use those graph lib methods, or simply count the tags because they will all be in the first few lines if the graph is small and still human-readable
#sinks            : use those graph lib methods, or simply count the tags because they will all be in the last few lines if the graph is small and still human-readable
```

# Troubleshooting - if this project does not compile after pulling/cloning
## missing dependency to @PT
- make sure all the libs files under {project_root}/libs/ are present
- add local jar(s) to the project's maven repo:
```xml
<!-- local maven repo for @PT 1.5.2 -->
<repositories>
    <repository>
        <id>annotation-based-PT-1.5.2</id>
        <url>file:///${project.basedir}/libs</url>
    </repository>
</repositories>
```
- deploy them to maven's local repo: (run the following three in your cmd/powershell/bash/whichever your preference)
```
mvn deploy:deploy-file -DgroupId=PARC-UoA -DartifactId=annotation-based-PT -Dversion=1.5.2 -Durl=file:./libs/ -DrepositoryId=annotation-based-PT-1.5.2 -DupdateReleaseInfo=true -Dfile=libs/@PT-1.5.2.jar
mvn deploy:deploy-file -DgroupId=PARC-UoA -DartifactId=annotation-based-PT-docs -Dversion=1.5.2 -Durl=file:./libs/ -DrepositoryId=annotation-based-PT-1.5.2 -DupdateReleaseInfo=true -Dfile=libs/@PT-1.5.2-docs.jar
mvn deploy:deploy-file -DgroupId=PARC-UoA -DartifactId=annotation-based-PT-src -Dversion=1.5.2 -Durl=file:./libs/ -DrepositoryId=annotation-based-PT-1.5.2 -DupdateReleaseInfo=true -Dfile=libs/@PT-1.5.2-src.jar
```
- add the dependency in the pom.xml:
```xml
<dependency>
  <groupId>PARC-UoA</groupId>
  <artifactId>annotation-based-PT</artifactId>
  <version>1.5.2</version>
</dependency>
```
- install the @PT jar:
```
mvn install:install-file -Dfile=libs/@PT-1.5.2.jar -DgroupId=PARC-UoA -DartifactId=annotation-based-PT -Dversion=1.5.2 -Dpackaging=jar
```
- install the @PT docs (optional):
```
mvn install:install-file -Dfile=libs/@PT-1.5.2-docs.jar -DgroupId=PARC-UoA -DartifactId=annotation-based-PT-docs -Dversion=1.5.2 -Dpackaging=jar
```
- install the @PT src (optional):
```
mvn install:install-file -Dfile=libs/@PT-1.5.2-src.jar -DgroupId=PARC-UoA -DartifactId=annotation-based-PT-src -Dversion=1.5.2 -Dpackaging=jar
```

## missing dependencies to PARCutils
same as adding dependencies for @PT
make sure the following entries present in the pom.xml:
```
<repositories>
    ...
    <repository>
        <id>PARCutils-1.2.4</id>
        <url>file:///${project.basedir}/libs</url>
    </repository>
    ...
</repositories>
        
        ...
        
<dependencies>
    ...
    <dependency>
            <groupId>PARC-UoA</groupId>
            <artifactId>PARCutils</artifactId>
            <version>1.2.4</version>
        </dependency>
        <dependency>
            <groupId>PARC-UoA</groupId>
            <artifactId>PARCutils-javadoc</artifactId>
            <version>1.2.4</version>
        </dependency>
        <dependency>
            <groupId>PARC-UoA</groupId>
            <artifactId>PARCutils-src</artifactId>
            <version>1.2.4</version>
        </dependency>
    ...
</dependencies>
        
```
then run the following in your cmd:
```
mvn deploy:deploy-file -DgroupId=PARC-UoA -DartifactId=PARCutils -Dversion=1.2.4 -Durl=file:./libs/ -DrepositoryId=PARCutils-1.2.4 -DupdateReleaseInfo=true -Dfile=libs/PARCutils-1.2.4.jar
mvn deploy:deploy-file -DgroupId=PARC-UoA -DartifactId=PARCutils-javadoc -Dversion=1.2.4 -Durl=file:./libs/ -DrepositoryId=PARCutils-1.2.4 -DupdateReleaseInfo=true -Dfile=libs/PARCutils-1.2.4-javadoc.jar
mvn deploy:deploy-file -DgroupId=PARC-UoA -DartifactId=PARCutils-src -Dversion=1.2.4 -Durl=file:./libs/ -DrepositoryId=PARCutils-1.2.4 -DupdateReleaseInfo=true -Dfile=libs/PARCutils-1.2.4-src.jar
```
finally:
```
mvn install:install-file -Dfile=libs/PARCutils-1.2.4.jar -DgroupId=PARC-UoA -DartifactId=PARCutils -Dversion=1.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=libs/PARCutils-1.2.4-javadoc.jar -DgroupId=PARC-UoA -DartifactId=PARCutils-javadoc -Dversion=1.2.4 -Dpackaging=jar
mvn install:install-file -Dfile=libs/PARCutils-1.2.4-src.jar -DgroupId=PARC-UoA -DartifactId=PARCutils-src -Dversion=1.2.4 -Dpackaging=jar
```

## missing javadocs
- add 3rd party javadocs from jar in IDEA: https://stackoverflow.com/questions/91307/attaching-additional-javadoc-in-intellij-idea
- add 3rd party javadocs from jar in Eclipse: https://stackoverflow.com/questions/9289617/eclipse-how-to-link-a-jar-containing-javadocs-source-with-its-binary
