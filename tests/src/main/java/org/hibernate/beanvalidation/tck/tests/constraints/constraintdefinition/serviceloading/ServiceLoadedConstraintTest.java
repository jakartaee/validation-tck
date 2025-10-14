/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.List;
import java.util.Set;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;
import org.testng.annotations.Test;

/**
 * @author Marko Bekhta
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@IntegrationTest
public class ServiceLoadedConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ServiceLoadedConstraintTest.class )
				.withResource(
						"jakarta.validation.ConstraintValidator",
						"META-INF/services/jakarta.validation.ConstraintValidator",
						true
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "a")
	public void testConstraintValidatorLoadedCustomConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( new Author() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ServiceLoadedConstraint.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "a")
	public void testConstraintValidatorLoadedBuiltInConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<Author>> constraintViolations = validator.validate( new Author( List.of( new Book( "Book 1", 3 ), new Book( "Book 2", 100 ) ) ) );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ServiceLoadedConstraint.class )
						.withProperty( "name" ),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "books" )
								.containerElement( "<list element>", true, null, 1, List.class, 0 ) )
		);
	}

}
