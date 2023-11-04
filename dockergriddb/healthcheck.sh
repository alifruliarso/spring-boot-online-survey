#!/bin/bash

nodeStatus=$(gs_stat -u admin/admin | jq --raw-output '.cluster.nodeStatus')

if [[ $nodeStatus == ACTIVE ]]
then
  exit 0
else
  exit 1
fi