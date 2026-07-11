/*
 * Jakarta Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a test class whose single test method is expected to fail during bootstrap or deployment.
 * <p>
 * The {@link TckArquillianExtension} handles two scenarios:
 * <ul>
 *   <li>Container mode: deployment fails before any test runs — the extension catches the exception
 *       and skips the test body if it matches {@link #value()}.</li>
 *   <li>Standalone mode: deployment succeeds — the test body is executed and the extension asserts
 *       that it throws an exception matching {@link #value()}.</li>
 * </ul>
 * <p>
 * The annotated class must contain exactly one {@code @Test} method. This ensures container mode
 * (single deployment failure) and standalone mode (test body execution) exercise the same scenario.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectBootstrapFailure {

	Class<? extends Exception> value();
}
