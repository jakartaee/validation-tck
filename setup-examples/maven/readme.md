# Maven setup example to run the Jakarta Validation TCK

This is an example setup to run the Jakarta Validation TCK 4.0 against WildFly Preview 37+ using [Maven](https://maven.apache.org).

## Prerequisites

* [Git](http://git-scm.com)
* [Maven](https://maven.apache.org) >= 3.9.0
* [JDK 17](https://adoptium.net/temurin/releases/)
* [WildFly Preview](https://github.com/wildfly/wildfly/releases) installation

## How to run

1. Prepare the WildFly Preview installation first. This can be done with the following maven command:
    ```shell
    ./mvnw clean package -f setup-examples/maven/pom.xml -Dtck.version=4.0.0-SNAPSHOT -Pprepare-server
    ```
   Or alternatively, download the WildFly distribution, patch the validation module with the required Jakarta Validation API version, 
   and with your validation provider.
1. Make sure that _container.home_ in _pom.xml_ points to your <container.home> directory. Can be passed as part of the maven command via `-Dcontainer.home=path/to/your/wildfly`
1. Extract the BV TCK distribution.
1. Edit the setup-examples/artifact-install.sh script and set the TCK_DIST to the path of the extracted BV TCK.
   Or use the `TCK_DIST` environment variable to point to the extracted TCK  
1. Run once:
   ```shell
   bash ../artifact-install.sh
   ```
1. Run the TCK tests against the default in-container profile:
   ```shell
   ./mvnw verify -f pom.xml -Pin-container-test -Dcontainer.home=path/to/your/wildfly
   ```
Test results can be found in _target/surefire-reports/index.html_

## Running local only tests
You can run the tests that do not require a Jakarta EE container
by using the pom-local.xml file:

```shell
./mvnw -f pom-local.xml test -Phibernate-validator
```

    # Note: pom-local.xml example comes with hibernate-validator/bval profiles
    # to run with the tests against one of the implementations.

NOTE: if you run these examples from this repository directly, you need to specify the TCK version to use via `-Dtck.version=4.0.0`
