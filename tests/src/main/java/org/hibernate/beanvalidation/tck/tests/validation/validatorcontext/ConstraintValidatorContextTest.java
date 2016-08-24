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
package org.hibernate.beanvalidation.tck.tests.validation.validatorcontext;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Payload;
import javax.validation.ValidationException;
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

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintViolationMessages;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ConstraintValidatorContextTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( ConstraintValidatorContextTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "3.4", id = "o")
	public void testDefaultError() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( false );
		DummyValidator.setCustomErrorMessages( null );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "dummy message" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "3.4", id = "t")
	public void testDisableDefaultErrorWithoutAddingCustomError() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );
		validator.validate( bean );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "p")
	})
	public void testDisableDefaultErrorWithCustomErrorNoSubNode() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		errors.put( null, "message1" );
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "message1" );
		assertCorrectPropertyPaths( constraintViolations, "value" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "p"),
			@SpecAssertion(section = "3.4", id = "s")
	})
	public void testDisableDefaultErrorWithCustomErrorWithSubNode() {
		Validator validator = TestUtil.getValidatorUnderTest();

		DummyValidator.disableDefaultError( true );
		Map<String, String> errors = new HashMap<String, String>();
		errors.put( "subnode", "subnode message" );
		DummyValidator.setCustomErrorMessages( errors );

		DummyBean bean = new DummyBean( "foobar" );

		Set<ConstraintViolation<DummyBean>> constraintViolations = validator.validate( bean );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectConstraintViolationMessages( constraintViolations, "subnode message" );
		assertCorrectPropertyPaths( constraintViolations, "value.subnode" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.4", id = "p"),
			@SpecAssertion(section = "3.4", id = "s")
	})
	public void propertyPathInIterable() {
		Validator validator = TestUtil.getValidatorUnderTest();
		Group group = new Group( Gender.MALE, new Person( Gender.FEMALE ) );

		Set<ConstraintViolation<Group>> constraintViolations = validator.validate( group );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectPropertyPaths( constraintViolations, "persons[0]" );
		assertCorrectConstraintTypes( constraintViolations, CompatiblePersons.class );
	}

	private enum Gender {
		MALE, FEMALE
	}

	@CompatiblePersons
	private class Group {
		Gender gender;
		List<Person> persons = new ArrayList<Person>();

		public Group(Gender gender, Person... persons) {
			this.gender = gender;
			this.persons.addAll( Arrays.asList( persons ) );
		}
	}

	private class Person {
		Gender gender;

		public Person(Gender gender) {
			this.gender = gender;
		}
	}

	@Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Constraint(validatedBy = { CompatiblePersonsValidator.class })
	@Documented
	public @interface CompatiblePersons {
		String message() default "";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	public static class CompatiblePersonsValidator implements ConstraintValidator<CompatiblePersons, Group> {
		@Override
		public void initialize(CompatiblePersons constraintAnnotation) {
		}

		@SuppressWarnings("deprecation")
		@Override
		public boolean isValid(Group group, ConstraintValidatorContext constraintValidatorContext) {
			if ( group == null ) {
				return true;
			}

			constraintValidatorContext.disableDefaultConstraintViolation();

			for ( int index = 0; index < group.persons.size(); index++ ) {
				Person person = group.persons.get( index );
				if ( !group.gender.equals( person.gender ) ) {
					constraintValidatorContext
							.buildConstraintViolationWithTemplate( "constraints.CompatiblePersons.gender.message" )
							.addNode( "persons" )
							.addNode( null ).inIterable().atIndex( index )
							.addConstraintViolation();
					return false;
				}
			}
			return true;
		}
	}
}
