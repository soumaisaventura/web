package adventure.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

	@Override
	public void initialize(final UniqueUserEmail annotation) {
	}

	@Override
	public boolean isValid(final String email, final ConstraintValidatorContext context) {
		boolean result = true;

		if (!Strings.isEmpty(email)) {
			UserDAO dao = Beans.getReference(UserDAO.class);
			User user = dao.loadByEmail(email, true);

			result = user == null;
		}

		return result;
	}
}
