/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;

/**
 * @author Gunnar Morling
 */
public class ActorCollectionBased extends Actor {

	@Valid
	private final Collection<Actor> playedWith = new ArrayList<Actor>();

	public ActorCollectionBased(String firstName, String lastName) {
		super( firstName, lastName );
	}

	@Override
	public void addPlayedWith(Actor playedWith) {
		this.playedWith.add( playedWith );
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
