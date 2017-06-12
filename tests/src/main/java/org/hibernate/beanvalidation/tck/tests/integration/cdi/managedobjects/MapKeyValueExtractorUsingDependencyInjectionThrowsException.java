/**
 * Bean Validation TCK
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
public class MapKeyValueExtractorUsingDependencyInjectionThrowsException implements ValueExtractor<Map<@ExtractedValue ?, ?>> {

	@SuppressWarnings("unused")
	@Inject
	private Greeter greeter;

	@Override
	public void extractValues(Map<?, ?> originalValue, ValueReceiver receiver) {
		throw new IllegalStateException( "Shouldn't be called as the XML declared value extractor has precedence." );
	}
}
