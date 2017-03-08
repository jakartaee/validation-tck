/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition;

import javax.validation.constraints.Size;

/**
 * @author Guillaume Smet
 */
public class Movie {

	@Size(min = 2)
	@Size(max = 20)
	private String title;

	public Movie() {
	}

	public Movie(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
