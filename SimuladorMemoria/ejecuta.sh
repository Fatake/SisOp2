#!/bin/bash
echo "Compilando..."
javac -d class MemoryManagement.java
cp commands memory.conf /class
echo "Ejecutando..."
java -cp class MemoryManagement commands memory.conf
#java -cp class MemoryManagement commands2 memory2.conf
