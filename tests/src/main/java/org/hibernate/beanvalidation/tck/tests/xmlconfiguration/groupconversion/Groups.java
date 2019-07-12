/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.groupconversion;

/**
 * @author Hardy Ferentschik
 */
@SuppressWarnings("unused")
public class Groups {

	private String foo;

	private String snafu;

	public String getSnafu() {
		return snafu;
	}

	private String convert(String s) {
		return null;
	}
}
