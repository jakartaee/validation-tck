/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupsequenceisolation;

import jakarta.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class B3 extends A {
	@SafeEncryption(groups = Heavy.class)
	String encryptionKey;

	@Size(max = 20)
	String nickname; 
}
