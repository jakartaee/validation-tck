/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
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
