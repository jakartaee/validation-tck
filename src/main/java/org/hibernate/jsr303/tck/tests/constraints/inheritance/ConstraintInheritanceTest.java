// $Id$
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
package org.hibernate.jsr303.tck.tests.constraints.inheritance;

import java.lang.annotation.Annotation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.testharness.AbstractTest;
import org.jboss.testharness.impl.packaging.Artifact;
import org.jboss.testharness.impl.packaging.ArtifactType;
import org.jboss.testharness.impl.packaging.Classes;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

import org.hibernate.jsr303.tck.util.TestUtil;

/**
 * @author Hardy Ferentschik
 */
@Artifact(artifactType = ArtifactType.JSR303)
@Classes({TestUtil.class, TestUtil.PathImpl.class, TestUtil.NodeImpl.class})
public class ConstraintInheritanceTest extends AbstractTest {

	@Test
	@SpecAssertion(section = "3.3", id = "b")
	public void testConstraintsOnSuperClassAreInherited() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Bar.class );

		String propertyName = "foo";
		assertTrue( beanDescriptor.getConstraintsForProperty( propertyName ) != null );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		// cast is required for JDK 5 - at least on Mac OS X
		Annotation constraintAnnotation = ( Annotation ) propDescriptor.getConstraintDescriptors()
				.iterator()
				.next().getAnnotation();
		assertTrue( constraintAnnotation.annotationType() == NotNull.class );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "3.3", id = "a"),
			@SpecAssertion(section = "3.3", id = "b")
	})
	public void testConstraintsOnInterfaceAreInherited() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( Bar.class );

		String propertyName = "fubar";
		assertTrue( beanDescriptor.getConstraintsForProperty( propertyName ) != null );
		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( propertyName );

		// cast is required for JDK 5 - at least on Mac OS X
		Annotation constraintAnnotation = ( Annotation ) propDescriptor.getConstraintDescriptors()
				.iterator()
				.next().getAnnotation();
		assertTrue( constraintAnnotation.annotationType() == NotNull.class );
	}
}