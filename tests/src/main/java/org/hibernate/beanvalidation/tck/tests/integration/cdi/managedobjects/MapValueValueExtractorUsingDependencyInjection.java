/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.util.Map;

import javax.inject.Inject;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

/**
 * @author Guillaume Smet
 */
public class MapValueValueExtractorUsingDependencyInjection implements ValueExtractor<Map<?, @ExtractedValue ?>> {

	@Inject
	private Greeter greeter;

	@Override
	public void extractValues(Map<?, ?> originalValue, ValueReceiver receiver) {
		for ( Map.Entry<?, ?> entry : originalValue.entrySet() ) {
			receiver.keyedValue( greeter.greet(), entry.getKey(), entry.getValue() );
		}
	}
}
