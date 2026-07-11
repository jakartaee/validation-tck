/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.assertConstraintDescriptors;
import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.getContainerElementDescriptor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import jakarta.validation.metadata.GroupConversionDescriptor;
import jakarta.validation.metadata.ReturnValueDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.StrictCustomerServiceChecks;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
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
		assertThat( descriptor.getElementClass() ).isEqualTo( int.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForVoidMethod() {
		ReturnValueDescriptor descriptor = Executables.parameterConstrainedMethod().getReturnValueDescriptor();
		assertThat( descriptor.getElementClass() ).isEqualTo( void.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForConstructor() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedConstructor().getReturnValueDescriptor();
		assertThat( descriptor.getElementClass() ).isEqualTo( CustomerService.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForMethodReturnValue() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getReturnValueDescriptor();
		assertThat( descriptor.isCascaded() ).as( "Should not be cascaded" ).isFalse();

		descriptor = Executables.cascadedReturnValueMethod().getReturnValueDescriptor();
		assertThat( descriptor.isCascaded() ).as( "Should be cascaded" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForConstructorReturnValue() {
		ReturnValueDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getReturnValueDescriptor();
		assertThat( descriptor.isCascaded() ).as( "Should not be cascaded" ).isFalse();

		descriptor = Executables.cascadedReturnValueConstructor().getReturnValueDescriptor();
		assertThat( descriptor.isCascaded() ).as( "Should be cascaded" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForVoidMethod() {
		ReturnValueDescriptor descriptor = Executables.parameterConstrainedMethod().getReturnValueDescriptor();
		assertThat( descriptor.isCascaded() ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForConstructorReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.constructorWithGroupConversionOnReturnValue()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertThat( groupConversions.size() ).isEqualTo( 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerServiceChecks.class ) ) {
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForVoidMethod() {
		ReturnValueDescriptor descriptor = Executables.parameterConstrainedMethod().getReturnValueDescriptor();
		assertThat( descriptor.getGroupConversions().isEmpty() ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForMethodReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.methodWithGroupConversionOnReturnValue()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertThat( groupConversions.size() ).isEqualTo( 2  );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( BasicChecks.class  );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerServiceChecks.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( StrictChecks.class  );
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
	public void testGetGroupConversionsReturnsEmptySetForConstructorReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.cascadedReturnValueMethod()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertThat( groupConversions  ).isNotNull();
		assertThat( groupConversions.isEmpty() ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	public void testGetGroupConversionsReturnsEmptySetForMethodReturnValue() {
		ReturnValueDescriptor returnValueDescriptor = Executables.cascadedReturnValueConstructor()
				.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversions = returnValueDescriptor.getGroupConversions();

		assertThat( groupConversions  ).isNotNull();
		assertThat( groupConversions.isEmpty() ).isTrue();
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

		assertThat( containerElementTypes.size() ).isEqualTo( 2  );

		ContainerElementTypeDescriptor productType = getContainerElementDescriptor( containerElementTypes, Map.class, 0 );
		assertThat( productType.getElementClass() ).isEqualTo( ProductType.class  );
		assertConstraintDescriptors( productType.getConstraintDescriptors(), NotNull.class );
		assertThat( productType.getConstrainedContainerElementTypes().size() ).isEqualTo( 0  );
		assertThat( productType.isCascaded() ).isTrue();
		assertThat( productType.getGroupConversions().size() ).isEqualTo( 2  );
		for ( GroupConversionDescriptor groupConversionDescriptor : productType.getGroupConversions() ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( BasicChecks.class  );
			}
			else if ( groupConversionDescriptor.getFrom().equals( ComplexChecks.class ) ) {
				assertThat( groupConversionDescriptor.getTo() ).isEqualTo( ComplexProductTypeChecks.class  );
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
		assertThat( orderLineList.getElementClass() ).isEqualTo( List.class  );
		assertConstraintDescriptors( orderLineList.getConstraintDescriptors(), Size.class );
		assertThat( orderLineList.isCascaded() ).isFalse();
		assertThat( orderLineList.getGroupConversions().size() ).isEqualTo( 0  );
		assertThat( orderLineList.getConstrainedContainerElementTypes().size() ).isEqualTo( 1  );

		ContainerElementTypeDescriptor orderLine = getContainerElementDescriptor( orderLineList.getConstrainedContainerElementTypes(), List.class, 0 );
		assertThat( orderLine.getElementClass() ).isEqualTo( ProductOrderLine.class  );
		assertConstraintDescriptors( orderLine.getConstraintDescriptors(), NotNull.class );
		assertThat( orderLine.getConstrainedContainerElementTypes().size() ).isEqualTo( 0  );
		assertThat( orderLine.isCascaded() ).isFalse();
	}
}
