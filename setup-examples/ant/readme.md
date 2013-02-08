# Ant + Ivy setup example to run the JSR-349 TCK

This is an example setup to run the Bean Validation TCK 1.1 against JBoss AS 7 using
[Ant](https://ant.apache.org) + [Ivy](http://ant.apache.org/ivy/) as build environment.

## Prerequisites

* [Git](http://git-scm.com/)
* [Ant](https://ant.apache.org) >= 1.8
* A EE container to run the tests in. The container must provide all Bean Validation dependencies, namely the BeanValidation API
as well as the Bean Validation provider. The latter is the so called Bean Validation provider under test.
This setup uses JBoss AS 7 as EE container, but can be easily modified to run against another container.

## How to run

1. Get the code:

        git clone git://github.com/beanvalidation/beanvalidation-tck.git
1. Change into the ant example directory:

        cd setup-examples/ant
1. Make sure that _container.home_ in _build.properties_ points to your container home directory. You can create a _user-build.properties_ file
and override the variable _container.home_ in there. _user-build.properties_ is ignored via _.gitignore_
1. Run once:

        ant install-testng-jar
1. Runs the TCK tests:

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
* _user-build.properties_ - Allows to override properties in _build.properties_

### Arquillian

* _arquillian.xml_ - Arquillian configuration file. Defines container protocol and settings
* _log4j.xml_ - Configure the logging framework

## How to adjust for other containers

To adjust the build script to another container, for example Glassfish, you would have to

* Change the Arquillian container adapter in _ivy.xml_ to use the adapter suitable for your container. If there is no
such adapter you have to write your own. See also [Container adapters](https://docs.jboss.org/author/display/ARQ/Container+adapters)
* Change the _container.home_ property in _build.properties_ to point to your container
* Change _validation.provider_ in _build.properties_ to the fully qualified classname of your Bean Validation provider
* Update the container information in _arquillian.xml_ to contain the required settings for your container




