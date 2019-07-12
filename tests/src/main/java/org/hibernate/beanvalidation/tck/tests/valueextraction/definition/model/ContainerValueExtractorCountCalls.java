/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

public class ContainerValueExtractorCountCalls implements ValueExtractor<Container<@ExtractedValue ?>> {

	public static int callCounter = 0;

	@Override
	public void extractValues(Container<?> originalValue, ValueReceiver receiver) {
		callCounter++;
	}
}
