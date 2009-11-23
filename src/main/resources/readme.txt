Notes on using SigTest
-----------------------------------------
Sigtest home page: https://sigtest.dev.java.net/
User Guide: http://java.sun.com/javame/sigtest/docs/sigtest2.1_usersguide.pdf

Generating a signature file
---------------------------
To generate a signature file, use the following command:

java -jar sigtestdev.jar Setup -classpath $JAVA_HOME/jre/lib/rt.jar:validation-api.jar -filename validation-api.sig -package javax.validation
