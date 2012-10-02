package org.hibernate.validator.tck.arquillian;

import org.jboss.arquillian.container.spi.ConfigurationException;
import org.jboss.arquillian.container.spi.client.container.ContainerConfiguration;

/**
 * @author Hardy Ferentschik
 */
public class BeanValidationLocalContainerConfiguration implements ContainerConfiguration {

	@Override
	public void validate() throws ConfigurationException {
	}
}


