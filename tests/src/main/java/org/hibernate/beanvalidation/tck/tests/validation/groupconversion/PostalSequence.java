/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.groupconversion;

import jakarta.validation.GroupSequence;

/**
 * @author Gunnar Morling
 */
@GroupSequence({ BasicPostal.class, ComplexPostal.class })
public interface PostalSequence {
}
