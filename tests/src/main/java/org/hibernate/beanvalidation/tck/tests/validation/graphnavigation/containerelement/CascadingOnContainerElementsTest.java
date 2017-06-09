/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ab")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_list_parameter_and_cascading_gets_validated() {
		TypeWithList l = new TypeWithList();
		l.bars = Arrays.asList( new Bar( 2 ), null );
		Set<ConstraintViolation<TypeWithList>> constraintViolations = getValidator().validate( l );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Min.class )
						.propertyPath( pathWith()
								.property( "bars" )
								.property( "number", true, null, 0, List.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "bars" )
								.containerElement( "<list element>", true, null, 1, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ab")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_map_parameter_valueand_cascading_gets_validated() {
		TypeWithMapValue m = new TypeWithMapValue();
		m.barMap = new HashMap<>();
		m.barMap.put( "bar", new Bar( 2 ) );
		m.barMap.put( "foo", null );

		Set<ConstraintViolation<TypeWithMapValue>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Min.class )
						.propertyPath( pathWith()
								.property( "barMap" )
								.property( "number", true, "bar", null, Map.class, 1 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "barMap" )
								.containerElement( "<map value>", true, "foo", null, Map.class, 1 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ab")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_map_parameter_keyand_cascading_gets_validated() {
		Bar bar = new Bar( 2 );
		TypeWithMapKey m = new TypeWithMapKey();
		m.barMap = new HashMap<>();
		m.barMap.put( bar, "bar" );
		m.barMap.put( null, "foo" );

		Set<ConstraintViolation<TypeWithMapKey>> constraintViolations = getValidator().validate( m );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Min.class )
						.propertyPath( pathWith()
								.property( "barMap" )
								.property( "number", true, bar, null, Map.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "barMap" )
								.containerElement( "<map key>", true, null, null, Map.class, 0 )
						)
		);
	}


	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ab")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_optional_parameter_and_cascading_gets_validated() {
		TypeWithOptional o = new TypeWithOptional();
		o.bar = Optional.empty();
		Set<ConstraintViolation<TypeWithOptional>> constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith().property( "bar" ) )
		);

		o = new TypeWithOptional();
		o.bar = Optional.of( new Bar( 2 ) );
		constraintViolations = getValidator().validate( o );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Min.class )
						.propertyPath( pathWith()
								.property( "bar" )
								.property( "number", false, null, null, Optional.class, 0 )
						)
		);
	}


	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS_GRAPHVALIDATION, id = "l")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ab")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void constraint_provided_on_custom_bean_used_as_set_parameter_and_cascading_gets_validated() {
		TypeWithSet s = new TypeWithSet();
		s.bars = CollectionHelper.asSet( new Bar( 2 ), null );
		Set<ConstraintViolation<TypeWithSet>> constraintViolations = getValidator().validate( s );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationWith()
						.constraintType( Min.class )
						.propertyPath( pathWith()
								.property( "bars" )
								.property( "number", true, null, null, Set.class, 0 )
						),
				violationWith()
						.constraintType( NotNull.class )
						.propertyPath( pathWith()
								.property( "bars" )
								.containerElement( "<iterable element>", true, null, null, Set.class, 0 )
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
}
