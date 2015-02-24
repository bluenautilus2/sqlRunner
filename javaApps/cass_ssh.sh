#!/bin/bash

while getopts S:p:U:P:D:f:c: option
do
        case "${option}"
        in
                S) IP=${OPTARG};;
                p) PORT=${OPTARG};;
                U) LOGIN=${OPTARG};;
                P) PASSWORD=${OPTARG};;
                D) DATABASE=${OPTARG};;
                f) FILE=${OPTARG};;
                c) CERT=${OPTARG};;
        esac
done

if [[ -n "$CERT" ]]; then
   echo "((ssh -i $CERT $LOGIN@$IP /home/cassandra/bin/cqlsh-localhost) < $FILE ) > cassout.txt"
   ((ssh -i $CERT $LOGIN@$IP /home/cassandra/bin/cqlsh-localhost) < $FILE ) > cassout.txt
else
  echo "((sshpass -p $PASSWORD ssh $LOGIN@$IP /home/cassandra/bin/cql) < $FILE ) > cassout.txt"
  ((sshpass -p $PASSWORD ssh $LOGIN@$IP /home/cassandra/bin/cql) < $FILE ) > cassout.txt
fi

echo "script complete"
