/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.containerelementlevel;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.executable.ExecutableValidator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ContainerElementTypeIgnoreAnnotationsMappingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementTypeIgnoreAnnotationsMappingTest.class )
				.withResource( "field-ignoreAnnotationsTrue-mapping.xml" )
				.withResource( "field-ignoreAnnotationsFalse-mapping.xml" )
				.withResource( "getter-ignoreAnnotationsTrue-mapping.xml" )
				.withResource( "getter-ignoreAnnotationsFalse-mapping.xml" )
				.withResource( "returnvalue-ignoreAnnotationsTrue-mapping.xml" )
				.withResource( "returnvalue-ignoreAnnotationsFalse-mapping.xml" )
				.withResource( "parameter-ignoreAnnotationsTrue-mapping.xml" )
				.withResource( "parameter-ignoreAnnotationsFalse-mapping.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "e")
	public void ignoreAnnotationsOnEncapsulatingFieldAppliesToContainerElementType() {
		Validator validator = getValidator( "field-ignoreAnnotationsTrue-mapping.xml" );

		Set<ConstraintViolation<OrderField>> violations = validator.validate( OrderField.invalid() );

		assertThat( violations ).containsOnlyViolations(
				violationOf( DecimalMin.class )
		);

		validator = getValidator( "field-ignoreAnnotationsFalse-mapping.xml" );

		violations = validator.validate( OrderField.invalid() );

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class ),
				violationOf( DecimalMin.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "e")
	public void ignoreAnnotationsOnEncapsulatingGetterAppliesToContainerElementType() {
		Validator validator = getValidator( "getter-ignoreAnnotationsTrue-mapping.xml" );

		Set<ConstraintViolation<OrderGetter>> violations = validator.validate( OrderGetter.invalid() );

		assertThat( violations ).containsOnlyViolations(
				violationOf( DecimalMin.class )
		);

		validator = getValidator( "getter-ignoreAnnotationsFalse-mapping.xml" );

		violations = validator.validate( OrderGetter.invalid() );

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class ),
				violationOf( DecimalMin.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "e")
	public void ignoreAnnotationsOnEncapsulatingMethodReturnValueAppliesToContainerElementType() throws NoSuchMethodException, SecurityException {
		ExecutableValidator validator = getValidator( "returnvalue-ignoreAnnotationsTrue-mapping.xml" ).forExecutables();

		OrderReturnValue invalidObject = OrderReturnValue.invalid();

		Set<ConstraintViolation<OrderReturnValue>> violations = validator.validateReturnValue( invalidObject,
				OrderReturnValue.class.getMethod( "retrieveLines" ), invalidObject.lines );

		assertThat( violations ).containsOnlyViolations(
				violationOf( DecimalMin.class )
		);

		validator = getValidator( "returnvalue-ignoreAnnotationsFalse-mapping.xml" ).forExecutables();

		violations = validator.validateReturnValue( invalidObject,
				OrderReturnValue.class.getMethod( "retrieveLines" ), invalidObject.lines );

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class ),
				violationOf( DecimalMin.class )
		);
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "e")
	public void ignoreAnnotationsOnEncapsulatingMethodParameterAppliesToContainerElementType() throws NoSuchMethodException, SecurityException {
		ExecutableValidator validator = getValidator( "parameter-ignoreAnnotationsTrue-mapping.xml" ).forExecutables();

		OrderParameter invalidObject = OrderParameter.invalid();
		Map<String, BigDecimal> additionalLines = new HashMap<>();
		additionalLines.put( "", BigDecimal.ZERO );

		Set<ConstraintViolation<OrderParameter>> violations = validator.validateParameters( invalidObject,
				OrderParameter.class.getMethod( "addLines", Map.class ), new Object[] { additionalLines } );

		assertThat( violations ).containsOnlyViolations(
				violationOf( DecimalMin.class )
		);

		validator = getValidator( "parameter-ignoreAnnotationsFalse-mapping.xml" ).forExecutables();

		violations = validator.validateParameters( invalidObject,
				OrderParameter.class.getMethod( "addLines", Map.class ), new Object[] { additionalLines } );

		assertThat( violations ).containsOnlyViolations(
				violationOf( NotBlank.class ),
				violationOf( DecimalMin.class )
		);
	}

	private Validator getValidator(String mappingFile) {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( getClass().getResourceAsStream( mappingFile ) );
		return config.buildValidatorFactory().getValidator();
	}

	private static class OrderField {

		private final Map<@NotBlank String, BigDecimal> lines = new HashMap<>();

		private static OrderField invalid() {
			OrderField order = new OrderField();
			order.lines.put( "", BigDecimal.ZERO );
			return order;
		}
	}

	private static class OrderGetter {

		private final Map<String, BigDecimal> lines = new HashMap<>();

		private static OrderGetter invalid() {
			OrderGetter order = new OrderGetter();
			order.lines.put( "", BigDecimal.ZERO );
			return order;
		}

		@SuppressWarnings("unused")
		public Map<@NotBlank String, BigDecimal> getLines() {
			return lines;
		}
	}

	public static class OrderReturnValue {

		private final Map<String, BigDecimal> lines = new HashMap<>();

		private static OrderReturnValue invalid() {
			OrderReturnValue order = new OrderReturnValue();
			order.lines.put( "", BigDecimal.ZERO );
			return order;
		}

		public Map<@NotBlank String, BigDecimal> retrieveLines() {
			return lines;
		}
	}

	public static class OrderParameter {

		private final Map<String, BigDecimal> lines = new HashMap<>();

		private static OrderParameter invalid() {
			OrderParameter order = new OrderParameter();
			order.lines.put( "", BigDecimal.ZERO );
			return order;
		}

		public void addLines(Map<@NotBlank String, BigDecimal> lines) {
			this.lines.putAll( lines );
		}
	}
}
