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

import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.Scope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ExecutableDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ExecutableDescriptorTest.class )
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
	public void testGetElementClassForMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertEquals( descriptor.getElementClass(), int.class );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "a")
	public void testGetElementClassForVoidMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertEquals( descriptor.getElementClass(), void.class );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "a")
	public void testGetElementClassForConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertEquals( descriptor.getElementClass(), CustomerService.class );
	}

	@Test
	@SpecAssertion(section = "6.7", id = "a")
	public void testGetNameForMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertEquals( descriptor.getName(), "createCustomer" );
	}

	@Test
	@SpecAssertion(section = "6.7", id = "a")
	public void testGetNameForConstructor() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructor();
		assertEquals( descriptor.getName(), "CustomerService" );
	}

	@Test
	@SpecAssertion(section = "6.7", id = "b")
	public void testGetParameterDescriptorsForMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertEquals(
				descriptor.getParameterDescriptors().size(),
				2,
				"Size of parameter descriptor list doesn't match method parameter count"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "b")
	public void testGetParameterDescriptorsForParameterlessMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertEquals(
				descriptor.getParameterDescriptors().size(),
				0,
				"Size of parameter descriptor list doesn't match method parameter count"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "b")
	public void testGetParameterDescriptorsForConstructor() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructor();
		assertEquals(
				descriptor.getParameterDescriptors().size(),
				2,
				"Size of parameter descriptor list doesn't match constructor parameter count"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "b")
	public void testGetParameterDescriptorsForConstructorOfInnerClass() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructorOfInnerClass();
		assertEquals(
				descriptor.getParameterDescriptors().size(),
				2,
				"Size of parameter descriptor list doesn't match constructor parameter count"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "b")
	public void testGetParameterDescriptorsForParameterlessConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertEquals(
				descriptor.getParameterDescriptors().size(),
				0,
				"Size of parameter descriptor list doesn't match constructor parameter count"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "c")
	public void testGetCrossParameterDescriptorForMethod() {
		MethodDescriptor descriptor = Executables.crossParameterConstrainedMethod();
		assertNotNull(
				descriptor.getCrossParameterDescriptor(),
				"Cross-parameter descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "c")
	public void testGetCrossParameterDescriptorForMethodWithoutCrossParameterConstraints() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertNotNull(
				descriptor.getCrossParameterDescriptor(),
				"Cross-parameter descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "c")
	public void testGetCrossParameterDescriptorForConstructor() {
		ConstructorDescriptor descriptor = Executables.crossParameterConstrainedConstructor();
		assertNotNull(
				descriptor.getCrossParameterDescriptor(),
				"Cross-parameter descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "c")
	public void testGetCrossParameterDescriptorForConstructorWithoutCrossParameterConstraints() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertNotNull(
				descriptor.getCrossParameterDescriptor(),
				"Cross-parameter descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "d")
	public void testGetReturnValueDescriptorForMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertNotNull(
				descriptor.getReturnValueDescriptor(),
				"Return value descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "d")
	public void testGetUnconstrainedReturnValueDescriptorForMethod() {
		MethodDescriptor descriptor = Executables.cascadedParameterMethod();
		assertNotNull(
				descriptor.getReturnValueDescriptor(),
				"Return value descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "d")
	public void testReturnValueDescriptorForVoidMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertNotNull(
				descriptor.getReturnValueDescriptor(),
				"Return value descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "d")
	public void testGetReturnValueDescriptorForConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertNotNull(
				descriptor.getReturnValueDescriptor(),
				"Return value descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "d")
	public void testGetUnconstrainedReturnValueDescriptorForConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedParameterConstructor();
		assertNotNull(
				descriptor.getReturnValueDescriptor(),
				"Return value descriptor should not be null"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForConstrainedMethod() {
		MethodDescriptor descriptor = Executables.parameterConstrainedMethod();
		assertTrue(
				descriptor.areParametersConstrained(),
				"Should be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForCascadedMethod() {
		MethodDescriptor descriptor = Executables.cascadedParameterMethod();
		assertTrue(
				descriptor.areParametersConstrained(),
				"Should be constrained on parameters"
		);
	}


	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForCrossParameterConstrainedMethod() {
		MethodDescriptor descriptor = Executables.crossParameterConstrainedMethod();
		assertTrue(
				descriptor.areParametersConstrained(),
				"Should be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForNotConstrainedMethod() {
		MethodDescriptor descriptor = Executables.cascadedReturnValueMethod();
		assertFalse(
				descriptor.areParametersConstrained(),
				"Should not be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForParameterlessMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertFalse(
				descriptor.areParametersConstrained(),
				"Should not be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.parameterConstrainedConstructor();
		assertTrue(
				descriptor.areParametersConstrained(),
				"Should be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForCascadedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedParameterConstructor();
		assertTrue(
				descriptor.areParametersConstrained(),
				"Should be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForCrossParameterConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.crossParameterConstrainedConstructor();
		assertTrue(
				descriptor.areParametersConstrained(),
				"Should be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForNotConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedReturnValueConstructor();
		assertFalse(
				descriptor.areParametersConstrained(),
				"Should not be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "e")
	public void testAreParametersConstrainedForParameterlessConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertFalse(
				descriptor.areParametersConstrained(),
				"Should not be constrained on parameters"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForConstrainedMethod() {
		MethodDescriptor descriptor = Executables.returnValueConstrainedMethod();
		assertTrue(
				descriptor.isReturnValueConstrained(),
				"Should be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForCascadedMethod() {
		MethodDescriptor descriptor = Executables.cascadedReturnValueMethod();
		assertTrue(
				descriptor.isReturnValueConstrained(),
				"Should be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForNotConstrainedMethod() {
		MethodDescriptor descriptor = Executables.cascadedParameterMethod();
		assertFalse(
				descriptor.isReturnValueConstrained(),
				"Should not be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForVoidMethod() {
		MethodDescriptor descriptor = Executables.crossParameterConstrainedMethod();
		assertFalse(
				descriptor.isReturnValueConstrained(),
				"Should not be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.returnValueConstrainedConstructor();
		assertTrue(
				descriptor.isReturnValueConstrained(),
				"Should be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForCascadedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedReturnValueConstructor();
		assertTrue(
				descriptor.isReturnValueConstrained(),
				"Should be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "f")
	public void testIsReturnValueConstrainedForNotConstrainedConstructor() {
		ConstructorDescriptor descriptor = Executables.cascadedParameterConstructor();
		assertFalse(
				descriptor.isReturnValueConstrained(),
				"Should not be constrained on return value"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "g")
	public void testHasConstraintsForMethod() {
		MethodDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedMethod();
		assertFalse(
				parameterConstrainedDescriptor.hasConstraints(),
				"Should have no constraints"
		);

		MethodDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedMethod();
		assertFalse(
				returnValueConstrainedDescriptor.hasConstraints(),
				"Should have no constraints"
		);
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethod();
		assertFalse(
				crossParameterConstrainedDescriptor.hasConstraints(),
				"Should have no constraints"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "g")
	public void testHasConstraintsForConstructor() {
		ConstructorDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedConstructor();
		assertFalse(
				parameterConstrainedDescriptor.hasConstraints(),
				"Should have no constraints"
		);

		ConstructorDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedConstructor();
		assertFalse(
				returnValueConstrainedDescriptor.hasConstraints(),
				"Should have no constraints"
		);
		ConstructorDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedConstructor();
		assertFalse(
				crossParameterConstrainedDescriptor.hasConstraints(),
				"Should have no constraints"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "g")
	public void testGetConstraintsForMethod() {
		MethodDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedMethod();
		assertTrue(
				parameterConstrainedDescriptor.getConstraintDescriptors().isEmpty(),
				"Should have no constraints"
		);

		MethodDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedMethod();
		assertTrue(
				returnValueConstrainedDescriptor.getConstraintDescriptors().isEmpty(),
				"Should have no constraints"
		);
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethod();
		assertTrue(
				crossParameterConstrainedDescriptor.getConstraintDescriptors().isEmpty(),
				"Should have no constraints"
		);
	}

	@Test
	@SpecAssertion(section = "6.7", id = "g")
	public void testGetConstraintsForConstructor() {
		ConstructorDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedConstructor();
		assertTrue(
				parameterConstrainedDescriptor.getConstraintDescriptors().isEmpty(),
				"Should have no constraints"
		);

		ConstructorDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedConstructor();
		assertTrue(
				returnValueConstrainedDescriptor.getConstraintDescriptors().isEmpty(),
				"Should have no constraints"
		);
		ConstructorDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedConstructor();
		assertTrue(
				crossParameterConstrainedDescriptor.getConstraintDescriptors().isEmpty(),
				"Should have no constraints"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.7", id = "g")
	})
	public void testFindConstraintsForMethod() {
		MethodDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedMethod();
		assertTrue(
				parameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no constraints"
		);

		MethodDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedMethod();
		assertTrue(
				returnValueConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no constraints"
		);
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethod();
		assertTrue(
				crossParameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no constraints"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "g"),
			@SpecAssertion(section = "6.7", id = "g")
	})
	public void testFindConstraintsForMethodLookingAt() {
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.methodOverridingCrossParameterConstrainedMethod();
		assertEquals(
				crossParameterConstrainedDescriptor.findConstraints()
						.lookingAt( Scope.LOCAL_ELEMENT )
						.getConstraintDescriptors()
						.size(),
				0,
				"Should have no local constraints"
		);

		assertTrue(
				crossParameterConstrainedDescriptor.findConstraints().lookingAt( Scope.HIERARCHY )
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no hierarchy constraints"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "g"),
			@SpecAssertion(section = "6.7", id = "g")
	})
	public void testFindConstraintsForMethodDefinedOnSuperTypeLookingAt() {
		MethodDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedMethodFromSuperType();
		assertEquals(
				crossParameterConstrainedDescriptor.findConstraints()
						.lookingAt( Scope.LOCAL_ELEMENT )
						.getConstraintDescriptors()
						.size(),
				0,
				"Should have no local constraints"
		);

		assertTrue(
				crossParameterConstrainedDescriptor.findConstraints().lookingAt( Scope.HIERARCHY )
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no hierarchy constraint"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.7", id = "g")
	})
	public void testFindConstraintsForConstructor() {
		ConstructorDescriptor parameterConstrainedDescriptor = Executables.parameterConstrainedConstructor();
		assertTrue(
				parameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no constraints"
		);

		ConstructorDescriptor returnValueConstrainedDescriptor = Executables.returnValueConstrainedConstructor();
		assertTrue(
				returnValueConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no constraints"
		);
		ConstructorDescriptor crossParameterConstrainedDescriptor = Executables.crossParameterConstrainedConstructor();
		assertTrue(
				crossParameterConstrainedDescriptor.findConstraints()
						.getConstraintDescriptors()
						.isEmpty(),
				"Should have no constraints"
		);
	}
}
