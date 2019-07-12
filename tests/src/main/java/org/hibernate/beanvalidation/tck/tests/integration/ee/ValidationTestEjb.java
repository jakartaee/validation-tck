/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

/**
 * A test EJB which retrieves validator and validator factory via
 * {@code @Resource} injection.
 *
 * @author Gunnar Morling
 */
@Stateless
public class ValidationTestEjb {

	@Resource
	public ValidatorFactory defaultValidatorFactory;

	@Resource
	public Validator defaultValidator;

	public void assertDefaultValidatorFactoryGetsInjected() {
		assertNotNull( defaultValidatorFactory, "Default validator factory should be injectable." );
		assertTrue(
				defaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator,
				"Injected default validator factory should be configured based on META-INF/validation.xml."
		);

		Set<ConstraintViolation<Foo>> violations = defaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	public void assertDefaultValidatorGetsInjected() {
		assertNotNull( defaultValidator, "Default validator should be injectable." );

		Set<ConstraintViolation<Foo>> violations = defaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}
}
