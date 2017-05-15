/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.crossparameter;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintTarget;
import javax.validation.ConstraintViolation;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GenericAndCrossParameterConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( GenericAndCrossParameterConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "e")
	public void testAnnotatedElementIsTargetedByDefault() throws Exception {
		Object object = new Calendar();
		Method method = Calendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateReturnValue( object, method, returnValue );
		assertNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, GenericConstraint.class );
		assertEquals( violations.iterator().next().getInvalidValue(), returnValue );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "e")
	public void testAnnotatedElementIsTargetedUsingSupportedValidationTarget() throws Exception {
		Object object = new WebCalendar();
		Method method = WebCalendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateReturnValue( object, method, returnValue );
		assertNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, ExplicitGenericConstraint.class );
		assertEquals( violations.iterator().next().getInvalidValue(), returnValue );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "e")
	public void testParametersAreTargetedUsingSupportedValidationTarget() throws Exception {
		Object object = new OnlineCalendar();
		Method method = OnlineCalendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);
		assertNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateParameters( object, method, parameterValues );
		assertNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, CrossParameterConstraint.class );
		assertEquals( violations.iterator().next().getInvalidValue(), parameterValues );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_RETURNVALUECONSTRAINTS, id = "b")
	public void testOneValidatorSupportsBothValidationTargets() throws Exception {
		Object object = new MobileCalendar();
		Method method = MobileCalendar.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];
		Object returnValue = new Object();

		//constraint applies to return value
		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
		assertNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateReturnValue( object, method, returnValue );
		assertNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, GenericAndCrossParameterConstraintWithOneValidator.class );
		assertEquals( violations.iterator().next().getInvalidValue(), returnValue );

		method = MobileCalendar.class.getMethod( "addEvent", Date.class, Date.class );

		//constraint applies to parameters
		violations = getExecutableValidator().validateReturnValue( object, method, returnValue );
		assertNumberOfViolations( violations, 0 );

		violations = getExecutableValidator().validateParameters( object, method, parameterValues );
		assertNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, GenericAndCrossParameterConstraintWithOneValidator.class );
		assertEquals( violations.iterator().next().getInvalidValue(), parameterValues );
	}

	private static class Calendar {

		@GenericConstraint
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}

	private static class WebCalendar {

		@ExplicitGenericConstraint
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}


	private static class OnlineCalendar {

		@CrossParameterConstraint
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}

	private static class MobileCalendar {

		@GenericAndCrossParameterConstraintWithOneValidator(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
		public Object createEvent(Date start, Date end) {
			return null;
		}

		@GenericAndCrossParameterConstraintWithOneValidator(validationAppliesTo = ConstraintTarget.PARAMETERS)
		public Object addEvent(Date start, Date end) {
			return null;
		}
	}
}
