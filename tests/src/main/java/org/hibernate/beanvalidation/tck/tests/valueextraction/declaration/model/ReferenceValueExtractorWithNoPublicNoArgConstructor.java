/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

/**
 * @author Guillaume Smet
 */
public class ReferenceValueExtractorWithNoPublicNoArgConstructor implements ValueExtractor<Reference<@ExtractedValue ?>> {

	public ReferenceValueExtractorWithNoPublicNoArgConstructor(String parameter) {
	}

	@Override
	public void extractValues(Reference<?> originalValue, ValueExtractor.ValueReceiver receiver) {
		receiver.value( null, originalValue.getValue() );
	}
}
