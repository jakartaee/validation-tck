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
package org.hibernate.beanvalidation.tck.tests.messageinterpolation;

import java.util.Date;
import java.util.Locale;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.MessageInterpolator;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.validation.metadata.ConstraintDescriptor;

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
import static org.hibernate.beanvalidation.tck.util.TestUtil.getDefaultMessageInterpolator;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getValidatorUnderTest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class MessageInterpolationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( MessageInterpolationTest.class )
				.withResource( "ValidationMessages.properties", "ValidationMessages.properties", true )
				.withResource( "ValidationMessages_de.properties", "ValidationMessages_de.properties", true )
				.build();
	}

	@Test
	@SpecAssertion(section = "5.3.1", id = "a")
	public void testDefaultMessageInterpolatorIsNotNull() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		assertNotNull( interpolator, "Each bean validation provider must provide a default message interpolator." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1", id = "e"),
			@SpecAssertion(section = "5.3.1.1", id = "a")
	})
	public void testSuccessfulInterpolationOfValidationMessagesValue() {

		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "replacement worked";
		String actual = interpolator.interpolate( "{foo}", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "replacement worked replacement worked";
		actual = interpolator.interpolate( "{foo} {foo}", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "This replacement worked just fine";
		actual = interpolator.interpolate( "This {foo} just fine", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "{} replacement worked {unknown}";
		actual = interpolator.interpolate( "{} {foo} {unknown}", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "b")
	public void testRecursiveMessageInterpolation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "fubar" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "recursion worked";
		String actual = interpolator.interpolate( (String) descriptor.getAttributes().get( "message" ), context );
		assertEquals(
				expected, actual, "Expansion should be recursive"
		);
	}

	@Test
	@SpecAssertion(section = "5.3.1", id = "d")
	public void testMessagesCanBeOverriddenAtConstraintLevel() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Set<ConstraintViolation<DummyEntity>> constraintViolations = validator.validateProperty(
				new DummyEntity(), "snafu"
		);
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages(
				constraintViolations, "messages can also be overridden at constraint declaration."
		);
	}


	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.3.1", id = "f"),
			@SpecAssertion(section = "5.3.1", id = "g"),
			@SpecAssertion(section = "5.3.1", id = "h")
	})
	public void testLiteralCurlyBraces() {

		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "{";
		String actual = interpolator.interpolate( "\\{", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "}";
		actual = interpolator.interpolate( "\\}", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "\\";
		actual = interpolator.interpolate( "\\", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "a")
	public void testUnSuccessfulInterpolation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "foo";  // missing {}
		String actual = interpolator.interpolate( "foo", context );
		assertEquals( actual, expected, "Wrong substitution" );

		expected = "#{foo  {}";
		actual = interpolator.interpolate( "#{foo  {}", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "a")
	public void testUnknownTokenInterpolation() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "{bar}";  // unknown token {}
		String actual = interpolator.interpolate( "{bar}", context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "c")
	public void testParametersAreExtractedFromBeanValidationProviderBundle() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( Person.class, "birthday" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String key = "{javax.validation.constraints.Past.message}"; // Past is a built-in constraint so the provider must provide a default message
		String actual = interpolator.interpolate( key, context );
		assertFalse(
				key.equals( actual ),
				"There should have been a message interpolation from the bean validator provider bundle."
		);
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "f")
	public void testConstraintAttributeValuesAreInterpolated() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "bar" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "size must be between 5 and 10";
		String actual = interpolator.interpolate( (String) descriptor.getAttributes().get( "message" ), context );
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "g")
	public void testMessageInterpolationWithLocale() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String expected = "kann nicht null sein";
		String actual = interpolator.interpolate(
				(String) descriptor.getAttributes().get( "message" ), context, Locale.GERMAN
		);
		assertEquals( actual, expected, "Wrong substitution" );
	}

	@Test
	@SpecAssertion(section = "5.3.1.1", id = "h")
	public void testIfNoLocaleIsSpecifiedTheDefaultLocaleIsAssumed() {
		MessageInterpolator interpolator = getDefaultMessageInterpolator();
		ConstraintDescriptor<?> descriptor = getDescriptorFor( DummyEntity.class, "foo" );
		String messageTemplate = (String) descriptor.getAttributes().get( "message" );
		MessageInterpolator.Context context = new TestContext( descriptor );

		String messageInterpolatedWithNoLocale = interpolator.interpolate( messageTemplate, context );
		String messageInterpolatedWithDefaultLocale = interpolator.interpolate(
				messageTemplate, context, Locale.getDefault()
		);

		assertEquals( messageInterpolatedWithNoLocale, messageInterpolatedWithDefaultLocale, "Wrong substitution" );
	}

	private ConstraintDescriptor<?> getDescriptorFor(Class<?> clazz, String propertyName) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz )
				.getConstraintsForProperty( propertyName )
				.getConstraintDescriptors()
				.iterator()
				.next();
	}

	public class TestContext implements MessageInterpolator.Context {
		ConstraintDescriptor<?> descriptor;

		TestContext(ConstraintDescriptor<?> descriptor) {
			this.descriptor = descriptor;
		}

		@Override
		public ConstraintDescriptor<?> getConstraintDescriptor() {
			return descriptor;
		}

		@Override
		public Object getValidatedValue() {
			return null;
		}

		@Override
		public <T> T unwrap(Class<T> type) {
			throw new RuntimeException( "ups" );
		}
	}

	public class DummyEntity {
		@NotNull
		String foo;

		@Size(min = 5, max = 10, message = "size must be between {min} and {max}")
		String bar;

		@Max(value = 10, message = "{replace.in.user.bundle1}")
		String fubar;

		@NotNull(message = "messages can also be overridden at constraint declaration.")
		String snafu;
	}

	public class Person {

		String name;

		@Past
		Date birthday;
	}
}
