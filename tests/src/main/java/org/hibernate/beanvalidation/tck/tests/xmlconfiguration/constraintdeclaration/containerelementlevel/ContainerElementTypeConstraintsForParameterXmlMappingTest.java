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
@SpecVersion(spec = "beanvalidation", version = "3.0.0")
public class ContainerElementTypeConstraintsForParameterXmlMappingTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ContainerElementTypeConstraintsForParameterXmlMappingTest.class )
				.withResource( "parameter-canDeclareContainerElementTypeConstraints-mapping.xml" )
				.withResource( "parameter-canDeclareNestedContainerElementTypeConstraints-mapping.xml" )
				.withResource( "parameter-canDeclareDeeplyNestedContainerElementTypeConstraints-mapping.xml" )
				.withResource( "parameter-canDeclareContainerElementCascades-mapping.xml" )
				.withResource( "parameter-declaringContainerElementTypeConstraintOnNonGenericParameterCausesException-mapping.xml" )
				.withResource( "parameter-declaringContainerElementTypeConstraintForNonExistingTypeArgumentIndexOnParameterCausesException-mapping.xml" )
				.withResource( "parameter-declaringContainerElementTypeConstraintForNonExistingNestedTypeArgumentIndexOnParameterCausesException-mapping.xml" )
				.withResource( "parameter-omittingTypeArgumentForMultiTypeArgumentTypeOnParameterCausesException-mapping.xml" )
				.withResource( "parameter-configuringSameContainerElementTwiceCausesException-mapping.xml" )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "a")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "b")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "c")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "g")
	public void canDeclareContainerElementTypeConstraintsForParameterWithXmlMapping() {
		Validator validator = getValidator( "parameter-canDeclareContainerElementTypeConstraints-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			HashMap<String, Integer> fishCountByType = new HashMap<>();
			fishCountByType.put( "A", -1 );
			fishCountByType.put( "BB", -2 );

			fishTank.test1( Optional.of( "Too long" ), fishCountByType );

			fail( "Expected exception wasn't raised" );
		}
		catch (ConstraintViolationException e) {
			assertThat( e.getConstraintViolations() ).containsOnlyViolations(
					violationOf( Size.class ).withMessage( "size must be between 0 and 5" ),
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
	public void canDeclareNestedContainerElementTypeConstraintsForParameterWithXmlMapping() {
		Validator validator = getValidator( "parameter-canDeclareNestedContainerElementTypeConstraints-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			Map<String, List<Fish>> fishOfTheMonth = new HashMap<>();
			List<Fish> january = Arrays.asList( null, new Fish() );
			fishOfTheMonth.put( "january", january );

			fishTank.test2( fishOfTheMonth );

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
	public void canDeclareDeeplyNestedContainerElementTypeConstraintsForParameterWithXmlMapping() {
		Validator validator = getValidator( "parameter-canDeclareDeeplyNestedContainerElementTypeConstraints-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			Set<String> bobsTags = CollectionHelper.asSet( (String) null );
			Map<String, Set<String>> januaryTags = new HashMap<>();
			januaryTags.put( "bob", bobsTags );
			List<Map<String, Set<String>>> tagsOfFishOfTheMonth = new ArrayList<>();
			tagsOfFishOfTheMonth.add( januaryTags );

			fishTank.test3( tagsOfFishOfTheMonth );

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
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "d")
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "h")
	public void canDeclareContainerElementCascadesForParameterWithXmlMapping() {
		Validator validator = getValidator( "parameter-canDeclareContainerElementCascades-mapping.xml" );

		IFishTank fishTank = TestUtil.getValidatingProxy( new FishTank(), validator );

		try {
			fishTank.test4( Optional.of( new Fish() ) );

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
	public void declaringContainerElementTypeConstraintOnNonGenericParameterCausesException() {
		getValidator( "parameter-declaringContainerElementTypeConstraintOnNonGenericParameterCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "k")
	public void declaringContainerElementTypeConstraintForNonExistingTypeArgumentIndexOnParameterCausesException() {
		getValidator( "parameter-declaringContainerElementTypeConstraintForNonExistingTypeArgumentIndexOnParameterCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "k")
	public void declaringContainerElementTypeConstraintForNonExistingNestedTypeArgumentIndexOnParameterCausesException() {
		getValidator( "parameter-declaringContainerElementTypeConstraintForNonExistingNestedTypeArgumentIndexOnParameterCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "l")
	public void omittingTypeArgumentForMultiTypeArgumentTypeOnParameterCausesException() {
		getValidator( "parameter-omittingTypeArgumentForMultiTypeArgumentTypeOnParameterCausesException-mapping.xml" );
	}

	@Test(expectedExceptions = ValidationException.class)
	@SpecAssertion(section = Sections.XML_MAPPING_CONSTRAINTDECLARATIONINXML_CONTAINERELEMENTCONSTRAINTS, id = "m")
	public void configuringSameContainerElementTwiceCausesException() {
		getValidator( "parameter-configuringSameContainerElementTwiceCausesException-mapping.xml" );
	}

	private Validator getValidator(String mappingFile) {
		Configuration<?> config = TestUtil.getConfigurationUnderTest();
		config.addMapping( getClass().getResourceAsStream( mappingFile ) );
		return config.buildValidatorFactory().getValidator();
	}

	public interface IFishTank {
		void test1(Optional<String> model, Map<String, Integer> fishCountByType);
		void test2(Map<String, List<Fish>> fishOfTheMonth);
		void test3(List<Map<String, Set<String>>> tagsOfFishOfTheMonth);
		void test4(Optional<Fish> boss);
		void setSize(int size);
	}

	public static class FishTank implements IFishTank {

		@Override
		public void test1(Optional<String> model, Map<String, Integer> fishCountByType) {
		}

		@Override
		public void test2(Map<String, List<Fish>> fishOfTheMonth) {
		}

		@Override
		public void test3(List<Map<String, Set<String>>> tagsOfFishOfTheMonth) {
		}

		@Override
		public void test4(Optional<Fish> boss) {
		}

		@Override
		public void setSize(int size) {
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
