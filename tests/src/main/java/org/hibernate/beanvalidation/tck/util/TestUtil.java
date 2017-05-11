/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.util;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Configuration;
import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.MessageInterpolator;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.bootstrap.ProviderSpecificBootstrap;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ConstructorDescriptor;
import javax.validation.metadata.ElementDescriptor;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.PropertyDescriptor;
import javax.validation.spi.ValidationProvider;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.FileAssert.fail;

import org.hibernate.beanvalidation.tck.tests.BaseExecutableValidatorTest;
import org.hibernate.beanvalidation.tck.tests.BaseValidatorTest;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;

/**
 * @author Hardy Ferentschik
 * @author Gunnar Morling
 */
public final class TestUtil {

	/**
	 * Expected name for return value nodes.
	 */
	public static final String BEAN_NODE_NAME = null;

	/**
	 * Expected name for return value nodes.
	 */
	public static final String RETURN_VALUE_NODE_NAME = "<return value>";

	/**
	 * Expected name for cross-parameter nodes.
	 */
	public static final String CROSS_PARAMETER_NODE_NAME = "<cross-parameter>";

	private static final String VALIDATION_PROVIDER_TEST_CLASS = "validation.provider";

	private static ValidationProvider<?> validationProviderUnderTest;

	private TestUtil() {
	}

	public static Validator getValidatorUnderTest() {
		return getValidatorFactoryUnderTest().getValidator();
	}

	public static ValidationProvider<?> getValidationProviderUnderTest() {
		if ( validationProviderUnderTest == null ) {
			instantiateValidationProviderUnderTest();
		}
		return validationProviderUnderTest;
	}

	public static ValidatorFactory getValidatorFactoryUnderTest() {
		Configuration<?> config = getConfigurationUnderTest();
		return config.buildValidatorFactory();
	}

	public static Configuration<?> getConfigurationUnderTest() {
		if ( validationProviderUnderTest == null ) {
			instantiateValidationProviderUnderTest();
		}

		@SuppressWarnings("unchecked")
		ProviderSpecificBootstrap<?> bootstrap = Validation.byProvider( validationProviderUnderTest.getClass() );
		return bootstrap.configure();
	}

	public static MessageInterpolator getDefaultMessageInterpolator() {
		Configuration<?> config = getConfigurationUnderTest();
		return config.getDefaultMessageInterpolator();
	}

	public static <T> void assertCorrectNumberOfViolations(Set<ConstraintViolation<T>> violations, int expectedViolations) {
		assertEquals(
				violations.size(),
				expectedViolations,
				"Wrong number of constraint violations. Actual violations: " + violations
		);
	}

	public static <T> void assertCorrectConstraintViolationMessages(Set<ConstraintViolation<T>> violations, String... messages) {
		List<String> actualMessages = new ArrayList<String>();
		for ( ConstraintViolation<?> violation : violations ) {
			actualMessages.add( violation.getMessage() );
		}

		List<String> expectedMessages = Arrays.asList( messages );

		Collections.sort( actualMessages );
		Collections.sort( expectedMessages );

		assertEquals(
				actualMessages,
				expectedMessages,
				String.format( "Expected messages %s, but got %s.", expectedMessages, actualMessages )
		);
	}

	public static void assertCorrectConstraintTypes(Set<? extends ConstraintViolation<?>> violations, Class<?>... expectedConstraintTypes) {
		List<String> actualConstraintTypeNames = new ArrayList<String>();
		for ( ConstraintViolation<?> violation : violations ) {
			actualConstraintTypeNames.add(
					violation.getConstraintDescriptor()
							.getAnnotation()
							.annotationType()
							.getName()
			);
		}

		List<String> expectedConstraintTypeNames = new ArrayList<String>();
		for ( Class<?> expectedConstraintType : expectedConstraintTypes ) {
			expectedConstraintTypeNames.add( expectedConstraintType.getName() );
		}

		Collections.sort( actualConstraintTypeNames );
		Collections.sort( expectedConstraintTypeNames );

		assertEquals(
				actualConstraintTypeNames,
				expectedConstraintTypeNames,
				String.format( "Expected %s, but got %s", expectedConstraintTypeNames, actualConstraintTypeNames )
		);
	}

	public static <T> void assertCorrectPropertyPaths(Set<ConstraintViolation<T>> violations, String... propertyPaths) {
		List<Path> propertyPathsOfViolations = new ArrayList<Path>();
		for ( ConstraintViolation<?> violation : violations ) {
			propertyPathsOfViolations.add( violation.getPropertyPath() );
		}

		assertEquals(
				propertyPathsOfViolations.size(),
				propertyPaths.length,
				"Wrong number of property paths."
		);

		for ( String propertyPath : propertyPaths ) {
			Path expectedPath = PathImpl.createPathFromString( propertyPath );
			boolean containsPath = false;
			for ( Path actualPath : propertyPathsOfViolations ) {
				if ( assertEqualPaths( expectedPath, actualPath ) ) {
					containsPath = true;
					break;
				}
			}
			if ( !containsPath ) {
				fail( expectedPath + " is not in the list of path instances contained in the actual constraint violations: " + propertyPathsOfViolations );
			}
		}
	}

	public static void assertCorrectPathNodeKinds(Set<? extends ConstraintViolation<?>> violations, PathNodeKinds... kinds) {
		List<PathNodeKinds> actualDescriptorKinds = getPathDescriptorKinds( violations );
		List<PathNodeKinds> expectedDescriptorKinds = Arrays.asList( kinds );

		Collections.sort( actualDescriptorKinds );
		Collections.sort( expectedDescriptorKinds );

		assertEquals( actualDescriptorKinds, expectedDescriptorKinds );
	}

	public static void assertCorrectPathNodeNames(Set<? extends ConstraintViolation<?>> violations, PathNodeNames... names) {
		List<PathNodeNames> actualNodeNames = getPathNodeNames( violations );
		List<PathNodeNames> expectedNodeNames = Arrays.asList( names );

		Collections.sort( actualNodeNames );
		Collections.sort( expectedNodeNames );

		assertEquals(
				actualNodeNames,
				expectedNodeNames,
				String.format( "Expected path node names %s, but found %s.", expectedNodeNames, actualNodeNames )
		);
	}

	public static <T> void assertConstraintViolation(ConstraintViolation<T> violation, Class<?> rootBean, Object invalidValue, String propertyPath) {
		Path expectedPath = PathImpl.createPathFromString( propertyPath );
		if ( !assertEqualPaths( violation.getPropertyPath(), expectedPath ) ) {
			fail( "Property paths differ. Actual: " + violation.getPropertyPath() + " Expected: " + expectedPath );
		}

		assertEquals(
				violation.getRootBeanClass(),
				rootBean,
				"Wrong root bean."
		);
		assertEquals(
				violation.getInvalidValue(),
				invalidValue,
				"Wrong invalid value."
		);
	}

	public static void assertDescriptorKinds(Path path, ElementKind... kinds) {
		Iterator<Node> pathIterator = path.iterator();

		for ( ElementKind kind : kinds ) {
			assertTrue( pathIterator.hasNext() );
			assertEquals( pathIterator.next().getKind(), kind );
		}

		assertFalse( pathIterator.hasNext() );
	}

	public static void assertNodeNames(Path path, String... names) {
		Iterator<Node> pathIterator = path.iterator();

		for ( String name : names ) {
			assertTrue( pathIterator.hasNext() );
			assertEquals( pathIterator.next().getName(), name );
		}

		assertFalse( pathIterator.hasNext() );
	}

	public static boolean assertEqualPaths(Path p1, Path p2) {
		Iterator<Path.Node> p1Iterator = p1.iterator();
		Iterator<Path.Node> p2Iterator = p2.iterator();
		while ( p1Iterator.hasNext() ) {
			Path.Node p1Node = p1Iterator.next();
			if ( !p2Iterator.hasNext() ) {
				return false;
			}
			Path.Node p2Node = p2Iterator.next();

			// do the comparison on the node values
			if ( p2Node.getName() == null ) {
				if ( p1Node.getName() != null ) {
					return false;
				}
			}
			else if ( !p2Node.getName().equals( p1Node.getName() ) ) {
				return false;
			}

			if ( p2Node.isInIterable() != p1Node.isInIterable() ) {
				return false;
			}

			if ( p2Node.getIndex() == null ) {
				if ( p1Node.getIndex() != null ) {
					return false;
				}
			}
			else if ( !p2Node.getIndex().equals( p1Node.getIndex() ) ) {
				return false;
			}

			if ( p2Node.getKey() == null ) {
				if ( p1Node.getKey() != null ) {
					return false;
				}
			}
			else if ( !p2Node.getKey().equals( p1Node.getKey() ) ) {
				return false;
			}
		}

		return !p2Iterator.hasNext();
	}

	/**
	 * Retrieves the parameter names from the given set of constraint
	 * violations, which must represent method or constructor constraint
	 * violations.
	 *
	 * @param constraintViolations The violations to retrieve the names from.
	 *
	 * @return The parameter names.
	 */
	public static Set<String> getParameterNames(Set<? extends ConstraintViolation<?>> constraintViolations) {
		Set<String> parameterNames = new HashSet<String>();

		for ( ConstraintViolation<?> constraintViolation : constraintViolations ) {
			parameterNames.add( getParameterName( constraintViolation.getPropertyPath() ) );
		}

		return parameterNames;
	}

	public static <T> ConstraintViolation<T> getConstraintViolationForParameter(Set<ConstraintViolation<T>> constraintViolations, String parameterName) {
		for ( ConstraintViolation<T> constraintViolation : constraintViolations ) {
			if ( parameterName.equals( getParameterName( constraintViolation.getPropertyPath() ) ) ) {
				return constraintViolation;
			}
		}

		throw new IllegalArgumentException( "Found no constraint violation for parameter " + parameterName );
	}

	public static <T> ConstraintViolation<T> getConstraintViolationForConstraintType(Set<ConstraintViolation<T>> constraintViolations,
																					 Class<? extends Annotation> constraint) {
		for ( ConstraintViolation<T> constraintViolation : constraintViolations ) {
			if ( constraintViolation.getConstraintDescriptor().getAnnotation().annotationType().equals( constraint ) ) {
				return constraintViolation;
			}
		}

		throw new IllegalArgumentException( "Found no constraint violation for constraint " + constraint.getSimpleName() );
	}

	public static String getParameterName(Path path) {
		Iterator<Node> nodes = path.iterator();

		assertTrue( nodes.hasNext() );
		nodes.next();

		assertTrue( nodes.hasNext() );
		return nodes.next().getName();
	}

	public static <T> Set<T> asSet(T... ts) {
		return new HashSet<T>( Arrays.asList( ts ) );
	}

	public static PathNodeKinds kinds(ElementKind... kinds) {
		return new PathNodeKinds( kinds );
	}

	public static PathNodeNames names(String... names) {
		return new PathNodeNames( names );
	}

	public static PropertyDescriptor getPropertyDescriptor(Class<?> clazz, String property) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz ).getConstraintsForProperty( property );
	}

	public static MethodDescriptor getMethodDescriptor(Class<?> clazz, String name, Class<?>... parameterTypes) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz )
				.getConstraintsForMethod( name, parameterTypes );
	}

	public static ConstructorDescriptor getConstructorDescriptor(Class<?> clazz, Class<?>... parameterTypes) {
		Validator validator = getValidatorUnderTest();
		return validator.getConstraintsForClass( clazz ).getConstraintsForConstructor(
				parameterTypes
		);
	}

	public static Set<ConstraintDescriptor<?>> getConstraintDescriptorsFor(Class<?> clazz, String property) {
		ElementDescriptor elementDescriptor = getPropertyDescriptor( clazz, property );
		return elementDescriptor.getConstraintDescriptors();
	}

	public static InputStream getInputStreamForPath(String path) {
		// try the context class loader first
		InputStream inputStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream( path );

		// try the current class loader
		if ( inputStream == null ) {
			inputStream = TestUtil.class.getResourceAsStream( path );
		}
		return inputStream;
	}

	public static WebArchiveBuilder webArchiveBuilder() {
		return new WebArchiveBuilder().withClasses(
				BaseExecutableValidatorTest.class,
				BaseValidatorTest.class,
				WebArchiveBuilder.class,
				TestUtil.class,
				ConstraintViolationAssert.class
		);
	}

	private static <U extends ValidationProvider<?>> void instantiateValidationProviderUnderTest() {
		String validatorProviderClassName = System.getProperty( VALIDATION_PROVIDER_TEST_CLASS );
		if ( validatorProviderClassName == null ) {
			throw new RuntimeException(
					"The test harness must specify the class name of the validation provider under test. Set system property '" + VALIDATION_PROVIDER_TEST_CLASS + "'"
			);
		}

		Class<U> providerClass;
		try {
			@SuppressWarnings("unchecked")
			Class<U> tmpClazz = (Class<U>) TestUtil.class.getClassLoader()
					.loadClass( validatorProviderClassName );
			providerClass = tmpClazz;
		}
		catch ( ClassNotFoundException e ) {
			throw new RuntimeException( "Unable to load " + validatorProviderClassName );
		}

		try {
			validationProviderUnderTest = providerClass.newInstance();
		}
		catch ( Exception e ) {
			throw new RuntimeException( "Unable to instantiate " + validatorProviderClassName );
		}
	}

	private static List<PathNodeKinds> getPathDescriptorKinds(Set<? extends ConstraintViolation<?>> violations) {
		List<PathNodeKinds> descriptorKindsOfAllPaths = new ArrayList<PathNodeKinds>();

		for ( ConstraintViolation<?> violation : violations ) {
			descriptorKindsOfAllPaths.add( new PathNodeKinds( violation.getPropertyPath() ) );
		}

		return descriptorKindsOfAllPaths;
	}

	private static List<PathNodeNames> getPathNodeNames(Set<? extends ConstraintViolation<?>> violations) {
		List<PathNodeNames> nodeNamesOfAllPaths = new ArrayList<PathNodeNames>();

		for ( ConstraintViolation<?> violation : violations ) {
			nodeNamesOfAllPaths.add( new PathNodeNames( violation.getPropertyPath() ) );
		}

		return nodeNamesOfAllPaths;
	}

	public static class PathImpl implements Path {
		/**
		 * Regular expression used to split a string path into its elements.
		 *
		 * @see <a href="http://www.regexplanet.com/simple/index.jsp">Regular expression tester</a>
		 */
		private static final Pattern pathPattern = Pattern.compile(
				"(\\w+)(\\[(\\w*)\\])?(\\.(.*))*"
		);
		private static final int PROPERTY_NAME_GROUP = 1;
		private static final int INDEXED_GROUP = 2;
		private static final int INDEX_GROUP = 3;
		private static final int REMAINING_STRING_GROUP = 5;

		private static final String PROPERTY_PATH_SEPARATOR = ".";
		private static final String INDEX_OPEN = "[";
		private static final String INDEX_CLOSE = "]";

		private final List<Node> nodeList;

		/**
		 * Returns a {@code Path} instance representing the path described by the given string. To create a root node the empty string should be passed.
		 *
		 * @param propertyPath the path as string representation.
		 *
		 * @return a {@code Path} instance representing the path described by the given string.
		 *
		 * @throws IllegalArgumentException in case {@code property == null} or {@code property} cannot be parsed.
		 */
		public static PathImpl createPathFromString(String propertyPath) {
			if ( propertyPath == null ) {
				throw new IllegalArgumentException( "null is not allowed as property path." );
			}

			if ( propertyPath.length() == 0 ) {
				return createNewPath( null );
			}

			return parseProperty( propertyPath );
		}

		public static PathImpl createNewPath(String name) {
			PathImpl path = new PathImpl();
			NodeImpl node = new NodeImpl( name );
			path.addNode( node );
			return path;
		}

		private PathImpl() {
			nodeList = new ArrayList<Node>();
		}

		public void addNode(Node node) {
			nodeList.add( node );
		}

		@Override
		public Iterator<Path.Node> iterator() {
			return nodeList.iterator();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			Iterator<Path.Node> iter = iterator();
			boolean first = true;
			while ( iter.hasNext() ) {
				Node node = iter.next();
				if ( node.isInIterable() ) {
					appendIndex( builder, node );
				}
				if ( node.getName() != null ) {
					if ( !first ) {
						builder.append( PROPERTY_PATH_SEPARATOR );
					}
					builder.append( node.getName() );
				}
				first = false;
			}
			return builder.toString();
		}

		private void appendIndex(StringBuilder builder, Node node) {
			builder.append( INDEX_OPEN );
			if ( node.getIndex() != null ) {
				builder.append( node.getIndex() );
			}
			else if ( node.getKey() != null ) {
				builder.append( node.getKey() );
			}
			builder.append( INDEX_CLOSE );
		}

		private static PathImpl parseProperty(String property) {
			PathImpl path = new PathImpl();
			String tmp = property;
			boolean indexed = false;
			String indexOrKey = null;
			do {
				Matcher matcher = pathPattern.matcher( tmp );
				if ( matcher.matches() ) {
					String value = matcher.group( PROPERTY_NAME_GROUP );

					NodeImpl node = new NodeImpl( value );
					path.addNode( node );

					// need to look backwards!!
					if ( indexed ) {
						updateNodeIndexOrKey( indexOrKey, node );
					}

					indexed = matcher.group( INDEXED_GROUP ) != null;
					indexOrKey = matcher.group( INDEX_GROUP );

					tmp = matcher.group( REMAINING_STRING_GROUP );
				}
				else {
					throw new IllegalArgumentException( "Unable to parse property path " + property );
				}
			} while ( tmp != null );

			// check for a left over indexed node
			if ( indexed ) {
				NodeImpl node = new NodeImpl( (String) null );
				updateNodeIndexOrKey( indexOrKey, node );
				path.addNode( node );
			}
			return path;
		}

		private static void updateNodeIndexOrKey(String indexOrKey, NodeImpl node) {
			node.setInIterable( true );
			if ( indexOrKey != null && indexOrKey.length() > 0 ) {
				try {
					Integer i = Integer.parseInt( indexOrKey );
					node.setIndex( i );
				}
				catch ( NumberFormatException e ) {
					node.setKey( indexOrKey );
				}
			}
		}
	}

	public static class NodeImpl implements Path.Node {
		private final String name;
		private boolean isInIterable;
		private Integer index;
		private Object key;

		public NodeImpl(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public boolean isInIterable() {
			return isInIterable;
		}

		public void setInIterable(boolean inIterable) {
			isInIterable = inIterable;
		}

		@Override
		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			isInIterable = true;
			this.index = index;
		}

		@Override
		public Object getKey() {
			return key;
		}

		@Override
		public ElementKind getKind() {
			throw new UnsupportedOperationException();
		}

		@Override
		public <T extends Node> T as(Class<T> nodeType) {
			throw new UnsupportedOperationException();
		}

		public void setKey(Object key) {
			isInIterable = true;
			this.key = key;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append( "NodeImpl" );
			sb.append( "{index=" ).append( index );
			sb.append( ", name='" ).append( name ).append( '\'' );
			sb.append( ", isInIterable=" ).append( isInIterable );
			sb.append( ", key=" ).append( key );
			sb.append( '}' );
			return sb.toString();
		}
	}
}
