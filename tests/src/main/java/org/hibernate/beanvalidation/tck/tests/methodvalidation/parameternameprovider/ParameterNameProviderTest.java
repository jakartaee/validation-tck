/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.parameternameprovider;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.fail;

import java.lang.reflect.Method;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ExecutableValidator;

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
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ParameterNameProviderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ParameterNameProviderTest.class )
				.withClass( CustomParameterNameProvider.class )
				.withClass( BrokenCustomParameterNameProvider.class )
				.withClass( User.class )
				.build();
	}

	@Test(expectedExceptions = UnsupportedOperationException.class,
			expectedExceptionsMessageRegExp = "Exception in ParameterNameProvider")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_NAMINGPARAMETERS, id = "b")
	public void testExceptionInParameterNameProviderIsWrappedIntoValidationException()
			throws Throwable {
		Validator validator = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( new BrokenCustomParameterNameProvider() )
				.buildValidatorFactory()
				.getValidator();

		try {
			Object object = new User();
			Method method = User.class.getMethod( "setNames", String.class, String.class );
			Object[] parameters = new Object[] { null, null };

			validator.forExecutables().validateParameters( object, method, parameters );
			fail( "Expected exception wasn't thrown" );
		}
		catch ( ValidationException e ) {
			throw e.getCause();
		}
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "e")
	public void testGetParameterNameProviderFromValidatorFactory() {
		BrokenCustomParameterNameProvider parameterNameProvider = new BrokenCustomParameterNameProvider();

		ValidatorFactory validatorFactory = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( parameterNameProvider )
				.buildValidatorFactory();

		assertSame(
				validatorFactory.getParameterNameProvider(),
				parameterNameProvider,
				"getParameterNameProvider() should return the parameter name provider set via configuration"
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATORFACTORY, id = "g")
	public void testParameterNameProviderSetUsingContext() throws Exception {
		Object object = new User();
		Method method = User.class.getMethod( "setNames", String.class, String.class );
		Object[] parameters = new Object[] { null, null };

		ExecutableValidator executableValidator = TestUtil.getValidatorFactoryUnderTest()
				.usingContext()
				.parameterNameProvider( new CustomParameterNameProvider() )
				.getValidator()
				.forExecutables();
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameters
		);
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "setNames" )
							   .parameter( "param0", 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
							   .method( "setNames" )
							   .parameter( "param1", 1 )
						)
		);
	}
}
