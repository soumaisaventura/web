package adventure.entity;

import static adventure.util.Constants.EMAIL_SIZE;
import static adventure.util.Constants.ENUM_SIZE;
import static adventure.util.Constants.HASH_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static adventure.util.Constants.SLUG_SIZE;
import static adventure.util.Constants.TEXT_SIZE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "event")
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = SEQUENCE, generator = "seq_event")
	@SequenceGenerator(name = "seq_event", sequenceName = "seq_event", allocationSize = 1)
	private Integer id;

	@NotEmpty
	@Size(max = NAME_SIZE)
	@Column(name = "name")
	@Index(name = "idx_event_name")
	private String name;

	@Size(max = TEXT_SIZE)
	@Column(name = "description")
	@Index(name = "idx_event_description")
	private String description;

	@Column(name = "slug")
	@Size(max = SLUG_SIZE)
	@Index(name = "idx_event_slug")
	private String slug;

	@Lob
	@Column(name = "banner")
	private byte[] banner;

	@Size(max = EMAIL_SIZE)
	private String site;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "payment_type", length = ENUM_SIZE)
	@Index(name = "idx_race_payment_type")
	private PaymentType paymentType;

	@Size(max = TEXT_SIZE)
	@Column(name = "payment_info")
	private String paymentInfo;

	@Size(max = EMAIL_SIZE)
	@Column(name = "payment_account")
	private String paymentAccount;

	@Size(max = HASH_SIZE)
	@Column(name = "payment_token")
	private String paymentToken;

	public Event() {
	}

	public Event(Integer id) {
		this.id = id;
	}

	public Event(Integer id, String name, String description) {
		setId(id);
		setName(name);
		setDescription(description);
	}

	public Event(Integer id, String name, String description, String slug, String site, String paymentAccount,
			String paymentToken) {
		setId(id);
		setName(name);
		setDescription(description);
		setSlug(slug);
		setSite(site);
		setPaymentAccount(paymentAccount);
		setPaymentToken(paymentToken);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (!(obj instanceof Event)) {
			return false;
		}
		Event other = (Event) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public byte[] getBanner() {
		return banner;
	}

	public void setBanner(byte[] banner) {
		this.banner = banner;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentInfo() {
		return paymentInfo;
	}

	public void setPaymentInfo(String paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public String getPaymentAccount() {
		return paymentAccount;
	}

	public void setPaymentAccount(String paymentAccount) {
		this.paymentAccount = paymentAccount;
	}

	public String getPaymentToken() {
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken) {
		this.paymentToken = paymentToken;
	}
}
