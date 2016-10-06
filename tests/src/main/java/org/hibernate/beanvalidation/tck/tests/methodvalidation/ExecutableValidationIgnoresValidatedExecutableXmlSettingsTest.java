/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
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
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidStockItem;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.StockItem;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;
import static org.testng.Assert.assertEquals;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest extends Arquillian {

	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest.class )
				.withClass( StockItem.class )
				.withClass( ValidStockItem.class )
				.withValidationXml( "validation-ExecutableValidationIgnoresValidatedExecutableXmlSettingsTest.xml" )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forExecutables();
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(
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

		Set<ConstraintViolation<StockItem>> violations = executableValidator.validateConstructorParameters(
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

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
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

		Set<ConstraintViolation<StockItem>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				createdObject
		);

		assertCorrectConstraintTypes( violations, ValidStockItem.class );
		assertCorrectPathNodeNames( violations, names( StockItem.class.getSimpleName(), TestUtil.RETURN_VALUE_NODE_NAME ) );
		assertCorrectPathNodeKinds( violations, kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE ) );
	}
}
