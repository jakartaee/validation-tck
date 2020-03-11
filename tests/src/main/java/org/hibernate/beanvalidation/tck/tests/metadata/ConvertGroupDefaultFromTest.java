/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstructorDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getMethodDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.testng.Assert.assertEquals;

import java.util.Optional;
import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.ParameterDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ConvertGroupDefaultFromTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ConvertGroupDefaultFromTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	public void testConvertGroupDefaultFromForProperty() {
		PropertyDescriptor descriptor = getPropertyDescriptor( BeanHolder.class, "property" );

		Set<GroupConversionDescriptor> groupConversionDescriptors = descriptor.getGroupConversions();

		GroupConversionDescriptor groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, Default.class );
		assertEquals( groupConversionDescriptor.getTo(), ComplexChecks.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	public void testConvertGroupDefaultFromForConstructorParameter() {
		ParameterDescriptor descriptor = getConstructorDescriptor( BeanService.class, Bean.class )
				.getParameterDescriptors().iterator().next();

		Set<GroupConversionDescriptor> groupConversionDescriptors = descriptor.getGroupConversions();

		GroupConversionDescriptor groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, Default.class );
		assertEquals( groupConversionDescriptor.getTo(), ComplexChecks.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	public void testConvertGroupDefaultFromForMethodParameter() {
		ParameterDescriptor descriptor = getMethodDescriptor( BeanService.class, "addBean", Bean.class )
				.getParameterDescriptors().iterator().next();

		Set<GroupConversionDescriptor> groupConversionDescriptors = descriptor.getGroupConversions();

		GroupConversionDescriptor groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, Default.class );
		assertEquals( groupConversionDescriptor.getTo(), ComplexChecks.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "d")
	public void testConvertGroupDefaultFromForMethodReturnValue() {
		ReturnValueDescriptor descriptor = getMethodDescriptor( BeanService.class, "getBean" )
				.getReturnValueDescriptor();

		Set<GroupConversionDescriptor> groupConversionDescriptors = descriptor.getGroupConversions();

		GroupConversionDescriptor groupConversionDescriptor = getGroupConversionDescriptorByFrom( groupConversionDescriptors, Default.class );
		assertEquals( groupConversionDescriptor.getTo(), ComplexChecks.class );
	}

	private GroupConversionDescriptor getGroupConversionDescriptorByFrom(Set<GroupConversionDescriptor> groupConversionDescriptors, Class<?> from) {
		Optional<GroupConversionDescriptor> groupConversionDescriptor = groupConversionDescriptors.stream()
				.filter( gcd -> from.equals( gcd.getFrom() ) )
				.findAny();

		return groupConversionDescriptor
				.orElseThrow( () -> new IllegalStateException(
						String.format( "Unable to find group conversion with from %1$s in %2$s", from, groupConversionDescriptors ) ) );
	}

	private static class BeanHolder {

		@Valid
		@ConvertGroup(to = ComplexChecks.class)
		private String property;
	}

	@SuppressWarnings("unused")
	private static class BeanService {

		public BeanService(@Valid @ConvertGroup(to = ComplexChecks.class) Bean bean) {
		}

		@Valid
		@ConvertGroup(to = ComplexChecks.class)
		public Bean getBean() {
			return null;
		}

		public void addBean(@Valid @ConvertGroup(to = ComplexChecks.class) Bean bean) {
		}
	}

	private static class Bean {
	}
}
