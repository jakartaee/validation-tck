/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import jakarta.validation.ConstraintDeclarationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithContainerElementGroupConversionButWithoutValidAnnotationConstructorParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithContainerElementGroupConversionButWithoutValidAnnotationOnField;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithContainerElementGroupConversionButWithoutValidAnnotationOnMethodParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithContainerElementGroupConversionButWithoutValidAnnotationOnMethodReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithContainerElementGroupConversionButWithoutValidAnnotationOnProperty;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithContainerElementGroupConversionWithSequenceAsFrom;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.model.UserWithSeveralContainerElementGroupConversionsForSameFrom;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.model.User;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.UserReadService;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl.ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl.ImplementationOfInterfaceWithGroupConversionOnReturnValueAndSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl.ImplementationOfParallelInterfacesWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl.ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl.InterfaceImplementationWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.containerelement.service.impl.SubClassWithGroupConversionOnParameter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test for definition of group conversion rules.
 *
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidGroupDefinitionsOnContainerElementsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidGroupDefinitionsOnContainerElementsTest.class )
				.withPackage( User.class.getPackage() )
				.withPackage( UserWithSeveralContainerElementGroupConversionsForSameFrom.class.getPackage() )
				.withPackage( UserReadService.class.getPackage() )
				.withPackage( ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass.class.getPackage() )
				.build();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnField() {
		getValidator().validate( new UserWithContainerElementGroupConversionButWithoutValidAnnotationOnField() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnProperty() {
		getValidator().validate( new UserWithContainerElementGroupConversionButWithoutValidAnnotationOnProperty() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnMethodReturnValue() throws Exception {
		Object object = new UserWithContainerElementGroupConversionButWithoutValidAnnotationOnMethodReturnValue();
		Method method = UserWithContainerElementGroupConversionButWithoutValidAnnotationOnMethodReturnValue.class.getMethod(
				"retrieveAddresses"
		);
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnMethodParameter() throws Exception {
		Object object = new UserWithContainerElementGroupConversionButWithoutValidAnnotationOnMethodParameter();
		Method method = UserWithContainerElementGroupConversionButWithoutValidAnnotationOnMethodParameter.class.getMethod(
				"setAddresses",
				List.class
		);
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnConstructorParameter() throws Exception {
		Constructor<UserWithContainerElementGroupConversionButWithoutValidAnnotationConstructorParameter> constructor = UserWithContainerElementGroupConversionButWithoutValidAnnotationConstructorParameter.class
				.getConstructor( List.class );
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateConstructorParameters( constructor, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "f")
	public void testSeveralGroupConversionsWithSameFrom() {
		getValidator().validate( new UserWithSeveralContainerElementGroupConversionsForSameFrom() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "g")
	public void testGroupConversionWithSequenceAsFrom() {
		getValidator().validate( new UserWithContainerElementGroupConversionWithSequenceAsFrom() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnParameterInSubClass() throws Exception {
		Object object = new SubClassWithGroupConversionOnParameter();
		Method method = SubClassWithGroupConversionOnParameter.class.getMethod(
				"addUsers",
				List.class
		);
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnParameterInImplementingClass() throws Exception {
		Object object = new InterfaceImplementationWithGroupConversionOnParameter();
		Method method = InterfaceImplementationWithGroupConversionOnParameter.class.getMethod(
				"addUsers",
				List.class
		);
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnParameterInParallelInterfaces() throws Exception {
		Object object = new ImplementationOfParallelInterfacesWithGroupConversionOnParameter();
		Method method = ImplementationOfParallelInterfacesWithGroupConversionOnParameter.class.getMethod(
				"addUsers",
				List.class
		);
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "j")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnReturnValueInParallelInterfaces() throws Exception {
		Object object = new ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue();
		Method method = ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue.class.getMethod(
				"getUsers"
		);
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnParameterImplementedInterfaceWithSuperClass()
			throws Exception {
		Object object = new ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass();
		Method method = ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass.class
				.getMethod( "addUsers", List.class );
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "j")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnReturnValueInImplementedInterfaceWithSuperClass()
			throws Exception {
		Object object = new ImplementationOfInterfaceWithGroupConversionOnReturnValueAndSuperClass();
		Method method = ImplementationOfInterfaceWithGroupConversionOnReturnValueAndSuperClass.class
				.getMethod( "getUsers" );
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}
}
