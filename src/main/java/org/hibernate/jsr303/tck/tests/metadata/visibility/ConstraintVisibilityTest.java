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
package org.hibernate.jsr303.tck.tests.metadata.visibility;

import java.lang.annotation.ElementType;
import java.util.Set;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.Scope;

import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({ TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class })
public class ConstraintVisibilityTest extends AbstractTest {

	@Test
	public void testVisibility() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( SubClass.class );
		assertNotNull( beanDescriptor );

		Set<ConstraintDescriptor<?>> descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 2 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.LOCAL_ELEMENT )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 1 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.declaredOn( ElementType.TYPE )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 0 );

		descriptors = beanDescriptor.getConstraintsForProperty( "myField" )
				.findConstraints()
				.lookingAt( Scope.HIERARCHY )
				.declaredOn( ElementType.FIELD )
				.getConstraintDescriptors();
		assertTrue( descriptors.size() == 2 );
	}
}