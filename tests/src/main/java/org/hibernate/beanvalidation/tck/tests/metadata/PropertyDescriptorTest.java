/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import javax.validation.metadata.ContainerElementTypeDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.BasicChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictCustomerChecks;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class PropertyDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( PropertyDescriptorTest.class )
				.withClasses(
						Account.class,
						Order.class,
						Person.class,
						Customer.class,
						Severity.class,
						NotEmpty.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClass() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Order.class, "orderNumber" );
		assertEquals( descriptor.getElementClass(), Integer.class, "Wrong element class" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsNotCascaded() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Order.class, "orderNumber" );
		assertFalse( descriptor.isCascaded(), "Should not be cascaded" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascaded() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "orderList" );
		assertTrue( descriptor.isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PROPERTYDESCRIPTOR, id = "a")
	public void testPropertyName() {
		String propertyName = "orderList";
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, propertyName );
		assertEquals( descriptor.getPropertyName(), propertyName, "Wrong property name" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForField() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "orderList" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), StrictChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName()
						)
				);
			}
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b"),
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a"),
			@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	})
	public void testGetGroupConversionsForProperty() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "account" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), StrictChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName()
						)
				);
			}
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	public void testGetGroupConversionsReturnsEmptySet() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "firstName" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}

	@Test
	// FIXME: update spec assertions
	public void testGetContainerElementTypes() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Order.class, "orderLines" );

		List<ContainerElementTypeDescriptor> containerElementTypes = descriptor.getContainerElementTypes();

		ContainerElementTypeDescriptor productType = containerElementTypes.get( 0 );
		assertEquals( productType.getTypeArgumentIndex().intValue(), 0 );
		assertEquals( productType.getConstraintDescriptors().iterator().next().getAnnotation().annotationType(), NotNull.class );
		assertEquals( productType.getContainerElementTypes().size(), 0 );
		assertTrue( productType.isCascaded() );
		assertEquals( productType.getGroupConversions().size(), 2 );
		for ( GroupConversionDescriptor groupConversionDescriptor : productType.getGroupConversions() ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), Order.BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( Order.ComplexChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), Order.ComplexProductTypeChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName() ) );
			}
		}

		ContainerElementTypeDescriptor orderLineList = containerElementTypes.get( 1 );
		assertEquals( orderLineList.getTypeArgumentIndex().intValue(), 1 );
		assertEquals( orderLineList.getConstraintDescriptors().iterator().next().getAnnotation().annotationType(), Size.class );
		assertFalse( orderLineList.isCascaded() );
		assertEquals( orderLineList.getGroupConversions().size(), 0 );
		assertEquals( orderLineList.getContainerElementTypes().size(), 1 );

		ContainerElementTypeDescriptor orderLine = orderLineList.getContainerElementTypes().get( 0 );
		assertEquals( orderLine.getTypeArgumentIndex().intValue(), 0 );
		assertEquals( orderLine.getConstraintDescriptors().iterator().next().getAnnotation().annotationType(), NotNull.class );
		assertEquals( orderLine.getContainerElementTypes().size(), 0 );
		assertFalse( orderLine.isCascaded() );
	}
}
