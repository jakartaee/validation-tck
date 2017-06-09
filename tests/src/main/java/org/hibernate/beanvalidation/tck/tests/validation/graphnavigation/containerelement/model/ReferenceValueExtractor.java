/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

/**
 * @author Gunnar Morling
 *
 */
public class ReferenceValueExtractor implements ValueExtractor<Reference<@ExtractedValue ?>> {

	@Override
	public void extractValues(Reference<?> originalValue, ValueExtractor.ValueReceiver receiver) {
		receiver.value( null, originalValue.getValue() );
	}
}
