package adventure.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ExistentUserEmailValidator.class)
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface ExistentUserEmail {

    Class<?>[] groups() default {};

    String message() default "e-mail não cadastrado";

    Class<? extends Payload>[] payload() default {};
}
