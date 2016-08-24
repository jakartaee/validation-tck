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
package org.hibernate.beanvalidation.tck.tests.bootstrap;

import java.util.EnumSet;
import javax.validation.BootstrapConfiguration;
import javax.validation.executable.ExecutableType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Gunnar Morling
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class BootstrapConfigurationWithoutValidationXmlTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( BootstrapConfigurationWithoutValidationXmlTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = "5.5.3", id = "e")
	public void testGetBootstrapConfigurationNoValidationXml() {
		BootstrapConfiguration bootstrapConfiguration = TestUtil.getConfigurationUnderTest()
				.getBootstrapConfiguration();

		assertNotNull( bootstrapConfiguration );

		assertNotNull( bootstrapConfiguration.getConstraintMappingResourcePaths() );
		assertTrue( bootstrapConfiguration.getConstraintMappingResourcePaths().isEmpty() );

		assertNull( bootstrapConfiguration.getConstraintValidatorFactoryClassName() );
		assertNull( bootstrapConfiguration.getDefaultProviderClassName() );
		assertNull( bootstrapConfiguration.getMessageInterpolatorClassName() );
		assertNull( bootstrapConfiguration.getParameterNameProviderClassName() );

		assertNotNull( bootstrapConfiguration.getDefaultValidatedExecutableTypes() );
		assertEquals(
				bootstrapConfiguration.getDefaultValidatedExecutableTypes(),
				EnumSet.of( ExecutableType.CONSTRUCTORS, ExecutableType.NON_GETTER_METHODS )
		);

		assertNotNull( bootstrapConfiguration.getProperties() );
		assertTrue( bootstrapConfiguration.getProperties().isEmpty() );

		assertNull( bootstrapConfiguration.getTraversableResolverClassName() );
	}
}
