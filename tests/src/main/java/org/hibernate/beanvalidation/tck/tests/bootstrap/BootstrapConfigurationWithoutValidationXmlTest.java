/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

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
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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

		assertThat( bootstrapConfiguration ).isNotNull();

		assertThat( bootstrapConfiguration.getConstraintMappingResourcePaths() ).isNotNull();
		assertThat( bootstrapConfiguration.getConstraintMappingResourcePaths().isEmpty() ).isTrue();

		assertThat( bootstrapConfiguration.getConstraintValidatorFactoryClassName() ).isNull();
		assertThat( bootstrapConfiguration.getDefaultProviderClassName() ).isNull();
		assertThat( bootstrapConfiguration.getMessageInterpolatorClassName() ).isNull();
		assertThat( bootstrapConfiguration.getParameterNameProviderClassName() ).isNull();

		assertThat( bootstrapConfiguration.getDefaultValidatedExecutableTypes() ).isNotNull();
		assertThat( bootstrapConfiguration.getDefaultValidatedExecutableTypes() ).isEqualTo( EnumSet.of( ExecutableType.CONSTRUCTORS, ExecutableType.NON_GETTER_METHODS ) );

		assertThat( bootstrapConfiguration.getProperties() ).isNotNull();
		assertThat( bootstrapConfiguration.getProperties().isEmpty() ).isTrue();

		assertThat( bootstrapConfiguration.getTraversableResolverClassName() ).isNull();
	}
}
