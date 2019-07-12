/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.groupconversion.containerelement;

import org.hibernate.beanvalidation.tck.tests.validation.groupconversion.containerelement.AbstractContainerElementGroupConversionValidationTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class XmlBasedContainerElementGroupConversionValidationTest extends AbstractContainerElementGroupConversionValidationTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( XmlBasedContainerElementGroupConversionValidationTest.class )
				.withPackage( AbstractContainerElementGroupConversionValidationTest.class.getPackage() )
				.withValidationXml( "validation-XmlBasedContainerElementGroupConversionValidationTest.xml" )
				.withResource( "XmlBasedContainerElementGroupConversionValidationTest.xml" )
				.build();
	}
}
