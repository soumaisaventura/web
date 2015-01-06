package adventure.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueUserEmailValidator.class)
@Retention(RUNTIME)
@Target({ FIELD, PARAMETER })
public @interface UniqueUserEmail {

	Class<?>[] groups() default {};

	String message() default "e-mail já associado a outra conta";

	Class<? extends Payload>[] payload() default {};
}
