/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getParameterName;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Method;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.executable.ExecutableValidator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ParameterNameProviderSpecifiedInValidationXmlTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ParameterNameProviderSpecifiedInValidationXmlTest.class )
				.withClasses(
						CreditCard.class,
						CreditCardValidationService.class,
						ConfigurationDefinedConstraintValidatorFactoryResolver.class,
						CustomParameterNameProvider.class
				)
				.withValidationXml(
						"validation-ParameterNameProviderSpecifiedInValidationXmlTest.xml"
				)
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_BOOTSTRAPPING_XMLCONFIGURATION, id = "i")
	public void testParameterNameProviderSpecifiedInValidationXml() throws Exception {
		//given
		Object object = new CreditCardValidationService();
		Method method = CreditCardValidationService.class.getMethod(
				"validateCreditCard",
				CreditCard.class
		);
		Object[] parameterValues = new Object[] { null };

		ExecutableValidator validator = TestUtil.getValidatorUnderTest().forExecutables();

		//when
		Set<ConstraintViolation<Object>> violations = validator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		String actualName = getParameterName( violations.iterator().next().getPropertyPath() );
		assertEquals(
				actualName,
				"param0",
				"Parameter name provider configured in XML wasn't applied"
		);
	}
}
