/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

/**
 * @author Guillaume Smet
 */
public class ReferenceValueExtractor3 implements ValueExtractor<Reference<@ExtractedValue ?>> {

	@Override
	public void extractValues(Reference<?> originalValue, ValueExtractor.ValueReceiver receiver) {
		receiver.value( "3", originalValue.getValue() );
	}
}
