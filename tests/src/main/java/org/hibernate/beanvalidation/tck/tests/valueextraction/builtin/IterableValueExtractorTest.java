/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.builtin;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

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
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class IterableValueExtractorTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( IterableValueExtractorTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "a")
	public void iterableValueExtractor() {
		Validator validator = getValidator();

		Set<ConstraintViolation<IterableHolder>> violations = validator.validate( new IterableHolder( new IterableImpl<>( Arrays.asList( "valid", null ) ) ) );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "iterable" )
						.containerElement( "<iterable element>", true, null, null, Iterable.class, 0 )
		);
	}

	private static class IterableHolder {

		@SuppressWarnings("unused")
		private final Iterable<@NotNull String> iterable;

		private IterableHolder(Iterable<String> iterable) {
			this.iterable = iterable;
		}
	}

	private static class IterableImpl<T> implements Iterable<T> {

		private final List<T> innerList;

		private IterableImpl(List<T> innerList) {
			this.innerList = innerList;
		}

		@Override
		public Iterator<T> iterator() {
			return innerList.iterator();
		}
	}
}
