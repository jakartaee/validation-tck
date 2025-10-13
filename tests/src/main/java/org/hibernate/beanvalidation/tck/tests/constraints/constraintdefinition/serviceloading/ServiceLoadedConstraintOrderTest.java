/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

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
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.testng.annotations.Test;

/**
 * @author Marko Bekhta
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@IntegrationTest
public class ServiceLoadedConstraintOrderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ServiceLoadedConstraintOrderTest.class )
				.withValidationXml( "loader-validation.xml" )
				.withResource( "constraints-definition.xml", "META-INF/constraints-definition.xml", true )
				.withResource(
						"jakarta.validation.ConstraintValidator-order",
						"META-INF/services/jakarta.validation.ConstraintValidator",
						true
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "d")
	public void testConstraintValidatorLoadedOrderBuiltInConstraint() {
		assertThatThrownBy( () -> {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new BuiltInOrderBean() );
		} ).isInstanceOf( ValidationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "d")
	public void testConstraintValidatorLoadedOrderAnnotation() {
		assertThatThrownBy( () -> {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new OrderAnnotationBean( "foo" ) );
		} ).isInstanceOf( ValidationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "d")
	public void testConstraintValidatorLoadedOrderXml() {
		assertThatThrownBy( () -> {
			Validator validator = TestUtil.getValidatorUnderTest();
			validator.validate( new OrderXmlBean( "foo" ) );
		} ).isInstanceOf( ValidationException.class );

	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_REGISTER_VALIDATORS, id = "e")
	public void testConstraintValidatorLoadedOrderXmlOverride() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<OrderXmlOverrideBean>> constraintViolations = validator.validate( new OrderXmlOverrideBean( "foo" ) );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( OrderXmlOverrideConstraint.class )
		);

		constraintViolations = validator.validate( new OrderXmlOverrideBean( "bar" ) );
		assertNoViolations( constraintViolations );
	}

}
