/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import javax.validation.GroupSequence;
import javax.validation.constraints.Max;


/**
 * @author Hardy Ferentschik
 */
@GroupSequence({ SubClass.class, SubClass.DefaultGroup.class })
public class SubClass extends SuperClass {
	@Max(value = 10, groups = SubClass.DefaultGroup.class)
	private String myField = "1234567890";

	public String yourField = "";

	public interface DefaultGroup {
	}
}
