/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.unwrapping.model;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.UnwrapByDefault;
import jakarta.validation.valueextraction.ValueExtractor;

@UnwrapByDefault
public class UnwrapByDefaultIntegerWrapperValueExtractor implements ValueExtractor<@ExtractedValue(type = Integer.class) IntegerWrapper> {

	@Override
	public void extractValues(IntegerWrapper originalValue, ValueExtractor.ValueReceiver receiver) {
		receiver.value( "wrapper", originalValue.getValue() );
	}
}
