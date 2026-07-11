/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;

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
public class BootstrapConfigurationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( BootstrapConfigurationTest.class )
				.withValidationXml( "validation-BootstrapConfigurationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_CONFIGURATION, id = "f")
	public void testGetBootstrapConfiguration() {
		BootstrapConfiguration bootstrapConfiguration = TestUtil.getConfigurationUnderTest()
				.getBootstrapConfiguration();

		assertThat( bootstrapConfiguration ).isNotNull();

		assertThat( bootstrapConfiguration.getConstraintMappingResourcePaths() ).isNotNull();
		assertThat( bootstrapConfiguration.getConstraintMappingResourcePaths() ).isEqualTo( asSet( "mapping1", "mapping2" ) );

		assertThat( bootstrapConfiguration.getConstraintValidatorFactoryClassName() ).isEqualTo( "com.acme.ConstraintValidatorFactory" );
		assertThat( bootstrapConfiguration.getDefaultProviderClassName() ).isEqualTo( "com.acme.ValidationProvider" );
		assertThat( bootstrapConfiguration.getMessageInterpolatorClassName() ).isEqualTo( "com.acme.MessageInterpolator" );
		assertThat( bootstrapConfiguration.getParameterNameProviderClassName() ).isEqualTo( "com.acme.ParameterNameProvider" );

		assertThat( bootstrapConfiguration.getProperties() ).isNotNull();
		assertThat( bootstrapConfiguration.getProperties().size() ).isEqualTo( 2 );
		assertThat( bootstrapConfiguration.getProperties().get( "com.acme.Foo" ) ).isEqualTo( "Bar" );
		assertThat( bootstrapConfiguration.getProperties().get( "com.acme.Baz" ) ).isEqualTo( "Qux" );

		assertThat( bootstrapConfiguration.getTraversableResolverClassName() ).isEqualTo( "com.acme.TraversableResolver" );

		assertThat( bootstrapConfiguration.getDefaultValidatedExecutableTypes() ).isNotNull();
		assertThat( bootstrapConfiguration.getDefaultValidatedExecutableTypes() ).isEqualTo( EnumSet.of( ExecutableType.CONSTRUCTORS, ExecutableType.NON_GETTER_METHODS ) );
	}
}
