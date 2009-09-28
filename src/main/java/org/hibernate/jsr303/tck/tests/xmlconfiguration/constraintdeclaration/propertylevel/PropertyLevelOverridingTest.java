// $Id:$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import java.util.Set;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import org.jboss.testharness.impl.packaging.Resource;
import org.jboss.testharness.impl.packaging.jsr303.ValidationXml;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
@ValidationXml(value = "validation-PropertyLevelOverridingTest.xml")
@Resource(source = "user-constraints-PropertyLevelOverridingTest.xml",
		destination = "WEB-INF/classes/org/hibernate/jsr303/tck/tests/xmlconfiguration/constraintdeclaration/propertylevel/user-constraints-PropertyLevelOverridingTest.xml")
public class PropertyLevelOverridingTest extends AbstractTest {

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.1.3", id = "a"),
			@SpecAssertion(section = "7.1.1.3", id = "c")
	})
	public void testIgnoreAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstname" );
		assertNull( propDescriptor, "The annotation defined constraints should be ignored." );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.1.3", id = "a"),
			@SpecAssertion(section = "7.1.1.3", id = "d")
	})
	public void testIncludeAnnotations() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "lastname" );
		assertNotNull( propDescriptor );

		Set<ConstraintDescriptor<?>> constraintDescriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( constraintDescriptors.size(), 2, "There should be two constraints" );

		boolean foundNotNullConstraint = false;
		boolean foundPatternConstraint = false;
		for ( ConstraintDescriptor<?> descriptor : constraintDescriptors ) {
			if ( descriptor.getAnnotation() instanceof NotNull ) {
				foundNotNullConstraint = true;
			}
			else if ( descriptor.getAnnotation() instanceof Pattern ) {
				foundPatternConstraint = true;
			}
			else {
				fail( "Invalid constraint for property." );
			}
		}
		if ( !( foundNotNullConstraint && foundPatternConstraint ) ) {
			fail( "Not all configured constraints discovered." );
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "7.1.1.3", id = "e")
	})
	public void testCascadedConfiguration() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		assertNotNull( propDescriptor );
		assertTrue( propDescriptor.isCascaded(), "Cascaded validation is configured via xml." );

		propDescriptor = beanDescriptor.getConstraintsForProperty( "secondCreditCard" );
		assertNull( propDescriptor, "The @Valid annotation should be ignored." );
	}
}