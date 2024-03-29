/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.tck.arquillian;

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.container.LifecycleException;
import org.jboss.arquillian.container.spi.client.protocol.ProtocolDescription;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.descriptor.api.Descriptor;

/**
 * A dummy Arquillian container in order to run test locally
 *
 * @author Hardy Ferentschik
 */
public class BeanValidationLocalContainer implements ContainerConfiguration, DeployableContainer {
	private ClassLoader originalContextClassLoader;

	@Override
	public Class getConfigurationClass() {
		return BeanValidationLocalContainerConfiguration.class;
	}

	@Override
	public void setup(ContainerConfiguration configuration) {
	}

	@Override
	public void start() throws LifecycleException {
	}

	@Override
	public void stop() throws LifecycleException {
	}

	@Override
	public ProtocolDescription getDefaultProtocol() {
		return new ProtocolDescription( "Local" );
	}

	@Override
	public ProtocolMetaData deploy(Archive archive) throws DeploymentException {
		originalContextClassLoader = Thread.currentThread().getContextClassLoader();
		ArchiveClassLoader archiveClassLoader = new ArchiveClassLoader(
				originalContextClassLoader,
				archive
		);
		Thread.currentThread().setContextClassLoader( archiveClassLoader );
		return new ProtocolMetaData();
	}

	@Override
	public void undeploy(Archive archive) throws DeploymentException {
		if ( originalContextClassLoader != null ) {
			Thread.currentThread().setContextClassLoader( originalContextClassLoader );
		}
	}

	@Override
	public void deploy(Descriptor descriptor) throws DeploymentException {
	}

	@Override
	public void undeploy(Descriptor descriptor) throws DeploymentException {
	}

	@Override
	public void validate() throws ConfigurationException {
	}
}


