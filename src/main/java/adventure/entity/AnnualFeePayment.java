package adventure.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@IdClass(AnnualFeePaymentPk.class)
@Table(name = "ANNUAL_FEE_PAYMENT")
public class AnnualFeePayment implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "YEAR")
	@ForeignKey(name = "FK_ANNUAL_FEE_PAYMENT_ANNUAL_FEE")
	private AnnualFee annualFee;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "USER_ID")
	@ForeignKey(name = "FK_ANNUAL_FEE_PAYMENT_USER")
	private User user;

	@ManyToOne(optional = false)
	@JoinColumn(name = "REGISTRATION_ID")
	@ForeignKey(name = "FK_ANNUAL_FEE_PAYMENT_REGISTRATION")
	@Index(name = "IDX_ANNUAL_FEE_PAYMENT_REGISTRATION")
	private Registration registration;

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
		if (!(obj instanceof AnnualFeePayment)) {
			return false;
		}
		AnnualFeePayment other = (AnnualFeePayment) obj;
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

	public AnnualFee getAnnualFee() {
		return annualFee;
	}

	public void setAnnualFee(AnnualFee annualFee) {
		this.annualFee = annualFee;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Registration getRegistration() {
		return registration;
	}

	public void setRegistration(Registration registration) {
		this.registration = registration;
	}
}
