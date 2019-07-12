/**
 * Jakarta Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.validation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
public class MovieStudio {

	@NotNull
	private final String name;

	public MovieStudio() {
		this.name = null;
	}

	@ValidMovieStudio
	public MovieStudio(@NotNull String name, @NotNull @Valid Person generalManager) {
		this.name = name;
	}

	@Valid
	public MovieStudio(String name) {
		this.name = name;
	}

	public MovieStudio(String name, Person generalManager, @Valid List<Actor> recurringActors) {
		this.name = name;
	}

	public MovieStudio(String name, Person generalManager, @Valid Actor[] recurringActors) {
		this.name = name;
	}

	public MovieStudio(String name, Person generalManager, @Valid Set<Actor> recurringActors) {
		this.name = name;
	}

	public MovieStudio(String name, Person generalManager, @Valid Map<String, Actor> recurringActors) {
		this.name = name;
	}

	@ActorLikesGenre
	public MovieStudio(Actor mainActor, String genre) {
		this.name = null;
	}

	@NotNull
	public Movie makeMovie(@NotNull String title, @NotNull @Valid Person director, @Valid List<Actor> actors) {
		return null;
	}

	public Movie makeMovieArrayBased(String title, Person director, @Valid Actor[] actors) {
		return null;
	}

	public Movie makeMovieSetBased(String title, Person director, @Valid Set<Actor> actors) {
		return null;
	}

	public Movie makeMovieMapBased(String title, Person director, @Valid Map<String, Actor> actors) {
		return null;
	}

	@ActorLikesGenre
	public Movie makeMovie(Actor mainActor, String genre) {
		return null;
	}

	@Valid
	public Movie getBestSellingMovie() {
		return null;
	}

	@Valid
	public List<Movie> getBestSellingMoviesListBased() {
		return null;
	}

	@Valid
	public Movie[] getBestSellingMoviesArrayBased() {
		return null;
	}

	@Valid
	public Set<Movie> getBestSellingMoviesSetBased() {
		return null;
	}

	@Valid
	public Map<String, Movie> getBestSellingMoviesMapBased() {
		return null;
	}

	public String getName() {
		return name;
	}
}
