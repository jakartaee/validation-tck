/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Size} built-in constraint.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class SizeConstraintTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( SizeConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7", id = "a"),
			@SpecAssertion(section = "7", id = "k")
	})
	public void testSizeConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		SizeDummyEntity dummy = new SizeDummyEntity();

		Set<ConstraintViolation<SizeDummyEntity>> constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );

		dummy.collection = new HashSet<String>();
		dummy.collection.add( "foo" );
		dummy.collection.add( "bar" );

		dummy.string = "";

		dummy.map = new HashMap<String, String>();
		dummy.map.put( "key1", "value1" );
		dummy.map.put( "key2", "value2" );

		dummy.integerArray = new Integer[0];

		dummy.intArray = new int[0];

		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 5 );
		assertCorrectPropertyPaths(
				constraintViolations, "collection", "map", "string", "integerArray", "intArray"
		);

		dummy.collection.remove( "bar" );
		dummy.string = "a";
		dummy.integerArray = new Integer[1];
		dummy.intArray = new int[1];
		dummy.map.remove( "key1" );
		constraintViolations = validator.validate( dummy );
		assertCorrectNumberOfViolations( constraintViolations, 0 );
	}

	private static class SizeDummyEntity {
		@Size(min = 1, max = 1)
		private String string;

		@Size(min = 1, max = 1)
		private Collection<String> collection;

		@Size(min = 1, max = 1)
		private Map<String, String> map;

		@Size(min = 1, max = 1)
		private Integer[] integerArray;

		@Size(min = 1, max = 1)
		private int[] intArray;
	}

}
