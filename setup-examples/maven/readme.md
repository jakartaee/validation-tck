# Maven setup example to run the Jakarta Validation TCK

This is an example setup to run the Jakarta Validation TCK 3.1 against Eclipse GlassFish 7+ using [Maven](https://maven.apache.org).

## Prerequisites

* [Git](http://git-scm.com)
* [Maven](https://maven.apache.org) >= 3.9.0
* [JDK 17](https://adoptium.net/temurin/releases/)
* [Eclipse GlassFish 7+](https://projects.eclipse.org/projects/ee4j.glassfish/downloads) installation

## How to run

1. Extract Eclipse GlassFish into a directory (this directory is referenced subsequently as <container.home>)
1. Add the JVM option _validation.provider_ to _domain.xml_ under <container.home>/domains/domain1/config/domain.xml in
   the <java-config> section (this is used by the test harness to look up the Jakarta Validation provider under test):

        <java-config>
            ...
           <jvm-options>-Dvalidation.provider=org.hibernate.validator.HibernateValidator</jvm-options>
        </java-config>
1. Make sure that _container.home_ in _pom.xml_ points to your <container.home> directory
1. Extract the BV TCK distribution.
1. Edit the setup-examples/artifact-install.sh script and set the TCK_DIST to the path of the extracted BV TCK.
1. Run once:

        bash ../artifact-install.sh

1. Run the TCK tests against the default glassfish-managed profile:

        mvn test
> Note: when testing a staged final TCK you need to use the -Pstaging profile
        mvn -Pstaging test

Test results can be found in _target/surefire-reports/index.html_

## Running tests against a running GlassFish 7+ instance
There is a glassfish-remote profile that allows the testsuite to run against
a running GlassFish 7+ instance. To use that profile, run with:

        mvn -Pglassish-remote test

## Running local only tests
You can run the tests that do not require a Jakarta EE container
by using the pom-local.xml file:

        mvn -f pom-local.xml test
or when using staged TCKs:

        mvn -f pom-local.xml -Pstaging test
