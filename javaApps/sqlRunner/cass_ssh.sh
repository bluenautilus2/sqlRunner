#!/bin/bash

while getopts S:p:U:P:D:f: option
do
        case "${option}"
        in
                S) IP=${OPTARG};;
                p) PORT=${OPTARG};;
                U) LOGIN=${OPTARG};;
                P) PASSWORD=$OPTARG;;
                D) DATABASE=$OPTARG;;
                f) FILE=$OPTARG;;
        esac
done

#echo "Running script via bash..."

echo "(sshpass -p $PASSWORD ssh $LOGIN@$IP /home/cassandra/bin/cql) < $FILE"
((sshpass -p $PASSWORD ssh $LOGIN@$IP /home/cassandra/bin/cql) < $FILE ) > cassout.txt


#echo "End of bash output"
