/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.inheritance;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ConstraintInheritanceTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ConstraintInheritanceTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "b")
	public void testConstraintsOnSuperClassAreInherited() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "foo";
		Assertions.assertThat( beanDescriptor.getConstraintsForProperty( propertyName ) ).isNotNull();
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		Annotation constraintAnnotation = propDescriptor.getConstraintDescriptors()
				.iterator()
				.next().getAnnotation();
		Assertions.assertThat( constraintAnnotation.annotationType() ).isEqualTo( NotNull.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "b")
	public void testConstraintsOnInterfaceAreInherited() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "fubar";
		Assertions.assertThat( beanDescriptor.getConstraintsForProperty( propertyName ) ).isNotNull();
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		Annotation constraintAnnotation = propDescriptor.getConstraintDescriptors()
				.iterator()
				.next().getAnnotation();
		Assertions.assertThat( constraintAnnotation.annotationType() ).isEqualTo( NotNull.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "c")
	public void testConstraintsOnInterfaceAndImplementationAddUp() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "name";
		Assertions.assertThat( beanDescriptor.getConstraintsForProperty( propertyName ) ).isNotNull();
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		List<Class<? extends Annotation>> constraintTypes = getConstraintTypes( propDescriptor.getConstraintDescriptors() );

		Assertions.assertThat( constraintTypes ).hasSize( 2 );
		Assertions.assertThat( constraintTypes ).contains( DecimalMin.class );
		Assertions.assertThat( constraintTypes ).contains( Size.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_INHERITANCE, id = "c")
	public void testConstraintsOnSuperAndSubClassAddUp() {
		BeanDescriptor beanDescriptor = getValidator().getConstraintsForClass( Bar.class );

		String propertyName = "lastName";
		Assertions.assertThat( beanDescriptor.getConstraintsForProperty( propertyName ) ).isNotNull();
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		List<Class<? extends Annotation>> constraintTypes = getConstraintTypes( propDescriptor.getConstraintDescriptors() );

		Assertions.assertThat( constraintTypes ).hasSize( 2 );
		Assertions.assertThat( constraintTypes ).contains( DecimalMin.class );
		Assertions.assertThat( constraintTypes ).contains( Size.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE, id = "a")
	public void testValidationConsidersConstraintsFromSuperTypes() {
		Set<ConstraintViolation<Bar>> violations = getValidator().validate( new Bar() );
		assertThat( violations ).containsOnlyViolations(
				//Bar
				violationOf( DecimalMin.class ),
				violationOf( DecimalMin.class ),
				violationOf( ValidBar.class ),
				//Foo
				violationOf( NotNull.class ),
				violationOf( Size.class ),
				violationOf( ValidFoo.class ),
				//Fubar
				violationOf( NotNull.class ),
				violationOf( Size.class ),
				violationOf( ValidFubar.class )
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
