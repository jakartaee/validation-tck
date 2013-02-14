/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.validation;

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
import javax.validation.ExecutableValidator;
import javax.validation.Path;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.Validator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.hibernate.beanvalidation.tck.util.TestUtil.asSet;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeKinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPathNodeNames;
import static org.hibernate.beanvalidation.tck.util.TestUtil.kinds;
import static org.hibernate.beanvalidation.tck.util.TestUtil.names;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * Tests for property paths retrieved via {@link ConstraintViolation#getPropertyPath()}.
 *
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "1.1.0")
public class PropertyPathTest extends Arquillian {

	private Validator validator;

	private ExecutableValidator executableValidator;

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClass( PropertyPathTest.class )
				.withClasses(
						Actor.class,
						ActorArrayBased.class,
						ActorCollectionBased.class,
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
						ValidMovieStudioValidator.class

				)
				.build();
	}

	@BeforeMethod
	public void setupValidators() {
		validator = TestUtil.getValidatorUnderTest();
		executableValidator = validator.forExecutables();
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "i"),
			@SpecAssertion(section = "5.2", id = "o")
	})
	public void testPropertyPathWithConstraintViolationForRootObject() {
		Set<ConstraintViolation<VerySpecialClass>> constraintViolations = validator.validate( new VerySpecialClass() );
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		ConstraintViolation<VerySpecialClass> constraintViolation = constraintViolations.iterator()
				.next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();
		assertTrue( nodeIter.hasNext() );
		Path.Node node = nodeIter.next();
		assertNode( node, null, ElementKind.BEAN, false, null, null );
		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "j"),
			@SpecAssertion(section = "5.2", id = "n")
	})
	public void testPropertyPathTraversedObject() {
		Engine engine = new Engine();
		engine.setSerialNumber( "ABCDEFGH1234" );
		Set<ConstraintViolation<Engine>> constraintViolations = validator.validate( engine );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Engine> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();
		assertTrue( nodeIter.hasNext() );
		Path.Node node = nodeIter.next();
		assertEquals( node.getName(), "serialNumber" );
		assertFalse( node.isInIterable() );
		assertNull( node.getIndex() );
		assertNull( node.getKey() );
		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "k"),
			@SpecAssertion(section = "5.2", id = "m")
	})
	public void testPropertyPathWithList() {
		Actor clint = new ActorListBased( "Clint", "Eastwood" );
		Actor morgan = new ActorListBased( "Morgan", null );
		Actor charlie = new ActorListBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );

		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate( clint );
		checkActorViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "k"),
			@SpecAssertion(section = "5.2", id = "m")
	})
	public void testPropertyPathWithArray() {
		Actor clint = new ActorArrayBased( "Clint", "Eastwood" );
		Actor morgan = new ActorArrayBased( "Morgan", null );
		Actor charlie = new ActorArrayBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );

		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate( clint );
		checkActorViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "h"),
			@SpecAssertion(section = "5.2", id = "k"),
			@SpecAssertion(section = "5.2", id = "m")
	})
	public void testPropertyPathWithRuntimeTypeList() {
		Actor clint = new ActorCollectionBased( "Clint", "Eastwood" );
		Actor morgan = new ActorCollectionBased( "Morgan", null );
		Actor charlie = new ActorCollectionBased( "Charlie", "Sheen" );

		clint.addPlayedWith( charlie );
		charlie.addPlayedWith( clint );
		charlie.addPlayedWith( morgan );
		morgan.addPlayedWith( charlie );

		Set<ConstraintViolation<Actor>> constraintViolations = validator.validate( clint );
		checkActorViolations( constraintViolations );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "l"),
			@SpecAssertion(section = "5.2", id = "m")
	})
	public void testPropertyPathWithMap() {
		ActorDB db = new ActorDB();
		Actor morgan = new ActorArrayBased( "Morgan", null );
		Integer id = db.addActor( morgan );

		Set<ConstraintViolation<ActorDB>> constraintViolations = validator.validate( db );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<ActorDB> constraintViolation = constraintViolations.iterator().next();
		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();
		assertTrue( nodeIter.hasNext() );
		Path.Node node = nodeIter.next();
		assertEquals( node.getName(), "actors" );
		assertFalse( node.isInIterable() );
		assertNull( node.getKey() );
		assertNull( node.getIndex() );

		node = nodeIter.next();
		assertEquals( node.getName(), "lastName" );
		assertTrue( node.isInIterable() );
		assertEquals( node.getIndex(), null );
		assertEquals( node.getKey(), id );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "m")
	})
	public void testPropertyPathSet() {
		Customer customer = new Customer();
		customer.setFirstName( "John" );
		customer.setLastName( "Doe" );
		Order order = new Order();
		customer.addOrder( order );

		Set<ConstraintViolation<Customer>> constraintViolations = validator.validate( customer );
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Customer> constraintViolation = constraintViolations.iterator().next();
		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();
		assertTrue( nodeIter.hasNext() );
		Path.Node node = nodeIter.next();
		assertEquals( node.getName(), "orders" );
		assertFalse( node.isInIterable() );
		assertNull( node.getIndex() );
		assertNull( node.getKey() );

		node = nodeIter.next();
		assertEquals( node.getName(), "orderNumber" );
		assertTrue( node.isInIterable() );
		assertNull( node.getIndex() );
		assertNull( node.getKey() );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "p"),
			@SpecAssertion(section = "5.2", id = "q"),
			@SpecAssertion(section = "5.2", id = "r")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectPathNodeNames(
				constraintViolations,
				names( methodName, "arg0" ),
				names( methodName, "arg1" )
		);
		assertCorrectPathNodeKinds(
				constraintViolations,
				kinds( ElementKind.METHOD, ElementKind.PARAMETER ),
				kinds( ElementKind.METHOD, ElementKind.PARAMETER )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "q"),
			@SpecAssertion(section = "5.2", id = "r")
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
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectPathNodeNames(
				constraintViolations,
				names( methodName, "param0" ),
				names( methodName, "param1" )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "p"),
			@SpecAssertion(section = "5.2", id = "q"),
			@SpecAssertion(section = "5.2", id = "r")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectPathNodeNames( constraintViolations, names( methodName, null ) );
		assertCorrectPathNodeKinds(
				constraintViolations,
				kinds( ElementKind.METHOD, ElementKind.RETURN_VALUE )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "p"),
			@SpecAssertion(section = "5.2", id = "q"),
			@SpecAssertion(section = "5.2", id = "r")
	})
	public void testPropertyPathForConstructorParameterConstraint() throws Exception {
		//given
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
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectPathNodeNames(
				constraintViolations,
				names( "MovieStudio", "arg0" ),
				names( "MovieStudio", "arg1" )
		);
		assertCorrectPathNodeKinds(
				constraintViolations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.PARAMETER ),
				kinds( ElementKind.CONSTRUCTOR, ElementKind.PARAMETER )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "q"),
			@SpecAssertion(section = "5.2", id = "r")
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
		assertCorrectNumberOfViolations( constraintViolations, 2 );
		assertCorrectPathNodeNames(
				constraintViolations,
				names( "MovieStudio", "param0" ),
				names( "MovieStudio", "param1" )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "p"),
			@SpecAssertion(section = "5.2", id = "q"),
			@SpecAssertion(section = "5.2", id = "r")
	})
	public void testPropertyPathForConstructorReturnValueConstraint() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class
		);
		MovieStudio returnValue = new MovieStudio( null );

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );
		assertCorrectPathNodeNames( constraintViolations, names( "MovieStudio", null ) );
		assertCorrectPathNodeKinds(
				constraintViolations,
				kinds( ElementKind.CONSTRUCTOR, ElementKind.RETURN_VALUE )
		);
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg1", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "firstName", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "u"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "u"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "v"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters(
				object,
				method,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, "Garry" );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t")
	})
	public void testPropertyPathTraversingConstructorParameter() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor(
				String.class,
				Person.class
		);
		Object[] parameterValues = new Object[] { validStudioName(), employWithoutFirstName() };

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg1", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "firstName", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "u"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "u"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "v"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorParameters(
				constructor,
				parameterValues
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "arg2", ElementKind.PARAMETER, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "lastName", ElementKind.PROPERTY, true, null, "Garry" );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t")
	})
	public void testPropertyPathTraversingMethodReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMovie";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );
		Object returnValue = new Movie();

		//when
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), null, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "u"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), null, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "u"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), null, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, 1, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "w")
	})
	public void testPropertyPathTraversingMethodSetReturnValue() throws Exception {
		//given
		String methodName = "getBestSellingMoviesSetBased";
		Object object = new MovieStudio();
		Method method = MovieStudio.class.getMethod( methodName );
		Object returnValue = asSet( new Movie( validFilmTitle() ), new Movie() );

		//when
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), null, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t"),
			@SpecAssertion(section = "5.2", id = "v"),
			@SpecAssertion(section = "5.2", id = "w")
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
		Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateReturnValue(
				object,
				method,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), methodName, ElementKind.METHOD, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), null, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "title", ElementKind.PROPERTY, true, null, "NO_TITLE" );

		assertFalse( nodeIter.hasNext() );
	}

	@Test
	@SpecAssertions({
			@SpecAssertion(section = "5.2", id = "g"),
			@SpecAssertion(section = "5.2", id = "n"),
			@SpecAssertion(section = "5.2", id = "s"),
			@SpecAssertion(section = "5.2", id = "t")
	})
	public void testPropertyPathTraversingConstructorReturnValue() throws Exception {
		//given
		Constructor<MovieStudio> constructor = MovieStudio.class.getConstructor( String.class );
		MovieStudio returnValue = new MovieStudio( null );

		//when
		Set<ConstraintViolation<MovieStudio>> constraintViolations = executableValidator.validateConstructorReturnValue(
				constructor,
				returnValue
		);

		//then
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		Iterator<Path.Node> nodeIter = constraintViolations.iterator()
				.next()
				.getPropertyPath()
				.iterator();

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "MovieStudio", ElementKind.CONSTRUCTOR, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), null, ElementKind.RETURN_VALUE, false, null, null );

		assertTrue( nodeIter.hasNext() );
		assertNode( nodeIter.next(), "name", ElementKind.PROPERTY, false, null, null );

		assertFalse( nodeIter.hasNext() );
	}

	private void checkActorViolations(Set<ConstraintViolation<Actor>> constraintViolations) {
		assertCorrectNumberOfViolations( constraintViolations, 1 );

		ConstraintViolation<Actor> constraintViolation = constraintViolations.iterator().next();

		Iterator<Path.Node> nodeIter = constraintViolation.getPropertyPath().iterator();
		assertTrue( nodeIter.hasNext() );
		Path.Node node = nodeIter.next();
		assertEquals( node.getName(), "playedWith" );
		assertFalse( node.isInIterable() );
		assertNull( node.getIndex() );
		assertNull( node.getKey() );

		node = nodeIter.next();
		assertEquals( node.getName(), "playedWith" );
		assertTrue( node.isInIterable() );
		assertEquals( node.getIndex(), new Integer( 0 ) );
		assertNull( node.getKey() );

		node = nodeIter.next();
		assertEquals( node.getName(), "lastName" );
		assertTrue( node.isInIterable() );
		assertEquals( node.getIndex(), new Integer( 1 ) );
		assertNull( node.getKey() );

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

	@Special()
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
		public void initialize(Special constraintAnnotation) {
		}

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
