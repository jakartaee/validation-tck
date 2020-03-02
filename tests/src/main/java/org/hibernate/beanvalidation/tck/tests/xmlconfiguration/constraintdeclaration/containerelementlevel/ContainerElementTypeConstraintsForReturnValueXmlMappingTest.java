/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.constraintdeclaration.containerelementlevel;

import static org.assertj.core.api.Assertions.fail;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jakarta.validation.Configuration;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.CollectionHelper;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class ContainerElementTypeConstraintsForReturnValueXmlMappingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementTypeConstraintsForReturnValueXmlMappingTest.class )
				.withResource( "returnvalue-canDeclareContainerElementTypeConstraints-mapping.xml" )
				.withResource( "returnvalue-canDeclareNestedContainerElementTypeConstraints-mapping.xml" )
				.withResource( "returnvalue-canDeclareDeeplyNestedContainerElementTypeConstraints-mapping.xml" )
				.withResource( "returnvalue-canDeclareContainerElementCascades-mapping.xml" )
				.withResource( "returnvalue-declaringContainerElementTypeConstraintOnNonGenericReturnValueCausesException-mapping.xml" )
				.withResource( "returnvalue-declaringContainerElementTypeConstraintForNonExistingTypeArgumentIndexOnReturnValueCausesException-mapping.xml" )
				.withResource( "returnvalue-declaringContainerElementTypeConstraintForNonExistingNestedTypeArgumentIndexOnReturnValueCausesException-mapping.xml" )
				.withResource( "returnvalue-omittingTypeArgumentForMultiTypeArgumentTypeOnReturnValueCausesException-mapping.xml" )
				.withResource( "returnvalue-configuringSameContainerElementTwiceCausesException-mapping.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "g")
	public void canDeclareContainerElementTypeConstraintsForReturnValueWithXmlMapping() {
		Validator validator = getValidator( "returnvalue-canDeclareContainerElementTypeConstraints-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			fishTank.test1();

			fail( "Expected exception wasn't raised" );
		}
		catch (ConstraintViolationException e) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class ).withMessage( "size must be between 3 and 10" ),
					violationOf( Size.class ).withMessage( "size must be between 3 and 10" ),
					violationOf( Min.class ).withMessage( "must be greater than or equal to 1" ),
					violationOf( Min.class ).withMessage( "must be greater than or equal to 1" )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "f")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "g")
	public void canDeclareNestedContainerElementTypeConstraintsForReturnValueWithXmlMapping() {
		Validator validator = getValidator( "returnvalue-canDeclareNestedContainerElementTypeConstraints-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			fishTank.test2();

			fail( "Expected exception wasn't raised" );
		}
		catch (ConstraintViolationException e) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "f")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "g")
	public void canDeclareDeeplyNestedContainerElementTypeConstraintsForReturnValueWithXmlMapping() {
		Validator validator = getValidator( "returnvalue-canDeclareDeeplyNestedContainerElementTypeConstraints-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			fishTank.test3();

			fail( "Expected exception wasn't raised" );
		}
		catch (ConstraintViolationException e) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "h")
	public void canDeclareContainerElementCascadesForReturnValueWithXmlMapping() {
		Validator validator = getValidator( "returnvalue-canDeclareContainerElementCascades-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			fishTank.test4();

			fail( "Expected exception wasn't raised" );
		}
		catch (ConstraintViolationException e) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( NotNull.class )
			);
		}
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "j")
	public void declaringContainerElementTypeConstraintOnNonGenericReturnValueCausesException() {
		getValidator( "returnvalue-declaringContainerElementTypeConstraintOnNonGenericReturnValueCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "k")
	public void declaringContainerElementTypeConstraintForNonExistingTypeArgumentIndexOnReturnValueCausesException() {
		getValidator( "returnvalue-declaringContainerElementTypeConstraintForNonExistingTypeArgumentIndexOnReturnValueCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "k")
	public void declaringContainerElementTypeConstraintForNonExistingNestedTypeArgumentIndexOnReturnValueCausesException() {
		getValidator( "returnvalue-declaringContainerElementTypeConstraintForNonExistingNestedTypeArgumentIndexOnReturnValueCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "l")
	public void omittingTypeArgumentForMultiTypeArgumentTypeOnReturnValueCausesException() {
		getValidator( "returnvalue-omittingTypeArgumentForMultiTypeArgumentTypeOnReturnValueCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "m")
	public void configuringSameContainerElementTwiceCausesException() {
		getValidator( "returnvalue-configuringSameContainerElementTwiceCausesException-mapping.xml" );
	}

	private Validator getValidator(String mappingFile) {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( getClass().getResourceAsStream( mappingFile ) );
		return config.buildValidatorFactory().getValidator();
	}

	public interface IFishTank {
		Map<String, Integer> test1();
		Map<String, List<Fish>> test2();
		List<Map<String, Set<String>>> test3();
		Optional<Fish> test4();
		int getSize();
	}

	public static class FishTank implements IFishTank {

		@Override
		public Map<String, Integer> test1() {
			HashMap<String, Integer> fishCountByType = new HashMap<>();
			fishCountByType.put( "A", -1 );
			fishCountByType.put( "BB", -2 );

			return fishCountByType;
		}

		@Override
		public Map<String, List<Fish>> test2() {
			Map<String, List<Fish>> fishOfTheMonth = new HashMap<>();
			List<Fish> january = Arrays.asList( null, new Fish() );
			fishOfTheMonth.put( "january", january );

			return fishOfTheMonth;
		}

		@Override
		public List<Map<String, Set<String>>> test3() {
			Set<String> bobsTags = CollectionHelper.asSet( (String) null );
			Map<String, Set<String>> januaryTags = new HashMap<>();
			januaryTags.put( "bob", bobsTags );
			List<Map<String, Set<String>>> tagsOfFishOfTheMonth = new ArrayList<>();
			tagsOfFishOfTheMonth.add( januaryTags );

			return tagsOfFishOfTheMonth;
		}

		@Override
		public Optional<Fish> test4() {
			return Optional.of( new Fish() );
		}

		@Override
		public int getSize() {
			return 0;
		}

		public FishTank() {
		}
	}

	public static class Fish {

		public Fish() {
		}

		public String name;
	}
}
