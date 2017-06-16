/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.builtin;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class OptionalValueExtractorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( OptionalValueExtractorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "d")
	public void optionalValueExtractor() {
		Validator validator = getValidator();

		Set<ConstraintViolation<OptionalHolder>> violations = validator.validate( new OptionalHolder( Optional.of( "valid" ) ) );
		assertNumberOfViolations( violations, 0 );

		violations = validator.validate( new OptionalHolder( Optional.of( "" ) ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "optional" )
		);

		violations = validator.validate( new OptionalHolder( Optional.empty() ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class ).withProperty( "optional" ),
				violationOf( NotNull.class ).withProperty( "optional" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "e")
	public void optionalIntValueExtractor() {
		Validator validator = getValidator();

		Set<ConstraintViolation<OptionalIntHolder>> violations = validator.validate( new OptionalIntHolder( OptionalInt.of( 10 ) ) );
		assertNumberOfViolations( violations, 0 );

		violations = validator.validate( new OptionalIntHolder( OptionalInt.of( 3 ) ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "optionalInt" )
		);

		violations = validator.validate( new OptionalIntHolder( OptionalInt.empty() ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "optionalInt" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "e")
	public void optionalLongValueExtractor() {
		Validator validator = getValidator();

		Set<ConstraintViolation<OptionalLongHolder>> violations = validator.validate( new OptionalLongHolder( OptionalLong.of( 10 ) ) );
		assertNumberOfViolations( violations, 0 );

		violations = validator.validate( new OptionalLongHolder( OptionalLong.of( 3 ) ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Min.class ).withProperty( "optionalLong" )
		);

		violations = validator.validate( new OptionalLongHolder( OptionalLong.empty() ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "optionalLong" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "e")
	public void optionalDoubleValueExtractor() {
		Validator validator = getValidator();

		Set<ConstraintViolation<OptionalDoubleHolder>> violations = validator.validate( new OptionalDoubleHolder( OptionalDouble.of( 10 ) ) );
		assertNumberOfViolations( violations, 0 );

		violations = validator.validate( new OptionalDoubleHolder( OptionalDouble.of( 3 ) ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( DecimalMin.class ).withProperty( "optionalDouble" )
		);

		violations = validator.validate( new OptionalDoubleHolder( OptionalDouble.empty() ) );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "optionalDouble" )
		);
	}

	private static class OptionalHolder {

		@SuppressWarnings("unused")
		private final Optional<@NotNull @NotBlank String> optional;

		private OptionalHolder(Optional<String> optional) {
			this.optional = optional;
		}
	}

	private static class OptionalIntHolder {

		@NotNull
		@Min(5)
		private final OptionalInt optionalInt;

		private OptionalIntHolder(OptionalInt optionalInt) {
			this.optionalInt = optionalInt;
		}
	}

	private static class OptionalLongHolder {

		@NotNull
		@Min(5)
		private final OptionalLong optionalLong;

		private OptionalLongHolder(OptionalLong optionalLong) {
			this.optionalLong = optionalLong;
		}
	}

	private static class OptionalDoubleHolder {

		@NotNull
		@DecimalMin("5")
		private final OptionalDouble optionalDouble;

		private OptionalDoubleHolder(OptionalDouble optionalDouble) {
			this.optionalDouble = optionalDouble;
		}
	}
}
