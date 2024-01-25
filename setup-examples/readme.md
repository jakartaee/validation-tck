# Jakarta Bean Validation TCK setup examples

This directory contains [Ant](https://ant.apache.org) and [Maven](https://maven.apache.org/) setup examples for
running the [Jakarta Bean Validation](https://github.com/jakartaee/validation-tck) TCK against [Eclipse GlassFish 7+](https://projects.eclipse.org/projects/ee4j.glassfish/downloads),
the ratifying implementation for Jakarta EE 10.

Each setup contains a readme outlining how to run the test harness using the given build system. These setups
serve as guide and can be adjusted to run against other EE containers. An example for running against WildFly can
be found as part of the [Hibernate Validator TCK Runner](https://github.com/hibernate/hibernate-validator/tree/master/tck-runner)
setup. It is the responsibility of the container to provide all Jakarta Bean Validation dependencies, namely the Jakarta Bean Validation API
as well as the Jakarta Bean Validation provider. The latter is the so-called Jakarta Bean Validation provider under test.

Generally, to adjust the build script to another container, you would have to:

* Change the _validation.provider_ property to the fully qualified class name of your Jakarta Bean Validation provider.
* Change the Arquillian container adapter dependency to use the adapter suitable for your container
 (see [container adapters](https://docs.jboss.org/author/display/ARQ/Container+adapters)). If there is no such adapter
 you have to write your own. You can look at the [standalone container adapter](https://github.com/jakartaee/validation-tck/tree/main/standalone-container-adapter)
 provided by this TCK in order to run test in the current JVM. It is a simple version of a container adapter, but still
 contains all the required pieces.
* Change the _container.home_ property to point to your container.
* Update the _arquillian.xml_ configuration to match the required settings for your container.
* Make sure that the container starts with the system property _validation.provider_ to the fully qualified class name
of your Jakarta Bean Validation implementation.

Note, that is it **not** enough to just run and pass the TCK tests in order to be compliant Jakarta Bean Validation implementation.
Another requirement for example is to pass the API [signature test](http://docs.jboss.org/hibernate/beanvalidation/tck/3.0/reference/html_single/#sigtest).
More information about all requirements of the Jakarta Bean Validation TCK can be found [here](http://docs.jboss.org/hibernate/beanvalidation/tck/3.0/reference/html_single/index.html#passing-the-tck).
