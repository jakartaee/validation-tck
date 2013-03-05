/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ConfigurationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ConfigurationTest.class )
				.withClass( User.class )
				.withClass( Address.class )
				.withResource( "user-constraints-ConfigurationTest.xml" )
				.withResource( "address-constraints-ConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "j")
	public void testProviderUnderTestDefinesSubInterfaceOfConfiguration() {
		boolean foundSubinterfaceOfConfiguration = false;
		Type[] types = TestUtil.getValidationProviderUnderTest().getClass().getGenericInterfaces();
		for ( Type type : types ) {
			if ( type instanceof ParameterizedType ) {
				ParameterizedType paramType = (ParameterizedType) type;
				Type[] typeArguments = paramType.getActualTypeArguments();
				for ( Type typeArgument : typeArguments ) {
					if ( typeArgument instanceof Class && Configuration.class.isAssignableFrom( (Class<?>) typeArgument ) ) {
						foundSubinterfaceOfConfiguration = true;
					}
				}
			}
		}
		assertTrue(
				foundSubinterfaceOfConfiguration,
				"Could not find subinterface of Configuration"
		);
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "g")
	public void testMappingsCanBeAddedViaConfiguration() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();

		configuration.addMapping(
				ConfigurationTest.class.getResourceAsStream(
						"user-constraints-ConfigurationTest.xml"
				)
		);
		configuration.addMapping(
				ConfigurationTest.class.getResourceAsStream(
						"address-constraints-ConfigurationTest.xml"
				)
		);

		Validator validator = configuration.buildValidatorFactory().getValidator();

		Set<ConstraintViolation<User>> constraintViolations = validator.validate( new User() );
		assertCorrectPropertyPaths(
				constraintViolations,
				"firstName",
				"lastName",
				"address.street"
		);
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "h")
	public void testNonResettableInputStreamCanBeAddedViaConfigurationAndFactoryCanBeCreatedSeveralTimes() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();

		configuration.addMapping(
				new NonResettableInputStream(
						ConfigurationTest.class.getResourceAsStream(
								"user-constraints-ConfigurationTest.xml"
						)
				)
		);

		Validator validator1 = configuration.buildValidatorFactory().getValidator();

		Set<ConstraintViolation<User>> constraintViolations = validator1.validate( new User() );
		assertCorrectPropertyPaths(
				constraintViolations,
				"firstName",
				"lastName"
		);

		configuration.addMapping(
				new NonResettableInputStream(
						ConfigurationTest.class.getResourceAsStream(
								"address-constraints-ConfigurationTest.xml"
						)
				)
		);

		//re-reads first stream
		Validator validator2 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator2.validate( new User() );
		assertCorrectPropertyPaths(
				constraintViolations,
				"firstName",
				"lastName",
				"address.street"
		);

		//re-reads both streams
		validator2 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator2.validate( new User() );
		assertCorrectPropertyPaths(
				constraintViolations,
				"firstName",
				"lastName",
				"address.street"
		);
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "i")
	public void testSeveralFactoriesCanBeBuildFromOneConfiguration() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		Validator validator1 = configuration.buildValidatorFactory().getValidator();

		Set<ConstraintViolation<User>> constraintViolations = validator1.validate( new User() );
		assertCorrectPropertyPaths( constraintViolations, "firstName" );

		//add a mapping and get another validator
		configuration.addMapping(
				ConfigurationTest.class.getResourceAsStream(
						"user-constraints-ConfigurationTest.xml"
				)
		);
		Validator validator2 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator2.validate( new User() );
		assertCorrectPropertyPaths( constraintViolations, "firstName", "lastName" );

		//the mapping shouldn't alter the previously created validator
		constraintViolations = validator1.validate( new User() );
		assertCorrectPropertyPaths( constraintViolations, "firstName" );

		//add a mapping and get another validator
		configuration.addMapping(
				ConfigurationTest.class.getResourceAsStream(
						"address-constraints-ConfigurationTest.xml"
				)
		);
		Validator validator3 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator3.validate( new User() );
		assertCorrectPropertyPaths(
				constraintViolations,
				"firstName",
				"lastName",
				"address.street"
		);

		//the mapping shouldn't alter the previously created validators
		constraintViolations = validator1.validate( new User() );
		assertCorrectPropertyPaths( constraintViolations, "firstName" );

		constraintViolations = validator2.validate( new User() );
		assertCorrectPropertyPaths( constraintViolations, "firstName", "lastName" );
	}

	/**
	 * An input stream which does not support the mark/reset contract.
	 *
	 * @author Gunnar Morling
	 */
	private static class NonResettableInputStream extends InputStream {

		private final InputStream delegate;

		public NonResettableInputStream(InputStream delegate) {
			this.delegate = delegate;
		}

		@Override
		public int available() throws IOException {
			return delegate.available();
		}

		@Override
		public void close() throws IOException {
			delegate.close();
		}

		@Override
		public synchronized void mark(int readlimit) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean markSupported() {
			return false;
		}

		@Override
		public int read() throws IOException {
			return 0;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return delegate.read( b, off, len );
		}

		@Override
		public int read(byte[] b) throws IOException {
			return delegate.read( b );
		}

		@Override
		public synchronized void reset() throws IOException {
			throw new UnsupportedOperationException();
		}

		@Override
		public long skip(long n) throws IOException {
			return delegate.skip( n );
		}
	}
}
