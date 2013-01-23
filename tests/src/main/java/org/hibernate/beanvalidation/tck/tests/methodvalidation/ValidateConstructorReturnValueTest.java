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
package org.hibernate.beanvalidation.tck.tests.methodvalidation;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.MethodValidator;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.ElementDescriptor.Kind;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.ValidCustomer;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Address;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer.Basic;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Customer.Extended;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.Email;
import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathDescriptorKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class ValidateConstructorReturnValueTest extends Arquillian {

	private MethodValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidateConstructorReturnValueTest.class )
				.withPackage( MyCrossParameterConstraint.class.getPackage() )
				.withClass( Address.class )
				.withClass( Customer.class )
				.withClass( Email.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		executableValidator = TestUtil.getValidatorUnderTest().forMethods();
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "5.1.2", id = "j"),
			@SpecAssertion(section = "5.1.2", id = "k")
	})
	public void testOneViolation() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, ValidCustomer.class );
		assertCorrectPathNodeNames( violations, names( "Customer", null ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "k")
	public void testTwoViolations() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( String.class );
		Customer returnValue = null;

		Set<ConstraintViolation<Customer>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, NotNull.class, ValidCustomer.class );
		assertCorrectPathNodeNames(
				violations,
				names( "Customer", null ),
				names( "Customer", null )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "k")
	public void testTwoConstraintsOfSameType() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( CharSequence.class );
		Customer returnValue = new Customer();

		Set<ConstraintViolation<Customer>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, ValidCustomer.class, ValidCustomer.class );
		assertCorrectPathNodeNames(
				violations,
				names( "Customer", null ),
				names( "Customer", null )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "k")
	public void testNoViolations() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor();
		Customer returnValue = new Customer( "Bob" );

		Set<ConstraintViolation<Customer>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "k")
	public void testValidationWithGroup() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( long.class );
		Customer returnValue = null;

		Set<ConstraintViolation<Customer>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertCorrectPathNodeNames( violations, names( "Customer", null ) );
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "k")
	public void testValidationWithSeveralGroups() throws Exception {
		Constructor<Customer> constructor = Customer.class.getConstructor( Date.class );
		Customer returnValue = null;

		Set<ConstraintViolation<Customer>> violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue,
				Basic.class,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, NotNull.class, ValidCustomer.class );
		assertCorrectPathNodeNames(
				violations,
				names( "Customer", null ),
				names( "Customer", null )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE ),
				kinds( Kind.CONSTRUCTOR, Kind.RETURN_VALUE )
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "5.1.2", id = "j")
	public void testUnexpectedType() throws Exception {
		Constructor<Email> constructor = Email.class.getConstructor();
		Email returnValue = new Email();

		executableValidator.validateConstructorReturnValue( constructor, returnValue );
	}
}
