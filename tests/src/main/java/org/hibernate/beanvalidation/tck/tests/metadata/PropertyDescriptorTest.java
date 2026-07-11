/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.assertConstraintDescriptors;
import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.getContainerElementDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictCustomerChecks;
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
		assertThat( descriptor.getElementClass() ).as( "Wrong element class" ).isEqualTo( Integer.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsNotCascaded() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Order.class, "orderNumber" );
		assertThat( descriptor.isCascaded() ).as( "Should not be cascaded" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascaded() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "orderList" );
		assertThat( descriptor.isCascaded() ).as( "Should be cascaded" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PROPERTYDESCRIPTOR, id = "a")
	public void testPropertyName() {
		String propertyName = "orderList";
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, propertyName );
		assertThat( descriptor.getPropertyName() ).as( "Wrong property name" ).isEqualTo( propertyName );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForField() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "orderList" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertThat( groupConversions.size() ).isEqualTo( 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerChecks.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( StrictChecks.class );
			}
			else {
				Assertions.fail(
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

		assertThat( groupConversions.size() ).isEqualTo( 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerChecks.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( StrictChecks.class );
			}
			else {
				Assertions.fail(
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

		assertThat( groupConversions ).isNotNull();
		assertThat( groupConversions.isEmpty() ).isTrue();
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

		assertThat( containerElementTypes.size() ).isEqualTo( 2 );

		ContainerElementTypeDescriptor productType = getContainerElementDescriptor( containerElementTypes, Map.class, 0 );
		assertThat( productType.getElementClass() ).isEqualTo( ProductType.class );
		assertConstraintDescriptors( productType.getConstraintDescriptors(), NotNull.class );
		assertThat( productType.getConstrainedContainerElementTypes().size() ).isEqualTo( 0 );
		assertThat( productType.isCascaded() ).isTrue();
		assertThat( productType.getGroupConversions().size() ).isEqualTo( 2 );
		for ( GroupConversionDescriptor groupConversionDescriptor : productType.getGroupConversions() ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( ComplexChecks.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( ComplexProductTypeChecks.class );
			}
			else {
				Assertions.fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName() ) );
			}
		}

		ContainerElementTypeDescriptor orderLineList = getContainerElementDescriptor( containerElementTypes, Map.class, 1 );
		assertThat( orderLineList.getElementClass() ).isEqualTo( List.class );
		assertConstraintDescriptors( orderLineList.getConstraintDescriptors(), Size.class );
		assertThat( orderLineList.isCascaded() ).isFalse();
		assertThat( orderLineList.getGroupConversions().size() ).isEqualTo( 0 );
		assertThat( orderLineList.getConstrainedContainerElementTypes().size() ).isEqualTo( 1 );

		ContainerElementTypeDescriptor orderLine = getContainerElementDescriptor( orderLineList.getConstrainedContainerElementTypes(), List.class, 0 );
		assertThat( orderLine.getElementClass() ).isEqualTo( ProductOrderLine.class );
		assertConstraintDescriptors( orderLine.getConstraintDescriptors(), NotNull.class );
		assertThat( orderLine.getConstrainedContainerElementTypes().size() ).isEqualTo( 0 );
		assertThat( orderLine.isCascaded() ).isFalse();
	}
}
