/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidLineItem;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidWarehouseItem;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.LineItem;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.WarehouseItem;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ExecutableValidationIgnoresValidatedExecutableAnnotationSettingsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExecutableValidationIgnoresValidatedExecutableAnnotationSettingsTest.class )
				.withClass( LineItem.class )
				.withClass( WarehouseItem.class )
				.withClass( ValidLineItem.class )
				.withClass( ValidWarehouseItem.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateParametersYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnTypeLevel()
			throws Exception {
		Object object = new LineItem( null );
		String methodName = "setName";
		Method method = LineItem.class.getMethod( methodName, String.class );
		Object[] parameterValues = new Object[] { null };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "name", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateConstructorParametersYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnTypeLevel()
			throws Exception {
		Constructor<LineItem> constructor = LineItem.class.getConstructor( String.class );
		Object[] parameterValues = new Object[] { null };

		Set<ConstraintViolation<LineItem>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( LineItem.class )
								.parameter( "name", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateReturnValueYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnTypeLevel()
			throws Exception {
		Object object = new LineItem( null );
		String methodName = "setName";
		Method method = LineItem.class.getMethod( methodName, String.class );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateConstructorReturnValueYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnTypeLevel()
			throws Exception {
		Constructor<LineItem> constructor = LineItem.class.getConstructor( String.class );
		LineItem createdObject = new LineItem( null );

		Set<ConstraintViolation<LineItem>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				createdObject
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidLineItem.class )
						.withPropertyPath( pathWith()
								.constructor( LineItem.class )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateParametersYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnExecutableLevel()
			throws Exception {
		Object object = new WarehouseItem( null );
		String methodName = "setName";
		Method method = WarehouseItem.class.getMethod( methodName, String.class );
		Object[] parameterValues = new Object[] { null };

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.parameter( "name", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateConstructorParametersYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnExecutableLevel()
			throws Exception {
		Constructor<WarehouseItem> constructor = WarehouseItem.class.getConstructor( String.class );
		Object[] parameterValues = new Object[] { null };

		Set<ConstraintViolation<WarehouseItem>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( WarehouseItem.class )
								.parameter( "name", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateReturnValueYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnExecutableLevel()
			throws Exception {
		Object object = new WarehouseItem( null );
		String methodName = "setName";
		Method method = WarehouseItem.class.getMethod( methodName, String.class );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.method( methodName )
								.returnValue()
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateConstructorReturnValueYieldsConstraintViolationIfValidateExecutableIsSetToNONEOnExecutableLevel()
			throws Exception {
		Constructor<WarehouseItem> constructor = WarehouseItem.class.getConstructor( String.class );
		WarehouseItem createdObject = new WarehouseItem( null );

		Set<ConstraintViolation<WarehouseItem>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				createdObject
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidWarehouseItem.class )
						.withPropertyPath( pathWith()
								.constructor( WarehouseItem.class )
								.returnValue()
						)
		);
	}
}
