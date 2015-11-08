package adventure.business;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adventure.entity.Fee;
import br.gov.frameworkdemoiselle.util.Beans;

public class FeeBusiness {

	public static FeeBusiness getInstance() {
		return Beans.getReference(FeeBusiness.class);
	}

	public BigDecimal applyForEvent(BigDecimal price, List<Fee> raceFees, List<Fee> championshipFees) {
		BigDecimal result = price;
		Set<Fee> computedFees = new HashSet<Fee>();

		result = result.add(compute(price, championshipFees, computedFees));
		result = result.add(compute(price, raceFees, computedFees));
		return result;
	}

	private BigDecimal compute(BigDecimal price, List<Fee> fees, Set<Fee> computed) {
		BigDecimal result = BigDecimal.ZERO;

		if (fees != null) {
			for (Fee fee : fees) {
				result = result.add(compute(price, fee, computed));
			}
		}

		return result;
	}

	private BigDecimal compute(BigDecimal price, Fee fee, Set<Fee> computed) {
		BigDecimal result;

		boolean bypass = false;
		bypass |= fee.getOptional();
		bypass |= (fee.getOnce() != null && fee.getOnce());

		if (bypass) {
			result = BigDecimal.ZERO;
		} else {
			if (fee.getPercentage()) {
				result = price.multiply(fee.getValue()).divide(BigDecimal.valueOf(100));
			} else {
				result = fee.getValue();
			}
		}

		computed.add(fee);
		return result;
	}
}
