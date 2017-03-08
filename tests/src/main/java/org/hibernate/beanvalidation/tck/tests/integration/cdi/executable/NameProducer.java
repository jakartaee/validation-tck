/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.integration.cdi.executable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;

/**
 * @author Gunnar Morling
 */
@ApplicationScoped
public class NameProducer {

	private String name = "Bob";

	@Produces
	@Dependent
	@LongName
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
