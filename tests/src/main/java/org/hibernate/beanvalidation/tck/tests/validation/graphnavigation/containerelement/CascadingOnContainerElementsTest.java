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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model.Cinema;
import org.hibernate.beanvalidation.tck.util.CollectionHelper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class CascadingOnContainerElementsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( CascadingOnContainerElementsTest.class )
				.withPackage( Cinema.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ac")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_list_parameter_and_cascading_gets_validated() {
		TypeWithList l = new TypeWithList();
		l.bars = Arrays.asList( new Bar( 2 ), null );
		Set<ConstraintViolation<TypeWithList>> constraintViolations = getValidator().validate( l );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "bars" )
								.property( "number", true, null, 0, List.class, 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "bars" )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ac")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_map_parameter_value_and_cascading_gets_validated() {
		TypeWithMapValue m = new TypeWithMapValue();
		m.barMap = new HashMap<>();
		m.barMap.put( "bar", new Bar( 2 ) );
		m.barMap.put( "foo", null );

		Set<ConstraintViolation<TypeWithMapValue>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "barMap" )
								.property( "number", true, "bar", null, Map.class, 1 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "barMap" )
								.containerElement( "<map value>", true, "foo", null, Map.class, 1 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ac")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_map_parameter_key_and_cascading_gets_validated() {
		Bar bar = new Bar( 2 );
		TypeWithMapKey m = new TypeWithMapKey();
		m.barMap = new HashMap<>();
		m.barMap.put( bar, "bar" );
		m.barMap.put( null, "foo" );

		Set<ConstraintViolation<TypeWithMapKey>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "barMap" )
								.property( "number", true, bar, null, Map.class, 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "barMap" )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						)
		);
	}


	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ac")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_optional_parameter_and_cascading_gets_validated() {
		TypeWithOptional o = new TypeWithOptional();
		o.bar = Optional.empty();
		Set<ConstraintViolation<TypeWithOptional>> constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "bar" )
		);

		o = new TypeWithOptional();
		o.bar = Optional.of( new Bar( 2 ) );
		constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "bar" )
								.property( "number", false, null, null, Optional.class, 0 )
						)
		);
	}


	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ac")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_set_parameter_and_cascading_gets_validated() {
		TypeWithSet s = new TypeWithSet();
		s.bars = CollectionHelper.asSet( new Bar( 2 ), null );
		Set<ConstraintViolation<TypeWithSet>> constraintViolations = getValidator().validate( s );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.property( "bars" )
								.property( "number", true, null, null, Set.class, 0 )
						),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "bars" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	public void cascading_on_container_element_of_method_return_value_is_applied() throws NoSuchMethodException, SecurityException {
		Set<ConstraintViolation<BarService>> constraintViolations = getExecutableValidator()
				.validateReturnValue( new BarService(), BarService.class.getMethod( "retrieveBars" ), Arrays.asList( new Bar( 2 ) ) );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( "retrieveBars" )
								.returnValue()
								.property( "number", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	public void cascading_on_container_element_of_method_parameter_is_applied() throws NoSuchMethodException, SecurityException {
		Set<ConstraintViolation<BarService>> constraintViolations = getExecutableValidator()
				.validateParameters( new BarService(), BarService.class.getMethod( "addBars", List.class ), new Object[]{ Arrays.asList( new Bar( 2 ) ) } );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.method( "addBars" )
								.parameter( "bars", 0 )
								.property( "number", true, null, 0, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	public void cascading_on_container_element_of_constructor_parameter_is_applied() throws NoSuchMethodException, SecurityException {
		Set<ConstraintViolation<BarService>> constraintViolations = getExecutableValidator()
				.validateConstructorParameters( BarService.class.getConstructor( List.class ), new Object[]{ Arrays.asList( new Bar( 2 ) ) } );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Min.class )
						.withPropertyPath( pathWith()
								.constructor( BarService.class )
								.parameter( "bars", 0 )
								.property( "number", true, null, 0, List.class, 0 )
						)
		);
	}

	private static class TypeWithList {

		@SuppressWarnings("unused")
		private List<@NotNull @Valid Bar> bars;
	}

	private static class TypeWithMapKey {

		private Map<@NotNull @Valid Bar, String> barMap;
	}

	private static class TypeWithMapValue {

		private Map<String, @NotNull @Valid Bar> barMap;
	}

	private static class TypeWithOptional {

		@SuppressWarnings("unused")
		private Optional<@NotNull @Valid Bar> bar;
	}

	private static class TypeWithSet {

		@SuppressWarnings("unused")
		private Set<@NotNull @Valid Bar> bars;
	}

	private static class Bar {

		@Min(4)
		private Integer number;

		public Bar(Integer number) {
			this.number = number;
		}
	}

	private static class BarService {

		public BarService() {
		}

		@SuppressWarnings("unused")
		public BarService(List<@Valid Bar> bars) {
		}

		@SuppressWarnings("unused")
		public List<@Valid Bar> retrieveBars() {
			return null;
		}

		@SuppressWarnings("unused")
		public void addBars(List<@Valid Bar> bars) {
		}
	}
}
