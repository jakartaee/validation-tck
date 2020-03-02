/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap.customprovider;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.validation.Configuration;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class BootstrapCustomProviderDefinedInServiceFileTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapCustomProviderDefinedInServiceFileTest.class )
				.withClasses( TCKValidatorConfiguration.class, TCKValidationProvider.class )
				.withResource(
						"jakarta.validation.spi.ValidationProvider",
						"META-INF/services/jakarta.validation.spi.ValidationProvider",
						true
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "n")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_PROVIDER, id = "a")
	public void testGetFactoryByProviderSpecifiedProgrammatically() {
		TCKValidatorConfiguration configuration = Validation.byProvider( TCKValidationProvider.class ).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertNotNull( factory );
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "n")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_RESOLVER, id = "a")
	public void testProviderResolverReturnsListOfAvailableProviders() {

		// really an indirect test since there is no way to get hold of the default provider resolver.
		// we just confirm that the provider under test and the TCKValidationProvider are loadable.

		TCKValidatorConfiguration configuration = Validation.byProvider( TCKValidationProvider.class ).configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertNotNull( factory );

		@SuppressWarnings("unchecked")
		Configuration<?> config = Validation.byProvider( TestUtil.getValidationProviderUnderTest().getClass() )
				.configure();
		factory = config.buildValidatorFactory();
		assertNotNull( factory );
	}
}
