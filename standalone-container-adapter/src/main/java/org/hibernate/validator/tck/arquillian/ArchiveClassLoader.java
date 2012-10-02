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
package org.hibernate.validator.tck.arquillian;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author Hardy Ferentschik
 */
public class ArchiveClassLoader extends URLClassLoader {
	private static final String WEB_ARCHIVE_PREFIX = "WEB-INF/classes/";
	private static final String EMPTY_PREFIX = "";
	private final Archive archive;
	private final String archivePrefix;

	public ArchiveClassLoader(ClassLoader classLoader, Archive archive) {
		super( new URL[] { }, classLoader );
		this.archive = archive;
		if ( archive instanceof WebArchive ) {
			archivePrefix = WEB_ARCHIVE_PREFIX;
		}
		else {
			archivePrefix = EMPTY_PREFIX;
		}
	}

	public InputStream getResourceAsStream(String name) {
		if ( archive.get( archivePrefix + name ) != null ) {
			return loadFromArchive( archivePrefix + name );
		}
		else {
			return super.getResourceAsStream( name );
		}
	}

	private InputStream loadFromArchive(String name) {
		Asset asset = archive.get( name ).getAsset();

		InputStream in;
		if ( asset instanceof FileAsset ) {
			in = asset.openStream();
		}
		else if ( asset instanceof ByteArrayAsset ) {
			in = asset.openStream();
		}
		else {
			throw new RuntimeException( "Unsupported asset type " + asset.toString() );
		}

		return in;
	}
}


