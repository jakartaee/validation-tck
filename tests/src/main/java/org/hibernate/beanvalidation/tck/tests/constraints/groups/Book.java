/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Book {
	@NotNull(groups = First.class, message = "The book title cannot be null")
	@Size(min = 1, groups = First.class)
	private String title;

	@Size(max = 30, groups = Second.class, message = "The book's subtitle can only have {max} characters")
	private String subtitle;

	@Valid
	@NotNull(groups = First.class)
	private Author author;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@GroupSequence(value = { First.class, Second.class, Last.class })
	public interface All {
	}
}
