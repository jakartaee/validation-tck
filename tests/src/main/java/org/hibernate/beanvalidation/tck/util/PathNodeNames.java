/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Path;
import javax.validation.Path.Node;

/**
 * <p>
 * A list of descriptor names, representing the names of the nodes of a given
 * {@link Path}. Instances are retrieved via {@link TestUtil#names(String...)}.
 * </p>
 * <p>
 * Implemented as separate class since generic classes such as {@link List}
 * can't be passed without warning to varargs methods in Java 6.
 * </p>
 *
 * @author Gunnar Morling
 */
public class PathNodeNames implements Comparable<PathNodeNames> {

	private final List<String> nodeNames;

	PathNodeNames(String... nodeNames) {
		this.nodeNames = Arrays.asList( nodeNames );
	}

	PathNodeNames(Path path) {
		this.nodeNames = new ArrayList<String>();
		for ( Node node : path ) {
			nodeNames.add( node.getName() );
		}
	}

	@Override
	public int compareTo(PathNodeNames other) {
		return toString().compareTo( other.toString() );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ( ( nodeNames == null ) ? 0 : nodeNames.hashCode() );
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if ( this == obj ) {
			return true;
		}
		if ( obj == null ) {
			return false;
		}
		if ( getClass() != obj.getClass() ) {
			return false;
		}
		PathNodeNames other = (PathNodeNames) obj;
		if ( nodeNames == null ) {
			if ( other.nodeNames != null ) {
				return false;
			}
		}
		else if ( !nodeNames.equals( other.nodeNames ) ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PathNodeNames [nodeNames=" + nodeNames + "]";
	}
}
