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
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import javax.validation.ConstraintDeclarationException;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

/**
 * Test for definition of group conversion rules.
 *
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class GroupConversionDefinitionTest extends Arquillian {

	private Validator validator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( GroupConversionDefinitionTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutAtValidOnField() {
		validator.validate( new User1() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutAtValidOnProperty() {
		validator.validate( new User2() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutAtValidOnMethodReturnValue() throws Exception {
		Object object = new User3();
		Method method = User3.class.getMethod( "retrieveAddresses" );
		Object returnValue = null;

		validator.forMethods().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutAtValidOnMethodParameter() throws Exception {
		Object object = new User4();
		Method method = User4.class.getMethod( "setAddresses", List.class );
		Object[] parameters = new Object[] { null };

		validator.forMethods().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutAtValidOnConstructorReturnValue() throws Exception {
		User5 object = new User5();
		Constructor<User5> constructor = User5.class.getConstructor();

		validator.forMethods().validateConstructorReturnValue( constructor, object );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutAtValidOnConstructorParameter() throws Exception {
		Constructor<User6> constructor = User6.class.getConstructor( List.class );
		Object[] parameters = new Object[] { null };

		validator.forMethods().validateConstructorParameters( constructor, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "d")
	public void testSeveralGroupConversionsWithoutSameFrom() {
		//TODO GM: Should we test property, return value etc., too?
		validator.validate( new User7() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "e")
	public void testGroupConversionWithSequenceAsFrom() {
		validator.validate( new User8() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "g"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnParameterInSubClass() throws Exception {
		Object object = new UserServiceExtension1();
		Method method = UserServiceExtension1.class.getMethod( "addUser", User.class );
		Object[] parameters = new Object[] { null };

		validator.forMethods().validateParameters( object, method, parameters );
	}

	//fails since the RI currently adds up return value constraints
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups=Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "g"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnReturnValueInSubClass() throws Exception {
		Object object = new UserServiceExtension2();
		Method method = UserServiceExtension2.class.getMethod( "getUser" );
		Object returnValue = null;

		validator.forMethods().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "g"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnParameterInImplementingClass() throws Exception {
		Object object = new UserServiceImpl1();
		Method method = UserServiceImpl1.class.getMethod( "addUser", User.class );
		Object[] parameters = new Object[] { null };

		validator.forMethods().validateParameters( object, method, parameters );
	}

	//fails since the RI currently adds up return value constraints
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups=Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "g"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnReturnValueInImplementingClass() throws Exception {
		Object object = new UserServiceImpl2();
		Method method = UserServiceImpl2.class.getMethod( "getUser" );
		Object returnValue = null;

		validator.forMethods().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "h"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnParameterInParallelInterfaces() throws Exception {
		Object object = new UserServiceImpl3();
		Method method = UserServiceImpl3.class.getMethod( "addUser", User.class );
		Object[] parameters = new Object[] { null };

		validator.forMethods().validateParameters( object, method, parameters );
	}

	//fails since the RI currently adds up return value constraints
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups=Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "h"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnReturnValueInParallelInterfaces() throws Exception {
		Object object = new UserServiceImpl4();
		Method method = UserServiceImpl4.class.getMethod( "getUser" );
		Object returnValue = null;

		validator.forMethods().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "h"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnParameterInSuperClassAndImplementedInterface()
			throws Exception {
		Object object = new UserServiceImpl5();
		Method method = UserServiceImpl5.class.getMethod( "addUser", User.class );
		Object[] parameters = new Object[] { null };

		validator.forMethods().validateParameters( object, method, parameters );
	}

	//fails since the RI currently adds up return value constraints
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups=Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "h"),
			@SpecAssertion(section = "4.4.5", id = "i")
	})
	public void testGroupConversionGivenOnReturnValueInSuperClassAndImplementedInterface()
			throws Exception {
		Object object = new UserServiceImpl6();
		Method method = UserServiceImpl6.class.getMethod( "getUser" );
		Object returnValue = null;

		validator.forMethods().validateReturnValue( object, method, returnValue );
	}

}
