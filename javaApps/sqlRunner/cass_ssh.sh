#!/bin/bash

while getopts H:U:C:F: option
do
        case "${option}"
        in
                H) HOST=${OPTARG};;
                U) LOGIN=${OPTARG};;
                C) CONTAINER=${OPTARG};;
                F) FILE=${OPTARG};;
        esac
done


echo "script complete"

if [[ -n "$HOST" ]]; then
   echo "(ssh -t $LOGIN@$HOST "(docker exec -i $CONTAINER dse/bin/cqlsh localhost)" < $FILE) > cassout.txt"
   (ssh -t $LOGIN@$HOST "(docker exec -i $CONTAINER dse/bin/cqlsh localhost)" < $FILE) > cassout.txt
else
  echo "((docker exec -i $CONTAINER  dse/bin/cqlsh localhost) < $FILE ) > cassout.txt"
  ((docker exec -i $CONTAINER  dse/bin/cqlsh localhost) < $FILE ) > cassout.txt
fi

