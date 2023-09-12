/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.assertConstraintDescriptors;
import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.getContainerElementDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictCustomerChecks;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class PropertyDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( PropertyDescriptorTest.class )
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "e")
	public void testGetContainerElementMetaData() {
		PropertyDescriptor descriptor = getPropertyDescriptor( ComplexOrder.class, "orderLines" );

		Set<ContainerElementTypeDescriptor> containerElementTypes = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypes.size(), 2 );

		ContainerElementTypeDescriptor productType = getContainerElementDescriptor( containerElementTypes, Map.class, 0 );
		assertEquals( productType.getElementClass(), ProductType.class );
		assertConstraintDescriptors( productType.getConstraintDescriptors(), NotNull.class );
		assertEquals( productType.getConstrainedContainerElementTypes().size(), 0 );
		assertTrue( productType.isCascaded() );
		assertEquals( productType.getGroupConversions().size(), 2 );
		for ( GroupConversionDescriptor groupConversionDescriptor : productType.getGroupConversions() ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( ComplexChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), ComplexProductTypeChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName() ) );
			}
		}

		ContainerElementTypeDescriptor orderLineList = getContainerElementDescriptor( containerElementTypes, Map.class, 1 );
		assertEquals( orderLineList.getElementClass(), List.class );
		assertConstraintDescriptors( orderLineList.getConstraintDescriptors(), Size.class );
		assertFalse( orderLineList.isCascaded() );
		assertEquals( orderLineList.getGroupConversions().size(), 0 );
		assertEquals( orderLineList.getConstrainedContainerElementTypes().size(), 1 );

		ContainerElementTypeDescriptor orderLine = getContainerElementDescriptor( orderLineList.getConstrainedContainerElementTypes(), List.class, 0 );
		assertEquals( orderLine.getElementClass(), ProductOrderLine.class );
		assertConstraintDescriptors( orderLine.getConstraintDescriptors(), NotNull.class );
		assertEquals( orderLine.getConstrainedContainerElementTypes().size(), 0 );
		assertFalse( orderLine.isCascaded() );
	}
}
