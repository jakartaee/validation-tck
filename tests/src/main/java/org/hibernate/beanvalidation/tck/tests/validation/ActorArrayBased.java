/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,  
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.hibernate.beanvalidation.tck.tests.validation;

import javax.validation.Valid;

/**
 * @author Hardy Ferentschik
 */
public class ActorArrayBased extends Actor {
	public static final int MAX_ACTOR_SIZE = 100;

	@Valid
	private final Actor[] playedWith = new Actor[MAX_ACTOR_SIZE];

	int currentPointer = 0;

	public ActorArrayBased(String firstName, String lastName) {
		super( firstName, lastName );
	}

	@Override
	public void addPlayedWith(Actor playedWith) {
		if ( currentPointer == MAX_ACTOR_SIZE ) {
			throw new RuntimeException( "Exceeded allowed number of actors." );
		}
		this.playedWith[currentPointer] = playedWith;
		currentPointer++;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
