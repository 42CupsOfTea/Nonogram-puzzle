#!/bin/bash

#Setting classpath
export CLASSPATH=$CLASSPATH:./libs/hamcrest-all-1.3.jar:./libs/junit-4.13.2.jar:./libs/org.json-1.6-20240205.jar

# Compiling all scripts
javac -cp "./libs/*" -d ./CompiledScripts ./MainProgram/*.java ./Screen/*.java
javac -d "./CompiledScripts" -cp "./libs/*:./CompiledScripts" ./Testing/*.java

# Running all tests
java -cp "./CompiledScripts:./libs/*" org.junit.runner.JUnitCore Testing.PrimaryReqsTesting
java -cp "./CompiledScripts:./libs/*" org.junit.runner.JUnitCore Testing.SecondaryReqsTesting
java -cp "./CompiledScripts:./libs/*" org.junit.runner.JUnitCore Testing.TertiaryReqsTesting