#!/bin/bash

./gradlew assemble 

echo "nc localhost 8800"

java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=4000,suspend=n -javaagent:build/libs/berta.jar=transform=entagged.* -classpath "/usr/share/java/hsqldb.jar:/usr/share/java/entagged-tageditor.jar:/usr/share/java/squareness.jar" entagged.tageditor.TagEditorFrameSplash

