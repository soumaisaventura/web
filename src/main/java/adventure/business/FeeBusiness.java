package adventure.business;

import java.math.BigDecimal;
import java.util.List;

import adventure.entity.Fee;
import br.gov.frameworkdemoiselle.util.Beans;

public class FeeBusiness {

	public static FeeBusiness getInstance() {
		return Beans.getReference(FeeBusiness.class);
	}

	public BigDecimal applyForEvent(BigDecimal price, List<Fee> raceFees, List<Fee> championshipFees) {
		BigDecimal result = price;
		result = result.add(compute(price, championshipFees));
		result = result.add(compute(price, raceFees));
		return result;
	}

	private BigDecimal compute(BigDecimal price, List<Fee> fees) {
		BigDecimal result = BigDecimal.ZERO;

		if (fees != null) {
			for (Fee fee : fees) {
				result = result.add(compute(price, fee));
			}
		}

		return result;
	}

	private BigDecimal compute(BigDecimal price, Fee fee) {
		BigDecimal result;

		if (!fee.getOptional()) {
			if (fee.getPercentage()) {
				result = price.multiply(fee.getValue()).divide(BigDecimal.valueOf(100));
			} else {
				result = fee.getValue();
			}
		} else {
			result = BigDecimal.ZERO;
		}

		return result;
	}
}
