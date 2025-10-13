/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.constraintdefinition.serviceloading;

import java.util.List;

import jakarta.validation.constraints.Size;

public class Author {

	@ServiceLoadedConstraint
	public String name;

	public List<@Size(max = 5) Book> books;


	public Author() {
	}

	public Author(List<Book> books) {
		this.books = books;
	}
}
