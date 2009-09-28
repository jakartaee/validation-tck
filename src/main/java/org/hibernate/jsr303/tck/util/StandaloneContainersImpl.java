// $Id$
/*
* JBoss, Home of Professional Open Source
* Copyright 2008, Red Hat Middleware LLC, and individual contributors
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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jboss.testharness.api.DeploymentException;
import org.jboss.testharness.spi.StandaloneContainers;


public class StandaloneContainersImpl implements StandaloneContainers {

	public void deploy(Iterable<Class<?>> classes) throws DeploymentException {
	}

	public void undeploy() {
	}

	public void setup() {
	}

	public void cleanup() {
	}

	public boolean deploy(Iterable<Class<?>> classes, Iterable<URL> validationXmls) {
		if ( validationXmls == null || !validationXmls.iterator().hasNext() ) {
			Thread.currentThread()
					.setContextClassLoader( new IgnoringValidationXmlClassLoader() );
			return true;
		}

		URL validationXmlUrl = validationXmls.iterator().next();
		Thread.currentThread()
				.setContextClassLoader( new CustomValidationXmlClassLoader( validationXmlUrl.getPath() ) );
		return true;
	}

	public DeploymentException getDeploymentException() {
		return null;
	}

	private static class CustomValidationXmlClassLoader extends ClassLoader {
		private final String customValidationXmlPath;

		CustomValidationXmlClassLoader(String pathToCustomValidationXml) {
			super( CustomValidationXmlClassLoader.class.getClassLoader() );
			customValidationXmlPath = pathToCustomValidationXml;
		}

		public InputStream getResourceAsStream(String path) {
			InputStream in;
			if ( "META-INF/validation.xml".equals( path ) ) {
				if ( customValidationXmlPath.contains( ".jar!" ) ) {
					path = customValidationXmlPath.substring( customValidationXmlPath.indexOf( "!" ) + 2 );
					in = super.getResourceAsStream( path );
				}
				else {
					in = loadFromDisk();
				}
			}
			else {
				in = super.getResourceAsStream( path );
			}
			return in;
		}

		private InputStream loadFromDisk() {
			InputStream in;
			try {
				in = new BufferedInputStream( new FileInputStream( customValidationXmlPath ) );
			}
			catch ( IOException ioe ) {
				String msg = "Unble to load " + customValidationXmlPath + " from  disk";
				throw new RuntimeException( msg );
			}
			return in;
		}
	}

	private static class IgnoringValidationXmlClassLoader extends ClassLoader {
		IgnoringValidationXmlClassLoader() {
			super( IgnoringValidationXmlClassLoader.class.getClassLoader() );
		}

		public InputStream getResourceAsStream(String path) {
			if ( "META-INF/validation.xml".equals( path ) ) {
				return null;
			}
			return super.getResourceAsStream( path );
		}
	}
}
