/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.util.shrinkwrap;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.container.LibraryContainer;
import org.jboss.shrinkwrap.api.container.ResourceContainer;
import org.jboss.shrinkwrap.impl.base.URLPackageScanner;

/**
 * Abstract ShrinkWrap archive builder for Bean Validation TCK Arquillian test.
 * <p>
 * This is a base class for builders that try to solve most <b>JBoss Test Harness</b> to <b>Arquillian</b> migration issues. The
 * main goal was to use Bean Validation TCK 1.0 tests with minimum code changes.
 * </p>
 *
 * @param <T> Self type to enable abstract builder pattern
 * @param <A> Final shrinkwrap archive
 *
 * @author Martin Kouba
 * @author Hardy Ferentschik
 */
public abstract class ArchiveBuilder<T extends ArchiveBuilder<T, A>, A extends Archive<A>> {

	private String name;
	private Class<?> testClazz = null;
	protected List<ResourceDescriptor> resources = null;
	protected List<String> packages = null;
	protected List<String> classes = null;
	protected List<ServiceProviderDescriptor> serviceProviders = null;
	protected List<File> additionalJars = null;

	public T withName(String name) {
		this.name = name;
		return self();
	}

	public T withServiceProvider(ServiceProviderDescriptor serviceProvider) {

		if ( serviceProviders == null ) {
			serviceProviders = new ArrayList<>();
		}

		serviceProviders.add( serviceProvider );
		return self();
	}

	public T withClass(Class<?> clazz) {
		if ( this.classes == null ) {
			this.classes = new ArrayList<>();
		}

		this.classes.add( clazz.getName() );
		return self();
	}

	public T withClasses(Class<?>... classes) {

		for ( Class<?> clazz : classes ) {
			withClass( clazz );
		}
		return self();
	}

	/**
	 * Add all classes in the test class package to archive and set test class definition for configuration purpose.
	 *
	 * @param testClazz the test class
	 *
	 * @return self
	 */
	public T withTestClassPackage(Class<?> testClazz) {
		return withTestClassDefinition( testClazz ).withPackage( testClazz.getPackage() );
	}

	/**
	 * Add test class to archive and set test class definition for configuration purpose.
	 *
	 * @param testClazz the test class
	 *
	 * @return self
	 */
	public T withTestClass(Class<?> testClazz) {
		return withTestClassDefinition( testClazz ).withClass( testClazz );
	}

	public T withTestClassDefinition(Class<?> testClazz) {

		if ( this.testClazz != null ) {
			throw new IllegalStateException( "Cannot set more than one test class definition!" );
		}

		this.testClazz = testClazz;
		return self();
	}

	public T withPackage(Package pack) {

		if ( this.packages == null ) {
			this.packages = new ArrayList<>();
		}

		this.packages.add( pack.getName() );
		return self();
	}


	public T withResource(String source) {
		return withResource( source, null, true );
	}

	public T withResource(String source, boolean useTestPackageToLocateSource) {
		return withResource( source, null, useTestPackageToLocateSource );
	}

	public T withResource(String source, String target, boolean useTestPackageToLocateSource) {
		if ( this.resources == null ) {
			this.resources = new ArrayList<>();
		}

		this.resources.add( new ResourceDescriptor( source, target, useTestPackageToLocateSource ) );

		return self();
	}

	public T withValidationXml(String source) {
		return withResource( source, "META-INF/validation.xml", true );
	}

	public abstract T withEmptyBeansXml();

	public T withAdditionalJar(String jarPath) {
		if ( additionalJars == null ) {
			additionalJars = new ArrayList<>();
		}

		additionalJars.add( new File( jarPath ) );

		return self();
	}

	/**
	 * @return self to enable generic builder
	 */
	public abstract T self();

	/**
	 * @return shrinkwrap archive
	 */
	public A build() {
		if ( testClazz == null ) {
			throw new IllegalStateException( "Test class must be set!" );
		}

		return buildInternal();
	}

	/**
	 * @return concrete shrinkwrap archive
	 */
	protected abstract A buildInternal();

	protected void processPackages(final ClassContainer<?> archive) {

		if ( packages == null ) {
			return;
		}

		for ( String pack : packages ) {

			final URLPackageScanner.Callback callback = new URLPackageScanner.Callback() {
				@Override
				public void classFound(String className) {
					archive.addClass( className );
				}
			};

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			if ( classLoader == null ) {
				classLoader = getClass().getClassLoader();
			}

			final URLPackageScanner scanner = URLPackageScanner.newInstance(
					false,
					classLoader,
					callback,
					pack
			);
			scanner.scanPackage();
		}
	}

	protected void processClasses(ClassContainer<?> archive) {
		if ( classes == null ) {
			return;
		}

		for ( String clazz : classes ) {
			if ( testClazz.getName().equals( clazz ) ) {
				continue;
			}

			archive.addClass( clazz );
		}
	}

	protected void processResources(ResourceContainer<?> archive) {
		if ( resources == null ) {
			return;
		}

		for ( ResourceDescriptor resource : resources ) {
			if ( resource.getSource() != null ) {
				if ( resource.getTarget() == null ) {
					archive.addAsResource( resource.getSource() );
				}
				else {
					archive.addAsResource( resource.getSource(), resource.getTarget() );
				}
			}
			else if ( resource.getAsset() != null ) {
				archive.addAsResource( resource.getAsset(), resource.getTarget() );
			}
		}
	}

	protected void processAdditionalJars(LibraryContainer<?> archive) {
		if ( additionalJars == null ) {
			return;
		}

		for ( File additionalJar : additionalJars ) {
			archive.addAsLibrary( additionalJar );
		}
	}

	/**
	 * Internal service provider descriptor.
	 *
	 * @author Martin Kouba
	 */
	protected class ServiceProviderDescriptor {
		private final Class<?> serviceInterface;

		private final Class<?>[] serviceImplementations;

		public ServiceProviderDescriptor(Class<?> serviceInterface, Class<?>... serviceImplementations) {
			super();
			this.serviceInterface = serviceInterface;
			this.serviceImplementations = serviceImplementations;
		}

		public Class<?> getServiceInterface() {
			return serviceInterface;
		}

		public Class<?>[] getServiceImplementations() {
			return serviceImplementations;
		}
	}

	/**
	 * Internal resource descriptor.
	 *
	 * @author Martin Kouba
	 */
	protected class ResourceDescriptor {

		private final Asset asset;
		private final String source;
		private final String target;
		private final boolean useTestPackageToLocateSource;

		public ResourceDescriptor(String source, String target, boolean useTestPackageToLocateSource) {
			this.asset = null;
			this.source = source;
			this.target = target;
			this.useTestPackageToLocateSource = useTestPackageToLocateSource;
		}

		public ResourceDescriptor(Asset asset, String target) {
			this.asset = asset;
			this.source = null;
			this.target = target;
			this.useTestPackageToLocateSource = false;
		}

		public Asset getAsset() {
			return asset;
		}

		public String getSource() {
			return useTestPackageToLocateSource ? getTestPackagePath() + source : source;
		}

		public String getPlainSource() {
			return source;
		}

		public String getTarget() {
			return target;
		}
	}

	private String getTestPackagePath() {
		return this.testClazz.getPackage().getName().replace( '.', '/' ).concat( "/" );
	}

	/**
	 * @return name of final archive
	 */
	public String getName() {
		return name;
	}
}
