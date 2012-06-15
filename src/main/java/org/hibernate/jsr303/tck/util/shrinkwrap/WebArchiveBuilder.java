package org.hibernate.jsr303.tck.util.shrinkwrap;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;

/**
 * ShrinkWrap {@link org.jboss.shrinkwrap.api.spec.WebArchive} builder for CDI TCK Arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 *
 * @author Martin Kouba
 */
public class WebArchiveBuilder extends ArchiveBuilder<WebArchiveBuilder, WebArchive> {
	@Override
	public WebArchiveBuilder self() {
		return this;
	}

	@Override
	protected WebArchive buildInternal() {
		WebArchive webArchive;

		if ( getName() == null ) {
			// Let arquillian generate archive name in order to avoid reload issues in AS7 (AS7-1638)
			// webArchive = ShrinkWrap.create(WebArchive.class, DEFAULT_WAR_NAME);
			webArchive = ShrinkWrap.create( WebArchive.class );
		}
		else {
			webArchive = ShrinkWrap.create( WebArchive.class, getName() );
		}

		processPackages( webArchive );
		processClasses( webArchive );
		processResources( webArchive );

		webArchive.setWebXML( new StringAsset( Descriptors.create( WebAppDescriptor.class ).exportAsString() ) );
		return webArchive;
	}
}



