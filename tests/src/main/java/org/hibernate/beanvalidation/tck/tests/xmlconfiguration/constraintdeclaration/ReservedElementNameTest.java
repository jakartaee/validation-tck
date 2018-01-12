/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration;


import static org.testng.Assert.fail;

import javax.validation.Configuration;
import javax.validation.ValidationException;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractBootstrapFailureTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ReservedElementNameTest extends AbstractBootstrapFailureTCKTest {

	private final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/constraintdeclaration/";
	private final static String mappingFile1 = "constraints-GroupIsNotAllowedAsElementNameTest.xml";
	private final static String mappingFile2 = "constraints-MessageIsNotAllowedAsElementNameTest.xml";
	private final static String mappingFile3 = "constraints-PayloadIsNotAllowedAsElementNameTest.xml";

	@Override
	protected Class<? extends Exception> acceptedDeploymentExceptionType() {
		return ValidationException.class;
	}

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ReservedElementNameTest.class )
				.withClasses( User.class )
				.withResource( ReservedElementNameTest.mappingFile1 )
				.withResource( ReservedElementNameTest.mappingFile2 )
				.withResource( ReservedElementNameTest.mappingFile3 )
				.build();
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "c")
	public void testGroupIsNotAllowedAsElementName() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		config.buildValidatorFactory().getValidator();
		fail( "Validator creation should have failed since <element name=\"groups\"> was used." );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "c")
	public void testMessageIsNotAllowedAsElementName() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
		config.buildValidatorFactory().getValidator();
		fail( "Validator creation should have failed since <element name=\"message\"> was used." );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONSTRAINTDECLARATION, id = "c")
	public void testPayloadIsNotAllowedAsElementName() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		config.buildValidatorFactory().getValidator();
		fail( "Validator creation should have failed since <element name=\"groups\"> was used." );
	}
}
