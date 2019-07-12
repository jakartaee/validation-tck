/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation;

import javax.validation.GroupSequence;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Emmanuel Bernard
 */
public class Parent {
	private String name;
	private Child child;

	@NotNull(groups = ParentSecond.class)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Valid
	public Child getChild() {
		return child;
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public interface ParentSecond {
	}

	public interface ChildFirst {
	}

	@GroupSequence({ ChildFirst.class, ParentSecond.class })
	public interface ProperOrder {
	}
}
