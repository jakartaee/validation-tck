/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import java.util.Calendar;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
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
public class ExecutableValidationTest extends AbstractTCKTest {

	@Inject
	private CalendarService calendar;

	@Inject
	private CalendarServiceWithCascadingReturnValue cascadingCalendar;

	@Inject
	private BookingService bookingService;

	@Inject
	private Instance<AnotherBookingService> anotherBookingService;

	@Inject
	private NameProducer nameProducer;

	@Inject
	private AnnotatedCalendarService annotatedCalendar;

	@Inject
	private ClassLevelAnnotatedCalendarService classLevelAnnotatedCalendar;

	@Inject
	private Instance<UserService> userServiceInstance;

	@Inject
	private Instance<PersonService> personServiceInstance;

	@Inject
	private OrderService orderService;

	@Inject
	private ShipmentService shipmentService;

	@Inject
	private ShipmentServiceSubClass shipmentServiceSubClass;

	@Inject
	private AnotherShipmentService2stInHierarchy anotherShipmentService;

	@Inject
	private DeliveryServiceImpl deliveryService;

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ExecutableValidationTest.class )
				.withEmptyBeansXml()
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testParameterValidationOfConstrainedMethod() {
		try {
			Calendar endDate = Calendar.getInstance();
			endDate.set( 1980, 1, 1 );
			calendar.createEvent( null, endDate.getTime() );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class ),
					violationOf( Future.class ),
					violationOf( CrossParameterConstraint.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testReturnValueValidationOfConstrainedMethod() {
		try {
			calendar.createEvent();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testCascadedReturnValueValidationOfConstrainedMethod() {
		try {
			cascadingCalendar.createValidEvent();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testGettersAreNotValidatedByDefault() {
		Event event = calendar.getEvent();
		assertNull( event, "The event should be null, since getters are not validated by default." );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testParameterValidationOfConstrainedConstructor() {
		try {
			nameProducer.setName( "Bob" );
			userServiceInstance.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class ),
					violationOf( CrossParameterConstraint.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "a")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "b")
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "c")
	@SpecAssertion(section = Sections.INTEGRATION_DEPENDENCYINJECTION, id = "a")
	public void testReturnValueValidationOfConstrainedConstructor() {
		try {
			personServiceInstance.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( ValidPersonService.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "e")
	public void testValidationOfConstrainedMethodAnnotatedWithValidateOnExecutionContainingExecutableType() {
		try {
			annotatedCalendar.createEvent( null );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "e")
	public void testValidationOfConstrainedMethodAnnotatedWithValidateOnExecutionNotContainingExecutableType() {
		Event event = annotatedCalendar.createEvent( -10 );
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "f")
	public void testValidationOfConstrainedMethodOnClassAnnotatedWithValidateOnExecutionContainingExecutableType() {
		try {
			classLevelAnnotatedCalendar.getEvent();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "f")
	public void testValidationOfConstrainedMethodOnClassAnnotatedWithValidateOnExecutionNotContainingExecutableType() {
		Event event = classLevelAnnotatedCalendar.createEvent( null );
		assertNotNull( event );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "f")
	public void testValidationOfConstrainedMethodOnInterfaceAnnotatedWithValidateOnExecutionContainingExecutableType() {
		try {
			orderService.getOrder();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "f")
	public void testValidationOfConstrainedMethodOnInterfaceAnnotatedWithValidateOnExecutionNotContainingExecutableType() {
		Order order = orderService.placeOrder( null );
		assertNotNull( order );

		// success; the constraint is invalid, but no violation exception is
		// expected since the executable type is not given in @ValidateOnExecution
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_TRIGGERINGMETHODVALIDATION, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_TRIGGERINGMETHODVALIDATION, id = "b")
	public void testMethodValidationInvokesParameterAndReturnValueValidationUsingDefaultGroup() {
		//parameter constraint is violated
		try {
			bookingService.placeBooking( "9999" );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class )
			);
		}

		//method should not be invoked
		assertEquals( bookingService.getInvocationCount(), 0 );

		//parameter constraint is valid, but return value constraint is violated
		try {
			bookingService.placeBooking( "10000" );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( DecimalMin.class )
			);
		}

		//method should have been invoked
		assertEquals( bookingService.getInvocationCount(), 1 );

		//valid invocation
		String booking = bookingService.placeBooking( "10001" );
		assertEquals( booking, "10001" );

		assertEquals( bookingService.getInvocationCount(), 2 );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_TRIGGERINGMETHODVALIDATION, id = "a")
	@SpecAssertion(section = Sections.VALIDATIONAPI_TRIGGERINGMETHODVALIDATION, id = "b")
	public void testConstructorValidationInvokesParameterAndReturnValueValidationUsingDefaultGroup() {
		nameProducer.setName( "9999" );
		//parameter constraint is violated
		try {
			anotherBookingService.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class )
			);
		}

		//constructor should not be invoked
		assertEquals( AnotherBookingService.getInvocationCount(), 0 );

		//parameter constraint is valid, but return value constraint is violated
		nameProducer.setName( "10000" );
		try {
			anotherBookingService.get();
			fail( "Constructor invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( ValidAnotherBookingService.class )
			);
		}

		//constructor should have been invoked
		assertEquals( AnotherBookingService.getInvocationCount(), 1 );

		//valid invocation
		nameProducer.setName( "10001" );
		AnotherBookingService instance = anotherBookingService.get();
		assertNotNull( instance );

		assertEquals( AnotherBookingService.getInvocationCount(), 2 );
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesDefaultSettingSinceValidatedMethodImplementsAnInterfaceMethod() {
		try {
			shipmentService.findShipment( null );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesSettingFromSuperTypeMethodIfValidatedMethodImplementsAnInterfaceMethod() {
		try {
			shipmentService.getShipment();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesSettingFromSuperTypeIfValidatedMethodImplementsAnInterfaceMethod() {
		try {
			shipmentService.getAnotherShipment();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesDefaultSettingIfValidatedMethodOverridesASuperTypeMethod() {
		try {
			shipmentServiceSubClass.findShipment( null );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesSettingFromSuperTypeMethodIfValidatedMethodOverridesASuperTypeMethod() {
		try {
			shipmentServiceSubClass.getShipment();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesSettingFromSuperTypeIfValidatedMethodOverridesASuperTypeMethod() {
		try {
			shipmentServiceSubClass.getAnotherShipment();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesSettingFromHighestMethodInHierarchyIfValidatedMethodImplementsAnInterfaceMethod() {
		try {
			anotherShipmentService.getShipment();
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.INTEGRATION_GENERAL_EXECUTABLE, id = "h")
	public void testExecutableValidationUsesSettingFromSuperTypeForOverriddenMethodsAndLocalSettingForNonOverriddenMethod() {
		try {
			deliveryService.createDelivery( null );
			fail( "Method invocation should have caused a ConstraintViolationException" );
		}
		catch ( ConstraintViolationException e ) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}

		String expressDelivery = deliveryService.createExpressDelivery( null );
		assertNotNull( expressDelivery );

		// success; the constraint is invalid, but no violation exception is
		// expected since @ValidateOnExecution is given on the type
	}
}
