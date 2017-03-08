/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.inheritance;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.testng.Assert.assertEquals;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupInheritanceTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( GroupInheritanceTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.4.1", id = "a")
	public void testGroupCanInheritGroupsViaInterfaceInheritance() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Part part = new Part();
		part.setPartNumber( 123456 );

		Set<ConstraintViolation<Part>> violations = validator.validate( part, All.class );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Digits.class, AssertTrue.class );

		part.setPartNumber( 12345 );
		part.setQaChecked( true );
		violations = validator.validate( part, All.class );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = "4.4.1", id = "b")
	public void testGroupMembership() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor descriptor = validator.getConstraintsForClass( MiniaturePart.class );

		//  PreManufacturing belongs implicitly to All
		PropertyDescriptor propertyDescriptor = descriptor.getConstraintsForProperty( "partNumber" );
		Set<ConstraintDescriptor<?>> descriptorsForGroup = propertyDescriptor.findConstraints()
				.unorderedAndMatchingGroups( All.class )
				.getConstraintDescriptors();
		assertEquals( descriptorsForGroup.size(), 1, "Wrong number of descriptors" );
		assertEquals( descriptorsForGroup.iterator().next().getAnnotation().annotationType(), Digits.class );

		//  PostManufacturing belongs implicitly to All
		propertyDescriptor = descriptor.getConstraintsForProperty( "qaChecked" );
		descriptorsForGroup = propertyDescriptor.findConstraints()
				.unorderedAndMatchingGroups( All.class )
				.getConstraintDescriptors();
		assertEquals( descriptorsForGroup.size(), 1, "Wrong number of descriptors" );
		assertEquals( descriptorsForGroup.iterator().next().getAnnotation().annotationType(), AssertTrue.class );

		propertyDescriptor = descriptor.getConstraintsForProperty( "size" );
		descriptorsForGroup = propertyDescriptor.findConstraints()
				.unorderedAndMatchingGroups( All.class )
				.getConstraintDescriptors();
		assertEquals( descriptorsForGroup.size(), 1, "Wrong number of descriptors" );
		assertEquals( descriptorsForGroup.iterator().next().getAnnotation().annotationType(), Max.class );
	}

	class Part {
		@Digits(integer = 5, fraction = 0, groups = PreManufacturing.class)
		private int partNumber;

		@AssertTrue(groups = PostManufacturing.class)
		private boolean qaChecked;

		public int getPartNumber() {
			return partNumber;
		}

		public void setPartNumber(int partNumber) {
			this.partNumber = partNumber;
		}

		public boolean getQaChecked() {
			return qaChecked;
		}

		public void setQaChecked(boolean qaChecked) {
			this.qaChecked = qaChecked;
		}
	}

	class MiniaturePart extends Part {
		@Max(value = 10, groups = All.class)
		private int size;
	}

	interface PreManufacturing {
	}

	interface PostManufacturing {
	}

	interface All extends PreManufacturing, PostManufacturing {
	}

}
