/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;


import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class GetterDefinitionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( GetterDefinitionTest.class )
				.withClasses( Shipment.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "d")
	public void testGetterMethod() {
		Shipment shipment = new Shipment();

		Set<ConstraintViolation<Shipment>> constraintViolations = getValidator().validateProperty( shipment, "id" );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_REQUIREMENTS, id = "d")
	public void testBooleanGetterMethod() {
		Shipment shipment = new Shipment();

		Set<ConstraintViolation<Shipment>> constraintViolations = getValidator().validateProperty( shipment, "shipped" );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( AssertTrue.class )
		);
	}
}
