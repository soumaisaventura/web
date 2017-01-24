package core.business;

import br.gov.frameworkdemoiselle.transaction.Transactional;
import br.gov.frameworkdemoiselle.util.Beans;
import core.entity.Kit;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;

public class KitBusiness {

    public static KitBusiness getInstance() {
        return Beans.getReference(KitBusiness.class);
    }

    @Transactional

    public BigDecimal getPrice(Kit kit) {
        return kit == null ? ZERO : kit.getPrice();
    }
}
