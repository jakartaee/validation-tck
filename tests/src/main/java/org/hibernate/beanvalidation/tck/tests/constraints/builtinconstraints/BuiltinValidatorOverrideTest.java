/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.constraints.builtinconstraints;

import java.io.InputStream;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getInputStreamForPath;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class BuiltinValidatorOverrideTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( BuiltinConstraintsTest.class )
				.withResource( "builtin-constraints-override.xml" )
				.withClass( InvertedNotNullValidator.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "7", id = "b")
	public void testXmlConfiguredValidatorConfigurationHasPrecedence() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		InputStream in = getInputStreamForPath(
				"org/hibernate/beanvalidation/tck/tests/constraints/builtinconstraints/builtin-constraints-override.xml"
		);
		config.addMapping( in );
		Validator validator = config.buildValidatorFactory().getValidator();
		DummyEntity dummy = new DummyEntity();
		Set<ConstraintViolation<DummyEntity>> violations = validator.validate( dummy );
		assertCorrectNumberOfViolations( violations, 0 );

		dummy.dummyProperty = "foobar";
		violations = validator.validate( dummy );
		assertCorrectNumberOfViolations( violations, 1 );
	}

	class DummyEntity {
		@NotNull
		String dummyProperty;
	}
}
