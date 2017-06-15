# Ant + Ivy setup example to run the JSR-380 TCK

This is an example setup to run the Bean Validation TCK 2.0 against Glassfish 5 (the Java EE 8 reference implementation)
using [Ant](https://ant.apache.org) + [Ivy](http://ant.apache.org/ivy/) as build environment.

## Prerequisites

* [Git](http://git-scm.com/)
* [Ant](https://ant.apache.org) >= 1.8
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Glassfish 4](https://javaee.github.io/glassfish/download)

## How to run

1. Extract Glassfish into a directory (this directory is referenced subsequently as <container.home>)
1. Add the JVM option _validation.provider_ to _domain.xml_ under <container.home>/glassfish/domains/domain1/config in
   the <java-config> section (this is used by the test harness to look up the Bean Validation provider under test):

        <java-config>
            ...
           <jvm-options>-Dvalidation.provider=org.hibernate.validator.HibernateValidator</jvm-options>
        </java-config>
1. Make sure that _container.home_ in _build.properties_ points to your <container.home> directory
1. Run once:

        ant install-testng-jar
1. Run the TCK tests:

        ant test
1. Test results can be found in _target/test-results/index.html_

## How does it work

### Ivy

Ivy is used to download all the required dependencies. The following files are involved in the Ivy setup

* _ivy.xml_ - Lists all required dependencies to run the TCK
* _ivysettings.xml_ - Defines the repository URLs for the dependencies

### Ant

* _build.xml_ - The main Ant build file. Bootstraps Ivy, downloads dependencies, copy resources and runs the tests
* _build.properties_ - Defines properties for the build. Location of the app server, dependency versions, etc

### Arquillian

* _arquillian.xml_ - Arquillian configuration file. Defines container protocol and settings


