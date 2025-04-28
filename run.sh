#!/bin/bash

#Setting classpath
export CLASSPATH=$CLASSPATH:./libs/hamcrest-all-1.3.jar:./libs/junit-4.13.2.jar:./libs/org.json-1.6-20240205.jar

#Compiling the solution
javac -cp "./libs/*" -d ./CompiledScripts ./MainProgram/*.java ./Screen/*.java

#Running the solution
java -cp "./CompiledScripts:./libs/*" MainProgram.Main