/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.validator.tck.arquillian;

import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.core.spi.LoadableExtension;

/**
 * @author Hardy Ferentschik
 */
public class BeanValidationLocalContainerExtension implements LoadableExtension
{
	@Override
	public void register(ExtensionBuilder builder)
	{
		builder.service(DeployableContainer.class, BeanValidationLocalContainer.class);
	}
}


