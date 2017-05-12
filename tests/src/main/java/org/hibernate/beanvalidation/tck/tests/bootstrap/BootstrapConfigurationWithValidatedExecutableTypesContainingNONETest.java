/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.EnumSet;

import javax.validation.BootstrapConfiguration;
import javax.validation.executable.ExecutableType;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class BootstrapConfigurationWithValidatedExecutableTypesContainingNONETest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapConfigurationWithValidatedExecutableTypesContainingNONETest.class )
				.withValidationXml(
						"validation-BootstrapConfigurationWithValidatedExecutableTypesContainingNONETest.xml"
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "f")
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "m")
	public void testGetDefaultValidatedExecutableTypesReturnsEmptySetIfNONEIsContained() {
		BootstrapConfiguration bootstrapConfiguration = TestUtil.getConfigurationUnderTest()
				.getBootstrapConfiguration();

		assertNotNull( bootstrapConfiguration );

		assertEquals( bootstrapConfiguration.getDefaultValidatedExecutableTypes(), EnumSet.noneOf( ExecutableType.class ) );
	}
}
