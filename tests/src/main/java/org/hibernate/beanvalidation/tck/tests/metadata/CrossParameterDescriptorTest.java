/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.Set;

import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.CrossParameterDescriptor;
import jakarta.validation.metadata.Scope;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.MyCrossParameterConstraint;
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
public class CrossParameterDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( CrossParameterDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CROSSPARAMETERDESCRIPTOR, id = "a")
	public void testGetElementClass() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();
		assertThat( descriptor.getElementClass() ).isEqualTo( Object[].class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "c")
	public void testHasConstraintsForMethod() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();
		assertThat( descriptor.hasConstraints() ).as( "Should have constraints" ).isTrue();
	}

	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "c")
	public void testHasConstraintsForUnconstrainedMethod() {
		CrossParameterDescriptor descriptor = Executables.unconstrainedMethod()
				.getCrossParameterDescriptor();
		assertThat( descriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "c")
	public void testHasConstraintsForConstructor() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedConstructor()
				.getCrossParameterDescriptor();
		assertThat( descriptor.hasConstraints() ).as( "Should have constraints" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "c")
	public void testHasConstraintsForConstructorWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getCrossParameterDescriptor();
		assertThat( descriptor.hasConstraints() ).as( "Should have no constraints" ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetConstraintsForMethod() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have constraints" ).isEqualTo( 1 );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertThat( constraint.getAnnotation().annotationType() ).as( "Wrong constraint type" ).isEqualTo( MyCrossParameterConstraint.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetConstraintsForMethodWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have no constraints" ).isEqualTo( 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetConstraintsForConstructor() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have constraints" ).isEqualTo( 1 );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertThat( constraint.getAnnotation().annotationType() ).as( "Wrong constraint type" ).isEqualTo( MyCrossParameterConstraint.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testGetConstraintsForConstructorWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have no constraints" ).isEqualTo( 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testFindConstraintsForMethod() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have constraints" ).isEqualTo( 1 );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertThat( constraint.getAnnotation().annotationType() ).as( "Wrong constraint type" ).isEqualTo( MyCrossParameterConstraint.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testFindConstraintsForMethodLookingAt() {
		CrossParameterDescriptor descriptor = Executables.methodOverridingCrossParameterConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have no local constraints" ).isEqualTo( 0 );

		constraints = descriptor.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have constraints" ).isEqualTo( 1 );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertThat( constraint.getAnnotation().annotationType() ).as( "Wrong constraint type" ).isEqualTo( MyCrossParameterConstraint.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testFindConstraintsForMethodDefinedOnSuperTypeLookingAt() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethodFromSuperType()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have no local constraints" ).isEqualTo( 0 );

		constraints = descriptor.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have constraints" ).isEqualTo( 1 );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertThat( constraint.getAnnotation().annotationType() ).as( "Wrong constraint type" ).isEqualTo( MyCrossParameterConstraint.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testFindConstraintsForMethodWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have no constraints" ).isEqualTo( 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testFindConstraintsForConstructor() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have constraints" ).isEqualTo( 1 );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertThat( constraint.getAnnotation().annotationType() ).as( "Wrong constraint type" ).isEqualTo( MyCrossParameterConstraint.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_EXECUTABLEDESCRIPTOR, id = "c")
	public void testFindConstraintsForConstructorWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertThat( constraints.size() ).as( "Should have no constraints" ).isEqualTo( 0 );
	}
}
