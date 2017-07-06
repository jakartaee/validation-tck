/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Address;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.ContainerElementsOrder;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Item;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.OrderLine;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.ProductCategory;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User.Basic;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.User.Extended;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;

import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ValidateParametersTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValidateParametersTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withClass( Address.class )
				.withClass( Item.class )
				.withClass( OrderLine.class )
				.withClass( User.class )
				.withClass( ContainerElementsOrder.class )
				.withClass( ProductCategory.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "d")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "e")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void testOneViolation() throws Exception {
		String methodName = "setFirstName";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class );
		String arg0 = "B";
		Object[] parameterValues = new Object[] { arg0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "firstName", 0 )
						)
		);

		ConstraintViolation<Object> violation = violations.iterator().next();
		assertEquals( violation.getRootBean(), object );
		assertEquals( violation.getRootBeanClass(), User.class );
		assertEquals( violation.getLeafBean(), object );
		assertEquals( violation.getInvalidValue(), arg0 );
		assertEquals( violation.getExecutableParameters(), parameterValues );
		assertNull( violation.getExecutableReturnValue() );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "j")
	public void testOneViolationFromCrossParameterConstraint() throws Exception {
		String methodName = "setAddress";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, String.class );
		Object[] parameterValues = new Object[] { null, null };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);

		ConstraintViolation<Object> violation = violations.iterator().next();
		assertEquals( violation.getLeafBean(), object );
		assertEquals( violation.getInvalidValue(), parameterValues );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testTwoViolations() throws Exception {
		String methodName = "setNames";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, CharSequence.class );
		Object[] parameterValues = new Object[] { null, "S" };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "firstName", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "lastName", 1 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testTwoViolationsOnSameParameter() throws Exception {
		String methodName = "setFirstNameStrict";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, int.class );
		Object[] parameterValues = new Object[] { "S", 0 };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "firstName", 0 )
						),
				violationOf( Pattern.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "firstName", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testTwoConstraintsOfSameType() throws Exception {
		String methodName = "setLastNameStrict";

		Object object = new User();
		Method method = User.class.getMethod( methodName, CharSequence.class );
		Object[] parameterValues = new Object[] { "S" };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "lastName", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "lastName", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testCrossParameterConstraintGivenSeveralTimes() throws Exception {
		String methodName = "setAddress";

		Object object = new User();
		Method method = User.class.getMethod(
				methodName,
				String.class,
				String.class,
				String.class
		);
		Object[] parameterValues = new Object[] { null, null, null };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						),
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testNoViolations() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setNames", String.class, CharSequence.class );
		Object[] parameterValues = new Object[] { "Bob", "Smith" };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertNoViolations( violations );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testValidationWithGroup() throws Exception {
		String methodName = "setLastName";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, long.class );
		Object[] parameterValues = new Object[] { "S", 0l };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertNoViolations( violations );

		violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				Extended.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "lastName", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testCrossParameterConstraintValidationWithGroup() throws Exception {
		String methodName = "setAddressExtended";

		Object object = new User();
		Method method = User.class.getMethod( methodName, CharSequence.class, String.class );
		Object[] parameterValues = new Object[] { null, null };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertNoViolations( violations );

		violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				Extended.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( MyCrossParameterConstraint.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.crossParameter()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "b")
	public void testValidationWithSeveralGroups() throws Exception {
		String methodName = "setAllData";

		Object object = new User();
		Method method = User.class.getMethod( methodName, String.class, String.class, Date.class );
		Object[] parameterValues = new Object[] { null, "S", null };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertNoViolations( violations );

		violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				Basic.class,
				Extended.class
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "firstName", 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "lastName", 1 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "dateOfBirth", 2 )
						)
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "a")
	public void testUnexpectedType() throws Exception {
		String methodName = "setName";

		Object object = new Address();
		Method method = Address.class.getMethod( methodName, String.class );
		Object[] parameterValues = new Object[] { "S" };

		getExecutableValidator().validateParameters( object, method, parameterValues );
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "c")
	public void testNullPassedForObjectCausesException() throws Exception {
		Object object = null;
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = new Object[] { null };

		getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "c")
	public void testNullPassedForMethodCausesException() throws Exception {
		Object object = new User();
		Method method = null;
		Object[] parameterValues = new Object[] { null };

		getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "c")
	public void testNullPassedForParameterValuesCausesException() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = null;

		getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "c")
	public void testNullPassedForGroupsCausesException() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = new Object[] { null };

		getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				(Class<?>[]) null
		);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "c")
	public void testNullPassedAsSingleGroupCausesException() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setFirstName", String.class );
		Object[] parameterValues = new Object[] { null };

		getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues,
				(Class<?>) null
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "g")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "h")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "i")
	public void testOneViolationForCascadedValidation() throws Exception {
		String methodName = "setItem";

		Object object = new OrderLine( null );
		Item leaf = new Item( "foo" );
		Method method = OrderLine.class.getMethod( methodName, Item.class );
		Object[] parameterValues = new Object[] { leaf };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( Size.class )
		);

		ConstraintViolation<Object> violation = violations.iterator().next();

		assertEquals( violation.getLeafBean(), leaf );
		assertEquals( violation.getInvalidValue(), "foo" );
		assertEquals( violation.getExecutableParameters(), parameterValues );
		assertNull( violation.getExecutableReturnValue() );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "f")
	public void testContainerElementLeafBean() throws NoSuchMethodException, SecurityException {
		Method method = ContainerElementsOrder.class.getMethod( "replaceOrderLines", Map.class );

		ContainerElementsOrder order = new ContainerElementsOrder( "order" );
		Item invalidItem = new Item( "s" );

		Map<ProductCategory, List<OrderLine>> invalidLines = new HashMap<>();
		invalidLines.put( null, Arrays.asList( new OrderLine( new Item( "item name" ) ) ) );
		invalidLines.put( ProductCategory.MUSIC, Arrays.asList( new OrderLine( invalidItem ) ) );

		Object[] parameterValues = new Object[] { invalidLines };

		Set<ConstraintViolation<ContainerElementsOrder>> violations = getExecutableValidator().validateParameters(
				order,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withLeafBean( order ),
				violationOf( Size.class ).withLeafBean( invalidItem )
		);
	}
}
