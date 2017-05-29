/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion.containerelement;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecVersion;

@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class AnnotationBasedContainerElementGroupConversionValidationTest extends AbstractContainerElementGroupConversionValidationTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( AnnotationBasedContainerElementGroupConversionValidationTest.class )
				.build();
	}
}
