/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

/**
 * @author Gunnar Morling
 */
public class Greeter {

	public final static String MESSAGE = "Hello, %s!";

	public final static String FORMAL_MESSAGE = "Good morning, %s!";

	public String greet(String name) {
		return String.format( MESSAGE, name );
	}

	public String greetFormally(String name) {
		return String.format( FORMAL_MESSAGE, name );
	}
}
