/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Order implements Auditable {
	private String creationDate;
	private String lastUpdate;
	private String lastModifier;
	private String lastReader;
	private String orderNumber;

	public String getCreationDate() {
		return this.creationDate;
	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public String getLastModifier() {
		return this.lastModifier;
	}

	public String getLastReader() {
		return this.lastReader;
	}

	@NotNull
	@Size(min = 10, max = 10)
	public String getOrderNumber() {
		return this.orderNumber;
	}
}
