/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.xmlconfiguration.methodvalidation;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

/**
 * Hardy Ferentschik
 */
public class CustomerRepository {

	@NotNull
	private String repositoryId;

	public CustomerRepository() {
	}

	public List<Customer> listCustomers() {
		return Collections.emptyList();
	}

	public Customer findCustomer(String id) {
		return null;
	}

	public boolean isCustomer(String id) {
		return false;
	}

	public void addCustomers(Customer... customer) {
	}

	public Long notifyCustomer(Customer customer, String message) {
		return null;
	}

	public List<Customer> findByExample(Customer customer) {
		return Collections.emptyList();
	}

	public String getRepositoryId() {
		return repositoryId;
	}

	@Constraint(validatedBy = { ValidB2BRepositoryValidator.class })
	@Target({ TYPE, CONSTRUCTOR })
	@Retention(RUNTIME)
	public @interface ValidB2BRepository {
		String message() default "{ValidB2BRepository.message}";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	public static class ValidB2BRepositoryValidator
			implements ConstraintValidator<ValidB2BRepository, CustomerRepository> {

		@Override
		public boolean isValid(CustomerRepository repository, ConstraintValidatorContext context) {
			return false;
		}
	}
}
