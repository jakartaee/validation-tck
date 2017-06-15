#! /bin/bash

# This is a simple script to update an assertion id

LC_ALL=C

if [ $# -ne 3 ]; then
	echo "Usage: $0 <section> <old reference> <new reference>"
	exit 1
fi

section=$1
old_reference=$2
new_reference=$3

pattern='@SpecAssertion(section = Sections.'${section}', id = "'${old_reference}'")'
replacement='@SpecAssertion(section = Sections.'${section}', id = "'${new_reference}'")'

for file in `grep -lr "${pattern}" src/main/java`; do
	sed -i "s/${pattern}/${replacement}/g" ${file}
done
