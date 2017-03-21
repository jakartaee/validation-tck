/**
 * Bean Validation TCK
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package org.hibernate.beanvalidation.tck.tests.constraints.groups.groupsequenceisolation;

import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectConstraintTypes;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectNumberOfViolations;
import static org.hibernate.beanvalidation.tck.util.TestUtil.assertCorrectPropertyPaths;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import org.hibernate.beanvalidation.tck.beanvalidation.Sections;
import org.hibernate.beanvalidation.tck.util.TestUtil;
import org.hibernate.beanvalidation.tck.util.shrinkwrap.WebArchiveBuilder;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Hardy Ferentschik
 */
@SpecVersion(spec = "beanvalidation", version = "2.0.0")
public class GroupSequenceIsolationTest extends Arquillian {

	@Deployment
	public static WebArchive createTestArchive() {
		return new WebArchiveBuilder()
				.withTestClassPackage( GroupSequenceIsolationTest.class )
				.build();
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_FORMALDEFINITION, id = "c")
	public void testCorrectDefaultSequenceInheritance() {
		Validator validator = TestUtil.getValidatorUnderTest();
		B1 b = new B1();
		b.name = "this name is too long";
		b.nickname = "and this nickname as well";
		b.size = 20;
		b.encryptionKey = "not safe";

		//	when validating Default.class on B1, the following has to be validated sequentially:
		// - @Size on name and @Size on nickname (A is part of B1)
		// - @SafeEncryption on encryptionKey
		// note that @Max on size is not validated as it's not part of the sequence nor the group A
		Set<ConstraintViolation<B1>> violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Size.class, Size.class );

		b.name = "Jonathan";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Size.class );

		b.nickname = "Jon";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, SafeEncryption.class );

		b.encryptionKey = "secret";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "a")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_REDEFININGDEFAULTGROUP, id = "c")
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_FORMALDEFINITION, id = "c")
	public void testCorrectDefaultSequenceInheritance2() {
		Validator validator = TestUtil.getValidatorUnderTest();
		B2 b = new B2();
		b.name = "this name is too long";
		b.nickname = "and this nickname as well";
		b.size = 20;
		b.encryptionKey = "not safe";

		// when validating Default.class on B, you need to validate sequentially:
		//  - @Max on size (Minimal group)
		//  - @Size on name and @Size on nickname (A is part of B)
		//  - @SafeEncryption on encryptionKey 
		Set<ConstraintViolation<B2>> violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Max.class );

		b.size = 10;
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Size.class, Size.class );

		b.name = "Jonathan";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Size.class );

		b.nickname = "Jon";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, SafeEncryption.class );

		b.encryptionKey = "secret";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_GROUPSEQUENCE_FORMALDEFINITION, id = "b")
	public void testCorrectDefaultSequenceInheritance3() {
		Validator validator = TestUtil.getValidatorUnderTest();
		B3 b = new B3();
		b.name = "this name is too long";
		b.nickname = "and this nickname as well";
		b.size = 20;
		b.encryptionKey = "not safe";

		Set<ConstraintViolation<B3>> violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Max.class, Size.class );
		assertCorrectPropertyPaths( violations, "size", "nickname" );

		b.nickname = "nick";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Max.class );
		assertCorrectPropertyPaths( violations, "size" );

		b.size = 10;
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPropertyPaths( violations, "name" );

		b.nickname = "and this nickname as well";
		violations = validator.validate( b );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Size.class, Size.class );
		assertCorrectPropertyPaths( violations, "name", "nickname" );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_GRAPHVALIDATION, id = "e")
	public void testCorrectDefaultSequenceContainedCaseWithoutGroupRedefinitionOnContainedEntity() {
		Validator validator = TestUtil.getValidatorUnderTest();
		C c = new C();
		c.name = "this name is too long";
		c.d.nickname = "and this nickname as well";
		c.size = 20;
		c.d.encryptionKey = "not safe";

		Set<ConstraintViolation<C>> violations = validator.validate( c );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Max.class, Size.class );
		assertCorrectPropertyPaths( violations, "size", "d.nickname" );

		c.size = 10;
		violations = validator.validate( c );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Size.class, Size.class );
		assertCorrectPropertyPaths( violations, "name", "d.nickname" );

		c.d.nickname = "Jon";
		violations = validator.validate( c );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPropertyPaths( violations, "name" );

		c.name = "Johnatan";
		violations = validator.validate( c );
		assertCorrectNumberOfViolations( violations, 0 );
	}

	@Test
	@SpecAssertion(section = Sections.CONSTRAINTDECLARATIONVALIDATIONPROCESS_VALIDATIONROUTINE_GRAPHVALIDATION, id = "e")
	public void testCorrectDefaultSequenceContainedCaseWithGroupRedefinitionOnContainedEntity() {
		Validator validator = TestUtil.getValidatorUnderTest();
		E e = new E();
		e.name = "this name is too long";
		e.f.nickname = "and this nickname as well";
		e.size = 20;
		e.f.encryptionKey = "not safe";
		e.f.age = 16;


		Set<ConstraintViolation<E>> violations = validator.validate( e );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Max.class, Size.class );
		assertCorrectPropertyPaths( violations, "size", "f.nickname" );

		e.size = 10;
		violations = validator.validate( e );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Size.class, Size.class );
		assertCorrectPropertyPaths( violations, "name", "f.nickname" );

		e.f.nickname = "Jon";
		violations = validator.validate( e );
		assertCorrectNumberOfViolations( violations, 2 );
		assertCorrectConstraintTypes( violations, Size.class, IsAdult.class );
		assertCorrectPropertyPaths( violations, "name", "f.age" );

		e.f.age = 18;
		violations = validator.validate( e );
		assertCorrectNumberOfViolations( violations, 1 );
		assertCorrectConstraintTypes( violations, Size.class );
		assertCorrectPropertyPaths( violations, "name" );

		e.name = "Johnatan";
		violations = validator.validate( e );
		assertCorrectNumberOfViolations( violations, 0 );
	}
}
