#!/usr/bin/env bash

TCK_DIST=/tmp/BV/beanvalidation-tck-dist-2.0.5

# Parent pom
mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=${TCK_DIST}/src/pom.xml -DgroupId=org.hibernate.beanvalidation.tck \
-DartifactId=beanvalidation-tck-parent -Dversion=2.0.5 -Dpackaging=pom

mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=${TCK_DIST}/artifacts/beanvalidation-tck-tests-2.0.5.jar -DgroupId=org.hibernate.beanvalidation.tck \
-DartifactId=beanvalidation-tck-tests -Dversion=2.0.5 -Dpackaging=jar

mvn org.apache.maven.plugins:maven-install-plugin:3.0.0-M1:install-file \
-Dfile=${TCK_DIST}/artifacts/tck-tests.xml -DgroupId=org.hibernate.beanvalidation.tck \
-DartifactId=beanvalidation-tck-tests -Dversion=2.0.5 -Dpackaging=jar -Dclassifier=suite

