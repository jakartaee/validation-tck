/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.parameternameprovider;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ParameterNameProvider;
import jakarta.validation.constraints.NotNull;

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
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
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
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "setNames" )
							   .parameter( "firstName", 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "setNames" )
							   .parameter( "lastName", 1 )
						)
		);
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
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .constructor( User.class )
							   .parameter( "firstName", 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .constructor( User.class )
							   .parameter( "lastName", 1 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .constructor( User.class )
							   .parameter( "dateOfBirth", 2 )
						)
		);
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
