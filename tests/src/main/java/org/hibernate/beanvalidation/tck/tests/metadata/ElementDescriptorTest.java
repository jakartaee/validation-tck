/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getValidatorUnderTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.annotation.ElementType;
import java.util.Set;

import javax.validation.Validator;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.Scope;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ElementDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ElementDescriptorTest.class )
				.withClasses( SuperClass.class, SubClass.class, SuperConstraint.class, SuperConstraintValidator.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	public void testGetElementClass() {
		Validator validator = getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SuperClass.class );
		assertEquals( beanDescriptor.getElementClass(), SuperClass.class, "Wrong element class" );

		ElementDescriptor elementDescriptor = beanDescriptor.getConstraintsForProperty( "myField" );
		assertEquals( elementDescriptor.getElementClass(), String.class, "Wrong element class" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "b")
	public void testGetConstraintDescriptors() {
		ElementDescriptor descriptor = getPropertyDescriptor( SubClass.class, "myField" );
		assertEquals( descriptor.getConstraintDescriptors().size(), 2, "There should be two constraints on myField" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "c")
	public void testHasConstraints() {
		ElementDescriptor descriptor = getPropertyDescriptor( SubClass.class, "myField" );
		assertTrue( descriptor.hasConstraints() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "e")
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "e")
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "e")
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "f")
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
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "g")
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
