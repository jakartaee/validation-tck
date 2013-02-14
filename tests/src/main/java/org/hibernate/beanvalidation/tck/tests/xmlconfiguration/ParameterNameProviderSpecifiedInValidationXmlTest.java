/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.lang.reflect.Method;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getParameterName;
import static org.testng.Assert.assertEquals;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ParameterNameProviderSpecifiedInValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
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
	@SpecAssertion(section = "5.5.6", id = "i")
	public void testParameterNameSpecifiedInValidationXml() throws Exception {
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
