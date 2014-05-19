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

echo "Starting tsql"

(tsql -S $IP -p $PORT -U $LOGIN -P $PASSWORD -D $DATABASE < $FILE) > tsqlout.txt

echo "End of tsql output"
