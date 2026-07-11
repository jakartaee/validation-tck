/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration;

import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.ConstraintDescriptor;

import org.assertj.core.api.Assertions;
import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class DuplicateConfigurationTest extends AbstractTCKTest {

	public final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/";
	public final static String mappingFile1 = "user-constraints.xml";
	public final static String mappingFile2 = "user-constraints-MultipleBeanDefinitionTest.xml";
	public final static String mappingFile3 = "user-constraints-MultipleFieldDefinitionTest.xml";
	public final static String mappingFile4 = "user-constraints-MultipleGetterDefinitionTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( DuplicateConfigurationTest.class )
				.withClasses(
						User.class,
						UserType.class,
						Error.class,
						CreditCard.class,
						Optional.class,
						ConsistentUserInformation.class,
						CustomConsistentUserValidator.class,
						ConsistentUserValidator.class,
						TestGroup.class
				)
				.withResource( DuplicateConfigurationTest.mappingFile1 )
				.withResource( DuplicateConfigurationTest.mappingFile2 )
				.withResource( DuplicateConfigurationTest.mappingFile3 )
				.withResource( DuplicateConfigurationTest.mappingFile4 )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING, id = "a")
	public void testXmlConfiguredConstraintExposesCorrespondingAnnotationViaMetadata() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		BeanDescriptor beanDescriptor = validator.getConstraintsForClass( User.class );
		Set<ConstraintDescriptor<?>> constraintDescriptors = beanDescriptor.getConstraintDescriptors();
		assertThat( constraintDescriptors.size() ).as( "There should be one class level constraint defined in xml" ).isEqualTo( 1 );

		ConstraintDescriptor<?> descriptor = constraintDescriptors.iterator().next();
		assertThat( descriptor.getAnnotation() instanceof ConsistentUserInformation ).isTrue();

		constraintDescriptors = beanDescriptor.getConstraintsForProperty( "lastname" )
				.getConstraintDescriptors();
		assertThat( constraintDescriptors.size() ).as( "There should be one constraint defined in xml for 'lastname'" ).isEqualTo( 1 );
		descriptor = constraintDescriptors.iterator().next();
		assertThat( descriptor.getAnnotation() instanceof Pattern ).isTrue();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING, id = "b"),
			@SpecAssertion(section = Sections.XML_MAPPING, id = "e")
	})
	public void testBeanCannotBeDescribedMoreThanOnce() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
			config.buildValidatorFactory().getValidator();
			Assertions.fail( "You should not be able to define the same bean multiple times." );
		}
		catch ( ValidationException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING, id = "c"),
			@SpecAssertion(section = Sections.XML_MAPPING, id = "e")
	})
	public void testFieldMappingCannotOccurMoreThanOnce() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile3 ) );
			config.buildValidatorFactory().getValidator();
			Assertions.fail( "You should not be able to define multiple field mappings per entity" );
		}
		catch ( ValidationException e ) {
			// success
		}
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.XML_MAPPING, id = "d"),
			@SpecAssertion(section = Sections.XML_MAPPING, id = "e")
	})
	public void testGetterMappingCannotOccurMoreThanOnce() {
		try {
			Configuration<?> config = TestUtil.getConfigurationUnderTest();
			config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile4 ) );
			config.buildValidatorFactory().getValidator();
			Assertions.fail( "You should not be able to define multiple getter mappings per entity" );
		}
		catch ( ValidationException e ) {
			// success
		}
	}
}
