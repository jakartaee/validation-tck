// $Id$
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
package org.hibernate.jsr303.tck.tests.constraints.constraintcomposition;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 * @author Hardy Ferentschik
 */
@NotEmpty
@Size
// first pattern just duplicates the length of 5 characters, the second pattern is just to proof that parameters can be overridden.
@Pattern.List({ @Pattern(regexp = "....."), @Pattern(regexp = "bar") })
@Constraint(validatedBy = FrenchZipcodeConstraintValidator.class)
@Documented
@Target({ METHOD, FIELD, TYPE })
@Retention(RUNTIME)
public @interface FrenchZipcodeWithInvalidOverride {
	public abstract String message() default "Wrong zipcode";

	public abstract Class<?>[] groups() default { };

	public abstract Class<? extends Payload>[] payload() default {};

	@OverridesAttribute.List({
			@OverridesAttribute(constraint = Size.class, name = "min"),
			@OverridesAttribute(constraint = Size.class, name = "max")
	}) public abstract String size() default "5";

	@OverridesAttribute(constraint = Size.class, name = "message") public abstract String sizeMessage() default "A french zip code has a length of 5";

	@OverridesAttribute(constraint = Pattern.class, name = "regexp", constraintIndex = 2) public abstract String regex() default "\\d*";
}
