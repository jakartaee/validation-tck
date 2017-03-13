/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import java.util.EnumSet;
import javax.validation.BootstrapConfiguration;
import javax.validation.executable.ExecutableType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class BootstrapConfigurationWithValidatedExecutableTypesContainingALLAndNONETest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( BootstrapConfigurationWithValidatedExecutableTypesContainingALLAndNONETest.class )
				.withValidationXml(
						"validation-BootstrapConfigurationWithValidatedExecutableTypesContainingALLAndNONETest.xml"
				)
				.build();
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "f")
	@SpecAssertion(section = "5.5.6", id = "l")
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
