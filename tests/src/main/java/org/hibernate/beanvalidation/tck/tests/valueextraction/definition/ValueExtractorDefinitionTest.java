/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.definition;

import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.pathWith;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.testng.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.Container;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.ContainerElement;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.ContainerValueExtractorCompareInstance;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.ContainerValueExtractorCountCalls;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.CustomConstraint;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.IndexedValueContainerValueExtractor;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.IterableValueContainerValueExtractor;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.KeyedValueContainerValueExtractor;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.LocalMapKeyExtractor;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.LocalMapValueExtractor;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.NullNodeNameContainerValueExtractor;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.Order;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.RetailOrder;
import org.hibernate.beanvalidation.tck.tests.valueextraction.definition.model.ValueContainerValueExtractor;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Guillaume Smet
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class ValueExtractorDefinitionTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( ValueExtractorDefinitionTest.class )
				.withPackage( Container.class.getPackage() )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "c")
	public void instanceAndValueReceiverPassedToExtractValues() {
		Container<String> container = new Container<String>( null );
		StringContainerHolder containerHolder = new StringContainerHolder( container );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new ContainerValueExtractorCompareInstance( container ) )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( containerHolder );

		assertEquals( ContainerValueExtractorCompareInstance.callCounter, 1 );
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "b")
	public void valueExtractorNotInvokedIfContainerIsNull() {
		StringContainerHolder containerHolder = new StringContainerHolder( null );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new ContainerValueExtractorCountCalls() )
				.buildValidatorFactory()
				.getValidator();

		validator.validate( containerHolder );

		assertEquals( ContainerValueExtractorCountCalls.callCounter, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "c")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "g")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "c")
	public void value() {
		Container<ContainerElement> container = new Container<ContainerElement>( CustomConstraint.INSTANCE );
		ContainerHolder containerHolder = new ContainerHolder( container );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new ValueContainerValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerHolder>> violations = validator.validate( containerHolder );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "container" )
						.containerElement( "<node name>", false, null, null, Container.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "d")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "g")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "c")
	public void iterableValue() {
		Container<ContainerElement> container = new Container<ContainerElement>( CustomConstraint.INSTANCE );
		ContainerHolder containerHolder = new ContainerHolder( container );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new IterableValueContainerValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerHolder>> violations = validator.validate( containerHolder );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "container" )
						.containerElement( "<node name>", true, null, null, Container.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "e")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "g")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "c")
	public void indexedValue() {
		Container<ContainerElement> container = new Container<ContainerElement>( CustomConstraint.INSTANCE );
		ContainerHolder containerHolder = new ContainerHolder( container );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new IndexedValueContainerValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerHolder>> violations = validator.validate( containerHolder );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "container" )
						.containerElement( "<node name>", true, null, 13, Container.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "f")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "g")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "a")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION_EXTRACTEDVALUE, id = "c")
	public void keyedValue() {
		Container<ContainerElement> container = new Container<ContainerElement>( CustomConstraint.INSTANCE );
		ContainerHolder containerHolder = new ContainerHolder( container );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new KeyedValueContainerValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerHolder>> violations = validator.validate( containerHolder );

		assertThat( violations ).containsOnlyPaths(
				pathWith()
						.property( "container" )
						.containerElement( "<node name>", true, "key", null, Container.class, 0 )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "h")
	public void nullNodeName() {
		Container<ContainerElement> container = new Container<ContainerElement>( CustomConstraint.INSTANCE );
		ContainerHolder containerHolder = new ContainerHolder( container );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new NullNodeNameContainerValueExtractor() )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<ContainerHolder>> violations = validator.validate( containerHolder );

		assertThat( violations ).containsOnlyPaths(
				pathWith().property( "container" )
		);
	}

	@Test
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "j")
	@SpecAssertion(section = Sections.VALUEEXTRACTORDEFINITION, id = "k")
	public void valuePassedToExtractorRetrievedFromHost() {
		Map<String, Order> propertyMap = new HashMap<>();
		propertyMap.put( "name1", new Order( "INVALID-ID" ) );
		propertyMap.put( "name2", new Order( null ) );
		propertyMap.put( null, new Order( "RETAIL-1" ) );

		Map<String, Order> getterMap = new HashMap<>();
		getterMap.put( null, new Order( "RETAIL-2" ) );
		getterMap.put( "name2", new Order( "INVALID-ID" ) );

		MapHolder mapHolder = new MapHolder( propertyMap, getterMap );

		Validator validator = Validation.byDefaultProvider().configure()
				.addValueExtractor( new LocalMapValueExtractor( propertyMap ) )
				.addValueExtractor( new LocalMapKeyExtractor( getterMap ) )
				.buildValidatorFactory()
				.getValidator();

		Set<ConstraintViolation<MapHolder>> violations = validator.validate( mapHolder );
		assertThat( violations ).containsOnlyViolations(
				violationOf( RetailOrder.class )
						.withPropertyPath( pathWith()
								.property( "ordersByName" )
								.containerElement( "<map value>", true, "name1", null, Map.class, 1 ) ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "ordersByName" )
								.property( "id", true, "name2", null, Map.class, 1 ) ),
				violationOf( NotNull.class )
						.withPropertyPath( pathWith()
								.property( "ordersByName" )
								.containerElement( "<map key>", true, null, null, Map.class, 0 ) )
		);
	}

	private static class StringContainerHolder {

		@SuppressWarnings("unused")
		private final Container<@NotNull String> container;

		private StringContainerHolder(Container<String> container) {
			this.container = container;
		}
	}

	private static class ContainerHolder {

		@SuppressWarnings("unused")
		private final Container<@CustomConstraint ContainerElement> container;

		private ContainerHolder(Container<ContainerElement> container) {
			this.container = container;
		}
	}

	@SuppressWarnings("unused")
	private static class MapHolder {

		private final Map<String, @Valid @RetailOrder Order> ordersByName;

		private final Map<String, Order> getterMap;

		private MapHolder(Map<String, Order> propertyMap, Map<String, Order> getterMap) {
			this.ordersByName = propertyMap;
			this.getterMap = getterMap;
		}

		public Map<@NotNull String, Order> getOrdersByName() {
			return getterMap;
		}
	}
}
