/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.ExtendedChecks1;
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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class LegacyValidOnContainerCascadingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( LegacyValidOnContainerCascadingTest.class )
				.withPackage( ValidOnListAndOnTypeArgumentWithGroupConversions.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnList() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnList>> constraintViolations = validator.validate( ValidOnList.invalid() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "name", true, null, 0, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnListAndOnTypeArgument() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnListAndOnTypeArgument>> constraintViolations = validator.validate( ValidOnListAndOnTypeArgument.invalid() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "name", true, null, 0, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnListWithoutTypeArgument() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnListWithoutTypeArgument>> constraintViolations = validator.validate( ValidOnListWithoutTypeArgument.invalid() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "name", true, null, 0, MyListWithoutTypeArgument.class, null )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnListAndOnTypeArgumentWithGroupConversions() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnListAndOnTypeArgumentWithGroupConversions>> constraintViolations =
				validator.validate( ValidOnListAndOnTypeArgumentWithGroupConversions.invalid() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "name", true, null, 0, List.class, 0 )
		);

		constraintViolations = validator.validate( ValidOnListAndOnTypeArgumentWithGroupConversions.invalid(), ExtendedChecks1.class );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "extended2", true, null, 0, List.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	public void testValidOnIterable() {
		Validator validator = getValidator();
		Set<ConstraintViolation<ValidOnIterable>> constraintViolations = validator.validate( ValidOnIterable.invalid() );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "name", true, null, null, Set.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "h")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "j")
	public void testValidOnMap() {
		Validator validator = getValidator();

		Museum museum = Museum.invalid();

		Set<ConstraintViolation<ValidOnMap>> constraintViolations = validator.validate( ValidOnMap.invalid( museum ) );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "visitors" )
						.property( "name", true, museum, null, Map.class, 1 )
		);
	}

	private static class ValidOnList {

		@Valid
		private final List<Visitor> visitors;

		private ValidOnList(List<Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnList invalid() {
			return new ValidOnList( new ArrayList<Visitor>( Arrays.asList( new Visitor( null ) ) ) );
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
		private final Set<Visitor> visitors;

		private ValidOnIterable(Set<Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnIterable invalid() {
			return new ValidOnIterable( CollectionHelper.asSet( new Visitor( null ) ) );
		}
	}

	private static class ValidOnMap {

		@Valid
		private final Map<Museum, Visitor> visitors;

		private ValidOnMap(Map<Museum, Visitor> visitors) {
			this.visitors = visitors;
		}

		private static ValidOnMap invalid(Museum museum) {
			Map<Museum, Visitor> map = new HashMap<>();
			map.put( museum, new Visitor( null ) );

			return new ValidOnMap( map );
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
