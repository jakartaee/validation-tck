# Bean Validation TCK
Version: 1.1.0.CR1

This repository contains the Bean Validation TCK - ([JSR 349](http://www.jcp.org/en/jsr/detail?id=349)).

## Build from Source

You can build the TCK from source by cloning the git repository git://github.com/beanvalidation/beanvalidation-tck.git.
You will also need a JDK 6 or 7 and a Maven 3. With these prerequisites in place you can compile the source via

    mvn clean install -s settings-example.xml

After successful execution you can find the full distribution bundles in _distribution/target_. To inspect the different test artifacts
and the TCK coverage report refer to _tests/target/artifacts_ and _tests/target/coverage-report_ respectively.

# Documentation

The documentation for the TCK is included in the docs directory of the [distribution package](http://www.hibernate.org/subprojects/validator/download)
or viewable [online](http://www.hibernate.org/subprojects/validator/docs.html).

# Release Notes

The release notes for this release can be found [here](https://github.com/beanvalidation/beanvalidation-tck/blob/master/changelog.txt).

# Issue tracking

The Bean Validation TCK issue tracker is [BVTCK](http://opensource.atlassian.com/projects/hibernate/browse/BVTCK).



