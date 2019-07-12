/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.valueextraction.declaration.model;

import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
public class Cinema {

	private Optional<@NotBlank String> name;

	@SuppressWarnings("unused")
	private Reference<@Valid @NotNull Visitor> visitor;

	private Cinema(String name, Reference<Visitor> visitor) {
		this.name = Optional.ofNullable( name );
		this.visitor = visitor;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( getClass().getSimpleName() )
				.append( "<" ).append( name ).append( ">" );
		return sb.toString();
	}

	public static Cinema invalidName() {
		return new Cinema( "", null );
	}

	public static Cinema invalidReference() {
		return new Cinema( "Comoedia", new SomeReference<Visitor>( null ) );
	}

	public static Cinema invalidVisitor() {
		return new Cinema( "Comoedia", new SomeReference<Visitor>( new Visitor() ) );
	}
}
