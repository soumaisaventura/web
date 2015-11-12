package adventure.entity;

import static javax.persistence.EnumType.STRING;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Enumerated;

@Embeddable
public class Payment {

	@Enumerated(STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	@Column(name = "payment_info")
	private String paymentInfo;

	@Column(name = "payment_account")
	private String paymentAccount;

	@Column(name = "payment_token")
	private String paymentToken;
}
