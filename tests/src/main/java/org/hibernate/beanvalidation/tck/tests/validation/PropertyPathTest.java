/**
 * Bean Validation TCK
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
import static org.hibernate.beanvalidation.tck.util.ConstraintViolationAssert.assertNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;
import static org.hibernate.beanvalidation.tck.util.TestUtil.getConstraintViolationForParameter;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

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

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Path.BeanNode;
import javax.validation.Path.ConstructorNode;
import javax.validation.Path.ContainerElementNode;
import javax.validation.Path.CrossParameterNode;
import javax.validation.Path.MethodNode;
import javax.validation.Path.Node;
import javax.validation.Path.ParameterNode;
import javax.validation.Path.PropertyNode;
import javax.validation.Path.ReturnValueNode;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.executable.ExecutableValidator;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.tests.AbstractTCKTest;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Tests for property paths retrieved via {@link ConstraintViolation#getPropertyPath()}.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
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
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "w"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ai")
	})
	public void testPropertyPathWithConstraintViolationForRootObject() {
		Set<ConstraintViolation<VerySpecialClass>> constraintViolations = getValidator().validate( new VerySpecialClass() );
		assertNumberOfViolations( constraintViolations, 1 );
		ConstraintViolation<VerySpecialClass> constraintViolation = constraintViolations.iterator()
				.next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		Node node = nodeIter.next();
		assertNode( node, BEAN_NODE_NAME, ElementKind.BEAN, false, null, null );
		BeanNode beanNode = node.as( BeanNode.class );
		assertNotNull( beanNode );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "x"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah")
	})
	public void testPropertyPathTraversedObject() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = getValidator().validate( engine );
		assertNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Engine> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		Node node = nodeIter.next();
		assertNode( node, "serialNumber", ElementKind.PROPERTY, false, null, null );
		PropertyNode propertyNode = node.as( PropertyNode.class );
		assertNotNull( propertyNode );
		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "y"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aa")
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
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "y"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aa")
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
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "v"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "y"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aa")
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
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "z"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aa")
	})
	public void testPropertyPathWithMap() {
		ActorDB db = new ActorDB();
		Actor morgan = new ActorArrayBased( "Morgan", null );
		Integer id = db.addActor( morgan );

		Set<ConstraintViolation<ActorDB>> constraintViolations = getValidator().validate( db );
		assertNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<ActorDB> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "actors", ElementKind.PROPERTY, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, id );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aa")
	})
	public void testPropertyPathSet() {
		Customer customer = new Customer();
		customer.setFirstName( "John" );
		customer.setLastName( "Doe" );
		Order order = new Order();
		customer.addOrder( order );

		Set<ConstraintViolation<Customer>> constraintViolations = getValidator().validate( customer );
		assertNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Customer> constraintViolation = constraintViolations.iterator().next();
		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "orders", ElementKind.PROPERTY, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "orderNumber", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aj"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ak"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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
		assertNumberOfViolations( constraintViolations, 2 );

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"title"
		).getPropertyPath().iterator();

		//parameter 0
		assertTrue( nodeIter.hasNext() );
		Node nextNode = nodeIter.next();
		assertNode( nextNode, methodName, ElementKind.METHOD, false, null, null );

		MethodNode methodNode = nextNode.as( MethodNode.class );
		assertNotNull( methodNode );
		assertEquals(
				methodNode.getParameterTypes(),
				Arrays.<Class<?>>asList( String.class, Person.class, List.class )
		);

		assertTrue( nodeIter.hasNext() );
		nextNode = nodeIter.next();
		assertNode( nextNode, "title", ElementKind.PARAMETER, false, null, null );
		ParameterNode parameterNode = nextNode.as( ParameterNode.class );
		assertNotNull( parameterNode );
		assertEquals( parameterNode.getParameterIndex(), 0 );

		assertFalse( nodeIter.hasNext() );

		//parameter 1
		nodeIter = getConstraintViolationForParameter( constraintViolations, "director" ).getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		nextNode = nodeIter.next();
		assertNode( nextNode, "director", ElementKind.PARAMETER, false, null, null );
		parameterNode = nextNode.as( ParameterNode.class );
		assertNotNull( parameterNode );
		assertEquals( parameterNode.getParameterIndex(), 1 );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ak"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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
		assertNumberOfViolations( constraintViolations, 2 );

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"param0"
		).getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "param0", ElementKind.PARAMETER, false, null, null );

		assertFalse( nodeIter.hasNext() );

		nodeIter = getConstraintViolationForParameter( constraintViolations, "param1" ).getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "param1", ElementKind.PARAMETER, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aj"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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
		assertNumberOfViolations( constraintViolations, 1 );
		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		Node nextNode = nodeIter.next();
		assertNode( nextNode, RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		ReturnValueNode returnValueNode = nextNode.as( ReturnValueNode.class );
		assertNotNull( returnValueNode );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aj"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "al"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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
		assertNumberOfViolations( constraintViolations, 1 );
		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode(
				nodeIter.next(),
				CROSS_PARAMETER_NODE_NAME,
				ElementKind.CROSS_PARAMETER,
				false,
				null,
				null
		);

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "t"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aj"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ak"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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
		assertNumberOfViolations( constraintViolations, 2 );

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"name"
		).getPropertyPath().iterator();

		//parameter 0
		assertTrue( nodeIter.hasNext() );
		Node nextNode = nodeIter.next();
		assertNode( nextNode, "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		ConstructorNode constructorNode = nextNode.as( ConstructorNode.class );
		assertNotNull( constructorNode );
		assertEquals( constructorNode.getParameterTypes(), Arrays.<Class<?>>asList( String.class, Person.class ) );

		assertTrue( nodeIter.hasNext() );
		nextNode = nodeIter.next();
		assertNode( nextNode, "name", ElementKind.PARAMETER, false, null, null );
		ParameterNode parameterNode = nextNode.as( ParameterNode.class );
		assertNotNull( parameterNode );
		assertEquals( parameterNode.getParameterIndex(), 0 );

		assertFalse( nodeIter.hasNext() );

		//parameter 1
		nodeIter = getConstraintViolationForParameter( constraintViolations, "generalManager" ).getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		nextNode = nodeIter.next();
		assertNode( nextNode, "generalManager", ElementKind.PARAMETER, false, null, null );
		parameterNode = nextNode.as( ParameterNode.class );
		assertNotNull( parameterNode );
		assertEquals( parameterNode.getParameterIndex(), 1 );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ak"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "param0", ElementKind.PARAMETER, false, null, null );

		assertFalse( nodeIter.hasNext() );

		nodeIter = getConstraintViolationForParameter( constraintViolations, "param1" ).getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "param1", ElementKind.PARAMETER, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aj"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "al"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
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
		assertNotNull( crossParameterNode );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.CONSTRAINTSDEFINITIONIMPLEMENTATION_VALIDATIONIMPLEMENTATION, id = "s"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aj"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "am"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "an")
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
		assertNumberOfViolations( constraintViolations, 1 );
		Iterator<Path.Node> nodeIter = constraintViolations.iterator().next().getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "director", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "firstName", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "actors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, "Garry" );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "generalManager", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "firstName", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ap"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "recurringActors", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, "Garry" );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ar"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "as"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "at")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, null, "NO_TITLE" );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "l"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "m"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "n"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "o"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "p"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "q"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ah"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "ao"),
			@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "aq")
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
		assertNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), RETURN_VALUE_NODE_NAME, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "name", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test(expectedExceptions = ClassCastException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r")
	public void testPassingWrongTypeToAsOnBeanNodeCausesClassCastException() {
		Set<ConstraintViolation<VerySpecialClass>> constraintViolations = getValidator().validate( new VerySpecialClass() );
		assertNumberOfViolations( constraintViolations, 1 );
		ConstraintViolation<VerySpecialClass> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		Node node = nodeIter.next();
		assertNode( node, BEAN_NODE_NAME, ElementKind.BEAN, false, null, null );

		node.as( PropertyNode.class );
	}

	@Test(expectedExceptions = ClassCastException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r")
	public void testPassingWrongTypeToAsOnConstructorNodeCausesClassCastException() throws Exception {
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
		assertNumberOfViolations( constraintViolations, 2 );

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"name"
		).getPropertyPath().iterator();

		//parameter 0
		assertTrue( nodeIter.hasNext() );
		Node nextNode = nodeIter.next();
		assertNode( nextNode, "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		nextNode.as( PropertyNode.class );
	}

	@Test(expectedExceptions = ClassCastException.class)
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "r")
	public void testPassingWrongTypeToAsOnParameterNodeCausesClassCastException() throws Exception {
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
		assertNumberOfViolations( constraintViolations, 2 );

		Iterator<Path.Node> nodeIter = getConstraintViolationForParameter(
				constraintViolations,
				"name"
		).getPropertyPath().iterator();

		//parameter 0
		assertTrue( nodeIter.hasNext() );
		Node nextNode = nodeIter.next();
		assertNode( nextNode, "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		ConstructorNode constructorNode = nextNode.as( ConstructorNode.class );
		assertNotNull( constructorNode );
		assertEquals( constructorNode.getParameterTypes(), Arrays.<Class<?>>asList( String.class, Person.class ) );

		assertTrue( nodeIter.hasNext() );
		nextNode = nodeIter.next();
		assertNode( nextNode, "name", ElementKind.PARAMETER, false, null, null );
		nextNode.as( BeanNode.class );
	}

	@Test
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "u")
	@SpecAssertion(section = Sections.VALIDATIONAPI_CONSTRAINTVIOLATION, id = "au")
	public void testGetContainerClassGetTypeArgumentIndex() {
		// container element node
		Set<ConstraintViolation<MovieProduction>> constraintViolations = getValidator().validate( MovieProduction.invalidMapKey() );

		assertNumberOfViolations( constraintViolations, 1 );
		ConstraintViolation<MovieProduction> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		Node node = nodeIter.next();
		assertNode( node, "locationsByScene", ElementKind.PROPERTY, false, null, null );
		PropertyNode propertyNode = node.as( PropertyNode.class );
		assertNotNull( propertyNode );
		assertNull( propertyNode.getContainerClass() );
		assertNull( propertyNode.getTypeArgumentIndex() );

		assertTrue( nodeIter.hasNext() );
		node = nodeIter.next();
		assertNode( node, "<map key>", ElementKind.CONTAINER_ELEMENT, true, null, "" );
		ContainerElementNode containerElementNode = node.as( ContainerElementNode.class );
		assertNotNull( containerElementNode );
		assertEquals( containerElementNode.getContainerClass(), Map.class );
		assertEquals( containerElementNode.getTypeArgumentIndex(), Integer.valueOf( 0 ) );

		assertFalse( nodeIter.hasNext() );

		// property node
		constraintViolations = getValidator().validate( MovieProduction.invalidCascading() );

		assertNumberOfViolations( constraintViolations, 1 );
		constraintViolation = constraintViolations.iterator().next();

		nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		node = nodeIter.next();
		assertNode( node, "locationsByScene", ElementKind.PROPERTY, false, null, null );
		propertyNode = node.as( PropertyNode.class );
		assertNotNull( propertyNode );
		assertNull( propertyNode.getContainerClass() );
		assertNull( propertyNode.getTypeArgumentIndex() );

		assertTrue( nodeIter.hasNext() );
		node = nodeIter.next();
		assertNode( node, "zipCode", ElementKind.PROPERTY, true, null, "Scene 1" );
		propertyNode = node.as( PropertyNode.class );
		assertNotNull( propertyNode );
		assertEquals( propertyNode.getContainerClass(), Map.class );
		assertEquals( propertyNode.getTypeArgumentIndex(), Integer.valueOf( 1 ) );

		assertFalse( nodeIter.hasNext() );

		// bean node
		constraintViolations = getValidator().validate( MovieProduction.invalidExecutiveProducer() );

		assertNumberOfViolations( constraintViolations, 1 );
		constraintViolation = constraintViolations.iterator().next();

		nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		node = nodeIter.next();
		assertNode( node, "executiveProducers", ElementKind.PROPERTY, false, null, null );
		propertyNode = node.as( PropertyNode.class );
		assertNotNull( propertyNode );
		assertNull( propertyNode.getContainerClass() );
		assertNull( propertyNode.getTypeArgumentIndex() );

		assertTrue( nodeIter.hasNext() );
		node = nodeIter.next();
		assertNode( node, null, ElementKind.BEAN, true, 0, null );
		BeanNode beanNode = node.as( BeanNode.class );
		assertNotNull( beanNode );
		assertEquals( beanNode.getContainerClass(), List.class );
		assertEquals( beanNode.getTypeArgumentIndex(), Integer.valueOf( 0 ) );

		assertFalse( nodeIter.hasNext() );
	}

	private void checkActorViolations(Set<ConstraintViolation<Actor>> constraintViolations) {
		assertNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "playedWith", ElementKind.PROPERTY, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "playedWith", ElementKind.PROPERTY, true, 0, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	private void assertNode(Path.Node actualNode, String expectedName, ElementKind expectedKind, boolean expectedInIterable, Integer expectedIndex, Object expectedKey) {
		assertEquals( actualNode.getName(), expectedName );
		assertEquals( actualNode.getKind(), expectedKind );
		assertEquals( actualNode.isInIterable(), expectedInIterable );
		assertEquals( actualNode.getIndex(), expectedIndex );
		assertEquals( actualNode.getKey(), expectedKey );
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
		return "Bean Validation - Director's Cut";
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
