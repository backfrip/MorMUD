/usr/bin/bash
mkdir bin
javac -d bin -sourcepath src src/main/Main.java
java -cp bin main.Main
