/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

public class ValueContainerValueExtractor implements ValueExtractor<Container<@ExtractedValue ?>> {

	@Override
	public void extractValues(Container<?> originalValue, ValueReceiver receiver) {
		receiver.value( "<node name>", originalValue.getElement() );
	}
}
