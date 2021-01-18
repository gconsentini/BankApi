#!/bin/bash

java ${JAVA_OPTS} \
 "-Dspring.profiles.active=docker" \
 -jar "BankApi-0.0.1-SNAPSHOT.jar"