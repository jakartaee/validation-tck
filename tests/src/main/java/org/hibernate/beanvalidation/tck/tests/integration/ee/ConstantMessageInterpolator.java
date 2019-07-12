/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee;

import java.util.Locale;

import javax.validation.MessageInterpolator;

/**
 * @author Gunnar Morling
 */
public class ConstantMessageInterpolator implements MessageInterpolator {

	@Override
	public String interpolate(String messageTemplate, Context context) {
		return "Invalid constraint";
	}

	@Override
	public String interpolate(String messageTemplate, Context context, Locale locale) {
		return "Invalid constraint";
	}
}
