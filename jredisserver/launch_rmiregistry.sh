#!/bin/bash

DIR=./target/classes;

if [ ! -d ${DIR} ]; then 
	echo "${DIR} does not exist. Please run 'mvn install' to generate classes.";
else
	export CLASSPATH=${DIR};
	echo "rmiregistry is now running. You can now launch the server. CTRL-C to stop rmiregistry.";
	rmiregistry;
fi 
