package adventure.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import adventure.entity.Account;
import adventure.persistence.AccountDAO;
import br.gov.frameworkdemoiselle.util.Beans;
import br.gov.frameworkdemoiselle.util.Strings;

public class ExistentUserEmailValidator implements ConstraintValidator<ExistentUserEmail, String> {

	@Override
	public void initialize(final ExistentUserEmail annotation) {
	}

	@Override
	public boolean isValid(final String email, final ConstraintValidatorContext context) {
		boolean result = true;

		if (!Strings.isEmpty(email)) {
			AccountDAO dao = Beans.getReference(AccountDAO.class);
			Account user = dao.loadFull(email, true);

			result = user != null;
		}

		return result;
	}
}
