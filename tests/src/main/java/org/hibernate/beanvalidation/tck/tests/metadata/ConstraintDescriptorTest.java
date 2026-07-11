/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstraintDescriptorsFor;

import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintTarget;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ConstraintDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ConstraintDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ConstraintDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "m")
	public void testReportAsSingleViolation() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		assertThat( descriptor.isReportAsSingleViolation() ).isFalse();

		descriptor = getConstraintDescriptor( Person.class, "firstName" );
		assertThat( descriptor.isReportAsSingleViolation() ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "n")
	public void testEmptyComposingConstraints() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		assertThat( descriptor.getComposingConstraints().isEmpty() ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "d")
	public void testAnnotationAndMapParametersReflectParameterOverriding() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertThat( composingDescriptors.size() ).as( "Wrong number of composing constraints" ).isEqualTo( 2 );
		boolean hasSize = false;
		for ( ConstraintDescriptor<?> desc : composingDescriptors ) {
			if ( desc.getAnnotation().annotationType().equals( Size.class ) ) {
				hasSize = true;
				Size sizeAnn = (Size) desc.getAnnotation();
				assertThat( sizeAnn.min() ).as( "The min parameter should reflect the overridden parameter" ).isEqualTo( 5 );
				assertThat( desc.getAttributes().get( "min" ) ).as( "The min parameter should reflect the overridden parameter" ).isEqualTo( 5 );
				assertThat( desc.getAttribute( "min", Integer.class ) ).as( "The min parameter should reflect the overridden parameter" ).isEqualTo( 5 );

				Assertions.assertThatThrownBy( () -> desc.getAttribute( "min", StringBuilder.class ) )
						.isInstanceOf( ClassCastException.class );
			}
			else if ( desc.getAnnotation().annotationType().equals( NotNull.class ) ) {
			}
			else {
				Assertions.fail( "Unexpected annotation." );
			}
		}
		assertThat( hasSize ).as( "Size composed annotation not found" ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "c")
	public void testGetAttributesFromConstraintDescriptor() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		Map<String, Object> attributes = descriptor.getAttributes();
		assertThat( attributes.containsKey( "message" ) ).isTrue();
		assertThat( attributes.containsKey( "groups" ) ).isTrue();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "e")
	public void testGetMessageTemplate() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "middleName" );
		String messageTemplate = descriptor.getMessageTemplate();
		assertThat( messageTemplate ).isEqualTo( "must at least be {min} characters long" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "f")
	public void testGetGroups() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertThat( groups.size() ).isEqualTo( 1 );
		assertThat( groups.iterator().next() ).as( "Wrong group" ).isEqualTo( Person.PersonValidation.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "f")
	public void testGetGroupsOnInterface() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "lastName" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertThat( groups.size() ).isEqualTo( 1 );
		assertThat( groups.iterator().next() ).as( "Wrong group" ).isEqualTo( Default.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "f")
	public void testGetGroupsWithImplicitGroup() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Man.class, "lastName" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertThat( groups.size() ).isEqualTo( 2 );
		for ( Class<?> group : groups ) {
			if ( !( group.equals( Default.class ) || group.equals( Person.class ) ) ) {
				Assertions.fail( "Invalid group." );
			}
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "g")
	public void testDefaultGroupIsReturnedIfNoGroupSpecifiedInDeclaration() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertThat( groups.size() ).isEqualTo( 1 );
		assertThat( groups.iterator().next() ).as( "Wrong group" ).isEqualTo( Default.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "h")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "n")
	public void testComposingConstraints() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertThat( composingDescriptors.size() ).as( "Wrong number of composing constraints" ).isEqualTo( 2 );
		for ( ConstraintDescriptor<?> desc : composingDescriptors ) {
			assertThat( desc.getGroups().size() ).isEqualTo( 1 );
			assertThat( desc.getGroups().iterator().next() ).as( "Wrong group" ).isEqualTo( Person.PersonValidation.class );
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "i")
	public void testPayload() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<Class<? extends Payload>> payload = descriptor.getPayload();
		assertThat( payload.size() ).isEqualTo( 1  );
		assertThat( payload.iterator().next() ).as( "Wrong payload" ).isEqualTo( Severity.Info.class );

		descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		payload = descriptor.getPayload();
		assertThat( payload  ).isNotNull();
		assertThat( payload.size() ).isEqualTo( 0  );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "j")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "k")
	public void testComposingConstraintsPayload() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertThat( composingDescriptors.size() ).as( "Wrong number of composing constraints" ).isEqualTo( 2 );
		for ( ConstraintDescriptor<?> desc : composingDescriptors ) {
			assertThat( desc.getGroups().size() ).isEqualTo( 1  );
			assertThat( desc.getPayload().iterator().next() ).as( "Wrong payload" ).isEqualTo( Severity.Info.class );
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "l")
	public void testGetValidationAppliesTo() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "age" );
		ConstraintTarget constraintTarget = descriptor.getValidationAppliesTo();
		assertThat( constraintTarget  ).isNotNull();
		assertThat( constraintTarget ).isEqualTo( ConstraintTarget.RETURN_VALUE  );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "l")
	public void testGetValidationAppliesToFromComposingConstraint() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "age" );

		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertThat( composingDescriptors.size() ).as( "Wrong number of composing constraints" ).isEqualTo( 1 );

		ConstraintTarget constraintTarget = composingDescriptors.iterator().next().getValidationAppliesTo();
		assertThat( constraintTarget  ).isNotNull();
		assertThat( constraintTarget ).isEqualTo( ConstraintTarget.RETURN_VALUE  );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "l")
	public void testGetValidationAppliesToReturnsNull() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		ConstraintTarget constraintTarget = descriptor.getValidationAppliesTo();
		assertThat( constraintTarget  ).isNull();
	}

	private ConstraintDescriptor<?> getConstraintDescriptor(Class<?> clazz, String property) {
		Set<ConstraintDescriptor<?>> descriptors = getConstraintDescriptorsFor( clazz, property );
		assertThat( descriptors.size() ).as( "There should only by one descriptor." ).isEqualTo( 1 );
		return descriptors.iterator().next();
	}
}
