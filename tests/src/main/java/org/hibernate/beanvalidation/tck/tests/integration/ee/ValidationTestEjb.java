/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import org.assertj.core.api.Assertions;

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
		Assertions.assertThat( defaultValidatorFactory ).as( "Default validator factory should be injectable." ).isNotNull();
		Assertions.assertThat(
				defaultValidatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator
		).as( "Injected default validator factory should be configured based on META-INF/validation.xml." ).isTrue();

		Set<ConstraintViolation<Foo>> violations = defaultValidatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	public void assertDefaultValidatorGetsInjected() {
		Assertions.assertThat( defaultValidator ).as( "Default validator should be injectable." ).isNotNull();

		Set<ConstraintViolation<Foo>> violations = defaultValidator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}
}
