# Bean Validation TCK
*Version: 2.0.0-SNAPSHOT*

This repository contains the Bean Validation ([JSR 380](http://www.jcp.org/en/jsr/detail?id=380))
[TCK](http://en.wikipedia.org/wiki/Technology_Compatibility_Kit).

## Build from Source

You can build the TCK from source by cloning the git repository git://github.com/beanvalidation/beanvalidation-tck.git.
You will also need a [JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 8 and
[Maven](http://maven.apache.org/) 3. With these prerequisites in place you can compile the source via:

    mvn clean install -s settings-example.xml

After successful execution you can find the full distribution bundles in _distribution/target_. To inspect the different test artifacts
and the TCK coverage report refer to _tests/target/artifacts_ and _tests/target/coverage-report_ respectively.

# Documentation

The documentation for the TCK is included in the docs directory of the [distribution package](http://www.hibernate.org/subprojects/validator/download)
or viewable [online](http://www.hibernate.org/subprojects/validator/docs.html).

If you are looking at a quick start for testing your Bean Validation provider implementation against this TCK have a look at
the _[setup-examples](https://github.com/beanvalidation/beanvalidation-tck/tree/master/setup-examples)_ directory.
It contains a setup example for [Ant](https://github.com/beanvalidation/beanvalidation-tck/blob/master/setup-examples/ant/readme.md)
and [Maven](https://github.com/beanvalidation/beanvalidation-tck/blob/master/setup-examples/maven/readme.md).

# Release Notes

The release notes for this release can be found [here](https://github.com/beanvalidation/beanvalidation-tck/blob/master/changelog.txt).

# Issue tracking

The Bean Validation TCK issue tracker is [BVTCK](http://opensource.atlassian.com/projects/hibernate/browse/BVTCK).
