/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.traversableresolver;

import jakarta.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class Gentleman {
	private Suit suit;

	public void wearSuit(@Valid Suit suit) {
		this.suit = suit;
	}

	@Valid
	public Suit undress() {
		Suit tmpSuit = this.suit;
		this.suit = null;
		return tmpSuit;
	}
}


