// $Id:$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.jsr303.tck.tests.xmlconfiguration.constraintdeclaration.propertylevel;

import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.jsr303.ValidationXml;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
@ValidationXml(value = "validation-ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest.xml")
@Resource(source = "user-constraints-ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest.xml",
		destination = "WEB-INF/classes/org/hibernate/jsr303/tck/tests/xmlconfiguration/constraintdeclaration/propertylevel/user-constraints-ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest.xml")
public class ExcludePropertyLevelAnnotationsDueToBeanDefaultsTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.1.3", id = "b"),
			@SpecAssertion(section = "7.1.1.3", id = "c")
	})
	public void testIgnoreAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertNull( propDescriptor, "The annotation defined constraints should be ignored." );
	}
}