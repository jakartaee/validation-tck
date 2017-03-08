/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdefinition;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import javax.validation.Configuration;
import javax.validation.ConstraintValidator;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
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
import static org.testng.Assert.assertTrue;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class XmlConfiguredConstraintValidatorTest extends Arquillian {

	public final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/constraintdefinition/";
	public final static String mappingFile1 = "constraint-definition-ExludeExistingValidatorsTest.xml";
	public final static String mappingFile2 = "constraint-definition-IncludeExistingValidatorsTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( XmlConfiguredConstraintValidatorTest.class )
				.withResource( XmlConfiguredConstraintValidatorTest.mappingFile1 )
				.withResource( XmlConfiguredConstraintValidatorTest.mappingFile2 )
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.11", id = "o"),
			@SpecAssertion(section = "8.1.2", id = "b"),
			@SpecAssertion(section = "8.1.2", id = "e")
	})
	public <T extends Annotation> void testExcludeExistingValidators() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		PropertyDescriptor propDescriptor = validator.getConstraintsForClass( Name.class )
				.getConstraintsForProperty( "name" );

		Set<ConstraintDescriptor<?>> descriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( descriptors.size(), 1, "There should only be one constraint." );

		@SuppressWarnings("unchecked")
		ConstraintDescriptor<T> descriptor = (ConstraintDescriptor<T>) descriptors.iterator().next();
		List<Class<? extends ConstraintValidator<T, ?>>> validators = descriptor.getConstraintValidatorClasses();

		assertEquals(
				validators.size(),
				0,
				"No xml defined validator and annotations are ignored -> no validator"
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "6.11", id = "o"),
			@SpecAssertion(section = "8.1.2", id = "c"),
			@SpecAssertion(section = "8.1.2", id = "d"),
			@SpecAssertion(section = "8.1.2", id = "e")
	})
	public <T extends Annotation> void testIncludeExistingValidators() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		PropertyDescriptor propDescriptor = validator.getConstraintsForClass( Name.class )
				.getConstraintsForProperty( "name" );


		Set<ConstraintDescriptor<?>> descriptors = propDescriptor.getConstraintDescriptors();
		assertEquals( descriptors.size(), 1, "There should only be one constraint." );

		@SuppressWarnings("unchecked")
		ConstraintDescriptor<T> descriptor = (ConstraintDescriptor<T>) descriptors.iterator().next();
		List<Class<? extends ConstraintValidator<T, ?>>> validators = descriptor.getConstraintValidatorClasses();

		assertEquals( validators.size(), 2, "One validator should be defined in annotation and one in xml" );
		assertTrue(
				validators.contains( LengthValidator.class ),
				"Validator configured in annotation should be present"
		);
		assertTrue(
				validators.contains( DummyLengthValidator.class ),
				"Validator configured via XML should be present"
		);
	}
}
