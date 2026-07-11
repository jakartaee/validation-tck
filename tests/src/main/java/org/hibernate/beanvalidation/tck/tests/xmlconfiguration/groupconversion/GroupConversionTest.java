/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.groupconversion;

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
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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
		assertThat( methodDescriptor ).as( "the specified method should be configured in xml" ).isNotNull();

		ReturnValueDescriptor returnValueDescriptor = methodDescriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 2 ).isTrue();

		List<ParameterDescriptor> parameterDescriptors = methodDescriptor.getParameterDescriptors();
		assertThat( parameterDescriptors.size() == 1 ).isTrue();

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		groupConversionDescriptors = parameterDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 1 ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	public void testGroupConversionsAppliedOnConstructor() throws Exception {
		ConstructorDescriptor constructorDescriptor = TestUtil.getConstructorDescriptor(
				Groups.class
		);
		assertThat( constructorDescriptor ).as( "the specified constructor should be configured in xml" ).isNotNull();
		ReturnValueDescriptor returnValueDescriptor = constructorDescriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 1 ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	public void testGroupConversionsAppliedOnField() throws Exception {
		PropertyDescriptor propertyDescriptor = TestUtil.getPropertyDescriptor(
				Groups.class, "foo"
		);
		assertThat( propertyDescriptor ).as( "the specified property should be configured in xml" ).isNotNull();

		Set<GroupConversionDescriptor> groupConversionDescriptors = propertyDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 2 ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_GROUPCONVERSIONS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	public void testGroupConversionsAppliedOnGetter() throws Exception {
		PropertyDescriptor propertyDescriptor = TestUtil.getPropertyDescriptor(
				Groups.class, "snafu"
		);
		assertThat( propertyDescriptor ).as( "the specified property should be configured in xml" ).isNotNull();

		Set<GroupConversionDescriptor> groupConversionDescriptors = propertyDescriptor.getGroupConversions();
		assertThat( groupConversionDescriptors.size() == 3 ).isTrue();

		GroupConversionDescriptor groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, Default.class );
		assertThat( groupConversionDescriptor.getTo() ).isEqualTo( ConvertA.class  );

		groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, ConvertA.class );
		assertThat( groupConversionDescriptor.getTo() ).isEqualTo( ConvertB.class  );

		groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, ConvertB.class );
		assertThat( groupConversionDescriptor.getTo() ).isEqualTo( ConvertC.class  );
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
