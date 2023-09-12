/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation.graphnavigation.containerelement.model;

import jakarta.validation.Valid;

/**
 * @author Gunnar Morling
 *
 */
public class Cinema {

	private String name;

	@SuppressWarnings("unused")
	private Reference<@Valid Visitor> visitor;

	public Cinema() {
	}

	public Cinema(String name, Reference<Visitor> visitor) {
		this.name = name;
		this.visitor = visitor;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( getClass().getSimpleName() )
				.append( "<" ).append( name ).append( ">" );
		return sb.toString();
	}
}
