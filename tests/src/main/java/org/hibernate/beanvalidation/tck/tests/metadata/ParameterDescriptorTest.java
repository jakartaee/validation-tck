/**
 * Jakarta Validation TCK
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
import jakarta.validation.metadata.ParameterDescriptor;

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
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ParameterDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ParameterDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getElementClass(), String.class, "Wrong parameter class" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getElementClass(), String.class, "Wrong parameter class" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "a")
	public void testGetIndexForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getIndex(), 0, "Wrong parameter index" );
		assertEquals( parameters.get( 1 ).getIndex(), 1, "Wrong parameter index" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "a")
	public void testGetIndexForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getIndex(), 0, "Wrong parameter index" );
		assertEquals( parameters.get( 1 ).getIndex(), 1, "Wrong parameter index" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "b")
	public void testGetNameForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getName(), "firstName", "Wrong parameter name" );
		assertEquals( parameters.get( 1 ).getName(), "lastName", "Wrong parameter name" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "b")
	public void testGetNameForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertEquals( parameters.get( 0 ).getName(), "firstName", "Wrong parameter name" );
		assertEquals( parameters.get( 1 ).getName(), "lastName", "Wrong parameter name" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();
		assertFalse( parameters.get( 0 ).isCascaded(), "Should not be cascaded" );

		parameters = Executables.cascadedParameterMethod().getParameterDescriptors();
		assertTrue( parameters.get( 0 ).isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();
		assertFalse( parameters.get( 0 ).isCascaded(), "Should not be cascaded" );

		parameters = Executables.cascadedParameterConstructor().getParameterDescriptors();
		assertTrue( parameters.get( 0 ).isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForConstructorParameter() {
		List<ParameterDescriptor> parameters = Executables.constructorWithGroupConversionOnParameter()
				.getParameterDescriptors();
		Set<GroupConversionDescriptor> groupConversions = parameters.get( 1 ).getGroupConversions();

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
	public void testGetGroupConversionsForMethodParameter() {
		List<ParameterDescriptor> parameters = Executables.methodWithGroupConversionOnParameter()
				.getParameterDescriptors();
		Set<GroupConversionDescriptor> groupConversions = parameters.get( 0 ).getGroupConversions();

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
	public void testGetGroupConversionsReturnsEmptySetForConstructorParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors()
				.get( 0 );
		Set<GroupConversionDescriptor> groupConversions = parameterDescriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	public void testGetGroupConversionsReturnsEmptySetForMethodParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterConstrainedMethod()
				.getParameterDescriptors()
				.get( 0 );
		Set<GroupConversionDescriptor> groupConversions = parameterDescriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	public void testGetContainerElementMetaDataForMethodParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterWithCascadedContainerElements().getParameterDescriptors().get( 1 );

		checkContainerElementMetaDataOnParameterDescriptor( parameterDescriptor );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "e")
	public void testGetContainerElementMetaDataForConstructorParameter() {
		ParameterDescriptor parameterDescriptor = Executables.constructorWithCascadedContainerElementsOnParameter().getParameterDescriptors().get( 0 );

		checkContainerElementMetaDataOnParameterDescriptor( parameterDescriptor );
	}

	private void checkContainerElementMetaDataOnParameterDescriptor(ParameterDescriptor parameterDescriptor) {
		Set<ContainerElementTypeDescriptor> containerElementTypes = parameterDescriptor.getConstrainedContainerElementTypes();

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
