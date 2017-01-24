package core.validator;

import br.gov.frameworkdemoiselle.util.Strings;
import core.persistence.UserDAO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueUserEmailValidator implements ConstraintValidator<UniqueUserEmail, String> {

    @Override
    public void initialize(final UniqueUserEmail annotation) {
    }

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        boolean result = true;

        if (!Strings.isEmpty(email)) {
            result = UserDAO.getInstance().load(email) == null;
        }

        return result;
    }
}
