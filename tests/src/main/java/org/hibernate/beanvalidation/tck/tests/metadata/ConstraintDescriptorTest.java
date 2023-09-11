/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstraintDescriptorsFor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintTarget;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ConstraintDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
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
		assertFalse( descriptor.isReportAsSingleViolation() );

		descriptor = getConstraintDescriptor( Person.class, "firstName" );
		assertTrue( descriptor.isReportAsSingleViolation() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "n")
	public void testEmptyComposingConstraints() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		assertTrue( descriptor.getComposingConstraints().isEmpty() );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "d")
	public void testAnnotationAndMapParametersReflectParameterOverriding() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertEquals( composingDescriptors.size(), 2, "Wrong number of composing constraints" );
		boolean hasSize = false;
		for ( ConstraintDescriptor<?> desc : composingDescriptors ) {
			if ( desc.getAnnotation().annotationType().equals( Size.class ) ) {
				hasSize = true;
				Size sizeAnn = (Size) desc.getAnnotation();
				assertEquals( sizeAnn.min(), 5, "The min parameter should reflect the overridden parameter" );
				assertEquals(
						desc.getAttributes().get( "min" ),
						5,
						"The min parameter should reflect the overridden parameter"
				);
			}
			else if ( desc.getAnnotation().annotationType().equals( NotNull.class ) ) {
			}
			else {
				fail( "Unexpected annotation." );
			}
		}
		assertTrue( hasSize, "Size composed annotation not found" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "c")
	public void testGetAttributesFromConstraintDescriptor() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		Map<String, Object> attributes = descriptor.getAttributes();
		assertTrue( attributes.containsKey( "message" ) );
		assertTrue( attributes.containsKey( "groups" ) );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "e")
	public void testGetMessageTemplate() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "middleName" );
		String messageTemplate = descriptor.getMessageTemplate();
		assertEquals( messageTemplate, "must at least be {min} characters long" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "f")
	public void testGetGroups() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertTrue( groups.size() == 1 );
		assertEquals( groups.iterator().next(), Person.PersonValidation.class, "Wrong group" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "f")
	public void testGetGroupsOnInterface() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "lastName" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertTrue( groups.size() == 1 );
		assertEquals( groups.iterator().next(), Default.class, "Wrong group" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "f")
	public void testGetGroupsWithImplicitGroup() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Man.class, "lastName" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertTrue( groups.size() == 2 );
		for ( Class<?> group : groups ) {
			if ( !( group.equals( Default.class ) || group.equals( Person.class ) ) ) {
				fail( "Invalid group." );
			}
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "g")
	public void testDefaultGroupIsReturnedIfNoGroupSpecifiedInDeclaration() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		Set<Class<?>> groups = descriptor.getGroups();
		assertTrue( groups.size() == 1 );
		assertEquals( groups.iterator().next(), Default.class, "Wrong group" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "h")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "n")
	public void testComposingConstraints() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertEquals( composingDescriptors.size(), 2, "Wrong number of composing constraints" );
		for ( ConstraintDescriptor<?> desc : composingDescriptors ) {
			assertTrue( desc.getGroups().size() == 1 );
			assertEquals( desc.getGroups().iterator().next(), Person.PersonValidation.class, "Wrong group" );
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "i")
	public void testPayload() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<Class<? extends Payload>> payload = descriptor.getPayload();
		assertTrue( payload.size() == 1 );
		assertEquals( payload.iterator().next(), Severity.Info.class, "Wrong payload" );

		descriptor = getConstraintDescriptor( Order.class, "orderNumber" );
		payload = descriptor.getPayload();
		assertTrue( payload != null );
		assertTrue( payload.size() == 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "j")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "k")
	public void testComposingConstraintsPayload() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertEquals( composingDescriptors.size(), 2, "Wrong number of composing constraints" );
		for ( ConstraintDescriptor<?> desc : composingDescriptors ) {
			assertTrue( desc.getGroups().size() == 1 );
			assertEquals( desc.getPayload().iterator().next(), Severity.Info.class, "Wrong payload" );
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "l")
	public void testGetValidationAppliesTo() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "age" );
		ConstraintTarget constraintTarget = descriptor.getValidationAppliesTo();
		assertNotNull( constraintTarget );
		assertEquals( constraintTarget, ConstraintTarget.RETURN_VALUE );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "l")
	public void testGetValidationAppliesToFromComposingConstraint() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "age" );

		Set<ConstraintDescriptor<?>> composingDescriptors = descriptor.getComposingConstraints();
		assertEquals( composingDescriptors.size(), 1, "Wrong number of composing constraints" );

		ConstraintTarget constraintTarget = composingDescriptors.iterator().next().getValidationAppliesTo();
		assertNotNull( constraintTarget );
		assertEquals( constraintTarget, ConstraintTarget.RETURN_VALUE );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONSTRAINTDESCRIPTOR, id = "l")
	public void testGetValidationAppliesToReturnsNull() {
		ConstraintDescriptor<?> descriptor = getConstraintDescriptor( Person.class, "firstName" );
		ConstraintTarget constraintTarget = descriptor.getValidationAppliesTo();
		assertNull( constraintTarget );
	}

	private ConstraintDescriptor<?> getConstraintDescriptor(Class<?> clazz, String property) {
		Set<ConstraintDescriptor<?>> descriptors = getConstraintDescriptorsFor( clazz, property );
		assertTrue( descriptors.size() == 1, "There should only by one descriptor." );
		return descriptors.iterator().next();
	}
}
