/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Digits;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Digits} built-in constraint.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class DigitsConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DigitsConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_DIGITS, id = "a")
	public void testDigitsConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		DigitsDummyEntity dummy = new DigitsDummyEntity();

		dummy.intPrimitive = 42;
		dummy.longPrimitive = 42;
		dummy.bytePrimitive = 42;
		dummy.shortPrimitive = 42;

		Set<ConstraintViolation<DigitsDummyEntity>> constraintViolations = validator.validate( dummy );
		// only the max constraints on the primitive values should fail. Object values re still null and should pass per spec
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Digits.class ).withProperty( "bytePrimitive" ),
				violationOf( Digits.class ).withProperty( "intPrimitive" ),
				violationOf( Digits.class ).withProperty( "longPrimitive" ),
				violationOf( Digits.class ).withProperty( "shortPrimitive" )
		);

		dummy.intPrimitive = 1;
		dummy.longPrimitive = 1;
		dummy.bytePrimitive = 1;
		dummy.shortPrimitive = 1;

		dummy.intObject = Integer.valueOf( "102" );
		dummy.longObject = Long.valueOf( "12345" );
		dummy.byteObject = Byte.parseByte( "111" );
		dummy.shortObject = Short.parseShort( "1234" );
		dummy.bigDecimal = BigDecimal.valueOf( 102 );
		dummy.bigInteger = BigInteger.valueOf( 102 );

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Digits.class ).withProperty( "byteObject" ),
				violationOf( Digits.class ).withProperty( "intObject" ),
				violationOf( Digits.class ).withProperty( "longObject" ),
				violationOf( Digits.class ).withProperty( "shortObject" ),
				violationOf( Digits.class ).withProperty( "bigDecimal" ),
				violationOf( Digits.class ).withProperty( "bigInteger" )
		);

		dummy.intObject = Integer.valueOf( "1" );
		dummy.longObject = Long.valueOf( "1" );
		dummy.byteObject = Byte.parseByte( "1" );
		dummy.shortObject = Short.parseShort( "1" );
		dummy.bigDecimal = BigDecimal.valueOf( 1.93 );
		dummy.bigInteger = BigInteger.valueOf( 5 );

		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
	}

	private static class DigitsDummyEntity {
		@Digits(integer = 1, fraction = 2)
		private BigDecimal bigDecimal;

		@Digits(integer = 1, fraction = 0)
		private BigInteger bigInteger;

		@Digits(integer = 1, fraction = 0)
		private byte bytePrimitive;

		@Digits(integer = 1, fraction = 0)
		private short shortPrimitive;

		@Digits(integer = 1, fraction = 0)
		private int intPrimitive;

		@Digits(integer = 1, fraction = 0)
		private long longPrimitive;

		@Digits(integer = 1, fraction = 0)
		private Byte byteObject;

		@Digits(integer = 1, fraction = 0)
		private Short shortObject;

		@Digits(integer = 1, fraction = 0)
		private Integer intObject;

		@Digits(integer = 1, fraction = 0)
		private Long longObject;
	}

}
