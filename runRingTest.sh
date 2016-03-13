#!/bin/sh



libDir="lib/rabbitmq-java-client-bin-3.6.1"

java -cp .:bin/:$libDir/commons-io-1.2.jar:$libDir/commons-cli-1.1.jar:$libDir/rabbitmq-client.jar ringTopology.Test 5 127.0.0.1
