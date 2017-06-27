/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model;

import javax.validation.constraints.NotNull;

/**
 * @author Guillaume Smet
 */
public class ContainerElementEntity3 {

	@SuppressWarnings("unused")
	private IWrapper21<@NotNull String, Long> wrapper;

	public ContainerElementEntity3(String value1, Long value2) {
		this.wrapper = new Wrapper2<String, Long>( value1, value2 );
	}
}
