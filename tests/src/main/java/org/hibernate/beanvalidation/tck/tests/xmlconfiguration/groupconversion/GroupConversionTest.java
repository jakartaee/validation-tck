/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.groupconversion;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupConversionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( GroupConversionTest.class )
				.withValidationXml( "validation-GroupConversionTest.xml" )
				.withResource( "GroupConversionTest.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	public void testGroupConversionsAppliedOnMethod() throws Exception {
		MethodDescriptor methodDescriptor = TestUtil.getMethodDescriptor(
				Groups.class,
				"convert",
				String.class
		);
		assertNotNull( methodDescriptor, "the specified method should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = methodDescriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 2 );

		List<ParameterDescriptor> parameterDescriptors = methodDescriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		groupConversionDescriptors = parameterDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	public void testGroupConversionsAppliedOnConstructor() throws Exception {
		ConstructorDescriptor constructorDescriptor = TestUtil.getConstructorDescriptor(
				Groups.class
		);
		assertNotNull( constructorDescriptor, "the specified constructor should be configured in xml" );
		ReturnValueDescriptor returnValueDescriptor = constructorDescriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	public void testGroupConversionsAppliedOnField() throws Exception {
		PropertyDescriptor propertyDescriptor = TestUtil.getPropertyDescriptor(
				Groups.class, "foo"
		);
		assertNotNull( propertyDescriptor, "the specified property should be configured in xml" );

		Set<GroupConversionDescriptor> groupConversionDescriptors = propertyDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 2 );
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	public void testGroupConversionsAppliedOnGetter() throws Exception {
		PropertyDescriptor propertyDescriptor = TestUtil.getPropertyDescriptor(
				Groups.class, "snafu"
		);
		assertNotNull( propertyDescriptor, "the specified property should be configured in xml" );

		Set<GroupConversionDescriptor> groupConversionDescriptors = propertyDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 3 );

		GroupConversionDescriptor groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, Default.class );
		assertEquals( groupConversionDescriptor.getTo(), ConvertA.class );

		groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, ConvertA.class );
		assertEquals( groupConversionDescriptor.getTo(), ConvertB.class );

		groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, ConvertB.class );
		assertEquals( groupConversionDescriptor.getTo(), ConvertC.class );
	}

	private GroupConversionDescriptor getGroupConversionDescriptorByFrom(Set<GroupConversionDescriptor> groupConversionDescriptors, Class<?> from) {
		Optional<GroupConversionDescriptor> groupConversionDescriptor = groupConversionDescriptors.stream()
				.filter( gcd -> from.equals( gcd.getFrom() ) )
				.findAny();

		return groupConversionDescriptor
				.orElseThrow( () -> new IllegalStateException(
						String.format( "Unable to find group conversion with from %1$s in %2$s", from, groupConversionDescriptors ) ) );
	}
}
