/**
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.methodvalidation.model;

import jakarta.validation.constraints.NotNull;
import org.hibernate.beanvalidation.tck.tests.methodvalidation.constraint.MyCrossParameterConstraint;

public record ComplexStockItemRecord(@NotNull String name, @NotNull String description) {
    @MyCrossParameterConstraint
    public ComplexStockItemRecord {
    }
}
