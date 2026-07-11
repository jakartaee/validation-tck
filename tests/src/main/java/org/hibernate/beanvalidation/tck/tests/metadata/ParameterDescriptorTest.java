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
import jakarta.validation.metadata.ParameterDescriptor;

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

		assertThat( parameters.get( 0 ).getElementClass() ).as( "Wrong parameter class" ).isEqualTo( String.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertThat( parameters.get( 0 ).getElementClass() ).as( "Wrong parameter class" ).isEqualTo( String.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "a")
	public void testGetIndexForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertThat( parameters.get( 0 ).getIndex() ).as( "Wrong parameter index" ).isEqualTo( 0 );
		assertThat( parameters.get( 1 ).getIndex() ).as( "Wrong parameter index" ).isEqualTo( 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "a")
	public void testGetIndexForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertThat( parameters.get( 0 ).getIndex() ).as( "Wrong parameter index" ).isEqualTo( 0 );
		assertThat( parameters.get( 1 ).getIndex() ).as( "Wrong parameter index" ).isEqualTo( 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "b")
	public void testGetNameForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();

		assertThat( parameters.get( 0 ).getName() ).as( "Wrong parameter name" ).isEqualTo( "firstName" );
		assertThat( parameters.get( 1 ).getName() ).as( "Wrong parameter name" ).isEqualTo( "lastName" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_PARAMETERDESCRIPTOR, id = "b")
	public void testGetNameForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();

		assertThat( parameters.get( 0 ).getName() ).as( "Wrong parameter name" ).isEqualTo( "firstName" );
		assertThat( parameters.get( 1 ).getName() ).as( "Wrong parameter name" ).isEqualTo( "lastName" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForMethod() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedMethod()
				.getParameterDescriptors();
		assertThat( parameters.get( 0 ).isCascaded() ).as( "Should not be cascaded" ).isFalse();

		parameters = Executables.cascadedParameterMethod().getParameterDescriptors();
		assertThat( parameters.get( 0 ).isCascaded() ).as( "Should be cascaded" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "a")
	public void testIsCascadedForConstructor() {
		List<ParameterDescriptor> parameters = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors();
		assertThat( parameters.get( 0 ).isCascaded() ).as( "Should not be cascaded" ).isFalse();

		parameters = Executables.cascadedParameterConstructor().getParameterDescriptors();
		assertThat( parameters.get( 0 ).isCascaded() ).as( "Should be cascaded" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_GROUPCONVERSIONDESCRIPTOR, id = "b")
	public void testGetGroupConversionsForConstructorParameter() {
		List<ParameterDescriptor> parameters = Executables.constructorWithGroupConversionOnParameter()
				.getParameterDescriptors();
		Set<GroupConversionDescriptor> groupConversions = parameters.get( 1 ).getGroupConversions();

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
	public void testGetGroupConversionsForMethodParameter() {
		List<ParameterDescriptor> parameters = Executables.methodWithGroupConversionOnParameter()
				.getParameterDescriptors();
		Set<GroupConversionDescriptor> groupConversions = parameters.get( 0 ).getGroupConversions();

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
	public void testGetGroupConversionsReturnsEmptySetForConstructorParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterConstrainedConstructor()
				.getParameterDescriptors()
				.get( 0 );
		Set<GroupConversionDescriptor> groupConversions = parameterDescriptor.getGroupConversions();

		assertThat( groupConversions ).isNotNull();
		assertThat( groupConversions.isEmpty() ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CASCADABLEDESCRIPTOR, id = "b")
	public void testGetGroupConversionsReturnsEmptySetForMethodParameter() {
		ParameterDescriptor parameterDescriptor = Executables.parameterConstrainedMethod()
				.getParameterDescriptors()
				.get( 0 );
		Set<GroupConversionDescriptor> groupConversions = parameterDescriptor.getGroupConversions();

		assertThat( groupConversions  ).isNotNull();
		assertThat( groupConversions.isEmpty() ).isTrue();
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
