/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.builtin;

import jakarta.validation.valueextraction.ValueExtractor;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.javafx.JavaFXValueExtractorsTestImpl;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.JavaFXTest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for JavaFX {@link ValueExtractor}s.
 * <p>
 * The content of the tests is externalized in {@link JavaFXValueExtractorsTestImpl} to avoid a JavaFX dependency.
 *
 * @author Khalid Alqinyah
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 *
 * @see JavaFXValueExtractorsTestImpl
 */
@JavaFXTest
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class JavaFXValueExtractorsTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( JavaFXValueExtractorsTest.class )
				.withClass( JavaFXValueExtractorsTestImpl.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "f")
	public void testJavaFXBasicProperties() {
		JavaFXValueExtractorsTestImpl.testJavaFXBasicProperties();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "g")
	public void testValueExtractionForPropertyList() {
		JavaFXValueExtractorsTestImpl.testValueExtractionForPropertyList();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "h")
	public void testValueExtractionForPropertySet() {
		JavaFXValueExtractorsTestImpl.testValueExtractionForPropertySet();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "i")
	public void testValueExtractionForPropertyMap() {
		JavaFXValueExtractorsTestImpl.testValueExtractionForPropertyMap();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "g")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_BUILTINVALUEEXTRACTORS, id = "f")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_CONTAINERELEMENTCONSTRAINTS, id = "f")
	public void testValueExtractionForListOfStringProperty() {
		JavaFXValueExtractorsTestImpl.testValueExtractionForListOfStringProperty();
	}
}
