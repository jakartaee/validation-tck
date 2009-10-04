// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.jsr303.tck.tests.bootstrap.customprovider;

import javax.validation.Validation;
import javax.validation.ValidationException;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.jsr303.ValidationXml;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@ValidationXml(value = "validation-BootstrapUnknownCustomProviderTest.xml")
public class BootstrapUnknownCustomProviderTest extends AbstractTest {

	@Test
	@SpecAssertion(section = "8", id = "a")
	public void testWrongProviderConfiguredInValidationXml() {
		try {
			Validation.buildDefaultValidatorFactory();
			fail( "validation.xml contains a invalid provider. Validation.buildDefaultValidatorFactory() should have failed." );
		}
		catch ( ValidationException e ) {
			// success
		}
	}
}
