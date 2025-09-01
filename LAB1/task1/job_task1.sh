#!/bin/bash -l
#SBATCH -J task1c
#SBATCH -A edu25.dd2443
#SBATCH -t 00:15:00

#SBATCH -e task1c_e.txt
#SBATCH -o task1c_o.txt

#SBATCH -p shared
#SBATCH -N 1
#SBATCH -n 1
#SBATCH -c 64

ml PDC
ml java

javac MainC.java
srun java -cp . MainC