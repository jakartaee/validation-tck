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
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.util.Set;
import javax.validation.groups.Default;
import javax.validation.metadata.GroupConversionDescriptor;
import javax.validation.metadata.PropertyDescriptor;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.tests.metadata.Customer.BasicChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictChecks;
import org.hibernate.beanvalidation.tck.tests.metadata.Customer.StrictCustomerChecks;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static org.hibernate.beanvalidation.tck.util.TestUtil.getPropertyDescriptor;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class PropertyDescriptorTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( PropertyDescriptorTest.class )
				.withClasses(
						Account.class,
						Order.class,
						Person.class,
						Customer.class,
						Severity.class,
						NotEmpty.class
				)
				.build();
	}

	@Test
	@SpecAssertion(section = "6.4", id = "a")
	public void testIsNotCascaded() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Order.class, "orderNumber" );
		assertFalse( descriptor.isCascaded(), "Should not be cascaded" );
	}

	@Test
	@SpecAssertion(section = "6.4", id = "a")
	public void testIsCascaded() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "orderList" );
		assertTrue( descriptor.isCascaded(), "Should be cascaded" );
	}

	@Test
	@SpecAssertion(section = "6.6", id = "a")
	public void testPropertyName() {
		String propertyName = "orderList";
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, propertyName );
		assertEquals( descriptor.getPropertyName(), propertyName, "Wrong property name" );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.4", id = "b"),
			@SpecAssertion(section = "6.5", id = "a"),
			@SpecAssertion(section = "6.5", id = "b")
	})
	public void testGetGroupConversionsForField() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "orderList" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), StrictChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName()
						)
				);
			}
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.4", id = "b"),
			@SpecAssertion(section = "6.5", id = "a"),
			@SpecAssertion(section = "6.5", id = "b")
	})
	public void testGetGroupConversionsForProperty() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "account" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertEquals( groupConversions.size(), 2 );

		for ( GroupConversionDescriptor groupConversionDescriptor : groupConversions ) {
			if ( groupConversionDescriptor.getFrom().equals( Default.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), BasicChecks.class );
			}
			else if ( groupConversionDescriptor.getFrom().equals( StrictCustomerChecks.class ) ) {
				assertEquals( groupConversionDescriptor.getTo(), StrictChecks.class );
			}
			else {
				fail(
						String.format(
								"Encountered unexpected group conversion from %s to %s",
								groupConversionDescriptor.getFrom().getName(),
								groupConversionDescriptor.getTo().getName()
						)
				);
			}
		}
	}

	@Test
	@SpecAssertion(section = "6.4", id = "b")
	public void testGetGroupConversionsReturnsEmptySet() {
		PropertyDescriptor descriptor = getPropertyDescriptor( Customer.class, "firstName" );
		Set<GroupConversionDescriptor> groupConversions = descriptor.getGroupConversions();

		assertNotNull( groupConversions );
		assertTrue( groupConversions.isEmpty() );
	}
}
