/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.util.Locale;

import javax.validation.MessageInterpolator;

/**
 * @author Hardy Ferentschik
 */
public class ConfigurationDefinedMessageInterpolator implements MessageInterpolator {
	public static final String STATIC_INTERPOLATION_STRING = "Interpolator defined in Configuration was used.";

	public String interpolate(String messageTemplate, Context context) {
		return STATIC_INTERPOLATION_STRING;
	}

	public String interpolate(String messageTemplate, Context context, Locale locale) {
		return STATIC_INTERPOLATION_STRING;
	}
}
