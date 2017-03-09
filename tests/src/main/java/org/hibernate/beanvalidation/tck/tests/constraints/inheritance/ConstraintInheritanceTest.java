/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.BaseValidatorTest;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.webArchiveBuilder;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ConstraintInheritanceTest extends BaseValidatorTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ConstraintInheritanceTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "4.3", id = "b")
	public void testConstraintsOnSuperClassAreInherited() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "foo";
		assertTrue( beanDescriptor.getConstraintsForProperty( propertyName ) != null );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		Annotation constraintAnnotation = propDescriptor.getConstraintDescriptors()
				.iterator()
				.next().getAnnotation();
		assertTrue( constraintAnnotation.annotationType() == NotNull.class );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.3", id = "a"),
			@SpecAssertion(section = "4.3", id = "b")
	})
	public void testConstraintsOnInterfaceAreInherited() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "fubar";
		assertTrue( beanDescriptor.getConstraintsForProperty( propertyName ) != null );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		Annotation constraintAnnotation = propDescriptor.getConstraintDescriptors()
				.iterator()
				.next().getAnnotation();
		assertTrue( constraintAnnotation.annotationType() == NotNull.class );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.3", id = "a"),
			@SpecAssertion(section = "4.3", id = "c")
	})
	public void testConstraintsOnInterfaceAndImplementationAddUp() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "name";
		assertTrue( beanDescriptor.getConstraintsForProperty( propertyName ) != null );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		List<Class<? extends Annotation>> constraintTypes = getConstraintTypes( propDescriptor.getConstraintDescriptors() );

		assertEquals( constraintTypes.size(), 2 );
		assertTrue( constraintTypes.contains( DecimalMin.class ) );
		assertTrue( constraintTypes.contains( Size.class ) );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "4.3", id = "a"),
			@SpecAssertion(section = "4.3", id = "c")
	})
	public void testConstraintsOnSuperAndSubClassAddUp() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "lastName";
		assertTrue( beanDescriptor.getConstraintsForProperty( propertyName ) != null );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		List<Class<? extends Annotation>> constraintTypes = getConstraintTypes( propDescriptor.getConstraintDescriptors() );

		assertEquals( constraintTypes.size(), 2 );
		assertTrue( constraintTypes.contains( DecimalMin.class ) );
		assertTrue( constraintTypes.contains( Size.class ) );
	}

	@Test
	@SpecAssertion(section = "4.6", id = "a")
	public void testValidationConsidersConstraintsFromSuperTypes() {
		Set<ConstraintViolation<Bar>> violations = getValidator().validate( new Bar() );
		assertCorrectConstraintTypes(
				violations,
				DecimalMin.class, DecimalMin.class, ValidBar.class, //Bar
				NotNull.class, Size.class, ValidFoo.class, //Foo
				NotNull.class, Size.class, ValidFubar.class //Fubar
		);
	}

	private List<Class<? extends Annotation>> getConstraintTypes(Iterable<ConstraintDescriptor<?>> descriptors) {
		List<Class<? extends Annotation>> constraintTypes = new ArrayList<Class<? extends Annotation>>();

		for ( ConstraintDescriptor<?> constraintDescriptor : descriptors ) {
			constraintTypes.add( constraintDescriptor.getAnnotation().annotationType() );
		}

		return constraintTypes;
	}
}
