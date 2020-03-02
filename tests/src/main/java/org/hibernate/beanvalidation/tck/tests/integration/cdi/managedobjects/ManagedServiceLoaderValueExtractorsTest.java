/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.managedobjects;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.testng.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for dependency injection into value extractors loaded by the service loader.
 * All test objects rely on a {@link Greeter} object to be
 * injected which is then used to generate the node name.
 *
 * @author Guillaume Smet
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ManagedServiceLoaderValueExtractorsTest extends AbstractTCKTest {

	@Inject
	private Validator defaultValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ManagedServiceLoaderValueExtractorsTest.class )
				.withEmptyBeansXml()
				.withResource(
						"jakarta.validation.valueextraction.ValueExtractor",
						"META-INF/services/jakarta.validation.valueextraction.ValueExtractor",
						true
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION_CUSTOMCONFIGURATION, id = "b")
	public void testServiceLoaderValueExtractorsAreSubjectToDependencyInjection() {
		assertNotNull( defaultValidator );

		Set<ConstraintViolation<Foo>> violations = defaultValidator.validate( Foo.invalid() );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "property" )
						.containerElement( Greeter.MESSAGE, true, null, null, Map.class, 0 ),
				pathWith()
						.property( "property" )
						.containerElement( Greeter.MESSAGE, true, null, null, Map.class, 1 )
		);
	}

	private static class Foo {

		private final Map<@NotNull String, @NotNull String> property = new HashMap<>();

		private static Foo invalid() {
			Foo foo = new Foo();
			foo.property.put( null, null );
			return foo;
		}
	}
}
