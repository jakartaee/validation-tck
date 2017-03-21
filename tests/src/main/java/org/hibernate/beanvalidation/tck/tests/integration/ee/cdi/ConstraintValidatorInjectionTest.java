/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.ee.cdi;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.testng.Assert.assertNotNull;

import java.util.Set;

import javax.naming.InitialContext;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ConstraintValidatorInjectionTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ConstraintValidatorInjectionTest.class )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_JAVAEE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_CDI, id = "a")
	public void testJndiBoundValidatorFactoryIsCdiEnabled() throws Exception {
		ValidatorFactory validatorFactory = InitialContext.doLookup( "java:comp/ValidatorFactory" );
		assertNotNull(
				validatorFactory,
				"Default validator factory should be bound to JNDI tree."
		);

		Set<ConstraintViolation<Foo>> violations = validatorFactory.getValidator().validate( new Foo() );

		assertCorrectConstraintViolationMessages( violations, "Hello, bar!", "Good morning, qux!" );
	}

	private static class Foo {
		@GreetingConstraint(name = "bar")
		public String bar;

		@GreetingConstraint(name = "qux")
		public Integer qux;
	}
}
