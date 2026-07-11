/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.CrossParameterDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class MethodValidationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( MethodValidationTest.class )
				.withValidationXml( "validation-MethodValidationTest.xml" )
				.withResource( "customer-repository-constraints-MethodValidationTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "c"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "f")
	})
	public void testXmlMethodConfigurationApplied() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor( CustomerRepository.class, "listCustomers" );
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();
		assertThat( descriptor.hasConstrainedReturnValue() ).isTrue();
		assertThat( descriptor.hasConstrainedParameters() ).isFalse();

		descriptor = TestUtil.getMethodDescriptor( CustomerRepository.class, "findCustomer", String.class );
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();
		assertThat( descriptor.hasConstrainedReturnValue() ).isTrue();
		assertThat( descriptor.hasConstrainedParameters() ).isTrue();

		descriptor = TestUtil.getMethodDescriptor( CustomerRepository.class, "isCustomer", String.class );
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();
		assertThat( descriptor.hasConstrainedReturnValue() ).isFalse();
		assertThat( descriptor.hasConstrainedParameters() ).isTrue();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "c")
	})
	public void testVarargsMethodParameter() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"addCustomers",
				Customer[].class
		);
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();
		assertThat( descriptor.hasConstrainedParameters() ).isTrue();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "c"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "g"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "k")
	})
	public void testMethodCrossParameterConstraint() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"notifyCustomer",
				Customer.class,
				String.class
		);
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();
		CrossParameterDescriptor crossParameterDescriptor = descriptor.getCrossParameterDescriptor();
		assertThat( crossParameterDescriptor.hasConstraints() ).isTrue();

		Set<ConstraintDescriptor<?>> constraintDescriptors = crossParameterDescriptor.getConstraintDescriptors();
		assertThat( constraintDescriptors.size() == 1 ).isTrue();

		ConstraintDescriptor<?> constraintDescriptor = constraintDescriptors.iterator().next();
		assertThat( constraintDescriptor.getAnnotation().annotationType() ).as( "Unexpected constraint type" ).isEqualTo( CrossRepositoryConstraint.class );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "a"),
			@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "h")
	})
	public void testConstraintOnMethodReturnValueAndParameter() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"notifyCustomer",
				Customer.class,
				String.class
		);
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		Set<ConstraintDescriptor<?>> constraintDescriptors = returnValueDescriptor.getConstraintDescriptors();
		assertThat( constraintDescriptors.size() == 1 ).isTrue();

		ConstraintDescriptor<?> constraintDescriptor = constraintDescriptors.iterator().next();
		assertThat( constraintDescriptor.getAnnotation().annotationType() ).as( "Unexpected constraint type" ).isEqualTo( NotNull.class );

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertThat( parameterDescriptors.size() == 2 ).isTrue();

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		constraintDescriptors = parameterDescriptor.getConstraintDescriptors();
		assertThat( constraintDescriptors.size() == 1 ).isTrue();

		constraintDescriptor = constraintDescriptors.iterator().next();
		assertThat( constraintDescriptor.getAnnotation().annotationType() ).as( "Unexpected constraint type" ).isEqualTo( NotNull.class );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "i")
	public void testCascadingOnReturnValueAndParameter() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"findByExample",
				Customer.class
		);
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertThat( returnValueDescriptor.isCascaded() ).as( "<valid/> is used to configure cascading" ).isTrue();

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertThat( parameterDescriptors.size() == 1 ).isTrue();

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		assertThat( parameterDescriptor.isCascaded() ).as( "<valid/> is used to configure cascading" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "j")
	public void testGroupConversionOnReturnValueAndParameter() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"findByExample",
				Customer.class
		);
		assertThat( descriptor ).as( "the specified method should be configured in xml" ).isNotNull();

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 1 ).isTrue();

		GroupConversionDescriptor groupConversionDescriptor = groupConversionDescriptors.iterator().next();
		assertThat( groupConversionDescriptor.getFrom() ).as( "Wrong from class for group conversion" ).isEqualTo( Default.class );

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertThat( parameterDescriptors.size() == 1 ).isTrue();

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		groupConversionDescriptors = parameterDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 1 ).isTrue();

		groupConversionDescriptor = groupConversionDescriptors.iterator().next();
		assertThat( groupConversionDescriptor.getFrom() ).as( "Wrong from class for group conversion" ).isEqualTo( Default.class );
	}
}
