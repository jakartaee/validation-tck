/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupsequence;

import javax.validation.GroupSequence;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * @author Hardy Ferentschik
 */
@GroupSequence({ TimeConsumingChecks.class, TestEntity.class })
public class TestEntity {
	@NotNull
	public String foo;
}

interface TimeConsumingChecks {
}

@GroupSequence({ Default.class, TimeConsumingChecks.class })
interface Complete {
}
