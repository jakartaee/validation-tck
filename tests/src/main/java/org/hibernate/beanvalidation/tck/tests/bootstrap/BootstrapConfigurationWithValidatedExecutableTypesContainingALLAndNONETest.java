/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.EnumSet;

import jakarta.validation.BootstrapConfiguration;
import jakarta.validation.executable.ExecutableType;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class BootstrapConfigurationWithValidatedExecutableTypesContainingALLAndNONETest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapConfigurationWithValidatedExecutableTypesContainingALLAndNONETest.class )
				.withValidationXml(
						"validation-BootstrapConfigurationWithValidatedExecutableTypesContainingALLAndNONETest.xml"
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "q")
	public void testGetDefaultValidatedExecutableTypesReturnsSetWithAllOptionsIfALLAndNONEAreContained() {
		BootstrapConfiguration bootstrapConfiguration = TestUtil.getConfigurationUnderTest()
				.getBootstrapConfiguration();

		assertNotNull( bootstrapConfiguration );
		assertEquals(
				bootstrapConfiguration.getDefaultValidatedExecutableTypes(),
				EnumSet.of(
						ExecutableType.CONSTRUCTORS,
						ExecutableType.GETTER_METHODS,
						ExecutableType.NON_GETTER_METHODS
				)
		);
	}
}
