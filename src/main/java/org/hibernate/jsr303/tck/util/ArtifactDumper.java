/*
* JBoss, Home of Professional Open Source
* Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.jsr303.tck.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;

/**
 * @author Hardy Ferentschik
 */
public class ArtifactDumper {
	private static Logger logger = Logger.getLogger( ArtifactDumper.class.getName() );
	private static File artifactDir;

	static {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		// get a URL reference to something we now is part of the classpath (our own classes)
		String currentClassName = new RuntimeException().getStackTrace()[0].getClassName();
		int hopsToRoot = currentClassName.split( "\\." ).length;
		URL url = contextClassLoader.getResource( currentClassName.replace( '.', '/' ) + ".class" );
		if ( url == null ) {
			throw new RuntimeException( "Unable to determine URL of " + currentClassName );
		}
		// navigate back to '/target'
		File targetDir = new File( url.getFile() );
		for ( int i = 0; i <= hopsToRoot; i++ ) {
			targetDir = targetDir.getParentFile();
		}

		artifactDir = new File( targetDir, "artifacts" );
		if ( !artifactDir.mkdirs() ) {
			throw new RuntimeException( "Unable to create artifact dump directory: " + artifactDir.getPath() );
		}
	}

	public static void main(String[] args) throws Exception {
		List<Class<?>> testClasses = getClassesForPackage( "org.hibernate.jsr303.tck" );
		for ( Class<?> clazz : testClasses ) {
			processClass( clazz );
		}
	}

	private static void processClass(Class<?> clazz) throws Exception {
		for ( Method m : clazz.getMethods() ) {
			if ( m.isAnnotationPresent( Deployment.class ) ) {
				Object o = clazz.newInstance();
				Archive<?> archive = (Archive<?>) m.invoke( o );
				logger.info( archive.toString( true ) );
				archive.as( ZipExporter.class ).exportTo( new File( artifactDir, clazz.getName() + ".war" ), true );
			}
		}
	}

	private static List<Class<?>> getClassesForPackage(String packageName) throws ClassNotFoundException {
		ArrayList<File> directories = findDirectoriesContainingClassesOfPackage( packageName );

		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		// For every directory identified capture all the .class files
		for ( File directory : directories ) {
			if ( directory.exists() ) {
				addClassesForPackage( packageName, classes, directory );
			}
		}
		return classes;
	}

	private static ArrayList<File> findDirectoriesContainingClassesOfPackage(String packageName) {
		ArrayList<File> directories = new ArrayList<File>();
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			String path = packageName.replace( '.', '/' );
			// Ask for all resources for the path
			Enumeration<URL> resources = cld.getResources( path );
			while ( resources.hasMoreElements() ) {
				directories.add( new File( URLDecoder.decode( resources.nextElement().getPath(), "UTF-8" ) ) );
			}
		}
		catch ( Exception e ) {
			throw new RuntimeException( e.getMessage(), e );
		}
		return directories;
	}

	private static void addClassesForPackage(String packageName, ArrayList<Class<?>> classes, File directory) {
		assert directory != null;
		File[] files = directory.listFiles();
		if ( files == null ) { // directory is not really a directory ;-)
			return;
		}
		for ( File file : files ) {
			if ( file.getAbsolutePath().endsWith( ".class" ) ) {
				// removes the .class extension
				String className = file.getPath().substring( 0, file.getPath().length() - 6 );
				className = className.replace( "/", "." );
				className = className.substring( className.indexOf( packageName ) );

				try {
					Class<?> clazz = Class.forName( className );
					classes.add( clazz );
				}
				catch ( ClassNotFoundException e ) {
					// do nothing. this class hasn't been found by the loader, and we don't care.
				}
			}
			else if ( file.isDirectory() ) {
				addClassesForPackage( packageName, classes, file );
			}
		}
	}
}


