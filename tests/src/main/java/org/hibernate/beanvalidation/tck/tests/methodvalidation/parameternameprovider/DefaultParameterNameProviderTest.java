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
package org.hibernate.beanvalidation.tck.tests.methodvalidation.parameternameprovider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ParameterNameProvider;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getParameterNames;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class DefaultParameterNameProviderTest extends Arquillian {

	private Validator validator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( DefaultParameterNameProviderTest.class )
				.withClass( BrokenCustomParameterNameProvider.class )
				.withClass( User.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@Test
	@SpecAssertion(section = "4.5.2.2", id = "a")
	public void testDefaultParameterProviderForMethod() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setNames", String.class, String.class );
		Object[] parameters = new Object[] { null, null };

		Set<ConstraintViolation<Object>> constraintViolations = validator.forExecutables()
				.validateParameters( object, method, parameters );
		assertCorrectNumberOfViolations( constraintViolations, 2 );

		Set<String> actualParameterNames = getParameterNames( constraintViolations );
		Set<String> expectedParameterNames = asSet( "arg0", "arg1" );

		assertEquals( actualParameterNames, expectedParameterNames );
	}

	@Test
	@SpecAssertion(section = "4.5.2.2", id = "a")
	public void testDefaultParameterProviderForConstructor() throws Exception {
		Constructor<User> constructor = User.class.getConstructor(
				String.class,
				String.class,
				Date.class
		);
		Object[] parameters = new Object[] { null, null, null };

		Set<ConstraintViolation<User>> constraintViolations = validator.forExecutables()
				.validateConstructorParameters( constructor, parameters );
		assertCorrectNumberOfViolations( constraintViolations, 3 );

		Set<String> actualParameterNames = getParameterNames( constraintViolations );
		Set<String> expectedParameterNames = asSet( "arg0", "arg1", "arg2" );

		assertEquals( actualParameterNames, expectedParameterNames );
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "d")
	public void testGetDefaultParameterNameProviderFromConfiguration() throws Exception {
		Method method = User.class.getMethod( "setNames", String.class, String.class );
		Constructor<User> constructor = User.class.getConstructor(
				String.class,
				String.class,
				Date.class
		);

		ParameterNameProvider defaultParameterNameProvider = TestUtil.getConfigurationUnderTest()
				.getDefaultParameterNameProvider();
		assertNotNull(
				defaultParameterNameProvider,
				"getDefaultParameterNameProvider() must not return null"
		);
		assertEquals(
				defaultParameterNameProvider.getParameterNames( constructor ),
				Arrays.asList( "arg0", "arg1", "arg2" ),
				"Wrong constructor parameter names returned by default provider"
		);
		assertEquals(
				defaultParameterNameProvider.getParameterNames( method ),
				Arrays.asList( "arg0", "arg1" ),
				"Wrong method parameter names returned by default provider"
		);
	}
}
