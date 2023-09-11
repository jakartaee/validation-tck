/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupsequence;


import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.GroupDefinitionException;
import jakarta.validation.GroupSequence;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class SequenceResolutionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( SequenceResolutionTest.class )
				.build();
	}

	@Test(expectedExceptions = GroupDefinitionException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "i")
	@SpecAssertion(section = Sections.EXCEPTION_GROUPDEFINITION, id = "a")
	public void testInvalidDefinitionOfDefaultSequenceInEntity() {
		Validator validator = TestUtil.getValidatorUnderTest();
		TestEntity entity = new TestEntity();
		validator.validate( entity, Complete.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "c")
	public void testGroupSequenceContainerOtherGroupSequences() {
		Validator validator = TestUtil.getValidatorUnderTest();
		TestEntity entity = new TestEntity();
		try {
			validator.validate( entity, InvalidGroupSequence.class );
		}
		catch ( GroupDefinitionException e ) {
			// success
		}
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPSEQUENCE, id = "j")
	public void testOnlyFirstGroupInSequenceGetEvaluated() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Car car = new Car( "USd-298" );

		// the constraint fails for each group
		Set<ConstraintViolation<Car>> violations = validator.validate( car, First.class );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);

		violations = validator.validate( car, Second.class );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( Pattern.class )
		);

		// if we validate against the sequence All we only get one violation since group Second won't be executed
		violations = validator.validate( car, All.class );
		assertThat( violations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);

		// if we validate against the sequence AllReverse we only get two violations since group First won't be executed
		violations = validator.validate( car, AllReverse.class );
		assertThat( violations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( Pattern.class )
		);
	}

	class Car {
		@Pattern(regexp = "[A-Z][A-Z][A-Z]-[0-9][0-9][0-9]", groups = { First.class, Second.class })
		private String licensePlateNumber;

		@NotNull(groups = Second.class)
		private String make;

		Car(String licensePlateNumber) {
			this.licensePlateNumber = licensePlateNumber;
		}

		public String getLicensePlateNumber() {
			return licensePlateNumber;
		}
	}

	interface First {
	}

	interface Second {
	}

	interface Third {
	}

	@GroupSequence({ First.class, Second.class, Third.class })
	interface All {
	}

	@GroupSequence({ Third.class, Second.class, First.class })
	interface AllReverse {
	}

	@GroupSequence({ Second.class, Third.class, First.class })
	interface Mixed {
	}

	@GroupSequence({ First.class, Third.class, Mixed.class })
	interface InvalidGroupSequence {
	}
}
