/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import jakarta.validation.metadata.ConstructorDescriptor;
import jakarta.validation.metadata.MethodDescriptor;
import jakarta.validation.metadata.Scope;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
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
public class ExecutableDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ExecutableDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertThat( descriptor.getElementClass() ).isEqualTo( int.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForVoidMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertThat( descriptor.getElementClass() ).isEqualTo( void.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClassForConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertThat( descriptor.getElementClass() ).isEqualTo( CustomerService.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "a")
	public void testGetNameForMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertThat( descriptor.getName() ).isEqualTo( "createCustomer" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "a")
	public void testGetNameForConstructor() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructor();
		assertThat( descriptor.getName() ).isEqualTo( "CustomerService" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "b")
	public void testGetParameterDescriptorsForMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertThat( descriptor.getParameterDescriptors().size() ).as( "Size of parameter descriptor list doesn't match method parameter count" ).isEqualTo( 2 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "b")
	public void testGetParameterDescriptorsForParameterlessMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertThat( descriptor.getParameterDescriptors().size() ).as( "Size of parameter descriptor list doesn't match method parameter count" ).isEqualTo( 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "b")
	public void testGetParameterDescriptorsForConstructor() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructor();
		assertThat( descriptor.getParameterDescriptors().size() ).as( "Size of parameter descriptor list doesn't match constructor parameter count" ).isEqualTo( 2 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "b")
	public void testGetParameterDescriptorsForConstructorOfInnerClass() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructorOfInnerClass();
		assertThat( descriptor.getParameterDescriptors().size() ).as( "Size of parameter descriptor list doesn't match constructor parameter count" ).isEqualTo( 2 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "b")
	public void testGetParameterDescriptorsForParameterlessConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertThat( descriptor.getParameterDescriptors().size() ).as( "Size of parameter descriptor list doesn't match constructor parameter count" ).isEqualTo( 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetCrossParameterDescriptorForMethod() {
		MethodDescriptor descriptor = Executables.crossParameterConstrainedMethod();
		assertThat( descriptor.getCrossParameterDescriptor() ).as( "Cross-parameter descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetCrossParameterDescriptorForMethodWithoutCrossParameterConstraints() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertThat( descriptor.getCrossParameterDescriptor() ).as( "Cross-parameter descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetCrossParameterDescriptorForConstructor() {
		ConstructorDescriptor descriptor = Executables.crossParameterConstrainedConstructor();
		assertThat( descriptor.getCrossParameterDescriptor() ).as( "Cross-parameter descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetCrossParameterDescriptorForConstructorWithoutCrossParameterConstraints() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertThat( descriptor.getCrossParameterDescriptor() ).as( "Cross-parameter descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "d")
	public void testGetReturnValueDescriptorForMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertThat( descriptor.getReturnValueDescriptor() ).as( "Return value descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "d")
	public void testGetUnconstrainedReturnValueDescriptorForMethod() {
		MethodDescriptor descriptor = Executables.cascadedParameterMethod();
		assertThat( descriptor.getReturnValueDescriptor() ).as( "Return value descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "d")
	public void testReturnValueDescriptorForVoidMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertThat( descriptor.getReturnValueDescriptor() ).as( "Return value descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "d")
	public void testGetReturnValueDescriptorForConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertThat( descriptor.getReturnValueDescriptor() ).as( "Return value descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "d")
	public void testGetUnconstrainedReturnValueDescriptorForConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedParameterConstructor();
		assertThat( descriptor.getReturnValueDescriptor() ).as( "Return value descriptor should not be null" ).isNotNull();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForConstrainedMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should be constrained on parameters" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForCascadedMethod() {
		MethodDescriptor descriptor = Executables.cascadedParameterMethod();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should be constrained on parameters" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForCrossParameterConstrainedMethod() {
		MethodDescriptor descriptor = Executables.crossParameterConstrainedMethod();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should be constrained on parameters" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForNotConstrainedMethod() {
		MethodDescriptor descriptor = Executables.cascadedReturnValueMethod();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should not be constrained on parameters" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForParameterlessMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should not be constrained on parameters" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructor();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should be constrained on parameters" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForCascadedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedParameterConstructor();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should be constrained on parameters" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForCrossParameterConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.crossParameterConstrainedConstructor();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should be constrained on parameters" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForNotConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedReturnValueConstructor();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should not be constrained on parameters" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "e")
	public void testAreParametersConstrainedForParameterlessConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertThat( descriptor.hasConstrainedParameters() ).as( "Should not be constrained on parameters" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForConstrainedMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should be constrained on return value" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForCascadedMethod() {
		MethodDescriptor descriptor = Executables.cascadedReturnValueMethod();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should be constrained on return value" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForNotConstrainedMethod() {
		MethodDescriptor descriptor = Executables.cascadedParameterMethod();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should not be constrained on return value" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForVoidMethod() {
		MethodDescriptor descriptor = Executables.crossParameterConstrainedMethod();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should not be constrained on return value" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should be constrained on return value" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForCascadedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedReturnValueConstructor();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should be constrained on return value" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "f")
	public void testIsReturnValueConstrainedForNotConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedParameterConstructor();
		assertThat( descriptor.hasConstrainedReturnValue() ).as( "Should not be constrained on return value" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testHasConstraintsForMethod() {
		MethodDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedMethod();
		assertThat( parameterConstrainedDescriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();

		MethodDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedMethod();
		assertThat( returnValueConstrainedDescriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethod();
		assertThat( crossParameterConstrainedDescriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testHasConstraintsForConstructor() {
		ConstructorDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedConstructor();
		assertThat( parameterConstrainedDescriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();

		ConstructorDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedConstructor();
		assertThat( returnValueConstrainedDescriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();
		ConstructorDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedConstructor();
		assertThat( crossParameterConstrainedDescriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testGetConstraintsForMethod() {
		MethodDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedMethod();
		assertThat( parameterConstrainedDescriptor.getConstraintDescriptors().isEmpty() ).as( "Should have no constraints" ).isTrue();

		MethodDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedMethod();
		assertThat( returnValueConstrainedDescriptor.getConstraintDescriptors().isEmpty() ).as( "Should have no constraints" ).isTrue();
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethod();
		assertThat( crossParameterConstrainedDescriptor.getConstraintDescriptors().isEmpty() ).as( "Should have no constraints" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testGetConstraintsForConstructor() {
		ConstructorDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedConstructor();
		assertThat( parameterConstrainedDescriptor.getConstraintDescriptors().isEmpty() ).as( "Should have no constraints" ).isTrue();

		ConstructorDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedConstructor();
		assertThat( returnValueConstrainedDescriptor.getConstraintDescriptors().isEmpty() ).as( "Should have no constraints" ).isTrue();
		ConstructorDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedConstructor();
		assertThat( crossParameterConstrainedDescriptor.getConstraintDescriptors().isEmpty() ).as( "Should have no constraints" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testFindConstraintsForMethod() {
		MethodDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedMethod();
		assertThat( parameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no constraints" ).isTrue();

		MethodDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedMethod();
		assertThat( returnValueConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no constraints" ).isTrue();
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethod();
		assertThat( crossParameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no constraints" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testFindConstraintsForMethodLookingAt() {
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.methodOverridingCrossParameterConstrainedMethod();
		assertThat( crossParameterConstrainedDescriptor.findConstraints()
						.lookingAt( Scope.LOCAL_ELEMENT )
						.getConstraintDescriptors()
						.size() ).as( "Should have no local constraints" ).isEqualTo( 0 );

		assertThat( crossParameterConstrainedDescriptor.findConstraints().lookingAt( Scope.HIERARCHY )
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no hierarchy constraints" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testFindConstraintsForMethodDefinedOnSuperTypeLookingAt() {
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethodFromSuperType();
		assertThat( crossParameterConstrainedDescriptor.findConstraints()
						.lookingAt( Scope.LOCAL_ELEMENT )
						.getConstraintDescriptors()
						.size() ).as( "Should have no local constraints" ).isEqualTo( 0 );

		assertThat( crossParameterConstrainedDescriptor.findConstraints().lookingAt( Scope.HIERARCHY )
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no hierarchy constraint" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "g")
	public void testFindConstraintsForConstructor() {
		ConstructorDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedConstructor();
		assertThat( parameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no constraints" ).isTrue();

		ConstructorDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedConstructor();
		assertThat( returnValueConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no constraints" ).isTrue();
		ConstructorDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedConstructor();
		assertThat( crossParameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty() ).as( "Should have no constraints" ).isTrue();
	}
}
