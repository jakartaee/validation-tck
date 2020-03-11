/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.containerelement;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class SameElementContainedSeveralTimesInCollectionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( SameElementContainedSeveralTimesInCollectionTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void sameInvalidInstanceInListShouldBeReportedWithAllPaths() {
		ListContainer listContainer = new ListContainer( Arrays.asList( "", "A", "" ) );

		Set<ConstraintViolation<ListContainer>> constraintViolations = getValidator().validate( listContainer );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "values" )
								.containerElement( "<list element>", true, null, 0, List.class, 0 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "values" )
								.containerElement( "<list element>", true, null, 2, List.class, 0 )
						)
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "c")
	public void sameInvalidInstanceInMapShouldBeReportedWithAllPaths() {
		List<String> emptyList = Collections.emptyList();
		List<String> nonEmptyList = Arrays.asList( "A" );

		HashMap<String, List<String>> values = new HashMap<>();
		values.put( "NON_EMPTY", nonEmptyList );
		values.put( "EMPTY_1", emptyList );
		values.put( "EMPTY_2", emptyList );
		MapContainer withMap = new MapContainer( values );

		Set<ConstraintViolation<MapContainer>> constraintViolations = getValidator().validate( withMap );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "values" )
								.containerElement( "<map value>", true, "EMPTY_1", null, Map.class, 1 )
						),
				violationOf( Size.class )
						.withPropertyPath( pathWith()
								.property( "values" )
								.containerElement( "<map value>", true, "EMPTY_2", null, Map.class, 1 )
						)
		);
	}

	private static class ListContainer {

		@SuppressWarnings("unused")
		public List<@Size(min = 1) String> values;

		public ListContainer(List<String> values) {
			this.values = values;
		}
	}

	private static class MapContainer {

		@SuppressWarnings("unused")
		public Map<String, @Size(min = 1) List<String>> values;

		public MapContainer(Map<String, List<String>> values) {
			this.values = values;
		}
	}
}
