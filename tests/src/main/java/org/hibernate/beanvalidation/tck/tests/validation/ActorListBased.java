/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class ActorListBased extends Actor {

	@Valid
	private final List<Actor> playedWith = new ArrayList<Actor>();

	public ActorListBased(String firstName, String lastName) {
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
