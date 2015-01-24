package adventure.entity;

import java.io.Serializable;

public class AnnualFeePaymentPk implements Serializable {

	private static final long serialVersionUID = 1L;

	AnnualFee annualFee;

	User user;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annualFee == null) ? 0 : annualFee.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AnnualFeePaymentPk)) {
			return false;
		}
		AnnualFeePaymentPk other = (AnnualFeePaymentPk) obj;
		if (annualFee == null) {
			if (other.annualFee != null) {
				return false;
			}
		} else if (!annualFee.equals(other.annualFee)) {
			return false;
		}
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}
}
