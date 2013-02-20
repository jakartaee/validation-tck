/*
* JBoss, Home of Professional Open Source
* Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual contributors
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
package org.hibernate.beanvalidation.tck.tests.metadata;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import javax.validation.groups.ConvertGroup;
import javax.validation.groups.Default;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Gunnar Morling
 */
public class CustomerService {

	public interface BasicChecks {
	}

	public interface StrictCustomerServiceChecks {
	}

	public interface StrictChecks {
	}

	//constrained parameter
	public CustomerService(@NotNull @Size(min = 3) String firstName, @NotNull @Size(min = 3) String lastName) {
	}

	//cross-parameter constrained
	@MyCrossParameterConstraint
	public CustomerService(Customer customer) {
	}

	//cascaded parameter
	public CustomerService(@Valid Account account) {
	}

	//parameter with group conversions
	public CustomerService(
			int i,
			@Valid
			@ConvertGroup.List({
					@ConvertGroup(from = Default.class, to = BasicChecks.class),
					@ConvertGroup(from = StrictCustomerServiceChecks.class, to = StrictChecks.class)
			})
			Account account) {
	}

	//constrained return value
	@ValidCustomerService
	public CustomerService() {
	}

	//cascaded return value
	@Valid
	public CustomerService(long id) {
	}

	//return value with group conversions
	@Valid
	@ConvertGroup.List({
			@ConvertGroup(from = Default.class, to = BasicChecks.class),
			@ConvertGroup(from = StrictCustomerServiceChecks.class, to = StrictChecks.class)
	})
	public CustomerService(long id, int i) {
	}

	//unconstrained
	public CustomerService(String pk) {
	}

	//constrained parameter
	public void createCustomer(@NotNull @Size(min = 3) String firstName, @NotNull @Size(min = 3) String lastName) {
	}

	//cross-parameter constrained
	@MyCrossParameterConstraint
	public void removeCustomer(Customer customer) {
	}

	//cross-parameter constrained
	@MyCrossParameterConstraint
	public void updateCustomer(Customer customer) {
	}

	//cascaded parameter
	public Customer updateAccount(@Valid Account account) {
		return null;
	}

	//parameter with group conversions
	public Customer updateAccountStrictly(
			@Valid
			@ConvertGroup.List({
					@ConvertGroup(from = Default.class, to = BasicChecks.class),
					@ConvertGroup(from = StrictCustomerServiceChecks.class, to = StrictChecks.class)
			})
			Account account) {
		return null;
	}

	//constrained return value
	@Min(0)
	public int reset() {
		return 1;
	}

	//cascaded return value
	@Valid
	public Customer findCustomer(long id) {
		return null;
	}

	//return value with group conversions
	@Valid
	@ConvertGroup.List({
			@ConvertGroup(from = Default.class, to = BasicChecks.class),
			@ConvertGroup(from = StrictCustomerServiceChecks.class, to = StrictChecks.class)
	})
	public Customer findCustomer(long id, int i) {
		return null;
	}

	//unconstrained
	public void shutDown(String pk) {
	}

	@Target({ METHOD, ANNOTATION_TYPE, CONSTRUCTOR })
	@Retention(RUNTIME)
	@Constraint(validatedBy = MyCrossParameterConstraintValidator.class)
	public @interface MyCrossParameterConstraint {
		String message() default "";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	@SupportedValidationTarget(value = ValidationTarget.PARAMETERS)
	public static class MyCrossParameterConstraintValidator
			implements ConstraintValidator<MyCrossParameterConstraint, Object[]> {

		@Override
		public void initialize(MyCrossParameterConstraint constraintAnnotation) {
		}

		@Override
		public boolean isValid(Object[] value, ConstraintValidatorContext context) {
			return false;
		}
	}

	@Target({ METHOD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, FIELD, TYPE })
	@Retention(RUNTIME)
	@Constraint(validatedBy = ValidCustomerServiceValidator.class)
	public @interface ValidCustomerService {
		String message() default "";

		Class<?>[] groups() default { };

		Class<? extends Payload>[] payload() default { };
	}

	public static class ValidCustomerServiceValidator
			implements ConstraintValidator<ValidCustomerService, CustomerService> {

		@Override
		public void initialize(ValidCustomerService constraintAnnotation) {
		}

		@Override
		public boolean isValid(CustomerService value, ConstraintValidatorContext context) {
			return false;
		}
	}

	public class InnerClass {
		//constrained parameter on inner class constructor
		public InnerClass(@NotNull @Size(min = 3) String firstName) {
		}
	}
}
