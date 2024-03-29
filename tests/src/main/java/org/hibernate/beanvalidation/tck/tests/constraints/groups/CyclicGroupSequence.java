/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import jakarta.validation.GroupSequence;

/**
 * @author Hardy Ferentschik
 */
@GroupSequence(value = CyclicGroupSequence.class)
public interface CyclicGroupSequence {
}
