/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.validatorresolution;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.validation.constraints.Size;

/**
 * @author Hardy Ferentschik
 */
public class Suburb {
	public enum Facility {
		SHOPPING_MALL, BUS_TERMINAL
	}

	@Size(min = 5, max = 10)
	private String name;

	@Size(min = 2, max = 2)
	private Map<Facility, Boolean> facilities;

	@Size(min = 2)
	private Set<String> streetNames;

	@Size(min = 4, max = 1000)
	private Coordinate[] boundingBox;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Map<Facility, Boolean> getFacilities() {
		return facilities;
	}

	public void addFacility(Facility f, Boolean exist) {
		if ( facilities == null ) {
			facilities = new HashMap<Facility, Boolean>();
		}
		facilities.put( f, exist );
	}

	public Set<String> getStreetNames() {
		return streetNames;
	}

	public void addStreetName(String streetName) {
		if ( streetNames == null ) {
			streetNames = new HashSet<String>();
		}
		streetNames.add( streetName );
	}

	public Coordinate[] getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(Coordinate[] boundingBox) {
		this.boundingBox = boundingBox;
	}
}
