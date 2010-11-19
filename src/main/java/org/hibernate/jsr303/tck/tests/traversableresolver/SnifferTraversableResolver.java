// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.jsr303.tck.tests.traversableresolver;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Set;
import javax.validation.Path;
import javax.validation.TraversableResolver;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class SnifferTraversableResolver implements TraversableResolver {
	Set<String> reachPaths = new HashSet<String>();
	Set<String> cascadePaths = new HashSet<String>();
	Set<Call> expectedReachCalls = new HashSet<Call>();
	Set<Call> expectedCascadeCalls = new HashSet<Call>();
	Set<Call> executedReachableCalls = new HashSet<Call>();

	public SnifferTraversableResolver(Suit suit) {
		expectedReachCalls.add( new Call( suit, "size", Suit.class, "", ElementType.FIELD ) );
		expectedReachCalls.add( new Call( suit, "trousers", Suit.class, "", ElementType.FIELD ) );
		expectedCascadeCalls.add( new Call( suit, "trousers", Suit.class, "", ElementType.FIELD ) );
		expectedReachCalls.add( new Call( suit.getTrousers(), "length", Suit.class, "trousers", ElementType.FIELD ) );
		expectedReachCalls.add( new Call( suit, "jacket", Suit.class, "", ElementType.METHOD ) );
		expectedCascadeCalls.add( new Call( suit, "jacket", Suit.class, "", ElementType.METHOD ) );
		expectedReachCalls.add( new Call( suit.getJacket(), "width", Suit.class, "jacket", ElementType.METHOD ) );
	}

	public Set<String> getReachPaths() {
		return reachPaths;
	}

	public Set<String> getCascadePaths() {
		return cascadePaths;
	}

	public boolean isTraversable(Set<Call> calls, Set<String> paths, Call call, String traversableProperty, String pathToTraversableObject) {
		String path = "";
		if ( !( pathToTraversableObject == null || pathToTraversableObject.length() == 0 ) ) {
			path = pathToTraversableObject + ".";
		}
		paths.add( path + traversableProperty );

		if ( !calls.contains( call ) ) {
			throw new IllegalStateException( "Unexpected " + call.toString() );
		}
		return true;
	}

	public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		Call call = new Call(
				traversableObject,
				traversableProperty.getName(),
				rootBeanType,
				pathToTraversableObject.toString(),
				elementType
		);
		executedReachableCalls.add( call );

		return isTraversable(
				expectedReachCalls,
				reachPaths,
				call,
				traversableProperty.getName(),
				pathToTraversableObject.toString()
		);
	}

	public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		Call call = new Call(
				traversableObject,
				traversableProperty.getName(),
				rootBeanType,
				pathToTraversableObject.toString(),
				elementType
		);
		if ( !executedReachableCalls.contains( call ) ) {
			throw new IllegalStateException( "isCascadable called before a matching  isReachable call: " + call.toString() );
		}


		return isTraversable(
				expectedCascadeCalls,
				cascadePaths,
				call,
				traversableProperty.getName(),
				pathToTraversableObject.toString()
		);
	}

	/**
	 * Wrapper class for keeping track of the parameters for a single call to {@link SnifferTraversableResolver#isReachable} and {@link SnifferTraversableResolver#isCascadable}.
	 */
	private static final class Call {
		private Object traversableObject;
		private String traversableProperty;
		private Class<?> rootBeanType;
		private String pathToTraversableObject;
		private ElementType elementType;

		private Call(Object traversableObject, String traversableProperty, Class<?> rootBeanType, String pathToTraversableObject, ElementType elementType) {
			this.traversableObject = traversableObject;
			this.traversableProperty = traversableProperty;
			this.rootBeanType = rootBeanType;
			this.pathToTraversableObject = pathToTraversableObject;
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

			Call call = ( Call ) o;

			if ( elementType != call.elementType ) {
				return false;
			}
			if ( !pathToTraversableObject.equals( call.pathToTraversableObject ) ) {
				return false;
			}
			if ( !rootBeanType.equals( call.rootBeanType ) ) {
				return false;
			}
			if ( traversableObject != null ? !( traversableObject == call.traversableObject ) : call.traversableObject != null ) {
				return false;
			}
			if ( !traversableProperty.equals( call.traversableProperty ) ) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = traversableObject != null ? traversableObject.hashCode() : 0;
			result = 31 * result + traversableProperty.hashCode();
			result = 31 * result + rootBeanType.hashCode();
			result = 31 * result + pathToTraversableObject.hashCode();
			result = 31 * result + elementType.hashCode();
			return result;
		}

		@Override
		public String toString() {
			return "Call{" +
					"traversableObject=" + traversableObject +
					", traversableProperty='" + traversableProperty + '\'' +
					", rootBeanType=" + rootBeanType +
					", pathToTraversableObject='" + pathToTraversableObject + '\'' +
					", elementType=" + elementType +
					'}';
		}
	}
}
