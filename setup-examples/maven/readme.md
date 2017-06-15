# Maven setup example to run the JSR-380 TCK

This is an example setup to run the Bean Validation TCK 2.0 against Glassfish 5 using [Maven](https://maven.apache.org).

## Prerequisites

* [Git](http://git-scm.com)
* [Maven](https://maven.apache.org) >= 3.0.4
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Glassfish 5](https://javaee.github.io/glassfish/download) installation

## How to run

1. Extract Glassfish into a directory (this directory is referenced subsequently as <container.home>)
1. Add the JVM option _validation.provider_ to _domain.xml_ under <container.home>/glassfish/domains/domain1/config in
   the <java-config> section (this is used by the test harness to look up the Bean Validation provider under test):

        <java-config>
            ...
           <jvm-options>-Dvalidation.provider=org.hibernate.validator.HibernateValidator</jvm-options>
        </java-config>
1. Make sure that _container.home_ in _pom.xml_ points to your <container.home> directory
1. Run the TCK tests:

        mvn test
1. Test results can be found in _target/surefire-reports/index.html_

