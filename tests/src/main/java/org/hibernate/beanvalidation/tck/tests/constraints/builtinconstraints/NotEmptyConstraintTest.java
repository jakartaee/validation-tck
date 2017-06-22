/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotEmpty;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link NotEmpty} built-in constraint.
 *
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class NotEmptyConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( NotEmptyConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "v")
	public void testNotEmptyConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		NotEmptyDummyEntity dummy = new NotEmptyDummyEntity();

		dummy.collection = new HashSet<String>();
		dummy.string = "";
		dummy.stringBuilder = new StringBuilder();
		dummy.map = new HashMap<String, String>();
		dummy.integerArray = new Integer[0];
		dummy.booleanArray = new boolean[0];
		dummy.byteArray = new byte[0];
		dummy.charArray = new char[0];
		dummy.doubleArray = new double[0];
		dummy.floatArray = new float[0];
		dummy.intArray = new int[0];
		dummy.longArray = new long[0];
		dummy.shortArray = new short[0];

		Set<ConstraintViolation<NotEmptyDummyEntity>> constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyPaths(
				pathWith()
						.property( "collection" ),
				pathWith()
						.property( "map" ),
				pathWith()
						.property( "string" ),
				pathWith()
						.property( "stringBuilder" ),
				pathWith()
						.property( "integerArray" ),
				pathWith()
						.property( "booleanArray" ),
				pathWith()
						.property( "byteArray" ),
				pathWith()
						.property( "charArray" ),
				pathWith()
						.property( "doubleArray" ),
				pathWith()
						.property( "floatArray" ),
				pathWith()
						.property( "intArray" ),
				pathWith()
						.property( "longArray" ),
				pathWith()
						.property( "shortArray" )
		);
		assertCorrectConstraintTypes( constraintViolations, NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class,
				NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class, NotEmpty.class );

		dummy.collection.add( "foo" );
		dummy.string = "a";
		dummy.stringBuilder.append( "a" );
		dummy.map.put( "key1", "value1" );
		dummy.integerArray = new Integer[1];
		dummy.booleanArray = new boolean[1];
		dummy.byteArray = new byte[1];
		dummy.charArray = new char[1];
		dummy.doubleArray = new double[1];
		dummy.floatArray = new float[1];
		dummy.intArray = new int[1];
		dummy.longArray = new long[1];
		dummy.shortArray = new short[1];
		constraintViolations = validator.validate( dummy );
		assertNumberOfViolations( constraintViolations, 0 );
	}

	private static class NotEmptyDummyEntity {
		@NotEmpty
		private String string;

		@NotEmpty
		private StringBuilder stringBuilder;

		@NotEmpty
		private Collection<String> collection;

		@NotEmpty
		private Map<String, String> map;

		@NotEmpty
		private Integer[] integerArray;

		@NotEmpty
		private boolean[] booleanArray;

		@NotEmpty
		private byte[] byteArray;

		@NotEmpty
		private char[] charArray;

		@NotEmpty
		private double[] doubleArray;

		@NotEmpty
		private float[] floatArray;

		@NotEmpty
		private int[] intArray;

		@NotEmpty
		private long[] longArray;

		@NotEmpty
		private short[] shortArray;
	}
}
