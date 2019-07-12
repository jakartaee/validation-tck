/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.runtime;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValueExtractorExceptionWrapping extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractorExceptionWrapping.class )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "i")
	@SpecAssertion(section = Sections.EXCEPTION, id = "a")
	public void exception_in_value_extractor_is_wrapped() {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.addValueExtractor( new ThrowsExceptionInExtractValuesValueHolderExtractor() )
				.buildValidatorFactory()
				.getValidator();
		validator.validate( new Entity() );
	}

	private class Entity {

		@SuppressWarnings("unused")
		private final ValueHolder<@Min(10) Integer> integerHolder = new ValueHolder<>( 5 );
	}

	private class ValueHolder<T> {

		private final T value;

		private ValueHolder(T value) {
			this.value = value;
		}

		@SuppressWarnings("unused")
		public T getValue() {
			return value;
		}
	}

	private class ThrowsExceptionInExtractValuesValueHolderExtractor implements ValueExtractor<ValueHolder<@ExtractedValue ?>> {

		@Override
		public void extractValues(ValueHolder<@ExtractedValue ?> originalValue, ValueExtractor.ValueReceiver receiver) {
			throw new IllegalStateException( "Error while extracting value" );
		}
	}
}
