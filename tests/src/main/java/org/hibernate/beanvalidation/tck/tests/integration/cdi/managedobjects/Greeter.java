/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.time.ZoneId;

/**
 * @author Gunnar Morling
 */
public class Greeter {

	public static final String MESSAGE = "Hello from Greeter!";

	public static final ZoneId ZONE_ID = ZoneId.of( "Africa/Bujumbura" );

	public String greet() {
		return MESSAGE;
	}

	public ZoneId zoneId() {
		return ZONE_ID;
	}
}
