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
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidStockItem;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.StockItem;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest.class )
				.withClass( StockItem.class )
				.withClass( ValidStockItem.class )
				.withValidationXml( "validation-ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateParametersYieldsConstraintViolationIfValidateExecutableIsSetToNONEInXml() throws Exception {
		assertEquals(
				Validation.byDefaultProvider().configure().getBootstrapConfiguration().getDefaultValidatedExecutableTypes(),
				Collections.emptySet()
		);

		Object object = new StockItem( null );
		String methodName = "setName";
		Method method = StockItem.class.getMethod( methodName, String.class );
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
	public void testValidateConstructorParametersYieldsConstraintViolationIfValidateExecutableIsSetToNONEInXml()
			throws Exception {
		assertEquals(
				Validation.byDefaultProvider().configure().getBootstrapConfiguration().getDefaultValidatedExecutableTypes(),
				Collections.emptySet()
		);

		Constructor<StockItem> constructor = StockItem.class.getConstructor( String.class );
		Object[] parameterValues = new Object[] { null };

		Set<ConstraintViolation<StockItem>> violations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.constructor( StockItem.class )
								.parameter( "name", 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_VALIDATORAPI_METHODLEVELVALIDATIONMETHODS, id = "m")
	public void testValidateReturnValueYieldsConstraintViolationIfValidateExecutableIsSetToNONEInXml()
			throws Exception {
		assertEquals(
				Validation.byDefaultProvider().configure().getBootstrapConfiguration().getDefaultValidatedExecutableTypes(),
				Collections.emptySet()
		);

		Object object = new StockItem( null );
		String methodName = "setName";
		Method method = StockItem.class.getMethod( methodName, String.class );
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
	public void testValidateConstructorReturnValueYieldsConstraintViolationIfValidateExecutableIsSetToNONEInXml()
			throws Exception {
		assertEquals(
				Validation.byDefaultProvider().configure().getBootstrapConfiguration().getDefaultValidatedExecutableTypes(),
				Collections.emptySet()
		);

		Constructor<StockItem> constructor = StockItem.class.getConstructor( String.class );
		StockItem createdObject = new StockItem( null );

		Set<ConstraintViolation<StockItem>> violations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				createdObject
		);

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidStockItem.class )
						.withPropertyPath( pathWith()
								.constructor( StockItem.class )
								.returnValue()
						)
		);
	}
}
