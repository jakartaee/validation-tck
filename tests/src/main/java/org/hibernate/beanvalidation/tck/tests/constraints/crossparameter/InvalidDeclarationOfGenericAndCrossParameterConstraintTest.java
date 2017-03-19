/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.crossparameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import javax.validation.ConstraintDeclarationException;
import javax.validation.ConstraintTarget;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.BaseExecutableValidatorTest;

import static org.hibernate.beanvalidation.tck.util.TestUtil.webArchiveBuilder;
import static org.testng.Assert.fail;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidDeclarationOfGenericAndCrossParameterConstraintTest extends BaseExecutableValidatorTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidDeclarationOfGenericAndCrossParameterConstraintTest.class )
				.build();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "d")
	@SpecAssertion(section = "4.5.2.1", id = "c")
	@SpecAssertion(section = "4.5.3", id = "b")
	public void testConstraintTargetImplicitOnMethodWithParametersAndReturnValueCausesException() throws Exception {
		Object object = new Foo();
		Method method = Foo.class.getMethod( "createEvent", Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Usage of ConstraintTarget.IMPLICIT not allowed for methods with parameters and return value. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "d")
	@SpecAssertion(section = "4.5.2.1", id = "c")
	@SpecAssertion(section = "4.5.3", id = "b")
	public void testConstraintTargetImplicitOnConstructorWithParametersCausesException() throws Exception {
		Constructor<?> constructor = Bar.class.getConstructor( Date.class, Date.class );
		Object[] parameterValues = new Object[2];

		getExecutableValidator().validateConstructorParameters( constructor, parameterValues );
		fail( "Usage of ConstraintTarget.IMPLICIT not allowed for constructors with parameters. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "e")
	@SpecAssertion(section = "4.5.2.1", id = "b")
	public void testConstraintTargetParametersOnMethodWithoutParametersCausesException() throws Exception {
		Object object = new Qux();
		Method method = Qux.class.getMethod( "qux" );
		Object[] parameterValues = new Object[0];

		getExecutableValidator().validateParameters( object, method, parameterValues );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed for methods without parameters. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "e")
	@SpecAssertion(section = "4.5.2.1", id = "b")
	public void testConstraintTargetParametersOnConstructorWithoutParametersCausesException() throws Exception {
		Constructor<?> constructor = Baz.class.getConstructor();
		Object[] parameterValues = new Object[0];

		getExecutableValidator().validateConstructorParameters( constructor, parameterValues );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed for constructors without parameters. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "f")
	public void testConstraintTargetReturnValueOnVoidMethodCausesException() throws Exception {
		Object object = new Zap();
		Method method = Zap.class.getMethod( "zap" );
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed for methods without return value. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "g")
	public void testConstraintTargetParametersOnClassCausesException() throws Exception {
		getValidator().validate( new TypeWithConstraintTargetParameter() );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed on type definitions. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "g")
	public void testConstraintTargetReturnValueOnClassCausesException() throws Exception {
		getValidator().validate( new TypeWithConstraintTargetReturnValue() );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed on type definitions. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "g")
	public void testConstraintTargetParametersOnInterfaceCausesException() throws Exception {
		getValidator().validate( new InterfaceWithConstraintTargetParameterImpl() );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed on interface definitions. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "g")
	public void testConstraintTargetReturnValueOnInterfaceCausesException() throws Exception {
		getValidator().validate( new InterfaceWithConstraintTargetReturnValueImpl() );
		fail( "Usage of ConstraintTarget.RETURN_VALUE not allowed on interface definitions. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
	@SpecAssertion(section = "3.1.1.4", id = "g")
	public void testConstraintTargetParametersOnFieldCausesException() throws Exception {
		getValidator().validate( new TypeWithFieldWithConstraintTargetParameter() );
		fail( "Usage of ConstraintTarget.PARAMETERS not allowed on fields. Expected exception wasn't thrown." );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = "3.1.1.4", id = "c")
    @SpecAssertion(section = "3.1.1.4", id = "g")
	public void testConstraintTargetReturnValueOnFieldCausesException() throws Exception {
		getValidator().validate( new TypeWithFieldWithConstraintTargetReturnValue() );
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

	private static class InterfaceWithConstraintTargetParameterImpl implements InterfaceWithConstraintTargetParameter {
	}

	@GenericAndCrossParameterConstraint(validationAppliesTo = ConstraintTarget.RETURN_VALUE)
	private interface InterfaceWithConstraintTargetReturnValue {
	}

	private static class InterfaceWithConstraintTargetReturnValueImpl
			implements InterfaceWithConstraintTargetReturnValue {
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
