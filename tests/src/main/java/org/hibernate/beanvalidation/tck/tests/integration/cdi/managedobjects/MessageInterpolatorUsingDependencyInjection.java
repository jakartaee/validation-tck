/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.util.Locale;

import jakarta.inject.Inject;
import jakarta.validation.MessageInterpolator;

/**
 * @author Gunnar Morling
 */
public class MessageInterpolatorUsingDependencyInjection implements MessageInterpolator {

	@Inject
	private Greeter greeter;

	@Override
	public String interpolate(String messageTemplate, Context context) {
		return greeter.greet();
	}

	@Override
	public String interpolate(String messageTemplate, Context context, Locale locale) {
		return greeter.greet();
	}
}
