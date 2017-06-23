#! /bin/bash

# This is a simple script to update the section and the assertion id

LC_ALL=C

if [ $# -ne 4 ]; then
	echo "Usage: $0 <old section> <old reference> <new section> <new reference>"
	exit 1
fi

section=$1
old_reference=$2
new_section=$3
new_reference=$4

pattern='@SpecAssertion(section = Sections.'${section}', id = "'${old_reference}'")'
replacement='@SpecAssertion(section = Sections.'${new_section}', id = "'${new_reference}'")'

for file in `grep -lr "${pattern}" src/main/java`; do
	sed -i "s/${pattern}/${replacement}/g" ${file}
done
