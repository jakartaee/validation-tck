/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import java.util.Map;

import jakarta.inject.Inject;
import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

/**
 * @author Guillaume Smet
 */
public class MapKeyValueExtractorUsingDependencyInjectionThrowsException implements ValueExtractor<Map<@ExtractedValue ?, ?>> {

	@SuppressWarnings("unused")
	@Inject
	private Greeter greeter;

	@Override
	public void extractValues(Map<?, ?> originalValue, ValueReceiver receiver) {
		throw new IllegalStateException( "Shouldn't be called as the XML declared value extractor has precedence." );
	}
}
