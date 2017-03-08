/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintcomposition;

import javax.validation.Payload;

/**
 * @author Hardy Ferentschik
 */

public class Severity {
	public static class Info implements Payload {
	}

	public static class Warn implements Payload {
	}

	public static class Error implements Payload {
	}
}
