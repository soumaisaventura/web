package adventure.entity;

import adventure.util.Misc;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class RegistrationPayment {

    @Column(name = "payment_checkout_code")
    private String checkoutCode;

    @Column(name = "payment_transaction_code")
    private String transactionCode;

    public boolean isEmpty() {
        return Misc.isEmpty(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((checkoutCode == null) ? 0 : checkoutCode.hashCode());
        result = prime * result + ((transactionCode == null) ? 0 : transactionCode.hashCode());
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
        if (!(obj instanceof RegistrationPayment)) {
            return false;
        }
        RegistrationPayment other = (RegistrationPayment) obj;
        if (checkoutCode == null) {
            if (other.checkoutCode != null) {
                return false;
            }
        } else if (!checkoutCode.equals(other.checkoutCode)) {
            return false;
        }
        if (transactionCode == null) {
            if (other.transactionCode != null) {
                return false;
            }
        } else if (!transactionCode.equals(other.transactionCode)) {
            return false;
        }
        return true;
    }

    public String getCheckoutCode() {
        return checkoutCode;
    }

    public void setCheckoutCode(String checkoutCode) {
        this.checkoutCode = checkoutCode;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }
}
