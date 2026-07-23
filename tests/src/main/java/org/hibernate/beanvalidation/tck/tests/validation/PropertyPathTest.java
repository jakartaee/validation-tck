/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.BEAN_NODE_NAME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.CROSS_PARAMETER_NODE_NAME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.RETURN_VALUE_NODE_NAME;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertThat;
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.violationOf;
import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstraintViolationForParameter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.Path.BeanNode;
import jakarta.validation.Path.ConstructorNode;
import jakarta.validation.Path.ContainerElementNode;
import jakarta.validation.Path.CrossParameterNode;
import jakarta.validation.Path.MethodNode;
import jakarta.validation.Path.Node;
import jakarta.validation.Path.ParameterNode;
import jakarta.validation.Path.PropertyNode;
import jakarta.validation.Path.ReturnValueNode;
import jakarta.validation.Payload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.executable.ExecutableValidator;

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

/**
 * Tests for property paths retrieved via {@link ConstraintViolation#getPropertyPath()}.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "4.0.0")
public class PropertyPathTest extends AbstractTCKTest {

	@Deployment
	public static WebArchive createTestArchive() {
		return webArchiveBuilder()
				.withTestClass( PropertyPathTest.class )
				.withClasses(
						Actor.class,
						ActorArrayBased.class,
						ActorCollectionBased.class,
						ActorLikesGenre.class,
						ActorListBased.class,
						PlayedWith.class,
						Person.class,
						VerySpecialClass.class,
						Customer.class,
						Engine.class,
						Order.class,
						Employee.class,
						Movie.class,
						MovieStudio.class,
						CustomParameterNameProvider.class,
						ValidMovieStudio.class,
						ValidMovieStudioValidator.class,
						MovieProduction.class,
						ExecutiveProducer.class,
						ValidExecutiveProducer.class,
						Location.class
				)
				.build();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ab"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
	})
	public void testPropertyPathWithConstraintViolationForRootObject() {
		Set<ConstraintViolation<VerySpecialClass>> constraintViolations = getValidator().validate( new VerySpecialClass() );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Special.class )
		);
		ConstraintViolation<VerySpecialClass> constraintViolation = constraintViolations.iterator()
				.next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node node = nodeIter.next();
		assertNode( node, BEAN_NODE_NAME, ElementKind.BEAN, false, null, null );
		BeanNode beanNode = node.as( BeanNode.class );
		Assertions.assertThat( beanNode ).isNotNull();

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ac"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am")
	})
	public void testPropertyPathTraversedObject() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator().validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);

		ConstraintViolation<Engine> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node node = nodeIter.next();
		assertNode( node, "serialNumber", ElementKind.PROPERTY, false, null, null );
		PropertyNode propertyNode = node.as( PropertyNode.class );
		Assertions.assertThat( propertyNode ).isNotNull();
		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	})
	public void testPropertyPathWithList() {
		Actor clint = new ActorListBased( "Clint", "Eastwood" );
		Actor morgan = new ActorListBased( "Morgan", null );
		Actor charlie = new ActorListBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );

		Set<ConstraintViolation<Actor>> constraintViolations = getValidator().validate( clint );
		checkActorViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	})
	public void testPropertyPathWithArray() {
		Actor clint = new ActorArrayBased( "Clint", "Eastwood" );
		Actor morgan = new ActorArrayBased( "Morgan", null );
		Actor charlie = new ActorArrayBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );

		Set<ConstraintViolation<Actor>> constraintViolations = getValidator().validate( clint );
		checkActorViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aa"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ad"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	})
	public void testPropertyPathWithRuntimeTypeList() {
		Actor clint = new ActorCollectionBased( "Clint", "Eastwood" );
		Actor morgan = new ActorCollectionBased( "Morgan", null );
		Actor charlie = new ActorCollectionBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );

		Set<ConstraintViolation<Actor>> constraintViolations = getValidator().validate( clint );
		checkActorViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ae"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	})
	public void testPropertyPathWithMap() {
		ActorDB db = new ActorDB();
		Actor morgan = new ActorArrayBased( "Morgan", null );
		Integer id = db.addActor( morgan );

		Set<ConstraintViolation<ActorDB>> constraintViolations = getValidator().validate( db );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		ConstraintViolation<ActorDB> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "actors", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, id );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "af")
	})
	public void testPropertyPathSet() {
		Customer customer = new Customer();
		customer.setFirstName( "John" );
		customer.setLastName( "Doe" );
		Order order = new Order();
		customer.addOrder( order );

		Set<ConstraintViolation<Customer>> constraintViolations = getValidator().validate( customer );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		ConstraintViolation<Customer> constraintViolation = constraintViolations.iterator().next();
		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "orders", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "orderNumber", ElementKind.PROPERTY, true, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "x"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "y"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForMethodParameterConstraint() throws Exception {
		//given
		String methodName = "makeMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				List.class
		);
		Object[] parameterValues = new Object[] { null, null, null };

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"title"
		).getPropertyPath().iterator();

		//parameter 0
		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node nextNode = nodeIter.next();
		assertNode( nextNode, methodName, ElementKind.METHOD, false, null, null );

		MethodNode methodNode = nextNode.as( MethodNode.class );
		Assertions.assertThat( methodNode ).isNotNull();
		Assertions.assertThat( methodNode.getParameterTypes() ).isEqualTo( Arrays.<Class<?>>asList( String.class, Person.class, List.class ) );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		nextNode = nodeIter.next();
		assertNode( nextNode, "title", ElementKind.PARAMETER, false, null, null );
		ParameterNode parameterNode = nextNode.as( ParameterNode.class );
		Assertions.assertThat( parameterNode ).isNotNull();
		Assertions.assertThat( parameterNode.getParameterIndex() ).isEqualTo( 0  );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();

		//parameter 1
		nodeIter = getConstraintViolationForParameter( constraintViolations, "director" ).getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		nextNode = nodeIter.next();
		assertNode( nextNode, "director", ElementKind.PARAMETER, false, null, null );
		parameterNode = nextNode.as( ParameterNode.class );
		Assertions.assertThat( parameterNode  ).isNotNull();
		Assertions.assertThat( parameterNode.getParameterIndex() ).isEqualTo( 1  );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForMethodParameterConstraintWithCustomParameterNameProvider()
			throws Exception {
		//given
		String methodName = "makeMovie";
		ExecutableValidator executableValidator = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( new CustomParameterNameProvider() )
				.buildValidatorFactory()
				.getValidator()
				.forExecutables();

		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				List.class
		);
		Object[] parameterValues = new Object[] { null, null, null };

		//when
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"param0"
		).getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "param0", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();

		nodeIter = getConstraintViolationForParameter( constraintViolations, "param1" ).getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "param1", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForMethodReturnValueConstraint() throws Exception {
		//given
		String methodName = "makeMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				List.class
		);
		Object returnValue = null;

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);
		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node nextNode = nodeIter.next();
		assertNode( nextNode, RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		ReturnValueNode returnValueNode = nextNode.as( ReturnValueNode.class );
		Assertions.assertThat(  returnValueNode  ).isNotNull();

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForMethodCrossParameterConstraint() throws Exception {
		//given
		String methodName = "makeMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				Actor.class,
				String.class
		);
		Object[] parameterValues = new Object[] { null, null };

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ActorLikesGenre.class )
		);
		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode(
				nodeIter.next(),
				CROSS_PARAMETER_NODE_NAME,
				ElementKind.CROSS_PARAMETER,
				false,
				null,
				null
		);

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "x"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "y"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForConstructorParameterConstraint() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class
		);
		Object[] parameterValues = new Object[] { null, null };

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class ),
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"name"
		).getPropertyPath().iterator();

		//parameter 0
		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node nextNode = nodeIter.next();
		assertNode( nextNode, "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		ConstructorNode constructorNode = nextNode.as( ConstructorNode.class );
		Assertions.assertThat(  constructorNode  ).isNotNull();
		Assertions.assertThat(  constructorNode.getParameterTypes() ).isEqualTo( Arrays.<Class<?>>asList( String.class, Person.class  ) );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		nextNode = nodeIter.next();
		assertNode( nextNode, "name", ElementKind.PARAMETER, false, null, null );
		ParameterNode parameterNode = nextNode.as( ParameterNode.class );
		Assertions.assertThat(  parameterNode  ).isNotNull();
		Assertions.assertThat(  parameterNode.getParameterIndex() ).isEqualTo( 0  );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();

		//parameter 1
		nodeIter = getConstraintViolationForParameter( constraintViolations, "generalManager" ).getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		nextNode = nodeIter.next();
		assertNode( nextNode, "generalManager", ElementKind.PARAMETER, false, null, null );
		parameterNode = nextNode.as( ParameterNode.class );
		Assertions.assertThat(  parameterNode  ).isNotNull();
		Assertions.assertThat(  parameterNode.getParameterIndex() ).isEqualTo( 1  );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForConstructorParameterConstraintWithCustomParameterNameProvider()
			throws Exception {
		//given
		ExecutableValidator executableValidator = TestUtil.getConfigurationUnderTest()
				.parameterNameProvider( new CustomParameterNameProvider() )
				.buildValidatorFactory()
				.getValidator()
				.forExecutables();

		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class
		);
		Object[] parameterValues = new Object[] { null, null };

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"param0"
		).getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "param0", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();

		nodeIter = getConstraintViolationForParameter( constraintViolations, "param1" ).getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "param1", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForConstructorCrossParameterConstraint() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				Actor.class,
				String.class
		);
		Object[] parameterValues = new Object[] { null, null };

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node nextNode = nodeIter.next();
		assertNode(
				nextNode,
				CROSS_PARAMETER_NODE_NAME,
				ElementKind.CROSS_PARAMETER,
				false,
				null,
				null
		);

		CrossParameterNode crossParameterNode = nextNode.as( CrossParameterNode.class );
		Assertions.assertThat(  crossParameterNode  ).isNotNull();

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as")
	})
	public void testPropertyPathForConstructorReturnValueConstraint() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class
		);
		MovieStudio returnValue = new MovieStudio( null );

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ValidMovieStudio.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au")
	})
	public void testPropertyPathTraversingMethodParameter() throws Exception {
		//given
		String methodName = "makeMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				List.class
		);
		Object[] parameterValues = new Object[] {
				validFilmTitle(),
				employWithoutFirstName(),
				null
		};

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "director", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "firstName", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aw"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodListParameter() throws Exception {
		//given
		String methodName = "makeMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				List.class
		);
		Object[] parameterValues = new Object[] {
				validFilmTitle(),
				validEmployee(),
				Arrays.asList(
						validActor(),
						actorWithoutLastName()
				)
		};

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aw"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodArrayParameter() throws Exception {
		//given
		String methodName = "makeMovieArrayBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				Actor[].class
		);
		Object[] parameterValues = new Object[] {
				validFilmTitle(),
				validEmployee(),
				new Actor[] {
						validActor(),
						actorWithoutLastName()
				}
		};

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodSetParameter() throws Exception {
		//given
		String methodName = "makeMovieSetBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				Set.class
		);
		Object[] parameterValues = new Object[] {
				validFilmTitle(),
				validEmployee(),
				asSet( validActor(), actorWithoutLastName() )
		};

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ax"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodMapParameter() throws Exception {
		//given
		String methodName = "makeMovieMapBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod(
				methodName,
				String.class,
				Person.class,
				Map.class
		);
		Map<String, Actor> actors = new HashMap<String, Actor>();
		actors.put( "Glen", validActor() );
		actors.put( "Garry", actorWithoutLastName() );
		Object[] parameterValues = new Object[] {
				validFilmTitle(),
				validEmployee(),
				actors
		};

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, "Garry" );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au")
	})
	public void testPropertyPathTraversingConstructorParameter() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class
		);
		Object[] parameterValues = new Object[] { validStudioName(), employWithoutFirstName() };

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "generalManager", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "firstName", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aw"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingConstructorListParameter() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class,
				List.class
		);
		Object[] parameterValues = new Object[] {
				validStudioName(),
				validEmployee(),
				Arrays.asList(
						validActor(),
						actorWithoutLastName()
				)
		};

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aw"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingConstructorArrayParameter() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class,
				Actor[].class
		);
		Object[] parameterValues = new Object[] {
				validStudioName(),
				validEmployee(),
				new Actor[] {
						validActor(),
						actorWithoutLastName()
				}
		};

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingConstructorSetParameter() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class,
				Set.class
		);
		Object[] parameterValues = new Object[] {
				validStudioName(),
				validEmployee(),
				asSet( validActor(), actorWithoutLastName() )
		};

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ax"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingConstructorMapParameter() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class,
				Map.class
		);
		Map<String, Actor> actors = new HashMap<String, Actor>();
		actors.put( "Glen", validActor() );
		actors.put( "Garry", actorWithoutLastName() );
		Object[] parameterValues = new Object[] {
				validStudioName(),
				validEmployee(),
				actors
		};

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, "Garry" );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "av")
	})
	public void testPropertyPathTraversingMethodReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );
		Object returnValue = new Movie();

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "av"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aw"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodListReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMoviesListBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );
		Object returnValue = Arrays.asList(
				new Movie( validFilmTitle() ),
				new Movie()
		);

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "av"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aw"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodArrayReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMoviesArrayBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );
		Object returnValue = new Movie[] {
				new Movie( validFilmTitle() ),
				new Movie()
		};

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "av"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodSetReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMoviesSetBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );
		Object returnValue = asSet( new Movie( validFilmTitle() ), new Movie() );

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "av"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ax"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ay")
	})
	public void testPropertyPathTraversingMethodMapReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMoviesMapBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );

		Map<String, Movie> returnValue = new HashMap<String, Movie>();
		returnValue.put( "BVDC", new Movie( validFilmTitle() ) );
		returnValue.put( "NO_TITLE", new Movie() );

		//when
		Set<ConstraintViolation<Object>> constraintViolations = getExecutableValidator().validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, null, "NO_TITLE" );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "av")
	})
	public void testPropertyPathTraversingConstructorReturnValue() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor( String.class );
		MovieStudio returnValue = new MovieStudio( null );

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//then
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "name", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w")
	public void testPassingWrongTypeToAsOnBeanNodeCausesClassCastException() {
		Assertions.assertThatThrownBy( () -> {

			Set<ConstraintViolation<VerySpecialClass>> constraintViolations = getValidator().validate( new VerySpecialClass() );
			assertThat( constraintViolations ).containsOnlyViolations(
					violationOf( Special.class )
			);
			ConstraintViolation<VerySpecialClass> constraintViolation = constraintViolations.iterator().next();

			Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

			Assertions.assertThat( nodeIter.hasNext() ).isTrue();
			Node node = nodeIter.next();
			assertNode( node, BEAN_NODE_NAME, ElementKind.BEAN, false, null, null );

			node.as( PropertyNode.class );
	
		} ).isInstanceOf( ClassCastException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w")
	public void testPassingWrongTypeToAsOnConstructorNodeCausesClassCastException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			//given
			Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
					String.class,
					Person.class
			);
			Object[] parameterValues = new Object[] { null, null };

			//when
			Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
					constructor,
					parameterValues
			);

			//then
			assertThat( constraintViolations ).containsOnlyViolations(
					violationOf( NotNull.class ),
					violationOf( NotNull.class )
			);

			Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
					constraintViolations,
					"name"
			).getPropertyPath().iterator();

			//parameter 0
			Assertions.assertThat( nodeIter.hasNext() ).isTrue();
			Node nextNode = nodeIter.next();
			assertNode( nextNode, "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

			nextNode.as( PropertyNode.class );
	
		} ).isInstanceOf( ClassCastException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w")
	public void testPassingWrongTypeToAsOnParameterNodeCausesClassCastException() throws Exception {
		Assertions.assertThatThrownBy( () -> {

			//given
			Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
					String.class,
					Person.class
			);
			Object[] parameterValues = new Object[] { null, null };

			//when
			Set<ConstraintViolation<MovieStudio>> constraintViolations = getExecutableValidator().validateConstructorParameters(
					constructor,
					parameterValues
			);

			//then
			assertThat( constraintViolations ).containsOnlyViolations(
					violationOf( NotNull.class ),
					violationOf( NotNull.class )
			);

			Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
					constraintViolations,
					"name"
			).getPropertyPath().iterator();

			//parameter 0
			Assertions.assertThat( nodeIter.hasNext() ).isTrue();
			Node nextNode = nodeIter.next();
			assertNode( nextNode, "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

			ConstructorNode constructorNode = nextNode.as( ConstructorNode.class );
			Assertions.assertThat(  constructorNode  ).isNotNull();
			Assertions.assertThat(  constructorNode.getParameterTypes() ).isEqualTo( Arrays.<Class<?>>asList( String.class, Person.class  ) );

			Assertions.assertThat( nodeIter.hasNext() ).isTrue();
			nextNode = nodeIter.next();
			assertNode( nextNode, "name", ElementKind.PARAMETER, false, null, null );
			nextNode.as( BeanNode.class );
	
		} ).isInstanceOf( ClassCastException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "z")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "az")
	public void testGetContainerClassGetTypeArgumentIndex() {
		// container element node
		Set<ConstraintViolation<MovieProduction>> constraintViolations = getValidator().validate( MovieProduction.invalidMapKey() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
		);
		ConstraintViolation<MovieProduction> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		Node node = nodeIter.next();
		assertNode( node, "locationsByScene", ElementKind.PROPERTY, false, null, null );
		PropertyNode propertyNode = node.as( PropertyNode.class );
		Assertions.assertThat(  propertyNode  ).isNotNull();
		Assertions.assertThat(  propertyNode.getContainerClass() ).isNull();
		Assertions.assertThat(  propertyNode.getTypeArgumentIndex() ).isNull();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		node = nodeIter.next();
		assertNode( node, "<map key>", ElementKind.CONTAINER_ELEMENT, true, null, "" );
		ContainerElementNode containerElementNode = node.as( ContainerElementNode.class );
		Assertions.assertThat(  containerElementNode  ).isNotNull();
		Assertions.assertThat(  containerElementNode.getContainerClass() ).isEqualTo( Map.class  );
		Assertions.assertThat(  containerElementNode.getTypeArgumentIndex() ).isEqualTo( Integer.valueOf( 0  ) );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();

		// property node
		constraintViolations = getValidator().validate( MovieProduction.invalidCascading() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotBlank.class )
		);
		constraintViolation = constraintViolations.iterator().next();

		nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		node = nodeIter.next();
		assertNode( node, "locationsByScene", ElementKind.PROPERTY, false, null, null );
		propertyNode = node.as( PropertyNode.class );
		Assertions.assertThat(  propertyNode  ).isNotNull();
		Assertions.assertThat(  propertyNode.getContainerClass() ).isNull();
		Assertions.assertThat(  propertyNode.getTypeArgumentIndex() ).isNull();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		node = nodeIter.next();
		assertNode( node, "zipCode", ElementKind.PROPERTY, true, null, "Scene 1" );
		propertyNode = node.as( PropertyNode.class );
		Assertions.assertThat(  propertyNode  ).isNotNull();
		Assertions.assertThat(  propertyNode.getContainerClass() ).isEqualTo( Map.class  );
		Assertions.assertThat(  propertyNode.getTypeArgumentIndex() ).isEqualTo( Integer.valueOf( 1  ) );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();

		// bean node
		constraintViolations = getValidator().validate( MovieProduction.invalidExecutiveProducer() );

		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( ValidExecutiveProducer.class )
		);
		constraintViolation = constraintViolations.iterator().next();

		nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		node = nodeIter.next();
		assertNode( node, "executiveProducers", ElementKind.PROPERTY, false, null, null );
		propertyNode = node.as( PropertyNode.class );
		Assertions.assertThat(  propertyNode  ).isNotNull();
		Assertions.assertThat(  propertyNode.getContainerClass() ).isNull();
		Assertions.assertThat(  propertyNode.getTypeArgumentIndex() ).isNull();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		node = nodeIter.next();
		assertNode( node, null, ElementKind.BEAN, true, 0, null );
		BeanNode beanNode = node.as( BeanNode.class );
		Assertions.assertThat(  beanNode  ).isNotNull();
		Assertions.assertThat(  beanNode.getContainerClass() ).isEqualTo( List.class  );
		Assertions.assertThat(  beanNode.getTypeArgumentIndex() ).isEqualTo( Integer.valueOf( 0  ) );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	private void checkActorViolations(Set<ConstraintViolation<Actor>> constraintViolations) {
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( NotNull.class )
		);

		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "playedWith", ElementKind.PROPERTY, false, null, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "playedWith", ElementKind.PROPERTY, true, 0, null );

		Assertions.assertThat( nodeIter.hasNext() ).isTrue();
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		Assertions.assertThat( nodeIter.hasNext() ).isFalse();
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n")
	public void testPathLength() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator()
				.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);
		Path path = constraintViolations.iterator().next().getPropertyPath();

		Assertions.assertThat( path.length() ).isEqualTo( 1 );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o")
	public void testPathGetNode() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator()
				.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);
		Path path = constraintViolations.iterator().next().getPropertyPath();

		Node node = path.getNode( 0 );
		Assertions.assertThat( node.getName() ).isEqualTo( "serialNumber" );
		Assertions.assertThat( node.getKind() ).isEqualTo( ElementKind.PROPERTY );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o")
	public void testPathGetNodeOutOfBounds() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator()
				.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);
		Path path = constraintViolations.iterator().next().getPropertyPath();

		Assertions.assertThatThrownBy( () -> path.getNode( 1 ) )
				.isInstanceOf( IndexOutOfBoundsException.class );
		Assertions.assertThatThrownBy( () -> path.getNode( 10 ) )
				.isInstanceOf( IndexOutOfBoundsException.class );
		Assertions.assertThatThrownBy( () -> path.getNode( -1 ) )
				.isInstanceOf( IndexOutOfBoundsException.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p")
	public void testPathGetRootNode() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator()
				.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);
		Path path = constraintViolations.iterator().next().getPropertyPath();

		// getRootNode() must return the same node as getNode(0)
		Node rootNode = path.getRootNode();
		Node nodeZero = path.getNode( 0 );
		Assertions.assertThat( rootNode.getName() ).isEqualTo( nodeZero.getName() );
		Assertions.assertThat( rootNode.getKind() ).isEqualTo( nodeZero.getKind() );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q")
	public void testPathGetLeafNode() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator()
				.validate( engine );
		assertThat( constraintViolations ).containsOnlyViolations(
				violationOf( Pattern.class )
		);
		Path path = constraintViolations.iterator().next().getPropertyPath();

		Node leafNode = path.getLeafNode();
		Assertions.assertThat( leafNode.getName() ).isEqualTo( "serialNumber" );
		Assertions.assertThat( leafNode.getKind() ).isEqualTo( ElementKind.PROPERTY );

		// For a single-node path, leaf and root should be the same
		Assertions.assertThat( leafNode.getName() ).isEqualTo( path.getRootNode().getName() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q")
	})
	public void testPathDirectAccessConsistentWithIterator() {
		Employee employee = employWithoutFirstName();
		Set<ConstraintViolation<Employee>> constraintViolations = getValidator().validate( employee );
		Assertions.assertThat( constraintViolations ).hasSizeGreaterThanOrEqualTo( 1 );

		ConstraintViolation<Employee> constraintViolation = constraintViolations.iterator().next();
		Path path = constraintViolation.getPropertyPath();

		// Collect nodes via iterator
		int iteratorCount = 0;
		Node firstViaIterator = null;
		Node lastViaIterator = null;
		for ( Node node : path ) {
			if ( iteratorCount == 0 ) {
				firstViaIterator = node;
			}
			lastViaIterator = node;
			iteratorCount++;
		}

		Assertions.assertThat( firstViaIterator ).isNotNull();
		Assertions.assertThat( lastViaIterator ).isNotNull();

		// Verify length() matches iterator count
		Assertions.assertThat( path.length() ).isEqualTo( iteratorCount );

		// Verify getRootNode() matches first iterator node
		Assertions.assertThat( path.getRootNode().getName() ).isEqualTo( firstViaIterator.getName() );
		Assertions.assertThat( path.getRootNode().getKind() ).isEqualTo( firstViaIterator.getKind() );

		// Verify getLeafNode() matches last iterator node
		Assertions.assertThat( path.getLeafNode().getName() ).isEqualTo( lastViaIterator.getName() );
		Assertions.assertThat( path.getLeafNode().getKind() ).isEqualTo( lastViaIterator.getKind() );

		// Verify getNode(i) matches each iterator node
		int index = 0;
		for ( Node node : path ) {
			Node indexed = path.getNode( index );
			Assertions.assertThat( indexed.getName() ).isEqualTo( node.getName() );
			Assertions.assertThat( indexed.getKind() ).isEqualTo( node.getKind() );
			index++;
		}
	}

	private void assertNode(Path.Node actualNode, String expectedName, ElementKind expectedKind, boolean expectedInIterable, Integer expectedIndex, Object expectedKey) {
		Assertions.assertThat(  actualNode.getName() ).isEqualTo( expectedName  );
		Assertions.assertThat(  actualNode.getKind() ).isEqualTo( expectedKind  );
		Assertions.assertThat(  actualNode.isInIterable() ).isEqualTo( expectedInIterable  );
		Assertions.assertThat(  actualNode.getIndex() ).isEqualTo( expectedIndex  );
		Assertions.assertThat(  actualNode.getKey() ).isEqualTo( expectedKey  );
	}

	private Employee employWithoutFirstName() {
		return new Employee( null, "Hotchcick" );
	}

	private Employee validEmployee() {
		return new Employee( "Albert", "Hotchcick" );
	}

	private ActorListBased actorWithoutLastName() {
		return new ActorListBased( "Garry", null );
	}

	private ActorListBased validActor() {
		return new ActorListBased( "Glen", "Closed" );
	}

	private String validFilmTitle() {
		return "Jakarta Validation - Director's Cut";
	}

	private String validStudioName() {
		return "AcmeStudios";
	}

	@Special
	class VerySpecialClass {
	}

	@Constraint(validatedBy = { SpecialValidator.class })
	@Target({ TYPE })
	@Retention(RUNTIME)
	public @interface Special {
		String message() default "special validation failed";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	public static class SpecialValidator implements ConstraintValidator<Special, VerySpecialClass> {

		@Override
		public boolean isValid(VerySpecialClass clazz, ConstraintValidatorContext constraintValidatorContext) {
			return false;
		}
	}

	class ActorDB {
		private int idGen = 0;

		@Valid
		Map<Integer, Actor> actors = new HashMap<Integer, Actor>();

		public Integer addActor(Actor actor) {
			Integer id = idGen++;
			actors.put( id, actor );
			return id;
		}
	}
}
