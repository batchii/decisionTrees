#!/bin/bash

shuf --output="$2" "$1" 
 
split -l $(expr $(cat $1 | wc -l) \* 60 / 100) "$1" "$1"
