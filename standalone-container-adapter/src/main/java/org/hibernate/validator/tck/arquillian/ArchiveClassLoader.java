/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
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


