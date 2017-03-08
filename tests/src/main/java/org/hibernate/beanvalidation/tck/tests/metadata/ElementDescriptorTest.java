/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.lang.annotation.ElementType;
import java.util.Set;
import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.Scope;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getValidatorUnderTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ElementDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ElementDescriptorTest.class )
				.withClasses( SuperClass.class, SubClass.class, SuperConstraint.class, SuperConstraintValidator.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "6.2", id = "a")
	public void testGetElementClass() {
		Validator validator = getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SuperClass.class );
		assertEquals( beanDescriptor.getElementClass(), SuperClass.class, "Wrong element class" );

		ElementDescriptor elementDescriptor = beanDescriptor.getConstraintsForProperty( "myField" );
		assertEquals( elementDescriptor.getElementClass(), String.class, "Wrong element class" );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "b")
	public void testGetConstraintDescriptors() {
		ElementDescriptor descriptor = getPropertyDescriptor( SubClass.class, "myField" );
		assertEquals( descriptor.getConstraintDescriptors().size(), 2, "There should be two constraints on myField" );
	}

	@Test
	@SpecAssertion(section = "6.2", id = "c")
	public void testHasConstraints() {
		ElementDescriptor descriptor = getPropertyDescriptor( SubClass.class, "myField" );
		assertTrue( descriptor.hasConstraints() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "e")
	})
	public void testUnorderedAndMatchingGroups() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SubClass.class );
		assertNotNull( beanDescriptor );

		Set<ConstraintDescriptor<?>> descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.unorderedAndMatchingGroups( Default.class, SuperClass.BasicGroup.class )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 2 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.unorderedAndMatchingGroups( SuperClass.UnusedGroup.class )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 0 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "e")
	})
	public void testUnorderedAndMatchingGroupsWithInheritance() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SubClass.class );
		assertNotNull( beanDescriptor );

		Set<ConstraintDescriptor<?>> descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.unorderedAndMatchingGroups( SuperClass.InheritedGroup.class )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 1 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "e")
	})
	public void testUnorderedAndMatchingGroupsWithDefaultGroupOverriding() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SubClass.class );
		assertNotNull( beanDescriptor );

		Set<ConstraintDescriptor<?>> descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.unorderedAndMatchingGroups( Default.class )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 1 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "f")
	})
	public void testDeclaredOn() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SubClass.class );
		assertNotNull( beanDescriptor );

		Set<ConstraintDescriptor<?>> descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.declaredOn( ElementType.TYPE )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 0 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.declaredOn( ElementType.METHOD )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 0 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.declaredOn( ElementType.FIELD )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 2 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.2", id = "d"),
			@SpecAssertion(section = "6.2", id = "g")
	})
	public void testLookingAt() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SubClass.class );
		assertNotNull( beanDescriptor );

		Set<ConstraintDescriptor<?>> descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 2 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 1 );
	}
}
