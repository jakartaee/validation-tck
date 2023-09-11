/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNoViolations;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for {@link Size} built-in constraint.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class SizeConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( SizeConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_SIZE, id = "a")
	public void testSizeConstraint() {
		Validator validator = TestUtil.getValidatorUnderTest();
		SizeDummyEntity dummy = new SizeDummyEntity();

		Set<ConstraintViolation<SizeDummyEntity>> constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );

		dummy.collection = new HashSet<String>();
		dummy.collection.add( "foo" );
		dummy.collection.add( "bar" );
		dummy.string = "";
		dummy.map = new HashMap<String, String>();
		dummy.map.put( "key1", "value1" );
		dummy.map.put( "key2", "value2" );
		dummy.integerArray = new Integer[0];
		dummy.booleanArray = new boolean[0];
		dummy.byteArray = new byte[0];
		dummy.charArray = new char[0];
		dummy.doubleArray = new double[0];
		dummy.floatArray = new float[0];
		dummy.intArray = new int[0];
		dummy.longArray = new long[0];
		dummy.shortArray = new short[0];

		constraintViolations = validator.validate( dummy );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Size.class ).withProperty( "collection" ),
				violationOf( Size.class ).withProperty( "map" ),
				violationOf( Size.class ).withProperty( "string" ),
				violationOf( Size.class ).withProperty( "integerArray" ),
				violationOf( Size.class ).withProperty( "booleanArray" ),
				violationOf( Size.class ).withProperty( "byteArray" ),
				violationOf( Size.class ).withProperty( "charArray" ),
				violationOf( Size.class ).withProperty( "doubleArray" ),
				violationOf( Size.class ).withProperty( "floatArray" ),
				violationOf( Size.class ).withProperty( "intArray" ),
				violationOf( Size.class ).withProperty( "longArray" ),
				violationOf( Size.class ).withProperty( "shortArray" )
		);

		dummy.collection.remove( "bar" );
		dummy.string = "a";
		dummy.integerArray = new Integer[1];
		dummy.booleanArray = new boolean[1];
		dummy.byteArray = new byte[1];
		dummy.charArray = new char[1];
		dummy.doubleArray = new double[1];
		dummy.floatArray = new float[1];
		dummy.intArray = new int[1];
		dummy.longArray = new long[1];
		dummy.shortArray = new short[1];
		dummy.map.remove( "key1" );
		constraintViolations = validator.validate( dummy );
		assertNoViolations( constraintViolations );
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
		private boolean[] booleanArray;

		@Size(min = 1, max = 1)
		private byte[] byteArray;

		@Size(min = 1, max = 1)
		private char[] charArray;

		@Size(min = 1, max = 1)
		private double[] doubleArray;

		@Size(min = 1, max = 1)
		private float[] floatArray;

		@Size(min = 1, max = 1)
		private int[] intArray;

		@Size(min = 1, max = 1)
		private long[] longArray;

		@Size(min = 1, max = 1)
		private short[] shortArray;
	}
}
