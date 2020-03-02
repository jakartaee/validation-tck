/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model;

import jakarta.validation.constraints.NotNull;

/**
 * @author Guillaume Smet
 */
public class ContainerElementEntity2 {

	@SuppressWarnings("unused")
	private Wrapper2<@NotNull String, Long> wrapper;

	public ContainerElementEntity2(String value1, Long value2) {
		this.wrapper = new Wrapper2<String, Long>( value1, value2 );
	}
}
