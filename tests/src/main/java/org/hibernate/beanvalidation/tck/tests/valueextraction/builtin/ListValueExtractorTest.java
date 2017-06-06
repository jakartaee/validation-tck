/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.builtin;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Arrays;
import java.util.List;
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
public class ListValueExtractorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ListValueExtractorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "b")
	public void listValueExtractor() {
		Validator validator = getValidator();

		Set<ConstraintViolation<ListHolder>> violations = validator.validate( new ListHolder( Arrays.asList( "valid", null ) ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "list" )
						.containerElement( "<list element>", true, null, 1, List.class, 0 )
		);
	}

	private static class ListHolder {

		@SuppressWarnings("unused")
		private final List<@NotNull String> list;

		private ListHolder(List<String> list) {
			this.list = list;
		}
	}
}
