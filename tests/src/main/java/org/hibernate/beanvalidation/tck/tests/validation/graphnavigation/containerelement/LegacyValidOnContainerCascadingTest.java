/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.ValidOnListAndOnTypeArgumentWithGroupConversions;
import org.hibernate.beanvalidation.tck.util.CollectionHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that the legacy behavior of having @Valid on the container works correctly: it validates the elements of the
 * iterable.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class LegacyValidOnContainerCascadingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( LegacyValidOnContainerCascadingTest.class )
				.withPackage( ValidOnListAndOnTypeArgumentWithGroupConversions.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnList() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnList>> constraintViolations = validator.validate( ValidOnList.invalidValue() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "visitors" )
								.property( "name", true, null, 0, List.class, 0 )
						)
		);

		constraintViolations = validator.validate( ValidOnList.invalidSize() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "visitors" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnListAndOnTypeArgument() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnListAndOnTypeArgument>> constraintViolations = validator.validate( ValidOnListAndOnTypeArgument.invalid() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "visitors" )
								.property( "name", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "g")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnListWithoutTypeArgument() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnListWithoutTypeArgument>> constraintViolations = validator.validate( ValidOnListWithoutTypeArgument.invalid() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "visitors" )
								.property( "name", true, null, 0, MyListWithoutTypeArgument.class, null )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnIterableAndSet() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnIterable>> constraintViolations = validator.validate( ValidOnIterable.invalidValue() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "visitors" )
								.property( "name", true, null, null, Set.class, 0 )
						)
		);

		constraintViolations = validator.validate( ValidOnIterable.invalidSize() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "visitors" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "h")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "j")
	public void testValidOnMap() {
		Validator validator = getValidator();

		Museum museum = Museum.invalid();

		Set<ConstraintViolation<ValidOnMap>> constraintViolations = validator.validate( ValidOnMap.invalidValue( museum ) );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "visitors" )
								.property( "name", true, museum, null, Map.class, 1 )
						)
		);

		constraintViolations = validator.validate( ValidOnMap.invalidSize() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "visitors" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnArray() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnArray>> constraintViolations = validator.validate( ValidOnArray.invalidValue() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "visitors" )
								.property( "name", true, null, 0, Object[].class, null )
						)
		);

		constraintViolations = validator.validate( ValidOnArray.invalidSize() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "visitors" )
		);
	}

	private static class ValidOnList {

		@Valid
		@Size(min = 1)
		private final List<Visitor> visitors;

		private ValidOnList(List<Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnList invalidValue() {
			return new ValidOnList( new ArrayList<Visitor>( Arrays.asList( new Visitor( null ) ) ) );
		}

		private static ValidOnList invalidSize() {
			return new ValidOnList( Collections.emptyList() );
		}
	}

	private static class ValidOnListAndOnTypeArgument {

		@Valid
		private final List<@Valid Visitor> visitors;

		private ValidOnListAndOnTypeArgument(List<Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnListAndOnTypeArgument invalid() {
			return new ValidOnListAndOnTypeArgument( Arrays.asList( new Visitor( null ) ) );
		}
	}

	private static class ValidOnListWithoutTypeArgument {

		@Valid
		private final MyListWithoutTypeArgument visitors;

		private ValidOnListWithoutTypeArgument(MyListWithoutTypeArgument visitors) {
			this.visitors = visitors;
		}

		private static ValidOnListWithoutTypeArgument invalid() {
			return new ValidOnListWithoutTypeArgument( new MyListWithoutTypeArgument( Arrays.asList( new Visitor( null ) ) ) );
		}
	}

	private static class MyListWithoutTypeArgument extends ArrayList<Visitor> {

		private MyListWithoutTypeArgument(List<Visitor> elements) {
			addAll( elements );
		}
	}

	private static class Visitor {

		@NotNull
		private final String name;

		private Visitor(String name) {
			this.name = name;
		}
	}

	private static class ValidOnIterable {

		@Valid
		@Size(min = 1)
		private final Set<Visitor> visitors;

		private ValidOnIterable(Set<Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnIterable invalidValue() {
			return new ValidOnIterable( CollectionHelper.asSet( new Visitor( null ) ) );
		}

		private static ValidOnIterable invalidSize() {
			return new ValidOnIterable( Collections.emptySet() );
		}
	}

	private static class ValidOnMap {

		@Valid
		@Size(min = 1)
		private final Map<Museum, Visitor> visitors;

		private ValidOnMap(Map<Museum, Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnMap invalidValue(Museum museum) {
			Map<Museum, Visitor> map = new HashMap<>();
			map.put( museum, new Visitor( null ) );

			return new ValidOnMap( map );
		}

		private static ValidOnMap invalidSize() {
			return new ValidOnMap( new HashMap<>() );
		}
	}

	private static class ValidOnArray {

		@Valid
		@Size(min = 1)
		private final Visitor[] visitors;

		private ValidOnArray(Visitor[] visitors) {
			this.visitors = visitors;
		}

		private static ValidOnArray invalidValue() {
			Visitor[] visitors = new Visitor[]{ new Visitor( null ) };

			return new ValidOnArray( visitors );
		}

		private static ValidOnArray invalidSize() {
			Visitor[] visitors = new Visitor[0];

			return new ValidOnArray( visitors );
		}
	}

	private static class Museum {

		@NotNull
		private final String name;

		public Museum(String name) {
			this.name = name;
		}

		private static Museum invalid() {
			return new Museum( null );
		}
	}
}
