#!/bin/bash

export SOURCES=src
export CLASSES=classes
export CLASSPATH=$(find lib -name "*.jar" | tr '\n' ':')

# Compilation des fichiers .java
javac -cp ${CLASSPATH} -sourcepath ${SOURCES} -d ${CLASSES} $(find $SOURCES -name "*.java")

