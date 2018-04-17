# Softeng751_Project_Group_11
Project 1:  Parallelisation of graph algorithms in Java

# Group members
 - Mason Shi - GitHub : tshi206 - UPI : tshi206 - ID : 313368828
 - Vincent Chen - GitHub : bche722 - UPI : bche722 - ID : ??????????
 - Joseph Ge - GitHub : taotao - UPI : tge676 - ID : ??????????

# JDK prerequisite
at least 1.8

# Dependency
This project is relying on the support from @PT (https://github.com/ParallelAndReconfigurableComputing/ParallelTask/tree/ParaTask%40Spoon/WorkingDirectory/AnnotationProcessor). @PT's library and dependency has been included in the project's local maven repository. If the project fails to compile for the first time after the pulling or cloning, see the troubleshooting instructions down below.

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
