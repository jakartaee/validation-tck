/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap.customprovider;

import jakarta.validation.Validation;
import jakarta.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.ExpectBootstrapFailure;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
@ExpectBootstrapFailure(ValidationException.class)
public class BootstrapNonAvailableValidationProviderTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapNonAvailableValidationProviderTest.class )
				.withValidationXml( "validation-BootstrapUnknownCustomProviderTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_VALIDATION, id = "f")
	@SpecAssertion(section = Sections.EXCEPTION, id = "a")
	public void testUnknownProviderConfiguredInValidationXml() {
		Validation.byDefaultProvider().configure().buildValidatorFactory();
	}
}
