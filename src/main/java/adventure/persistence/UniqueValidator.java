package adventure.persistence;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

	@Override
	public void initialize(final Unique annotation) {
	}

	@Override
	public boolean isValid(final String password, final ConstraintValidatorContext context) {
		boolean result = true;

		if (password != null) {
			try {
				Double.parseDouble(password);
				result = false;

			} catch (NumberFormatException e) {
				result = true;
			}
		}

		return result;
	}
}
