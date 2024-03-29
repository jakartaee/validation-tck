/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import java.util.Map;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

public class LocalMapKeyExtractor implements ValueExtractor<Map<@ExtractedValue ?, ?>> {

	private final Map<?, ?> instance;

	public LocalMapKeyExtractor(Map<?, ?> instance) {
		this.instance = instance;
	}

	@Override
	public void extractValues(Map<?, ?> originalValue, ValueReceiver receiver) {
		if (originalValue != instance) {
			throw new IllegalArgumentException( "The instance passed to extractValues should be the same as the one registered in the constructor." );
		}

		for ( Map.Entry<?, ?> entry : originalValue.entrySet() ) {
			receiver.keyedValue( "<map key>", entry.getKey(), entry.getKey() );
		}
	}
}
