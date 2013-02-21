/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.Set;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.CrossParameterDescriptor;
import javax.validation.metadata.Scope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.metadata.CustomerService.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class CrossParameterDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( CrossParameterDescriptorTest.class )
				.withClasses(
						Account.class,
						Customer.class,
						CustomerService.class,
						CustomerServiceExtension.class,
						Executables.class,
						Person.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = "6.2", id = "a")
	public void testGetElementClass() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();
		assertEquals( descriptor.getElementClass(), Object[].class );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "c")
	public void testHasConstraintsForMethod() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();
		assertTrue( descriptor.hasConstraints(), "Should have constraints" );
	}

	@SpecAssertion(section = "6.2", id = "c")
	public void testHasConstraintsForUnconstrainedMethod() {
		CrossParameterDescriptor descriptor = Executables.unconstrainedMethod()
				.getCrossParameterDescriptor();
		assertFalse( descriptor.hasConstraints(), "Should have no constraints" );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "c")
	public void testHasConstraintsForConstructor() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedConstructor()
				.getCrossParameterDescriptor();
		assertTrue( descriptor.hasConstraints(), "Should have constraints" );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "c")
	public void testHasConstraintsForConstructorWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getCrossParameterDescriptor();
		assertFalse( descriptor.hasConstraints(), "Should have no constraints" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "b"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testGetConstraintsForMethod() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertEquals( constraints.size(), 1, "Should have constraints" );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertEquals(
				constraint.getAnnotation().annotationType(),
				MyCrossParameterConstraint.class,
				"Wrong constraint type"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "b"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testGetConstraintsForMethodWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertEquals( constraints.size(), 0, "Should have no constraints" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "b"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testGetConstraintsForConstructor() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertEquals( constraints.size(), 1, "Should have constraints" );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertEquals(
				constraint.getAnnotation().annotationType(),
				MyCrossParameterConstraint.class,
				"Wrong constraint type"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "b"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testGetConstraintsForConstructorWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.getConstraintDescriptors();
		assertEquals( constraints.size(), 0, "Should have no constraints" );
	}


	//TODO add "tck-testable"
	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "x"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testFindConstraintsForMethod() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertEquals( constraints.size(), 1, "Should have constraints" );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertEquals(
				constraint.getAnnotation().annotationType(),
				MyCrossParameterConstraint.class,
				"Wrong constraint type"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "g"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testFindConstraintsForMethodLookingAt() {
		CrossParameterDescriptor descriptor = Executables.methodOverridingCrossParameterConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertEquals(
				constraints.size(),
				0,
				"Should have no local constraints"
		);

		constraints = descriptor.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertEquals( constraints.size(), 1, "Should have constraints" );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertEquals(
				constraint.getAnnotation().annotationType(),
				MyCrossParameterConstraint.class,
				"Wrong constraint type"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "g"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testFindConstraintsForMethodDefinedOnSuperTypeLookingAt() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedMethodFromSuperType()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertEquals(
				constraints.size(),
				0,
				"Should have no local constraints"
		);

		constraints = descriptor.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertEquals( constraints.size(), 1, "Should have constraints" );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertEquals(
				constraint.getAnnotation().annotationType(),
				MyCrossParameterConstraint.class,
				"Wrong constraint type"
		);
	}

	//TODO Add tck-testable"
	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "x"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testFindConstraintsForMethodWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedMethod()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertEquals( constraints.size(), 0, "Should have no constraints" );
	}

	//TODO Add tck-testable"
	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "x"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testFindConstraintsForConstructor() {
		CrossParameterDescriptor descriptor = Executables.crossParameterConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertEquals( constraints.size(), 1, "Should have constraints" );

		ConstraintDescriptor<?> constraint = constraints.iterator().next();
		assertEquals(
				constraint.getAnnotation().annotationType(),
				MyCrossParameterConstraint.class,
				"Wrong constraint type"
		);
	}

	//TODO Add tck-testable"
	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "x"),
			@SpecAssertion(section = "6.7", id = "c")
	})
	public void testFindConstraintsForConstructorWithoutCrossParameterConstraints() {
		CrossParameterDescriptor descriptor = Executables.returnValueConstrainedConstructor()
				.getCrossParameterDescriptor();

		Set<ConstraintDescriptor<?>> constraints = descriptor.findConstraints().getConstraintDescriptors();
		assertEquals( constraints.size(), 0, "Should have no constraints" );
	}
}
