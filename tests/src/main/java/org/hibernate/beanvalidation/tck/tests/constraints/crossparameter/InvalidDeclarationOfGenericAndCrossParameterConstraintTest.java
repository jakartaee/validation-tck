/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.crossparameter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;

import jakarta.validation.ConstraintDeclarationException;
import jakarta.validation.ConstraintTarget;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class InvalidDeclarationOfGenericAndCrossParameterConstraintTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidDeclarationOfGenericAndCrossParameterConstraintTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_RETURNVALUECONSTRAINTS, id = "b")
	public void testConstraintTargetImplicitOnMethodWithParametersAndReturnValueCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new Foo();
			Method method = Foo.class.getMethod( "createEvent", Date.class, Date.class );
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "d")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_RETURNVALUECONSTRAINTS, id = "b")
	public void testConstraintTargetImplicitOnConstructorWithParametersCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Constructor<?> constructor = Bar.class.getConstructor( Date.class, Date.class );
			Object[] parameterValues = new Object[2];

			getExecutableValidator().validateConstructorParameters( constructor, parameterValues );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "b")
	public void testConstraintTargetParametersOnMethodWithoutParametersCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new Qux();
			Method method = Qux.class.getMethod( "qux" );
			Object[] parameterValues = new Object[0];

			getExecutableValidator().validateParameters( object, method, parameterValues );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "e")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_METHODLEVELCONSTRAINTS_PARAMETERCONSTRAINTS_CROSSPARAMETERCONSTRAINTS, id = "b")
	public void testConstraintTargetParametersOnConstructorWithoutParametersCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Constructor<?> constructor = Baz.class.getConstructor();
			Object[] parameterValues = new Object[0];

			getExecutableValidator().validateConstructorParameters( constructor, parameterValues );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "f")
	public void testConstraintTargetReturnValueOnVoidMethodCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			Object object = new Zap();
			Method method = Zap.class.getMethod( "zap" );
			Object returnValue = null;

			getExecutableValidator().validateReturnValue( object, method, returnValue );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "g")
	public void testConstraintTargetParametersOnClassCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new TypeWithConstraintTargetParameter() );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "g")
	public void testConstraintTargetReturnValueOnClassCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new TypeWithConstraintTargetReturnValue() );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "g")
	public void testConstraintTargetParametersOnInterfaceCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new InterfaceWithConstraintTargetParameterImpl() );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "g")
	public void testConstraintTargetReturnValueOnInterfaceCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new InterfaceWithConstraintTargetReturnValueImpl() );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "g")
	public void testConstraintTargetParametersOnFieldCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new TypeWithFieldWithConstraintTargetParameter() );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "c")
    @SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_CONSTRAINTDEFINITION_PROPERTIES_VALIDATIONAPPLIESTO, id = "g")
	public void testConstraintTargetReturnValueOnFieldCausesException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			getValidator().validate( new TypeWithFieldWithConstraintTargetReturnValue() );
	
		} ).isInstanceOf( ConstraintDeclarationException.class );
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
