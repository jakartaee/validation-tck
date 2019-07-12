/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.tck.arquillian;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.asset.FileAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 * @author Hardy Ferentschik
 * @author Guillaume Smet
 */
public class ArchiveClassLoader extends URLClassLoader {
	private static final String WEB_ARCHIVE_PREFIX = "WEB-INF/classes/";
	private static final String EMPTY_PREFIX = "";
	private final Archive<?> archive;
	private final String archivePrefix;

	public ArchiveClassLoader(ClassLoader classLoader, Archive<?> archive) {
		super( new URL[] { }, classLoader );
		this.archive = archive;
		if ( archive instanceof WebArchive ) {
			archivePrefix = WEB_ARCHIVE_PREFIX;
		}
		else {
			archivePrefix = EMPTY_PREFIX;
		}
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if ( archive.get( archivePrefix + name ) != null ) {
			return loadFromArchive( archivePrefix + name );
		}
		else {
			return super.getResourceAsStream( name );
		}
	}

	@Override
	public URL getResource(String name) {
		if ( archive.get( archivePrefix + name ) != null ) {
			try {
				return new URL( null, "archive:" + archive.getName() + "/" + name, new ArchiveURLStreamHandler() );
			}
			catch (MalformedURLException e) {
				throw new RuntimeException( "Could not create URL for archive: " + archive.getName() + " and resource " + name, e );
			}
		}
		else {
			return super.getResource( name );
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

	private class ArchiveURLStreamHandler extends URLStreamHandler {

		@Override
		protected URLConnection openConnection(final URL u) throws IOException {
			return new URLConnection( u ) {

				@Override
				public void connect() throws IOException {
				}

				@Override
				public InputStream getInputStream() throws IOException {
					final String path = convertToArchivePath( u );
					final Node node = archive.get( path );

					// SHRINKWRAP-308
					if ( node == null ) {
						// We've asked for a path that doesn't exist
						throw new FileNotFoundException( "Requested path: " + path + " does not exist in "
								+ archive.toString() );
					}

					final Asset asset = node.getAsset();

					// SHRINKWRAP-306
					if ( asset == null ) {
						// This is a directory, so return null InputStream to denote as such
						return null;
					}

					return asset.openStream();
				}

				private String convertToArchivePath(URL url) {
					String path = url.getPath();
					return path.replace( archive.getName() + "/", archivePrefix );
				}
			};
		}
	}
}
