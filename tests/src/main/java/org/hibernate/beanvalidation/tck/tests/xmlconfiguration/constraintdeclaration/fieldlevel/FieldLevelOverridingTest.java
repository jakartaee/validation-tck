// $Id:$
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
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.fieldlevel;

import java.util.Set;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class FieldLevelOverridingTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( FieldLevelOverridingTest.class )
				.withClasses( User.class, CreditCard.class )
				.withValidationXml( "validation-FieldLevelOverridingTest.xml" )
				.withResource( "user-constraints-FieldLevelOverridingTest.xml" )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.2", id = "a"),
			@SpecAssertion(section = "8.1.1.2", id = "c")
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
			@SpecAssertion(section = "8.1.1.2", id = "a"),
			@SpecAssertion(section = "8.1.1.2", id = "d")
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
			@SpecAssertion(section = "8.1.1.2", id = "e")
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

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "8.1.1.2", id = "f")
	})
	public void testGroupConversionsAreAdditive() {
		Validator validator = TestUtil.getValidatorUnderTest();
		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		assertNotNull( beanDescriptor );

		PropertyDescriptor propDescriptor = beanDescriptor.getConstraintsForProperty( "firstCreditCard" );
		assertNotNull( propDescriptor );
		assertTrue( propDescriptor.isCascaded(), "Cascaded validation is configured via xml." );
		Set<GroupConversionDescriptor> groupConversionDescriptorSet = propDescriptor.getGroupConversions();

		assertTrue(
				groupConversionDescriptorSet.size() == 2,
				"There should be two group conversions. One configured via annotations and one via XML"
		);

		boolean foundDefaultToRatingA = false;
		boolean foundDefaultToRatingAA = false;
		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversionDescriptorSet ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class )
					&& groupConversionDescriptor.getTo().equals( User.CreditRatingA.class ) ) {
				foundDefaultToRatingA = true;
			}
			else if ( groupConversionDescriptor.getFrom().equals( User.CreditRatingA.class )
					&& groupConversionDescriptor.getTo().equals( User.CreditRatingAA.class ) ) {
				foundDefaultToRatingAA = true;
			}
			else {
				fail( "Unexpected group conversion" );
			}
		}

		assertTrue(
				foundDefaultToRatingA && foundDefaultToRatingAA,
				"Group conversions defined via XML and Annotation are additive"
		);
	}
}
