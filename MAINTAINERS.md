# Guide for maintainers of Jakarta Validation

This guide is intended for maintainers of Jakarta Validation,
i.e. anybody with direct push access to the git repository.

## Preparing for the next iteration

When preparing the repository for the next major/minor version:

* Update the project version (if not already done by the release) using the following maven command
    ```
    mvn versions:set -DgenerateBackupPoms=false -DnewVersion=$NEW_VERSION
    ```
* Update the versions in the following files: 
  * [README.md](README.md).
  * [test-unit.properties](tests/src/main/resources/META-INF/test-unit.properties).
  * [preface.asciidoc](documentation/src/main/asciidoc/preface.asciidoc)
  * [sigtest.asciidoc](documentation/src/main/asciidoc/sigtest.asciidoc)
  * [readme.md](setup-examples/maven/readme.md)
  * [tck-audit.xml](tests/src/main/resources/tck-audit.xml)
* Update the `validation.api.version` property in the root [pom.xml](pom.xml)
* Update tests to use the new XML schema version
* Update spec version in @SpecVersion annotations
