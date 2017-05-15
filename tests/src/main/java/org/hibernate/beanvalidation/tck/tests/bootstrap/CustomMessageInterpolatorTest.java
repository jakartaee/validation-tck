/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.testng.Assert.assertFalse;

import java.util.Locale;
import java.util.Set;

import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class CustomMessageInterpolatorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( CustomMessageInterpolatorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "e")
	public void testCustomMessageInterpolatorViaConfiguration() {
		Configuration<?> config = Validation.byDefaultProvider().configure();
		config = config.messageInterpolator( new DummyMessageInterpolator() );
		Validator validator = config.buildValidatorFactory().getValidator();

		assertCustomMessageInterpolatorUsed( validator );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_MESSAGE_CUSTOMRESOLUTION, id = "e")
	public void testCustomMessageInterpolatorViaValidatorContext() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		DummyMessageInterpolator dummyMessageInterpolator = new DummyMessageInterpolator();
		Validator validator = factory.usingContext().messageInterpolator( dummyMessageInterpolator ).getValidator();
		assertCustomMessageInterpolatorUsed( validator );
		assertFalse(
				factory.getMessageInterpolator().equals( dummyMessageInterpolator ),
				"getMessageInterpolator() should return the default message interpolator."
		);
	}

	private void assertCustomMessageInterpolatorUsed(Validator validator) {
		Person person = new Person();
		person.setFirstName( "John" );
		person.setPersonalNumber( 1234567890l );

		Set<ConstraintViolation<Person>> constraintViolations = validator.validate( person );
		assertNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "my custom message" );
	}

	private static class DummyMessageInterpolator implements MessageInterpolator {
		@Override
		public String interpolate(String message, Context context) {
			return "my custom message";
		}

		@Override
		public String interpolate(String message, Context context, Locale locale) {
			throw new UnsupportedOperationException( "No specific locale is possible" );
		}
	}

	public class Person {
		@NotNull
		private String firstName;

		@NotNull
		private String lastName;

		@Digits(integer = 10, fraction = 0)
		private long personalNumber;


		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public long getPersonalNumber() {
			return personalNumber;
		}

		public void setPersonalNumber(long personalNumber) {
			this.personalNumber = personalNumber;
		}
	}
}
