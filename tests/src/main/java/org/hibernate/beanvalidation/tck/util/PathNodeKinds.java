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

import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Path.Node;

/**
 * A list of {@link ElementKind}s, representing the kinds of the nodes of a
 * given {@link Path}. Instances are retrieved via
 * {@link TestUtil#kinds(ElementKind...)}.
 * <p>
 * Implemented as separate class since generic classes such as {@link List}
 * can't be passed without warning to varargs methods in Java 6.
 *
 * @author Gunnar Morling
 */
public class PathNodeKinds implements Comparable<PathNodeKinds> {

	private final List<ElementKind> kinds;

	PathNodeKinds(ElementKind... kinds) {
		this.kinds = Arrays.asList( kinds );
	}

	PathNodeKinds(Path path) {
		this.kinds = new ArrayList<ElementKind>();
		for ( Node node : path ) {
			kinds.add( node.getKind() );
		}
	}

	@Override
	public int compareTo(PathNodeKinds other) {
		return toString().compareTo( other.toString() );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( kinds == null ) ? 0 : kinds.hashCode() );
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
		PathNodeKinds other = (PathNodeKinds) obj;
		if ( kinds == null ) {
			if ( other.kinds != null ) {
				return false;
			}
		}
		else if ( !kinds.equals( other.kinds ) ) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "PathDescriptorKinds [kinds=" + kinds + "]";
	}
}
