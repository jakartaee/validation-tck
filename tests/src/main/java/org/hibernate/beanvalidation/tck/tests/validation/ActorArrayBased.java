/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import javax.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class ActorArrayBased extends Actor {
	public static final int MAX_ACTOR_SIZE = 100;

	@Valid
	private final Actor[] playedWith = new Actor[MAX_ACTOR_SIZE];

	int currentPointer = 0;

	public ActorArrayBased(String firstName, String lastName) {
		super( firstName, lastName );
	}

	@Override
	public void addPlayedWith(Actor playedWith) {
		if ( currentPointer == MAX_ACTOR_SIZE ) {
			throw new RuntimeException( "Exceeded allowed number of actors." );
		}
		this.playedWith[currentPointer] = playedWith;
		currentPointer++;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
