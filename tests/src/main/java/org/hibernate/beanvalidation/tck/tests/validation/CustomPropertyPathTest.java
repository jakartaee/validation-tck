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
package org.hibernate.beanvalidation.tck.tests.validation;

import java.lang.annotation.Retention;
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
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import javax.validation.executable.ExecutableValidator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.PathUtil;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.PathUtil.assertViolationsContainOnlyPaths;
import static org.hibernate.beanvalidation.tck.util.PathUtil.assertViolationsContainPaths;
import static org.hibernate.beanvalidation.tck.util.PathUtil.pathWith;
import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class CustomPropertyPathTest extends Arquillian {

	private Validator validator;
	private ExecutableValidator executableValidator;

	@BeforeMethod
	public void setupValidators() {
		validator = TestUtil.getValidatorUnderTest();
		executableValidator = validator.forExecutables();
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( CustomPropertyPathTest.class )
				.withClass( CustomParameterNameProvider.class )
				.withClass( PathUtil.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "5.2", id = "an")
	public void testAddPropertyNode() {
		Set<ConstraintViolation<Foo>> constraintViolations = validator.validate( new Foo() );

		//violated constraint is class-level, thus the paths start with the first added sub-node
		assertViolationsContainOnlyPaths(
				constraintViolations,
				pathWith().property( "myNode1" ),
				pathWith().property( "myNode2" ).property( "myNode3" ),
				pathWith().property( "myNode4" ).property( "myNode5", true, null, null ),
				pathWith().property( "myNode6" ).property( "myNode7", true, null, 42 ),
				pathWith().property( "myNode8" ).property( "myNode9", true, "Foo", null ),
				pathWith().property( "myNode10" ).property( "myNode11", true, null, null ).property( "myNode12" )
		);
	}

	@Test
	@SpecAssertion(section = "5.2", id = "an")
	public void testAddBeanNode() {
		Set<ConstraintViolation<User>> constraintViolations = validator.validate( new User() );

		//violated constraint is property-level, thus the paths start with that property
		assertViolationsContainOnlyPaths(
				constraintViolations,
				pathWith().property( "address" ).bean(),
				pathWith().property( "address" ).property( "myNode1" ).bean(),
				pathWith().property( "address" ).property( "myNode2", true, null, null ).bean(),
				pathWith().property( "address" ).property( "myNode3", true, null, 84 ).bean(),
				pathWith().property( "address" ).property( "myNode4", true, "AnotherKey", null ).bean(),
				pathWith().property( "address" ).bean( true, null, null ),
				pathWith().property( "address" ).bean( true, null, 42 ),
				pathWith().property( "address" ).bean( true, "Key", null )
		);
	}

	@Test
	@SpecAssertion(section = "5.2", id = "an")
	public void testAddingNodesInClassLevelConstraintKeepsInIterableKeyAndIndex() {
		Set<ConstraintViolation<FooContainer>> constraintViolations = validator.validate( new FooContainer() );

		assertViolationsContainPaths(
				constraintViolations,
				pathWith().property( "fooList" ).property( "myNode1", true, null, 1 ),
				pathWith().property( "fooArray" ).property( "myNode1", true, null, 1 ),
				pathWith().property( "fooSet" ).property( "myNode1", true, null, null ),
				pathWith().property( "fooMap" ).property( "myNode1", true, "MapKey", null )
		);
	}

	@Test
	@SpecAssertion(section = "5.2", id = "an")
	public void testAddParameterNode() throws Exception {
		Set<ConstraintViolation<User>> constraintViolations = executableValidator.validateParameters(
				new User(),
				User.class.getMethod( "setAddresses", Map.class ),
				new Object[] { }
		);

		assertViolationsContainOnlyPaths(
				constraintViolations,
				pathWith().method( "setAddresses" ).parameter( "arg0", 0 ),
				pathWith().method( "setAddresses" ).parameter( "arg0", 0 ).bean(),
				pathWith().method( "setAddresses" )
						.parameter( "arg0", 0 )
						.property( "myNode1", true, null, 23 )
						.bean()
		);
	}

	@Test
	@SpecAssertion(section = "5.2", id = "an")
	public void testAddParameterNodeUsingCustomParameterNameProvider() throws Exception {
		ExecutableValidator executableValidator = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( new CustomParameterNameProvider() )
				.buildValidatorFactory()
				.getValidator()
				.forExecutables();

		Set<ConstraintViolation<User>> constraintViolations = executableValidator.validateParameters(
				new User(),
				User.class.getMethod( "setAddresses", Map.class ),
				new Object[] { }
		);


		assertViolationsContainOnlyPaths(
				constraintViolations,
				pathWith().method( "setAddresses" ).parameter( "param0", 0 ),
				pathWith().method( "setAddresses" ).parameter( "param0", 0 ).bean(),
				pathWith().method( "setAddresses" )
						.parameter( "param0", 0 )
						.property( "myNode1", true, null, 23 )
						.bean()
		);
	}

	@Test(expectedExceptions = Exception.class)
	@SpecAssertion(section = "5.2", id = "an")
	public void testAddParameterNodeForFieldLevelConstraintCausesException() throws Throwable {
		validator.validate( new Bar() );
	}

	@MyClassLevelValidation
	private static class MyObject {
		@NotNull
		String field1;

		@NotNull
		String field2;
	}

	@ClassLevelValidationAddingPropertyNodes
	private static class Foo {
	}

	private static class FooContainer {
		@Valid
		private final List<Foo> fooList = Arrays.asList( null, new Foo() );

		@Valid
		private final Foo[] fooArray = new Foo[] { null, new Foo() };

		@Valid
		private final Set<Foo> fooSet = asSet( null, new Foo() );

		@Valid
		private final Map<String, Foo> fooMap;

		public FooContainer() {
			fooMap = new HashMap<String, Foo>();
			fooMap.put( "MapKey", new Foo() );
		}

	}

	private static class Bar {

		@FieldLevelValidationAddingParameterNode
		private String bar;
	}

	private static class User {
		@PropertyLevelValidationAddingBeanAndPropertyNodes
		public Address getAddress() {
			return null;
		}

		@CrossParameterValidationAddingParameterBeanAndPropertyNodes
		public void setAddresses(Map<String, Address> addresses) {

		}
	}

	private static class Address {
		@SuppressWarnings("unused")
		public String getStreet() {
			return null;
		}

		@SuppressWarnings("unused")
		public Country getCountry() {
			return null;
		}
	}

	private static class Country {
		@SuppressWarnings("unused")
		public String getName() {
			return null;
		}
	}

	@Retention(RUNTIME)
	@Constraint(validatedBy = MyClassLevelValidation.Validator.class)
	public @interface MyClassLevelValidation {
		String message() default "failed";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator implements ConstraintValidator<MyClassLevelValidation, MyObject> {
			@Override
			public void initialize(MyClassLevelValidation constraintAnnotation) {
			}

			@SuppressWarnings("deprecation")
			@Override
			public boolean isValid(MyObject value, ConstraintValidatorContext context) {
				context.disableDefaultConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addNode( "myNode1" )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addNode( "myNode2" )
						.addNode( "myNode3" ).inIterable().atKey( "key" )
						.addConstraintViolation();
				return false;
			}
		}
	}

	@Retention(RUNTIME)
	@Constraint(validatedBy = ClassLevelValidationAddingPropertyNodes.Validator.class)
	public @interface ClassLevelValidationAddingPropertyNodes {
		String message() default "failed";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator implements ConstraintValidator<ClassLevelValidationAddingPropertyNodes, Foo> {
			@Override
			public void initialize(ClassLevelValidationAddingPropertyNodes constraintAnnotation) {
			}

			@Override
			public boolean isValid(Foo value, ConstraintValidatorContext context) {
				context.disableDefaultConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode1" )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode2" )
						.addPropertyNode( "myNode3" )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode4" )
						.addPropertyNode( "myNode5" )
						.inIterable()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode6" )
						.addPropertyNode( "myNode7" )
						.inIterable().atIndex( 42 )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode8" )
						.addPropertyNode( "myNode9" )
						.inIterable().atKey( "Foo" )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode10" )
						.addPropertyNode( "myNode11" )
						.inIterable()
						.addPropertyNode( "myNode12" )
						.addConstraintViolation();

				return false;
			}
		}
	}

	@Retention(RUNTIME)
	@Constraint(validatedBy = PropertyLevelValidationAddingBeanAndPropertyNodes.Validator.class)
	public @interface PropertyLevelValidationAddingBeanAndPropertyNodes {
		String message() default "failed";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator
				implements ConstraintValidator<PropertyLevelValidationAddingBeanAndPropertyNodes, Address> {
			@Override
			public void initialize(PropertyLevelValidationAddingBeanAndPropertyNodes constraintAnnotation) {
			}

			@Override
			public boolean isValid(Address value, ConstraintValidatorContext context) {
				context.disableDefaultConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addBeanNode()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode1" )
						.addBeanNode()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode2" )
						.inIterable()
						.addBeanNode()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode3" )
						.inIterable().atIndex( 84 )
						.addBeanNode()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addPropertyNode( "myNode4" )
						.inIterable().atKey( "AnotherKey" )
						.addBeanNode()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addBeanNode()
						.inIterable()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addBeanNode()
						.inIterable().atIndex( 42 )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addBeanNode()
						.inIterable().atKey( "Key" )
						.addConstraintViolation();

				return false;
			}
		}
	}

	@Retention(RUNTIME)
	@Constraint(validatedBy = CrossParameterValidationAddingParameterBeanAndPropertyNodes.Validator.class)
	public @interface CrossParameterValidationAddingParameterBeanAndPropertyNodes {
		String message() default "failed";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		@SupportedValidationTarget(ValidationTarget.PARAMETERS)
		public static class Validator
				implements ConstraintValidator<CrossParameterValidationAddingParameterBeanAndPropertyNodes, java.lang.Object[]> {
			@Override
			public void initialize(CrossParameterValidationAddingParameterBeanAndPropertyNodes constraintAnnotation) {
			}

			@Override
			public boolean isValid(java.lang.Object[] value, ConstraintValidatorContext context) {
				context.disableDefaultConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addParameterNode( 0 )
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addParameterNode( 0 )
						.addBeanNode()
						.addConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addParameterNode( 0 )
						.addPropertyNode( "myNode1" )
						.inIterable().atIndex( 23 )
						.addBeanNode()
						.addConstraintViolation();

				return false;
			}
		}
	}

	@Retention(RUNTIME)
	@Constraint(validatedBy = FieldLevelValidationAddingParameterNode.Validator.class)
	public @interface FieldLevelValidationAddingParameterNode {
		String message() default "failed";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };

		public static class Validator
				implements ConstraintValidator<FieldLevelValidationAddingParameterNode, String> {
			@Override
			public void initialize(FieldLevelValidationAddingParameterNode constraintAnnotation) {
			}

			@Override
			public boolean isValid(String value, ConstraintValidatorContext context) {
				context.disableDefaultConstraintViolation();

				context.buildConstraintViolationWithTemplate( context.getDefaultConstraintMessageTemplate() )
						.addParameterNode( 0 )
						.addConstraintViolation();

				return false;
			}
		}
	}
}
