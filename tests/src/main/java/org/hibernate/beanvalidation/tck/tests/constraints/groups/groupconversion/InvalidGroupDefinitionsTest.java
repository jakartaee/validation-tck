/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import javax.validation.ConstraintDeclarationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
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
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfInterfaceWithGroupConversionOnReturnValueAndSuperClass;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfParallelInterfacesWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.ImplementationOfParallelInterfacesWithGroupConversionOnReturnValue;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.InterfaceImplementationWithGroupConversionOnParameter;
import org.hibernate.beanvalidation.tck.tests.constraints.groups.groupconversion.service.impl.SubClassWithGroupConversionOnParameter;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test for definition of group conversion rules.
 *
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class InvalidGroupDefinitionsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( InvalidGroupDefinitionsTest.class )
				.withPackage( User.class.getPackage() )
				.withPackage( SubClassWithGroupConversionOnParameter.class.getPackage() )
				.withPackage( UserReadService.class.getPackage() )
				.build();
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnField() {
		getValidator().validate( new UserWithGroupConversionButWithoutValidAnnotationOnField() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnProperty() {
		getValidator().validate( new UserWithGroupConversionButWithoutValidAnnotationOnProperty() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnMethodReturnValue() throws Exception {
		Object object = new UserWithGroupConversionButWithoutValidAnnotationOnMethodReturnValue();
		Method method = UserWithGroupConversionButWithoutValidAnnotationOnMethodReturnValue.class.getMethod(
				"retrieveAddresses"
		);
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnMethodParameter() throws Exception {
		Object object = new UserWithGroupConversionButWithoutValidAnnotationOnMethodParameter();
		Method method = UserWithGroupConversionButWithoutValidAnnotationOnMethodParameter.class.getMethod(
				"setAddresses",
				List.class
		);
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateParameters( object, method, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnConstructorReturnValue()
			throws Exception {
		UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue object = new UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue();
		Constructor<UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue> constructor = UserWithGroupConversionButWithoutValidAnnotationOnConstructorReturnValue.class
				.getConstructor();

		getExecutableValidator().validateConstructorReturnValue( constructor, object );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "a")
	public void testGroupConversionWithoutValidAnnotationOnConstructorParameter() throws Exception {
		Constructor<UserWithGroupConversionButWithoutValidAnnotationConstructorParameter> constructor = UserWithGroupConversionButWithoutValidAnnotationConstructorParameter.class
				.getConstructor( List.class );
		Object[] parameters = new Object[] { null };

		getExecutableValidator().validateConstructorParameters( constructor, parameters );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "f")
	public void testSeveralGroupConversionsWithSameFrom() {
		getValidator().validate( new UserWithSeveralGroupConversionsForSameFrom() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "g")
	public void testGroupConversionWithSequenceAsFrom() {
		getValidator().validate( new UserWithGroupConversionWithSequenceAsFrom() );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnParameterInSubClass() throws Exception {
		Object object = new SubClassWithGroupConversionOnParameter();
		Method method = SubClassWithGroupConversionOnParameter.class.getMethod(
				"addUser",
				User.class
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
				"addUser",
				User.class
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
				"addUser",
				User.class
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
				"getUser"
		);
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}

	@Test(expectedExceptions = ConstraintDeclarationException.class)
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "i")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_GROUPCONVERSION, id = "k")
	public void testGroupConversionGivenOnParameterInImplementedInterfaceWithSuperClass()
			throws Exception {
		Object object = new ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass();
		Method method = ImplementationOfInterfaceWithGroupConversionOnParameterAndSuperClass.class
				.getMethod( "addUser", User.class );
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
				.getMethod( "getUser" );
		Object returnValue = null;

		getExecutableValidator().validateReturnValue( object, method, returnValue );
	}
}
