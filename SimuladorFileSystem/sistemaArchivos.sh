#!/bin/bash

echo "Ejercicio 1"
java -cp class mkfs filesys.dat 64 8 > creacionBloques.txt

java -cp class dump filesys.dat > dump.txt
java -cp class mkdir /home
java -cp class dump filesys.dat > dump2.txt
java -cp class mkdir /home/user
java -cp class dump filesys.dat > dump3.txt
java -cp class mkdir /bin
java -cp class dump filesys.dat > dump4.txt
java -cp class mkdir /etc
java -cp class dump filesys.dat > dump5.txt
java -cp class mkdir /mnt
java -cp class dump filesys.dat > dump6.txt
java -cp class ls / > ls.txt
java -cp class ls /home >ls2.txt
