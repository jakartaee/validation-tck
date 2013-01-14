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

import java.lang.reflect.Method;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ParameterNameProviderTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ParameterNameProviderTest.class )
				.withClass( CustomParameterNameProvider.class )
				.withClass( User.class )
				.build();
	}

	@Test(expectedExceptions = UnsupportedOperationException.class,
			expectedExceptionsMessageRegExp = "Exception in ParameterNameProvider")
	@SpecAssertion(section = "4.5.2.2", id = "b")
	public void testExceptionInParameterNameProviderIsWrappedIntoValidationException()
			throws Throwable {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( new CustomParameterNameProvider() )
				.buildValidatorFactory()
				.getValidator();

		try {
			Object object = new User();
			Method method = User.class.getMethod( "setNames", String.class, String.class );
			Object[] parameters = new Object[] { null, null };

			validator.forMethods().validateParameters( object, method, parameters );
			fail( "Expected exception wasn't thrown" );
		}
		catch ( ValidationException e ) {
			throw e.getCause();
		}
	}

	@Test
	@SpecAssertion(section = "5.5.2", id = "b")
	public void testGetParameterNameProviderFromValidatorFactory() {
		CustomParameterNameProvider parameterNameProvider = new CustomParameterNameProvider();

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( parameterNameProvider )
				.buildValidatorFactory();

		assertSame(
				validatorFactory.getParameterNameProvider(),
				parameterNameProvider,
				"getParameterNameProvider() should return the parameter name provider set via context"
		);
	}
}
