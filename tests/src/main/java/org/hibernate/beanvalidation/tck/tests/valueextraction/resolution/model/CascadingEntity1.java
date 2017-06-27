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
public class CascadingEntity1 {

	@SuppressWarnings("unused")
	private IWrapper111<@Valid Bean11, @NotNull Bean12> wrapper;

	public CascadingEntity1(String value) {
		this.wrapper = new Wrapper1<Bean11, Bean12>( new Bean11( value ), new Bean12( value ) );
	}

	private static class Bean11 {

		@NotNull
		private String property;

		private Bean11(String property) {
			this.property = property;
		}
	}

	private static class Bean12 {

		@NotNull
		private String property;

		private Bean12(String property) {
			this.property = property;
		}
	}
}
