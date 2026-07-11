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

import javax.naming.InitialContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class JndiRetrievalTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( JndiRetrievalTest.class )
				.withClass( ConstantMessageInterpolator.class )
				.withClass( Foo.class )
				.withValidationXml( "test-validation.xml" )
				.withBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAKARTAEE, id = "a")
	void testDefaultValidatorFactoryCanBeRetrievedFromJndi() throws Exception {
		ValidatorFactory validatorFactory = InitialContext.doLookup( "java:comp/ValidatorFactory" );
		Assertions.assertThat( validatorFactory ).as( "Default validator factory should be bound to JNDI tree." ).isNotNull();
		Assertions.assertThat( validatorFactory.getMessageInterpolator() instanceof ConstantMessageInterpolator ).as( "Default validator factory bound to JNDI should be configured based on META-INF/validation.xml." ).isTrue();

		Set<ConstraintViolation<Foo>> violations = validatorFactory.getValidator()
				.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAKARTAEE, id = "a")
	void testDefaultValidatorCanBeRetrievedFromJndi() throws Exception {
		Validator validator = InitialContext.doLookup( "java:comp/Validator" );
		Assertions.assertThat( validator ).as( "Default validator should be bound to JNDI tree." ).isNotNull();

		Set<ConstraintViolation<Foo>> violations = validator.validate( new Foo() );

		//expecting message from interpolator configured in META-INF/validation.xml
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ).withMessage( "Invalid constraint" )
		);
	}
}
