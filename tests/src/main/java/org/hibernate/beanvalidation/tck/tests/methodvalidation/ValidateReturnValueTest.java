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

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.MethodValidator;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
public class ValidateReturnValueTest extends Arquillian {

	private MethodValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( ValidateReturnValueTest.class )
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
			@SpecAssertion(section = "5.1.2", id = "c"),
			@SpecAssertion(section = "5.1.2", id = "d")
	})
	public void testOneViolation() throws Exception {
		String methodName = "getAddress";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName );
		Object returnValue = null;

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 1 );

		assertCorrectConstraintTypes( violations, NotNull.class );
		assertCorrectPathNodeNames( violations, names( methodName, null ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD, Kind.RETURN_VALUE ) );
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "d")
	public void testTwoViolations() throws Exception {
		String methodName = "getFirstName";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, String.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Pattern.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, null ),
				names( methodName, null )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "d")
	public void testTwoConstraintsOfSameType() throws Exception {
		String methodName = "getLastName";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, CharSequence.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 2 );

		assertCorrectConstraintTypes( violations, Size.class, Size.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, null ),
				names( methodName, null )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);
	}

	@Test
	@SpecAssertion(section = "5.1.2", id = "d")
	public void testNoViolations() throws Exception {
		Object object = new Customer();
		Method method = Customer.class.getMethod( "getFirstName", String.class );
		Object returnValue = "aaa";

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "d")
	public void testValidationWithGroup() throws Exception {
		String methodName = "getLastName";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, long.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPathNodeNames( violations, names( methodName, null ) );
		assertCorrectPathDescriptorKinds( violations, kinds( Kind.METHOD, Kind.RETURN_VALUE ) );
	}

	//fails on RI due to wrong return value node name
	@Test(groups = Groups.FAILING_IN_RI)
	@SpecAssertion(section = "5.1.2", id = "d")
	public void testValidationWithSeveralGroups() throws Exception {
		String methodName = "getAllData";

		Object object = new Customer();
		Method method = Customer.class.getMethod( methodName, Date.class );
		Object returnValue = "S";

		Set<ConstraintViolation<Object>> violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		assertCorrectNumberOfViolations( violations, 0 );

		violations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue,
				Basic.class,
				Extended.class
		);

		assertCorrectConstraintTypes( violations, Size.class, Pattern.class );
		assertCorrectPathNodeNames(
				violations,
				names( methodName, null ),
				names( methodName, null )
		);
		assertCorrectPathDescriptorKinds(
				violations,
				kinds( Kind.METHOD, Kind.RETURN_VALUE ),
				kinds( Kind.METHOD, Kind.RETURN_VALUE )
		);
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = "5.1.2", id = "c")
	public void testUnexpectedType() throws Exception {
		String methodName = "getValue";

		Object object = new Email();
		Method method = Email.class.getMethod( methodName );
		Object returnValue = "S";

		executableValidator.validateReturnValue( object, method, returnValue );
	}
}
