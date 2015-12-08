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


echo "((docker exec -i dse47_cassandra_1  dse/bin/cqlsh localhost) < $FILE ) > cassout.txt"
((docker exec -i dse47_cassandra_1  dse/bin/cqlsh localhost) < $FILE ) > cassout.txt

echo "script complete"
