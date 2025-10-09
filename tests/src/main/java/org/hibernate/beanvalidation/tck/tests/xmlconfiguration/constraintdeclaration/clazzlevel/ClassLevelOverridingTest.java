/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.clazzlevel;


import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ClassLevelOverridingTest extends AbstractTCKTest {

	public final static String packageName = "/org/hibernate/beanvalidation/tck/tests/xmlconfiguration/constraintdeclaration/clazzlevel/";
	public final static String mappingFile1 = "package-constraints-ClassLevelOverridingTest.xml";
	public final static String mappingFile2 = "package-constraints-ClassLevelOverridingImplicitOverrideTest.xml";
	public final static String mappingFile3 = "package-constraints-ClassLevelOverridingWithAnnotationTest.xml";

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClassPackage( ClassLevelOverridingTest.class )
				.withResource( ClassLevelOverridingTest.mappingFile1 )
				.withResource( ClassLevelOverridingTest.mappingFile2 )
				.withResource( ClassLevelOverridingTest.mappingFile3 )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "c")
	public void testIgnoreClassLevelAnnotations() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile1 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		Package p = new Package();
		Set<ConstraintViolation<Package>> violations = validator.validate( p );

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidPackage.class ).withMessage( "ValidPackage defined in XML" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "b")
	public void testIgnoreAnnotationsFromEnclosingBeanIsApplied() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile2 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		Package p = new Package();
		Set<ConstraintViolation<Package>> violations = validator.validate( p );

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidPackage.class ).withMessage( "ValidPackage defined in XML" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CLASSLEVELOVERRIDING, id = "d")
	public void testClassLevelAnnotationsApplied() {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( TestUtil.getInputStreamForPath( packageName + mappingFile3 ) );
		Validator validator = config.buildValidatorFactory().getValidator();

		Package p = new Package();
		Set<ConstraintViolation<Package>> violations = validator.validate( p );

		assertThat( violations ).containsOnlyViolations(
				violationOf( ValidPackage.class ).withMessage( "ValidPackage defined in XML" ),
				violationOf( ValidPackage.class ).withMessage( "ValidPackage defined as annotation" )
		);
	}
}
