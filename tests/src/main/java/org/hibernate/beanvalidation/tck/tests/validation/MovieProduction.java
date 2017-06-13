/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Guillaume Smet
 */
public class MovieProduction {

	private Map<@NotBlank String, @Valid @NotNull Location> locationsByScene = new HashMap<>();

	private List<@Valid ExecutiveProducer> executiveProducers = new ArrayList<>();

	private MovieProduction() {
	}

	public Map<String, Location> getLocationsByScene() {
		return locationsByScene;
	}

	public static MovieProduction invalidMapKey() {
		MovieProduction production = new MovieProduction();
		production.locationsByScene.put( "", new Location( "Parc de la Tête d'Or", "Lyon", "69006" ) );
		return production;
	}

	public static MovieProduction invalidCascading() {
		MovieProduction production = new MovieProduction();
		production.locationsByScene.put( "Scene 1", new Location( "Parc de la Tête d'Or", "Lyon", null ) );
		return production;
	}

	public static MovieProduction invalidExecutiveProducer() {
		MovieProduction production = new MovieProduction();
		production.executiveProducers.add( new ExecutiveProducer( "", "" ) );
		return production;
	}
}
