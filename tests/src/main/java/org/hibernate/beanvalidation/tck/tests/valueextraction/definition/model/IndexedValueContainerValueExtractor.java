/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

public class IndexedValueContainerValueExtractor implements ValueExtractor<Container<@ExtractedValue ?>> {

	@Override
	public void extractValues(Container<?> originalValue, ValueReceiver receiver) {
		receiver.indexedValue( "<node name>", 13, originalValue.getElement() );
	}
}
