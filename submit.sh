#!/bin/bash

submit_script="/Users/luoli/dev/git/luoli/spark/bin/spark-submit"

clz=$1
shift

bash $submit_script \
            --master local \
            --class $clz \
            /Users/luoli/dev/practice/spark_project/target/scala-2.11/spark_project_2.11-1.0.jar \
            $@
