/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.CrossParameterDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
		assertNotNull( descriptor, "the specified method should be configured in xml" );
		assertTrue( descriptor.hasConstrainedReturnValue() );
		assertFalse( descriptor.hasConstrainedParameters() );

		descriptor = TestUtil.getMethodDescriptor( CustomerRepository.class, "findCustomer", String.class );
		assertNotNull( descriptor, "the specified method should be configured in xml" );
		assertTrue( descriptor.hasConstrainedReturnValue() );
		assertTrue( descriptor.hasConstrainedParameters() );

		descriptor = TestUtil.getMethodDescriptor( CustomerRepository.class, "isCustomer", String.class );
		assertNotNull( descriptor, "the specified method should be configured in xml" );
		assertFalse( descriptor.hasConstrainedReturnValue() );
		assertTrue( descriptor.hasConstrainedParameters() );
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
		assertNotNull( descriptor, "the specified method should be configured in xml" );
		assertTrue( descriptor.hasConstrainedParameters() );
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
		assertNotNull( descriptor, "the specified method should be configured in xml" );
		CrossParameterDescriptor crossParameterDescriptor = descriptor.getCrossParameterDescriptor();
		assertTrue( crossParameterDescriptor.hasConstraints() );

		Set<ConstraintDescriptor<?>> constraintDescriptors = crossParameterDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		ConstraintDescriptor<?> constraintDescriptor = constraintDescriptors.iterator().next();
		assertEquals(
				constraintDescriptor.getAnnotation().annotationType(),
				CrossRepositoryConstraint.class,
				"Unexpected constraint type"
		);
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
		assertNotNull( descriptor, "the specified method should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		Set<ConstraintDescriptor<?>> constraintDescriptors = returnValueDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		ConstraintDescriptor<?> constraintDescriptor = constraintDescriptors.iterator().next();
		assertEquals(
				constraintDescriptor.getAnnotation().annotationType(),
				NotNull.class,
				"Unexpected constraint type"
		);

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 2 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		constraintDescriptors = parameterDescriptor.getConstraintDescriptors();
		assertTrue( constraintDescriptors.size() == 1 );

		constraintDescriptor = constraintDescriptors.iterator().next();
		assertEquals(
				constraintDescriptor.getAnnotation().annotationType(),
				NotNull.class,
				"Unexpected constraint type"
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "i")
	public void testCascadingOnReturnValueAndParameter() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"findByExample",
				Customer.class
		);
		assertNotNull( descriptor, "the specified method should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		assertTrue( returnValueDescriptor.isCascaded(), "<valid/> is used to configure cascading" );

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		assertTrue( parameterDescriptor.isCascaded(), "<valid/> is used to configure cascading" );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_METHODLEVELOVERRIDING, id = "j")
	public void testGroupConversionOnReturnValueAndParameter() throws Exception {
		MethodDescriptor descriptor = TestUtil.getMethodDescriptor(
				CustomerRepository.class,
				"findByExample",
				Customer.class
		);
		assertNotNull( descriptor, "the specified method should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = descriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );

		GroupConversionDescriptor groupConversionDescriptor = groupConversionDescriptors.iterator().next();
		assertEquals( groupConversionDescriptor.getFrom(), Default.class, "Wrong from class for group conversion" );

		List<ParameterDescriptor> parameterDescriptors = descriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		groupConversionDescriptors = parameterDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );

		groupConversionDescriptor = groupConversionDescriptors.iterator().next();
		assertEquals( groupConversionDescriptor.getFrom(), Default.class, "Wrong from class for group conversion" );
	}
}
