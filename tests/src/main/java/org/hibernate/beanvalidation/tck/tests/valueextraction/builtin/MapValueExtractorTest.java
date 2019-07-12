/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.builtin;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

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
public class MapValueExtractorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( MapValueExtractorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "c")
	public void mapValueExtractor() {
		Validator validator = getValidator();

		Map<String, String> map = new HashMap<>();
		map.put( "valid1", "valid2" );
		map.put( null, "valid3" );
		map.put( "valid4", null );

		Set<ConstraintViolation<MapHolder>> violations = validator.validate( new MapHolder( map ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "map" )
						.containerElement( "<map value>", true, "valid4", null, Map.class, 1 ),
				pathWith()
						.property( "map" )
						.containerElement( "<map key>", true, null, null, Map.class, 0 )
		);
	}

	private static class MapHolder {

		@SuppressWarnings("unused")
		private final Map<@NotNull String, @NotNull String> map;

		private MapHolder(Map<String, String> map) {
			this.map = map;
		}
	}
}
