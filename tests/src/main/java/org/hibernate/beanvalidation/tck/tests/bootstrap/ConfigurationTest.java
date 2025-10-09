/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ConfigurationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ConfigurationTest.class )
				.withClass( User.class )
				.withClass( Address.class )
				.withResource( "user-constraints-ConfigurationTest.xml" )
				.withResource( "address-constraints-ConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "l")
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
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "i")
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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" ),
				pathWith()
						.property( "address" )
						.property( "street" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "j")
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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" )
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
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" ),
				pathWith()
						.property( "address" )
						.property( "street" )
		);

		//re-reads both streams
		validator2 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator2.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" ),
				pathWith()
						.property( "address" )
						.property( "street" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "k")
	public void testSeveralFactoriesCanBeBuildFromOneConfiguration() {
		Configuration<?> configuration = TestUtil.getConfigurationUnderTest();
		Validator validator1 = configuration.buildValidatorFactory().getValidator();

		Set<ConstraintViolation<User>> constraintViolations = validator1.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" )
		);

		//add a mapping and get another validator
		configuration.addMapping(
				ConfigurationTest.class.getResourceAsStream(
						"user-constraints-ConfigurationTest.xml"
				)
		);
		Validator validator2 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator2.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" )
		);

		//the mapping shouldn't alter the previously created validator
		constraintViolations = validator1.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" )
		);

		//add a mapping and get another validator
		configuration.addMapping(
				ConfigurationTest.class.getResourceAsStream(
						"address-constraints-ConfigurationTest.xml"
				)
		);
		Validator validator3 = configuration.buildValidatorFactory().getValidator();

		constraintViolations = validator3.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" ),
				pathWith()
						.property( "address" )
						.property( "street" )
		);

		//the mapping shouldn't alter the previously created validators
		constraintViolations = validator1.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" )
		);

		constraintViolations = validator2.validate( new User() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "firstName" ),
				pathWith()
						.property( "lastName" )
		);
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
