# Maven setup example to run the Jakarta Bean Validation TCK

This is an example setup to run the Jakarta Bean Validation TCK 2.0 against Eclipse GlassFish 5.1+ using [Maven](https://maven.apache.org).

## Prerequisites

* [Git](http://git-scm.com)
* [Maven](https://maven.apache.org) >= 3.0.4
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
* [Eclipse GlassFish 6+](https://projects.eclipse.org/projects/ee4j.glassfish/downloads) installation
* A local build of the [arquillian-container-glassfish6](https://github.com/jakartaredhat/arquillian-container-glassfish6)

## How to run

1. Extract Eclipse GlassFish into a directory (this directory is referenced subsequently as <container.home>)
1. Add the JVM option _validation.provider_ to _domain.xml_ under <container.home>/glassfish/domains/domain1/config in
   the <java-config> section (this is used by the test harness to look up the Jakarta Bean Validation provider under test):

        <java-config>
            ...
           <jvm-options>-Dvalidation.provider=org.hibernate.validator.HibernateValidator</jvm-options>
        </java-config>
1. Make sure that _container.home_ in _pom.xml_ points to your <container.home> directory
1. Launch the glassfish6 server from the <container.home> directory:

        bin/asadmin start-domain
1. Extract the BV TCK distribution.
1. Edit the setup-examples/artifact-install.sh script and set the TCK_DIST to the path of the extracted BV TCK.
1. Run once:

        bash ../artifact-install.sh

1. Run the TCK tests:

        mvn test
> Note: when testing a staged final TCK you need to use the -Pstaing profile
        mvn -Pstaging test

Test results can be found in _target/surefire-reports/index.html_

## Running tests against a running GlassFish 6+ instance
There is a glassfish-remote profile that allows the testsuite to run against
a running GlassFish 6+ instance. To use that profile, run with:

        mvn -P test


## Running local only tests
You can run the tests that do not require a Jakarta EE container
by using the pom-local.xml file:

        mvn -f pom-local.xml test
or when using staged TCKs:

        mvn -f pom-local.xml -Pstaging test
