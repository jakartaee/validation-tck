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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.clazzlevel;

import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ClassLevelOverridingTest extends Arquillian {

	public final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/constraintdeclaration/clazzlevel/";
	public final static String mappingFile1 = "package-constraints-ClassLevelOverridingTest.xml";
	public final static String mappingFile2 = "package-constraints-ClassLevelOverridingImplicitOverrideTest.xml";
	public final static String mappingFile3 = "package-constraints-ClassLevelOverridingWithAnnotationTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ClassLevelOverridingTest.class )
				.withResource( ClassLevelOverridingTest.mappingFile1 )
				.withResource( ClassLevelOverridingTest.mappingFile2 )
				.withResource( ClassLevelOverridingTest.mappingFile3 )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1.1", id = "c")
	})
	public void testIgnoreClassLevelAnnotations() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		Package p = new Package();
		Set<ConstraintViolation<Package>> violations = validator.validate( p );

		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintViolationMessages( violations, "ValidPackage defined in XML" );
	}

	@Test
	@SpecAssertion(section = "8.1.1.1", id = "b")
	public void testIgnoreAnnotationsFromEnclosingBeanIsApplied() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		Package p = new Package();
		Set<ConstraintViolation<Package>> violations = validator.validate( p );

		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintViolationMessages( violations, "ValidPackage defined in XML" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.1", id = "a"),
			@SpecAssertion(section = "8.1.1.1", id = "d")
	})
	public void testClassLevelAnnotationsApplied() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile3 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		Package p = new Package();
		Set<ConstraintViolation<Package>> violations = validator.validate( p );

		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintViolationMessages(
				violations, "ValidPackage defined in XML", "ValidPackage defined as annotation"
		);
	}
}
