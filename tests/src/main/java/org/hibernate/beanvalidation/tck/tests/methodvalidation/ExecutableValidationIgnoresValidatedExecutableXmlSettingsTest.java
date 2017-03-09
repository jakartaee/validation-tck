/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Validation;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidStockItem;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.StockItem;
import org.hibernate.beanvalidation.tck.tests.BaseExecutableValidatorTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;
import static org.hibernate.beanvalidation.tck.util.TestUtil.webArchiveBuilder;
import static org.testng.Assert.assertEquals;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest extends BaseExecutableValidatorTest {

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
	@SpecAssertion(section = "5.1.2", id = "m")
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

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertCorrectPathNodeNames( violations, names( methodName, "name" ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.PARAMETER ) );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "m")
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

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertCorrectPathNodeNames( violations, names( StockItem.class.getSimpleName(), "name" ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.CONSTRUCTOR, ElementKind.PARAMETER ) );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "m")
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

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertCorrectPathNodeNames( violations, names( methodName, TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE ) );
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "m")
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

		assertCorrectConstraintTypes( violations, ValidStockItem.class );
		assertCorrectPathNodeNames( violations, names( StockItem.class.getSimpleName(), TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE ) );
	}
}
