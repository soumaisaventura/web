package adventure.entity;

import static javax.persistence.EnumType.STRING;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "event")
public class Event implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String name;

	private String description;

	@Column(name = "slug")
	private String slug;

	@Lob
	private byte[] banner;

	private String site;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	@Column(name = "payment_info")
	private String paymentInfo;

	@Column(name = "payment_account")
	private String paymentAccount;

	@Column(name = "payment_token")
	private String paymentToken;

	@Embedded
	private Layout layout;

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

	public Event(Integer id, String name, String description, String slug, String site, String layoutTextColor,
			String layoutBackgroundColor, String layoutButtonColor) {
		setId(id);
		setName(name);
		setDescription(description);
		setSlug(slug);
		setSite(site);
		setLayout(new Layout());
		getLayout().setTextColor(layoutTextColor);
		getLayout().setBackgroundColor(layoutBackgroundColor);
		getLayout().setButtonColor(layoutButtonColor);
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

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
