package adventure.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Bill implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal racePrice;

	private BigDecimal annualFee;

	public BigDecimal getRacePrice() {
		return racePrice;
	}

	public void setRacePrice(BigDecimal racePrice) {
		this.racePrice = racePrice;
	}

	public BigDecimal getAnnualFee() {
		return annualFee;
	}

	public void setAnnualFee(BigDecimal annualFee) {
		this.annualFee = annualFee;
	}
}
