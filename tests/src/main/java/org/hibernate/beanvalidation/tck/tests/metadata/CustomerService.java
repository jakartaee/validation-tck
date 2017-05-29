/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.metadata;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;

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

	//cascaded container elements
	public CustomerService(Map<
			@Valid @NotNull
			@ConvertGroup(from = Default.class, to = BasicChecks.class) @ConvertGroup(from = ComplexChecks.class, to = ComplexProductTypeChecks.class)
			ProductType,
			@Size(min = 2)
			List<@NotNull ProductOrderLine>> orderTemplate) {
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

	//parameter with cascaded container elements
	public ComplexOrder createOrder(long id, Map<
			@Valid @NotNull
			@ConvertGroup(from = Default.class, to = BasicChecks.class) @ConvertGroup(from = ComplexChecks.class, to = ComplexProductTypeChecks.class)
			ProductType,
			@Size(min = 2)
			List<@NotNull ProductOrderLine>> orders) {
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

	//return value with cascaded container elements
	public Map<
			@Valid @NotNull
			@ConvertGroup(from = Default.class, to = BasicChecks.class) @ConvertGroup(from = ComplexChecks.class, to = ComplexProductTypeChecks.class)
			ProductType,
			@Size(min = 2)
			List<@NotNull ProductOrderLine>> getOrderContent(long id) {
		return null;
	}

	//unconstrained
	public void shutDown(String pk) {
	}

	//constrained getter
	@NotNull
	public Customer getBestCustomer() {
		return null;
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
