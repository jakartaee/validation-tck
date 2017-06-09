#! /bin/bash

# This is a simple script to increment assertion ids when one assertion
# is inserted, causing all the assertions to be incremented by 1.
# Note that it is a rather naive script for now: it only deals with
# +1 increments and it doesn't support 2 letters assertion ids.

LC_ALL=C

if [ $# -ne 3 ]; then
	echo "Usage: $0 <section> <start letter> <end letter>"
	exit 1
fi

section=$1
start_letter=$2
end_letter=$3

if [[ $start_letter != [a-y] ]] ; then
	echo "Start letter must be between a and y"
fi
if [[ $end_letter != [${start_letter}-y] ]] ; then
	echo "End letter must be between a and y"
fi

for letter in $(eval echo "{${end_letter}..${start_letter}}"); do
	next_letter=$(echo "$letter" | tr "_a-z" "a-z_")
	pattern='@SpecAssertion(section = Sections.'${section}', id = "'${letter}'")'
	replacement='@SpecAssertion(section = Sections.'${section}', id = "'${next_letter}'")'

	for file in `grep -lr "${pattern}" src/main/java`; do
		sed -i "s/${pattern}/${replacement}/g" ${file}
	done
done
