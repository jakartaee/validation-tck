/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ContainerElementConstraintGroupValidationTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementConstraintGroupValidationTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "e")
	public void sameGroupValidationRulesAppliesOnContainerElementConstraints() {
		Set<ConstraintViolation<MovieProduction>> constraintViolations = getValidator().validate( MovieProduction.invalid() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "directors" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MovieProduction.invalid(), ActorGroup.class );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "mainActor" ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "actors" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MovieProduction.invalid(), LocationGroup.class );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "locations" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MovieProduction.invalid(), EmployeeGroupSequence.class );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ).withProperty( "mainActor" ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "actors" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);

		constraintViolations = getValidator().validate( MovieProduction.invalidWithValidActors(), EmployeeGroupSequence.class );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "executiveProducers" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						)
		);
	}

	private static class MovieProduction {

		private final List<@NotNull String> directors = new ArrayList<>();

		@NotNull(groups = ActorGroup.class)
		private String mainActor;

		private final List<@NotNull(groups = ActorGroup.class) String> actors = new ArrayList<>();

		private final List<@NotNull(groups = ExecutiveProducerGroup.class) String> executiveProducers = new ArrayList<>();

		private final List<@NotNull(groups = LocationGroup.class) String> locations = new ArrayList<>();

		private MovieProduction() {
		}

		private static MovieProduction invalid() {
			MovieProduction movieProduction = new MovieProduction();
			movieProduction.directors.add( null );
			movieProduction.actors.add( null );
			movieProduction.mainActor = null;
			movieProduction.executiveProducers.add( null );
			movieProduction.locations.add( null );
			return movieProduction;
		}

		private static MovieProduction invalidWithValidActors() {
			MovieProduction movieProduction = new MovieProduction();
			movieProduction.directors.add( null );
			movieProduction.actors.add( "Zach Braff" );
			movieProduction.mainActor = "Zach Braff";
			movieProduction.executiveProducers.add( null );
			movieProduction.locations.add( null );
			return movieProduction;
		}
	}

	public interface ActorGroup {
	}

	public interface ExecutiveProducerGroup {
	}

	@GroupSequence({ ActorGroup.class, ExecutiveProducerGroup.class })
	public interface EmployeeGroupSequence {
	}

	public interface LocationGroup {
	}
}
