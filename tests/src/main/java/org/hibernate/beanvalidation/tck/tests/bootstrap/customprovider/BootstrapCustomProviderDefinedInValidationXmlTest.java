/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap.customprovider;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class BootstrapCustomProviderDefinedInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapCustomProviderDefinedInValidationXmlTest.class )
				.withClasses( TCKValidatorConfiguration.class, TCKValidationProvider.class )
				.withValidationXml( "validation-BootstrapCustomProviderDefinedInValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATIONPROVIDER_PROVIDER, id = "a")
	public void testGetFactoryByProviderSpecifiedInValidationXml() {

		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			@Override
			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( TestUtil.getValidationProviderUnderTest() );
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		ValidatorFactory factory = Validation.byDefaultProvider()
				.providerResolver( resolver )
				.configure()
				.buildValidatorFactory();
		assertNotNull( factory );
		assertTrue(
				factory instanceof TCKValidationProvider.DummyValidatorFactory,
				"Since TCKValidationProvider is configured in validation.xml it should be the bootstrapped provider " +
						"even though the provider under test is first in the list of providers in the resolver."
		);
	}
}
