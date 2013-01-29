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
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.validation.Path;
import javax.validation.TraversableResolver;

import static org.testng.Assert.fail;

/**
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class SnifferTraversableResolver implements TraversableResolver {
	int isReachableCallCount = 0;
	int isCascadableCallCount = 0;
	Set<Call> expectedReachCalls = new HashSet<Call>();
	Set<Call> expectedCascadeCalls = new HashSet<Call>();
	Set<Call> executedReachableCalls = new HashSet<Call>();

	public SnifferTraversableResolver(Set<Call> expectedReachCalls, Set<Call> expectedCascadeCalls) {
		this.expectedReachCalls.addAll( expectedReachCalls );
		this.expectedCascadeCalls.addAll( expectedCascadeCalls );
	}

	public int getReachableCallCount() {
		return isReachableCallCount;
	}

	public int getCascadableCallCount() {
		return isCascadableCallCount;
	}

	public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		List<String> names = extractNodeName( pathToTraversableObject );
		Call call = new Call(
				traversableObject,
				traversableProperty.getName(),
				rootBeanType,
				elementType,
				names.toArray( new String[names.size()] )
		);
		executedReachableCalls.add( call );
		isReachableCallCount++;

		return assertIsExpectedCall(
				expectedReachCalls,
				call
		);
	}

	public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType) {
		List<String> names = extractNodeName( pathToTraversableObject );
		Call call = new Call(
				traversableObject,
				traversableProperty.getName(),
				rootBeanType,
				elementType,
				names.toArray( new String[names.size()] )
		);
		if ( !executedReachableCalls.contains( call ) ) {
			throw new IllegalStateException( "isCascadable called before a matching  isReachable call: " + call.toString() );
		}

		isCascadableCallCount++;
		return assertIsExpectedCall(
				expectedCascadeCalls,
				call
		);
	}

	private boolean assertIsExpectedCall(Set<Call> calls, Call call) {
		if ( !calls.contains( call ) ) {
			fail( "Unexpected call to " + call.toString() );
		}
		return true;
	}

	private void registerPath(Set<Path> cascadePaths, Path pathToTraversableObject) {
		cascadePaths.add( pathToTraversableObject );
	}

	private List<String> extractNodeName(Path path) {
		LinkedList<String> names = new LinkedList<String>();
		Iterator<Path.Node> iter = path.iterator();
		while ( iter.hasNext() ) {
			names.add( iter.next().getName() );
		}
		return names;
	}
}
