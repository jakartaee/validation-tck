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
package org.hibernate.beanvalidation.tck.tests.constraints.crossparameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintTarget;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

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

import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class InvalidDeclarationOfGenericAndCrossParameterConstraintTest extends Arquillian {

	private Validator validator;
	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( InvalidDeclarationOfGenericAndCrossParameterConstraintTest.class )
				.build();
	}

	@BeforeMethod
	public void setupValidator() {
		validator = TestUtil.getValidatorUnderTest();
		executableValidator = validator.forExecutables();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "d")
	})
	public void testConstraintTargetImplicitOnMethodWithParametersAndReturnValueCausesException() throws Exception {
		Object object = new Foo();
		Method method = Foo.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Usage of ConstraintTarget.IMPLICIT not allowed for methods with parameters and return value. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "d")
	})
	public void testConstraintTargetImplicitOnConstructorWithParametersCausesException() throws Exception {
		Constructor<?> constructor = Bar.class.getConstructor( Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		executableValidator.validateConstructorParameters( constructor, parameterValues );
		fail( "Usage of ConstraintTarget.IMPLICIT not allowed for constructors with parameters. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "e"),
			@SpecAssertion(section = "4.5.2.1", id = "b")
	})
	public void testConstraintTargetParametersOnMethodWithoutParametersCausesException() throws Exception {
		Object object = new Qux();
		Method method = Qux.class.getMethod( "qux" );
		Object[] parameterValues = new Object[0];

		executableValidator.validateParameters( object, method, parameterValues );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed for methods without parameters. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "e"),
			@SpecAssertion(section = "4.5.2.1", id = "b")
	})
	public void testConstraintTargetParametersOnConstructorWithoutParametersCausesException() throws Exception {
		Constructor<?> constructor = Baz.class.getConstructor();
		Object[] parameterValues = new Object[0];

		executableValidator.validateConstructorParameters( constructor, parameterValues );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed for constructors without parameters. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "f")
	})
	public void testConstraintTargetReturnValueOnVoidMethodCausesException() throws Exception {
		Object object = new Zap();
		Method method = Zap.class.getMethod( "zap" );
		Object returnValue = null;

		executableValidator.validateReturnValue( object, method, returnValue );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed for methods without return value. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "g")
	})
	public void testConstraintTargetParametersOnClassCausesException() throws Exception {
		validator.validate( new TypeWithConstraintTargetParameter() );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed on type definitions. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "g")
	})
	public void testConstraintTargetReturnValueOnClassCausesException() throws Exception {
		validator.validate( new TypeWithConstraintTargetReturnValue() );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed on type definitions. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "g")
	})
	public void testConstraintTargetParametersOnInterfaceCausesException() throws Exception {
		validator.validate( new InterfaceWithConstraintTargetParameterImpl() );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed on interface definitions. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "g")
	})
	public void testConstraintTargetReturnValueOnInterfaceCausesException() throws Exception {
		validator.validate( new InterfaceWithConstraintTargetReturnValueImpl() );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed on interface definitions. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "g")
	})
	public void testConstraintTargetParametersOnFieldCausesException() throws Exception {
		validator.validate( new TypeWithFieldWithConstraintTargetParameter() );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed on fields. Expected exception wasn't thrown." );
	}

	//Fails due to HV-731
	@Test(expectedExceptions = ConstraintDeclarationException.class, groups = Groups.FAILING_IN_RI)
	@SpecAssertions({
			@SpecAssertion(section = "3.1.1.4", id = "c"),
			@SpecAssertion(section = "3.1.1.4", id = "g")
	})
	public void testConstraintTargetReturnValueOnFieldCausesException() throws Exception {
		validator.validate( new TypeWithFieldWithConstraintTargetReturnValue() );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed on fields. Expected exception wasn't thrown." );
	}

	private static class Foo {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.IMPLICIT)
		public Object createEvent(Date start, Date end) {
			return null;
		}
	}

	private static class Bar {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.IMPLICIT)
		public Bar(Date start, Date end) {
		}
	}

	private static class Qux {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.PARAMETERS)
		public void qux() {
		}
	}

	private static class Baz {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.PARAMETERS)
		public Baz() {
		}
	}

	private static class Zap {

		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
		public void zap() {
		}
	}

	@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.PARAMETERS)
	private static class TypeWithConstraintTargetParameter {
	}

	@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
	private static class TypeWithConstraintTargetReturnValue {
	}

	@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.PARAMETERS)
	private interface InterfaceWithConstraintTargetParameter {
	}

	private static class InterfaceWithConstraintTargetParameterImpl {
	}

	@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
	private interface InterfaceWithConstraintTargetReturnValue {
	}

	private static class InterfaceWithConstraintTargetReturnValueImpl {
	}

	private static class TypeWithFieldWithConstraintTargetParameter {
		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.PARAMETERS)
		private String foo;
	}

	private static class TypeWithFieldWithConstraintTargetReturnValue {
		@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
		private String foo;
	}
}
