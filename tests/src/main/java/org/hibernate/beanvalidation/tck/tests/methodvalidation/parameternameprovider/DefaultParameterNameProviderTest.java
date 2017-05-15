/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.parameternameprovider;

import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getParameterNames;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ParameterNameProvider;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class DefaultParameterNameProviderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DefaultParameterNameProviderTest.class )
				.withClasses(
						BrokenCustomParameterNameProvider.class,
						User.class
				).build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_NAMINGPARAMETERS, id = "a")
	public void testDefaultParameterProviderForMethod() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setNames", String.class, String.class );
		Object[] parameters = new Object[] { null, null };

		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator()
				.validateParameters( object, method, parameters );
		assertNumberOfViolations( constraintViolations, 2 );

		Set<String> actualParameterNames = getParameterNames( constraintViolations );
		Set<String> expectedParameterNames = asSet( "firstName", "lastName" );

		assertEquals( actualParameterNames, expectedParameterNames );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_NAMINGPARAMETERS, id = "a")
	public void testDefaultParameterProviderForConstructor() throws Exception {
		Constructor<User> constructor = User.class.getConstructor(
				String.class,
				String.class,
				Date.class
		);
		Object[] parameters = new Object[] { null, null, null };

		Set<ConstraintViolation<User>> constraintViolations = getExecutableValidator()
				.validateConstructorParameters( constructor, parameters );
		assertNumberOfViolations( constraintViolations, 3 );

		Set<String> actualParameterNames = getParameterNames( constraintViolations );
		Set<String> expectedParameterNames = asSet( "firstName", "lastName", "dateOfBirth" );

		assertEquals( actualParameterNames, expectedParameterNames );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "d")
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
				Arrays.asList( "firstName", "lastName", "dateOfBirth" ),
				"Wrong constructor parameter names returned by default provider"
		);
		assertEquals(
				defaultParameterNameProvider.getParameterNames( method ),
				Arrays.asList( "firstName", "lastName" ),
				"Wrong method parameter names returned by default provider"
		);
	}
}
