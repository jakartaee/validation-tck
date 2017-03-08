/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;


import java.lang.annotation.ElementType;
import java.util.Arrays;

/**
 * Wrapper class for keeping track of the parameters for a single call to {@link SnifferTraversableResolver#isReachable}
 * and {@link SnifferTraversableResolver#isCascadable}.
 *
 * @author Hardy Ferentschik
 */
final class Call {
	private final Object traversableObject;
	private final String traversableProperty;
	private final Class<?> rootBeanType;
	private final String[] nodeNamesToTraversableObject;
	private final ElementType elementType;

	Call(Object traversableObject,
		 String traversableProperty,
		 Class<?> rootBeanType,
		 ElementType elementType,
		 String[] nodeNamesToTraversableObject) {
		this.traversableObject = traversableObject;
		this.traversableProperty = traversableProperty;
		this.rootBeanType = rootBeanType;
		this.nodeNamesToTraversableObject = nodeNamesToTraversableObject;
		this.elementType = elementType;
	}

	@Override
	public boolean equals(Object o) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}

		Call call = (Call) o;

		if ( elementType != call.elementType ) {
			return false;
		}
		if ( !Arrays.equals( nodeNamesToTraversableObject, call.nodeNamesToTraversableObject ) ) {
			return false;
		}
		if ( rootBeanType != null ? !rootBeanType.equals( call.rootBeanType ) : call.rootBeanType != null ) {
			return false;
		}
		if ( traversableObject != null ? !traversableObject.equals( call.traversableObject ) : call.traversableObject != null ) {
			return false;
		}
		if ( traversableProperty != null ? !traversableProperty.equals( call.traversableProperty ) : call.traversableProperty != null ) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = traversableObject != null ? traversableObject.hashCode() : 0;
		result = 31 * result + ( traversableProperty != null ? traversableProperty.hashCode() : 0 );
		result = 31 * result + ( rootBeanType != null ? rootBeanType.hashCode() : 0 );
		result = 31 * result + ( nodeNamesToTraversableObject != null ? Arrays.hashCode( nodeNamesToTraversableObject ) : 0 );
		result = 31 * result + ( elementType != null ? elementType.hashCode() : 0 );
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append( "Call" );
		sb.append( "{traversableObject=" ).append( traversableObject );
		sb.append( ", traversableProperty='" ).append( traversableProperty ).append( '\'' );
		sb.append( ", rootBeanType=" ).append( rootBeanType );
		sb.append( ", nodeNamesToTraversableObject=" )
				.append(
						nodeNamesToTraversableObject == null ? "null" : Arrays.asList( nodeNamesToTraversableObject )
								.toString()
				);
		sb.append( ", elementType=" ).append( elementType );
		sb.append( '}' );
		return sb.toString();
	}
}
