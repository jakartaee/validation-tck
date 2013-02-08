# Maven setup example to run the JSR-349 TCK

This is an example setup to run the Bean Validation TCK 1.1 against JBoss AS 7 using [Maven](https://maven.apache.org).

## Prerequisites

* [Git](http://git-scm.com/)
* [Maven](https://maven.apache.org) >= 3.0.4
* A EE container to run the tests in. The container must provide all Bean Validation dependencies, namely the BeanValidation API
as well as the Bean Validation provider. The latter is the so called Bean Validation provider under test.
This setup uses JBoss AS 7 as EE container, but can be easily modified to run against another container.

## How to run

1. Get the code:

        git clone git://github.com/beanvalidation/beanvalidation-tck.git
1. Change into the maven example directory:

        cd setup-examples/maven
1. Make sure that _containerHome_ in _pom.xml_ points to your container home directory. You can also specify this property on
   the command line via _-DcontainerHome=\<path to container\>_
1. Runs the TCK tests:

        mvn test
1. Test results can be found in _target/surefire-reports/index.html_

## How to adjust for other containers

To adjust the build script to another container, for example Glassfish, you would have to

* Change the Arquillian container adapter dependency (_org.jboss.as:jboss-as-arquillian-container-managed_) to use
  the adapter suitable for your container. If there is no such adapter you have to write your own.
  See also [Container adapters](https://docs.jboss.org/author/display/ARQ/Container+adapters)
* Change the _containerHome_ property in _pom.xml_ to point to your container
* Change _validation.provider_ in _build.properties_ to the fully qualified classname of your Bean Validation provider
* Update the container information in _arquillian.xml_ to contain the required settings for your container




