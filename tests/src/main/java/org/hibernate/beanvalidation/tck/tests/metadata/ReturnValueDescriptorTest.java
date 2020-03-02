/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.assertConstraintDescriptors;
import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.getContainerElementDescriptor;
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
import jakarta.validation.metadata.ReturnValueDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.StrictCustomerServiceChecks;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ReturnValueDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ReturnValueDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForMethod() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedMethod().getReturnValueDescriptor();
		assertEquals( descriptor.getElementClass(), int.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForVoidMethod() {
		ReturnValueDescriptor descriptor = Executables.parameterConstrainedMethod().getReturnValueDescriptor();
		assertEquals( descriptor.getElementClass(), void.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForConstructor() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedConstructor().getReturnValueDescriptor();
		assertEquals( descriptor.getElementClass(), CustomerService.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForMethodReturnValue() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getReturnValueDescriptor();
		assertFalse( descriptor.isCascaded(), "Should not be cascaded" );

		descriptor = Executables.cascadedReturnValueMethod().getReturnValueDescriptor();
		assertTrue( descriptor.isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForConstructorReturnValue() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getReturnValueDescriptor();
		assertFalse( descriptor.isCascaded(), "Should not be cascaded" );

		descriptor = Executables.cascadedReturnValueConstructor().getReturnValueDescriptor();
		assertTrue( descriptor.isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForVoidMethod() {
		ReturnValueDescriptor descriptor = Executables.parameterConstrainedMethod().getReturnValueDescriptor();
		assertFalse( descriptor.isCascaded() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForConstructorReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.constructorWithGroupConversionOnReturnValue()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerServiceChecks.class ) ) {
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForVoidMethod() {
		ReturnValueDescriptor descriptor = Executables.parameterConstrainedMethod().getReturnValueDescriptor();
		assertTrue( descriptor.getGroupConversions().isEmpty() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForMethodReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.methodWithGroupConversionOnReturnValue()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerServiceChecks.class ) ) {
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
	public void testGetGroupConversionsReturnsEmptySetForConstructorReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.cascadedReturnValueMethod()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	public void testGetGroupConversionsReturnsEmptySetForMethodReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.cascadedReturnValueConstructor()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "e")
	public void testGetContainerElementMetaDataForReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.returnValueWithCascadedContainerElements().getReturnValueDescriptor();

		Set<ContainerElementTypeDescriptor> containerElementTypes = returnValueDescriptor.getConstrainedContainerElementTypes();

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
