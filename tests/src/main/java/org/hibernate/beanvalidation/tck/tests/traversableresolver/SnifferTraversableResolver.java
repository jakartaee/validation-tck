/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import static org.testng.Assert.fail;

import java.lang.annotation.ElementType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.validation.Path;
import javax.validation.TraversableResolver;

/**
 * A {@link TraversableResolver} implementation used for asserting that the
 * actual calls to the resolver by the engine under test match the expected
 * calls.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public class SnifferTraversableResolver implements TraversableResolver {
	private int isReachableCallCount = 0;
	private int isCascadableCallCount = 0;
	private final Set<Call> expectedReachCalls = new HashSet<Call>();
	private final Set<Call> expectedCascadeCalls = new HashSet<Call>();
	private final Set<Call> executedReachableCalls = new HashSet<Call>();

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

	@Override
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

	@Override
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

	private List<String> extractNodeName(Path path) {
		LinkedList<String> names = new LinkedList<String>();
		Iterator<Path.Node> iter = path.iterator();
		while ( iter.hasNext() ) {
			names.add( iter.next().getName() );
		}
		return names;
	}
}
