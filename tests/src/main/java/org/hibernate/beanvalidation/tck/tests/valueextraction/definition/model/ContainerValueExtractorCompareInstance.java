/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model;

import jakarta.validation.valueextraction.ExtractedValue;
import jakarta.validation.valueextraction.ValueExtractor;

public class ContainerValueExtractorCompareInstance implements ValueExtractor<Container<@ExtractedValue ?>> {

	public static int callCounter = 0;

	private final Container<?> containerInstance;

	public ContainerValueExtractorCompareInstance(Container<?> containerInstance) {
		this.containerInstance = containerInstance;
	}

	@Override
	public void extractValues(Container<?> originalValue, ValueReceiver receiver) {
		callCounter++;

		if ( originalValue != containerInstance ) {
			throw new IllegalArgumentException( "The instance passed to extractValues should be the same as the one registered in the construtor." );
		}

		if ( receiver == null ) {
			throw new IllegalArgumentException( "The value receiver may not be null." );
		}
	}
}
