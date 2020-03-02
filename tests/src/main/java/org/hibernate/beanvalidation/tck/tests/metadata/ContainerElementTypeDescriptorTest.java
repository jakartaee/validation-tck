/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.assertConstraintDescriptors;
import static org.hibernate.beanvalidation.tck.tests.metadata.MetaDataTestUtil.getContainerElementDescriptor;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import jakarta.validation.metadata.PropertyDescriptor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ContainerElementTypeDescriptorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ContainerElementTypeDescriptorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	public void testGetContainerElementMetaDataForRoles() {
		PropertyDescriptor descriptor = getPropertyDescriptor( EmployeeImpl.class, "roles" );

		Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypeDescriptors.size(), 2 );

		ContainerElementTypeDescriptor iterableContainerElementTypeDescriptor = getContainerElementDescriptor( containerElementTypeDescriptors, Iterable.class, 0 );
		assertEquals( iterableContainerElementTypeDescriptor.getElementClass(), String.class );
		assertConstraintDescriptors( iterableContainerElementTypeDescriptor.getConstraintDescriptors(), NotNull.class );
		assertEquals( iterableContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 0 );
		assertFalse( iterableContainerElementTypeDescriptor.isCascaded() );
		assertEquals( iterableContainerElementTypeDescriptor.getGroupConversions().size(), 0 );

		ContainerElementTypeDescriptor setContainerElementTypeDescriptor = getContainerElementDescriptor( containerElementTypeDescriptors, Set.class, 0 );
		assertEquals( setContainerElementTypeDescriptor.getElementClass(), String.class );
		assertConstraintDescriptors( setContainerElementTypeDescriptor.getConstraintDescriptors(), NotBlank.class, NotEmpty.class );
		assertEquals( setContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 0 );
		assertFalse( setContainerElementTypeDescriptor.isCascaded() );
		assertEquals( setContainerElementTypeDescriptor.getGroupConversions().size(), 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	public void testGetContainerElementMetaDataForDivision() {
		PropertyDescriptor descriptor = getPropertyDescriptor( EmployeeImpl.class, "division" );

		Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypeDescriptors.size(), 1 );

		ContainerElementTypeDescriptor iterableContainerElementTypeDescriptor = getContainerElementDescriptor( containerElementTypeDescriptors, Optional.class, 0 );
		assertEquals( iterableContainerElementTypeDescriptor.getElementClass(), String.class );
		assertConstraintDescriptors( iterableContainerElementTypeDescriptor.getConstraintDescriptors(), NotBlank.class );
		assertEquals( iterableContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 0 );
		assertFalse( iterableContainerElementTypeDescriptor.isCascaded() );
		assertEquals( iterableContainerElementTypeDescriptor.getGroupConversions().size(), 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	public void testGetContainerElementMetaDataForColleagues() {
		PropertyDescriptor descriptor = getPropertyDescriptor( EmployeeImpl.class, "colleagues" );

		Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypeDescriptors.size(), 1 );

		ContainerElementTypeDescriptor iterableContainerElementTypeDescriptor = getContainerElementDescriptor( containerElementTypeDescriptors, Set.class, 0 );
		assertEquals( iterableContainerElementTypeDescriptor.getElementClass(), Employee.class );
		assertEquals( iterableContainerElementTypeDescriptor.getConstraintDescriptors().size(), 0 );
		assertEquals( iterableContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 0 );
		assertTrue( iterableContainerElementTypeDescriptor.isCascaded() );
		assertEquals( iterableContainerElementTypeDescriptor.getGroupConversions().size(), 1 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_ELEMENTDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "d")
	public void testGetContainerElementMetaDataForAddresses() {
		PropertyDescriptor descriptor = getPropertyDescriptor( EmployeeImpl.class, "addresses" );

		Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypeDescriptors.size(), 2 );

		ContainerElementTypeDescriptor mapKeyContainerElementTypeDescriptor = getContainerElementDescriptor( containerElementTypeDescriptors, Map.class, 0 );
		assertEquals( mapKeyContainerElementTypeDescriptor.getElementClass(), String.class );
		assertConstraintDescriptors( mapKeyContainerElementTypeDescriptor.getConstraintDescriptors(), NotNull.class );
		assertEquals( mapKeyContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 0 );
		assertFalse( mapKeyContainerElementTypeDescriptor.isCascaded() );
		assertEquals( mapKeyContainerElementTypeDescriptor.getGroupConversions().size(), 0 );

		ContainerElementTypeDescriptor mapValueContainerElementTypeDescriptor = getContainerElementDescriptor( containerElementTypeDescriptors, Map.class, 1 );
		assertEquals( mapValueContainerElementTypeDescriptor.getElementClass(), List.class );
		assertConstraintDescriptors( mapValueContainerElementTypeDescriptor.getConstraintDescriptors(), NotEmpty.class );
		assertEquals( mapValueContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 1 );
		assertFalse( mapValueContainerElementTypeDescriptor.isCascaded() );
		assertEquals( mapValueContainerElementTypeDescriptor.getGroupConversions().size(), 0 );

		Set<ContainerElementTypeDescriptor> nestedMapValueContainerElementTypeDescriptors = mapValueContainerElementTypeDescriptor
				.getConstrainedContainerElementTypes();

		assertEquals( nestedMapValueContainerElementTypeDescriptors.size(), 1 );

		ContainerElementTypeDescriptor addressContainerElementTypeDescriptor = getContainerElementDescriptor( nestedMapValueContainerElementTypeDescriptors, List.class, 0 );
		assertEquals( addressContainerElementTypeDescriptor.getElementClass(), Address.class );
		assertConstraintDescriptors( addressContainerElementTypeDescriptor.getConstraintDescriptors(), NotNull.class, ValidAddress.class );
		assertEquals( addressContainerElementTypeDescriptor.getConstrainedContainerElementTypes().size(), 0 );
		assertTrue( addressContainerElementTypeDescriptor.isCascaded() );
		assertEquals( addressContainerElementTypeDescriptor.getGroupConversions().size(), 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "b")
	public void testGetContainerElementMetaDataIfNotAContainerElement() {
		PropertyDescriptor descriptor = getPropertyDescriptor( EmployeeImpl.class, "intern" );

		Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypeDescriptors.size(), 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTMETADATA_CONTAINERDESCRIPTOR, id = "b")
	public void testGetContainerElementMetaDataForContainerElementWithoutConstraintAndValid() {
		PropertyDescriptor descriptor = getPropertyDescriptor( EmployeeImpl.class, "zoneId" );

		Set<ContainerElementTypeDescriptor> containerElementTypeDescriptors = descriptor.getConstrainedContainerElementTypes();

		assertEquals( containerElementTypeDescriptors.size(), 0 );
	}

	public interface LegalEntity {

		Iterable<@NotNull String> getRoles();
	}

	public interface Person extends LegalEntity {

		@Override
		Set<@NotBlank String> getRoles();
	}

	public interface Address {
	}

	public interface Employee extends Person {

		@Override
		Set<@NotEmpty String> getRoles();

		Map<@NotNull String, @NotEmpty List<@NotNull @Valid Address>> getAddresses();

		Set<@Valid @ConvertGroup(from = Default.class, to = BasicChecks.class) Employee> getColleagues();

		Optional<@NotBlank String> getDivision();
	}

	public class Roles extends HashSet<String> {
	}

	public class EmployeeImpl implements Employee {

		@Override
		public Roles getRoles() {
			return null;
		}

		@Override
		public Map<String, List<@ValidAddress Address>> getAddresses() {
			return null;
		}

		@Override
		public Set<Employee> getColleagues() {
			return null;
		}

		@Override
		public Optional<String> getDivision() {
			return null;
		}

		@AssertFalse
		public boolean isIntern() {
			return false;
		}

		@NotNull
		public Optional<ZoneId> getZoneId() {
			return Optional.of( ZoneId.systemDefault() );
		}
	}

	@Documented
	@Constraint(validatedBy = ValidAddressValidator.class)
	@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
	@Retention(RUNTIME)
	public @interface ValidAddress {

		String message() default "{org.hibernate.beanvalidation.tck.tests.metadata..ValidAddress.message}";

		Class<?>[] groups() default {};

		Class<? extends Payload>[] payload() default {};
	}

	public static class ValidAddressValidator implements ConstraintValidator<ValidAddress, Address> {

		@Override
		public boolean isValid(Address value, ConstraintValidatorContext context) {
			return true;
		}
	}
}
