/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.groupconversion;

import java.util.List;
import java.util.Set;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import javax.validation.metadata.ReturnValueDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupConversionTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( GroupConversionTest.class )
				.withValidationXml( "validation-GroupConversionTest.xml" )
				.withResource( "GroupConversionTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.7", id = "a"),
			@SpecAssertion(section = "8.1.1.7", id = "b")
	})
	public void testGroupConversionsAppliedOnMethod() throws Exception {
		MethodDescriptor methodDescriptor = TestUtil.getMethodDescriptor(
				Groups.class,
				"convert",
				String.class
		);
		assertNotNull( methodDescriptor, "the specified method should be configured in xml" );

		ReturnValueDescriptor returnValueDescriptor = methodDescriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 2 );

		List<ParameterDescriptor> parameterDescriptors = methodDescriptor.getParameterDescriptors();
		assertTrue( parameterDescriptors.size() == 1 );

		ParameterDescriptor parameterDescriptor = parameterDescriptors.get( 0 );
		groupConversionDescriptors = parameterDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.7", id = "a"),
			@SpecAssertion(section = "8.1.1.7", id = "b")
	})
	public void testGroupConversionsAppliedOnConstructor() throws Exception {
		ConstructorDescriptor constructorDescriptor = TestUtil.getConstructorDescriptor(
				Groups.class
		);
		assertNotNull( constructorDescriptor, "the specified constructor should be configured in xml" );
		ReturnValueDescriptor returnValueDescriptor = constructorDescriptor.getReturnValueDescriptor();
		Set<GroupConversionDescriptor> groupConversionDescriptors = returnValueDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 1 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.7", id = "a"),
			@SpecAssertion(section = "8.1.1.7", id = "b")
	})
	public void testGroupConversionsAppliedOnField() throws Exception {
		PropertyDescriptor propertyDescriptor = TestUtil.getPropertyDescriptor(
				Groups.class, "foo"
		);
		assertNotNull( propertyDescriptor, "the specified property should be configured in xml" );

		Set<GroupConversionDescriptor> groupConversionDescriptors = propertyDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 2 );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.7", id = "a"),
			@SpecAssertion(section = "8.1.1.7", id = "b")
	})
	public void testGroupConversionsAppliedOnGetter() throws Exception {
		PropertyDescriptor propertyDescriptor = TestUtil.getPropertyDescriptor(
				Groups.class, "snafu"
		);
		assertNotNull( propertyDescriptor, "the specified property should be configured in xml" );

		Set<GroupConversionDescriptor> groupConversionDescriptors = propertyDescriptor.getGroupConversions();
		assertTrue( groupConversionDescriptors.size() == 3 );
	}
}
