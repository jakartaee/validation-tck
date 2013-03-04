package org.hibernate.beanvalidation.tck.util.shrinkwrap;

import java.util.ArrayList;
import java.util.List;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;

/**
 * ShrinkWrap {@link org.jboss.shrinkwrap.api.spec.WebArchive} builder for CDI TCK Arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 *
 * @author Martin Kouba
 * @author Gunnar Morling
 *
 */
public class WebArchiveBuilder extends ArchiveBuilder<WebArchiveBuilder, WebArchive> {

	private List<ResourceDescriptor> webInfResources = null;

	@Override
	public WebArchiveBuilder self() {
		return this;
	}

	@Override
	protected WebArchive buildInternal() {
		WebArchive webArchive;

		if ( getName() == null ) {
			webArchive = ShrinkWrap.create( WebArchive.class );
		}
		else {
			webArchive = ShrinkWrap.create( WebArchive.class, getName() );
		}

		processPackages( webArchive );
		processClasses( webArchive );
		processResources( webArchive );
		processWebInfResources( webArchive );

		webArchive.setWebXML( new StringAsset( Descriptors.create( WebAppDescriptor.class ).exportAsString() ) );
		return webArchive;
	}

	@Override
	public WebArchiveBuilder withEmptyBeansXml() {
		return withWebInfResource( EmptyAsset.INSTANCE, "beans.xml" );
	}

	private WebArchiveBuilder withWebInfResource(Asset asset, String target) {
		if ( this.webInfResources == null ) {
			this.webInfResources = new ArrayList<ResourceDescriptor>();
		}

		this.webInfResources.add( new ResourceDescriptor( asset, target ) );

		return self();
	}

	private void processWebInfResources(WebArchive archive) {
		if ( webInfResources == null ) {
			return;
		}

		for ( ResourceDescriptor resource : webInfResources ) {
			if ( resource.getSource() != null ) {
				if ( resource.getTarget() == null ) {
					archive.addAsWebInfResource( resource.getSource() );
				}
				else {
					archive.addAsWebInfResource( resource.getSource(), resource.getTarget() );
				}
			}
			else if ( resource.getAsset() != null ) {
				archive.addAsWebInfResource( resource.getAsset(), resource.getTarget() );
			}
		}
	}
}
