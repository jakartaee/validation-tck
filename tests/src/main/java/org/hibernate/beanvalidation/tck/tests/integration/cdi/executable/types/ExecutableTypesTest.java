/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable.types;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.IntegrationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@IntegrationTest
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ExecutableTypesTest extends AbstractTCKTest {

	@Inject
	private CalendarService calendar;

	@Inject
	private Instance<OnlineCalendarService> onlineCalendar;

	@Inject
	private Instance<OfflineCalendarService> offlineCalendar;

	@Inject
	private Instance<AnotherCalendarService> anotherCalendar;

	@Inject
	private Instance<YetAnotherCalendarService> yetAnotherCalendar;

	@Inject
	private DeliveryService deliveryService;

	@Inject
	private Instance<AnotherDeliveryService> anotherDeliveryService;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ExecutableTypesTest.class )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "i")
	public void testValidationOfConstrainedMethodWithExecutableTypeNONE() {
		Event event = calendar.createEvent( null );
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "i")
	public void testValidationOfConstrainedMethodWithEmptyExecutableTypes() {
		Event event = calendar.createEvent( -10 );
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "i")
	public void testValidationOfConstrainedMethodWithExecutableTypeNONEAndOther() {
		try {
			calendar.createEvent( (long) -10 );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Min.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "j")
	public void testValidationOfConstrainedConstructorParametersWithExecutableTypeCONSTRUCTORS() {
		try {
			onlineCalendar.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "j")
	public void testValidationOfConstrainedConstructorReturnValueWithExecutableTypeCONSTRUCTORS() {
		try {
			offlineCalendar.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( ValidObject.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "j")
	public void testValidationOfConstrainedConstructorWithoutExecutableTypeCONSTRUCTORS() {
		AnotherCalendarService calendar = anotherCalendar.get();
		assertNotNull( calendar );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "k")
	public void testValidationOfConstrainedMethodParametersWithExecutableTypeNON_GETTER_METHODS() {
		try {
			calendar.createEvent( (short) -10 );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Min.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "k")
	public void testValidationOfConstrainedMethodReturnValueWithExecutableTypeNON_GETTER_METHODS() {
		try {
			calendar.createEvent( (byte) -10 );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( ValidObject.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "k")
	public void testValidationOfConstrainedGetterWithExecutableTypeNON_GETTER_METHODS() {
		Event event = calendar.getEvent();
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "l")
	public void testValidationOfConstrainedGetterReturnValueWithExecutableTypeGETTER_METHODS() {
		try {
			calendar.getSpecialEvent();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( ValidObject.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "l")
	public void testValidationOfConstrainedMethodWithExecutableTypeGETTER_METHODS() {
		Event event = calendar.getSpecialEvent( 0 );
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "m")
	public void testValidationOfConstrainedMethodWithExecutableTypeALL() {
		try {
			calendar.createEvent( -10.0 );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Min.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "m")
	public void testValidationOfConstrainedGetterWithExecutableTypeALL() {
		try {
			calendar.getVerySpecialEvent();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( ValidObject.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "m")
	public void testValidationOfConstrainedConstructorWithExecutableTypeALL() {
		try {
			yetAnotherCalendar.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "m")
	public void testValidationOfConstrainedMethodWithExecutableTypesALLAndNONE() {
		try {
			calendar.createEvent( (float) -10.0 );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Min.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "n")
	public void testValidationOfConstrainedMethodWithExecutableTypeIMPLICIT() {
		try {
			deliveryService.findDelivery( null );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "n")
	public void testValidationOfConstrainedGetterWithExecutableTypeIMPLICIT() {
		try {
			deliveryService.getDelivery();
			fail( "Getter invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "n")
	public void testValidationOfConstrainedGetterWithExecutableTypeIMPLICITOnTypeLevel() {
		Delivery delivery = deliveryService.getAnotherDelivery();
		assertNull( delivery );

		// success; the constraint is invalid, but no violation exception is
		// expected since @ValidateOnExecution(type=IMPLICIT) on the type-level
		// should have no effect and thus the default settings apply
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "n")
	public void testValidationOfConstrainedConstructorWithExecutableTypeIMPLICIT() {
		try {
			anotherDeliveryService.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class )
			);
		}
	}
}
