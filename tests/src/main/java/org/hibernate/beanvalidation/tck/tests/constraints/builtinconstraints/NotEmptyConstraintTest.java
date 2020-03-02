/**
 * Jakarta Bean Validation TCK
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
import jakarta.validation.constraints.NotEmpty;

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
	@SpecAssertion(section = Sections.BUILTINCONSTRAINTS_NOTEMPTY, id = "a")
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
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotEmpty.class ).withProperty( "collection" ),
				violationOf( NotEmpty.class ).withProperty( "map" ),
				violationOf( NotEmpty.class ).withProperty( "string" ),
				violationOf( NotEmpty.class ).withProperty( "stringBuilder" ),
				violationOf( NotEmpty.class ).withProperty( "integerArray" ),
				violationOf( NotEmpty.class ).withProperty( "booleanArray" ),
				violationOf( NotEmpty.class ).withProperty( "byteArray" ),
				violationOf( NotEmpty.class ).withProperty( "charArray" ),
				violationOf( NotEmpty.class ).withProperty( "doubleArray" ),
				violationOf( NotEmpty.class ).withProperty( "floatArray" ),
				violationOf( NotEmpty.class ).withProperty( "intArray" ),
				violationOf( NotEmpty.class ).withProperty( "longArray" ),
				violationOf( NotEmpty.class ).withProperty( "shortArray" )
		);

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
		assertNoViolations( constraintViolations );
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
