/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.resolution.model;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Guillaume Smet
 */
public class CascadingEntity2 {

	@SuppressWarnings("unused")
	private IWrapper212<@Valid Bean21, @Valid Bean22> wrapper;

	public CascadingEntity2(String value) {
		this.wrapper = new Wrapper2<Bean21, Bean22>( new Bean21( value ), new Bean22( value ) );
	}

	private static class Bean21 {

		@NotNull
		private String property;

		private Bean21(String property) {
			this.property = property;
		}
	}

	private static class Bean22 {

		@NotNull
		private String property;

		private Bean22(String property) {
			this.property = property;
		}
	}
}
