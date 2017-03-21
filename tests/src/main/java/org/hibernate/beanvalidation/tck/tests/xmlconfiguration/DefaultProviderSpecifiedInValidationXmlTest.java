/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;


import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.ValidationProviderResolver;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.common.TCKValidationProvider;
import org.hibernate.beanvalidation.tck.common.TCKValidatorConfiguration;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class DefaultProviderSpecifiedInValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( DefaultProviderSpecifiedInValidationXmlTest.class )
				.withClasses(
						TCKValidationProvider.class,
						TCKValidatorConfiguration.class
				)
				.withValidationXml( "validation-DefaultProviderSpecifiedInValidationXmlTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "e"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "s")
	})
	public void testProviderSpecifiedInValidationXml() {
		ValidationProviderResolver resolver = new ValidationProviderResolver() {

			@Override
			public List<ValidationProvider<?>> getValidationProviders() {
				List<ValidationProvider<?>> list = new ArrayList<ValidationProvider<?>>();
				list.add( TestUtil.getValidationProviderUnderTest() );
				list.add( new TCKValidationProvider() );
				return list;
			}
		};

		Configuration<?> configuration = Validation
				.byDefaultProvider()
				.providerResolver( resolver )
				.configure();
		ValidatorFactory factory = configuration.buildValidatorFactory();
		assertTrue( factory instanceof TCKValidationProvider.DummyValidatorFactory );
	}
}
