/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.model.StockItemRecord;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.METHOD;

/**
 * A constraint which validates {@link StockItemRecord} instances.
 */
@Target({ METHOD, CONSTRUCTOR, ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidStockItemRecord.Validator.class)
@Documented
public @interface ValidStockItemRecord {
	String message() default "{ValidStockItem.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

	public class Validator implements ConstraintValidator<ValidStockItemRecord, StockItemRecord> {

		@Override
		public boolean isValid(StockItemRecord object, ConstraintValidatorContext context) {
			return false;
		}
	}
}
