#!/bin/bash

while getopts H:U:C:F:T: option
do
        case "${option}"
        in
                H) HOST=${OPTARG};;
                U) LOGIN=${OPTARG};;
                C) CONTAINER=${OPTARG};;
                F) FILE=${OPTARG};;
                T) TYPE=${OPTARG};;
        esac
done



if [[ "$TYPE" == "DOCKER_REMOTE" ]]; then
  echo "Running on remote host"
   (ssh  $LOGIN@$HOST "(docker exec -i $CONTAINER dse/bin/cqlsh localhost)" < $FILE) > cassout.txt
else
  echo "Running on localhost"
  ((docker exec -i $CONTAINER  dse/bin/cqlsh localhost) < $FILE ) > cassout.txt
fi

echo "script complete"