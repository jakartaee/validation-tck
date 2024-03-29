/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
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
import org.jboss.shrinkwrap.descriptor.api.webcommon30.WebAppVersionType;

/**
 * ShrinkWrap {@link org.jboss.shrinkwrap.api.spec.WebArchive} builder for CDI TCK Arquillian test. This builder is intended to provide basic functionality
 * covering common TCK needs. Use shrinkwrap API to adapt archive to advanced scenarios.
 *
 * @author Martin Kouba
 * @author Gunnar Morling
 */
public class WebArchiveBuilder extends ArchiveBuilder<WebArchiveBuilder, WebArchive> {

	private static String BEANS_XML = "<beans xmlns=\"https://jakarta.ee/xml/ns/jakartaee\" " +
			"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
			"xsi:schemaLocation=\"https://jakarta.ee/xml/ns/jakartaee https://jakarta.ee/xml/ns/jakartaee/beans_3_0.xsd\"\n" +
			"bean-discovery-mode=\"all\"></beans>";

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
		processAdditionalJars( webArchive );

		WebAppDescriptor webAppDescriptor = Descriptors.create( WebAppDescriptor.class )
				.version( WebAppVersionType._3_0 );
		webArchive.setWebXML( new StringAsset( webAppDescriptor.exportAsString() ) );

		return webArchive;
	}

	@Override
	public WebArchiveBuilder withEmptyBeansXml() {
		return withWebInfResource( EmptyAsset.INSTANCE, "beans.xml" );
	}

	public WebArchiveBuilder withBeansXml() {
		return withWebInfResource(new StringAsset(BEANS_XML), "beans.xml");
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
