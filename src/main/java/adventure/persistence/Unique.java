package adventure.persistence;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = UniqueValidator.class)
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface Unique {
	
	Class<?>[] groups() default {};
	
	String message() default "Esta senha é muito fácil";
	
	Class<? extends Payload>[] payload() default {};
	
}