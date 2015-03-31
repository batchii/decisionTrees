#!/bin/bash

split -l $(expr $(cat $1 | wc -l) \* 75 / 100) "$1" "$1" 