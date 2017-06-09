/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition;

import java.util.List;
import java.util.Map;

import javax.validation.Validation;
import javax.validation.valueextraction.ExtractedValue;
import javax.validation.valueextraction.ValueExtractor;
import javax.validation.valueextraction.ValueExtractorDefinitionException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.Container;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test the exceptions thrown in case of an invalid {@link ValueExtractor}.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidValueExtractorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( InvalidValueExtractorTest.class )
				.withPackage( Container.class.getPackage() )
				.build();
	}

	@Test(expectedExceptions = ValueExtractorDefinitionException.class)
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXAMPLES, id = "a")
	@SpecAssertion(section = Sections.EXCEPTION_VALUEEXTRACTORDEFINITION, id = "a")
	public void severalExtractedValuesThrowException() {
		Validation.byDefaultProvider().configure()
				.addValueExtractor( new SeveralExtractedValuesValueExtractor() )
				.buildValidatorFactory()
				.getValidator();
	}

	@Test(expectedExceptions = ValueExtractorDefinitionException.class)
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "d")
	@SpecAssertion(section = Sections.EXCEPTION_VALUEEXTRACTORDEFINITION, id = "a")
	public void noExtractedValueThrowsException() {
		Validation.byDefaultProvider().configure()
				.addValueExtractor( new NoExtractedValueValueExtractor() )
				.buildValidatorFactory()
				.getValidator();
	}

	private class SeveralExtractedValuesValueExtractor implements ValueExtractor<Map<@ExtractedValue ?, @ExtractedValue ?>> {

		@Override
		public void extractValues(Map<?, ?> originalValue, ValueReceiver receiver) {
			throw new IllegalStateException( "May not be called" );
		}
	}

	private class NoExtractedValueValueExtractor implements ValueExtractor<List<?>> {

		@Override
		public void extractValues(List<?> originalValue, ValueReceiver receiver) {
			throw new IllegalStateException( "May not be called" );
		}
	}
}
