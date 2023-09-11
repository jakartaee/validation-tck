/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

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
public class BootstrapConfigurationWithoutValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapConfigurationWithoutValidationXmlTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "f")
	public void testGetBootstrapConfigurationNoValidationXml() {
		BootstrapConfiguration bootstrapConfiguration = TestUtil.getConfigurationUnderTest()
				.getBootstrapConfiguration();

		assertNotNull( bootstrapConfiguration );

		assertNotNull( bootstrapConfiguration.getConstraintMappingResourcePaths() );
		assertTrue( bootstrapConfiguration.getConstraintMappingResourcePaths().isEmpty() );

		assertNull( bootstrapConfiguration.getConstraintValidatorFactoryClassName() );
		assertNull( bootstrapConfiguration.getDefaultProviderClassName() );
		assertNull( bootstrapConfiguration.getMessageInterpolatorClassName() );
		assertNull( bootstrapConfiguration.getParameterNameProviderClassName() );

		assertNotNull( bootstrapConfiguration.getDefaultValidatedExecutableTypes() );
		assertEquals(
				bootstrapConfiguration.getDefaultValidatedExecutableTypes(),
				EnumSet.of( ExecutableType.CONSTRUCTORS, ExecutableType.NON_GETTER_METHODS )
		);

		assertNotNull( bootstrapConfiguration.getProperties() );
		assertTrue( bootstrapConfiguration.getProperties().isEmpty() );

		assertNull( bootstrapConfiguration.getTraversableResolverClassName() );
	}
}
