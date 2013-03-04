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

import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionButWithoutValidAnnotationConstructorParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionButWithoutValidAnnotationOnField;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionButWithoutValidAnnotationOnMethodParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionButWithoutValidAnnotationOnMethodReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionButWithoutValidAnnotationOnProperty;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithGroupConversionWithSequenceAsFrom;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.UserWithSeveralGroupConversionsForSameFrom;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.UserReadService;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfInterfaceAndSuperClassBothWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfInterfaceAndSuperClassBothWithGroupConversionOnReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfParallelInterfacesWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.InterfaceImplementationWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.SubClassWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.util.Groups;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

/**
 * Test for definition of group conversion rules.
 *
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class InvalidGroupDefinitionsTest extends Arquillian {

	private Validator validator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( InvalidGroupDefinitionsTest.class )
				.withPackage( User.class.getPackage() )
				.withPackage( SubClassWithGroupConversionOnParameter.class.getPackage() )
				.withPackage( UserReadService.class.getPackage() )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutValidAnnotationOnField() {
		validator.validate( new UserWithGroupConversionButWithoutValidAnnotationOnField() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutValidAnnotationOnProperty() {
		validator.validate( new UserWithGroupConversionButWithoutValidAnnotationOnProperty() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutValidAnnotationOnMethodReturnValue() throws Exception {
		Object object = new UserWithGroupConversionButWithoutValidAnnotationOnMethodReturnValue();
		Method method = UserWithGroupConversionButWithoutValidAnnotationOnMethodReturnValue.class.getMethod(
				"retrieveAddresses"
		);
		Object returnValue = null;

		validator.forExecutables().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutValidAnnotationOnMethodParameter() throws Exception {
		Object object = new UserWithGroupConversionButWithoutValidAnnotationOnMethodParameter();
		Method method = UserWithGroupConversionButWithoutValidAnnotationOnMethodParameter.class.getMethod(
				"setAddresses",
				List.class
		);
		Object[] parameters = new Object[] { null };

		validator.forExecutables().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutValidAnnotationOnConstructorReturnValue()
			throws Exception {
		UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue object = new UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue();
		Constructor<UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue> constructor = UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue.class
				.getConstructor();

		validator.forExecutables().validateConstructorReturnValue( constructor, object );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "a")
	public void testGroupConversionWithoutValidAnnotationOnConstructorParameter() throws Exception {
		Constructor<UserWithGroupConversionButWithoutValidAnnotationConstructorParameter> constructor = UserWithGroupConversionButWithoutValidAnnotationConstructorParameter.class
				.getConstructor( List.class );
		Object[] parameters = new Object[] { null };

		validator.forExecutables().validateConstructorParameters( constructor, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "e")
	public void testSeveralGroupConversionsWithSameFrom() {
		//TODO GM: Should we test property, return value etc., too?
		validator.validate( new UserWithSeveralGroupConversionsForSameFrom() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "4.4.5", id = "f")
	public void testGroupConversionWithSequenceAsFrom() {
		validator.validate( new UserWithGroupConversionWithSequenceAsFrom() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "h"),
			@SpecAssertion(section = "4.4.5", id = "j")
	})
	public void testGroupConversionGivenOnParameterInSubClass() throws Exception {
		Object object = new SubClassWithGroupConversionOnParameter();
		Method method = SubClassWithGroupConversionOnParameter.class.getMethod(
				"addUser",
				User.class
		);
		Object[] parameters = new Object[] { null };

		validator.forExecutables().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "h"),
			@SpecAssertion(section = "4.4.5", id = "j")
	})
	public void testGroupConversionGivenOnParameterInImplementingClass() throws Exception {
		Object object = new InterfaceImplementationWithGroupConversionOnParameter();
		Method method = InterfaceImplementationWithGroupConversionOnParameter.class.getMethod(
				"addUser",
				User.class
		);
		Object[] parameters = new Object[] { null };

		validator.forExecutables().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "i"),
			@SpecAssertion(section = "4.4.5", id = "j")
	})
	public void testGroupConversionGivenOnParameterInParallelInterfaces() throws Exception {
		Object object = new ImplementationOfParallelInterfacesWithGroupConversionOnParameter();
		Method method = ImplementationOfParallelInterfacesWithGroupConversionOnParameter.class.getMethod(
				"addUser",
				User.class
		);
		Object[] parameters = new Object[] { null };

		validator.forExecutables().validateParameters( object, method, parameters );
	}

	//Currently not clear whether that test makes sense. See BVAL-365
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "i"),
			@SpecAssertion(section = "4.4.5", id = "j")
	})
	public void testGroupConversionGivenOnReturnValueInParallelInterfaces() throws Exception {
		Object object = new ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue();
		Method method = ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue.class.getMethod(
				"getUser"
		);
		Object returnValue = null;

		validator.forExecutables().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "i"),
			@SpecAssertion(section = "4.4.5", id = "j")
	})
	public void testGroupConversionGivenOnParameterInSuperClassAndImplementedInterface()
			throws Exception {
		Object object = new ImplementationOfInterfaceAndSuperClassBothWithGroupConversionOnParameter();
		Method method = ImplementationOfInterfaceAndSuperClassBothWithGroupConversionOnParameter.class
				.getMethod( "addUser", User.class );
		Object[] parameters = new Object[] { null };

		validator.forExecutables().validateParameters( object, method, parameters );
	}

	//Currently not clear whether that test makes sense. See BVAL-365
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "4.4.5", id = "i"),
			@SpecAssertion(section = "4.4.5", id = "j")
	})
	public void testGroupConversionGivenOnReturnValueInSuperClassAndImplementedInterface()
			throws Exception {
		Object object = new ImplementationOfInterfaceAndSuperClassBothWithGroupConversionOnReturnValue();
		Method method = ImplementationOfInterfaceAndSuperClassBothWithGroupConversionOnReturnValue.class
				.getMethod( "getUser" );
		Object returnValue = null;

		validator.forExecutables().validateReturnValue( object, method, returnValue );
	}
}
