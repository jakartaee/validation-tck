#!/usr/bin/env bash

# Location where the Validation TCK dist has been extracted
TCK_DIST=/tmp/BV/validation-tck-dist-${tck.version}

# Parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file \
-Dfile=${TCK_DIST}/src/pom.xml -DgroupId=jakarta.validation \
-DartifactId=validation-tck-parent -Dversion=${tck.version} -Dpackaging=pom

# Install the tests jar
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file \
-Dfile=${TCK_DIST}/artifacts/validation-tck-tests-${tck.version}.jar -DgroupId=jakarta.validation \
-DartifactId=validation-tck-tests -Dversion=${tck.version} -Dpackaging=jar

# Install the test-suite.xml
mvn org.apache.maven.plugins:maven-install-plugin:3.1.1:install-file \
-Dfile=${TCK_DIST}/artifacts/tck-tests.xml -DgroupId=jakarta.validation \
-DartifactId=validation-tck-tests -Dversion=${tck.version} -Dpackaging=xml -Dclassifier=suite

