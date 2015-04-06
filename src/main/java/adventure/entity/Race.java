package adventure.entity;

import static adventure.entity.RaceStatusType.CLOSED;
import static adventure.entity.RaceStatusType.END;
import static adventure.entity.RaceStatusType.OPEN;
import static adventure.entity.RaceStatusType.SOON;
import static adventure.util.Constants.EMAIL_SIZE;
import static adventure.util.Constants.ENUM_SIZE;
import static adventure.util.Constants.HASH_SIZE;
import static adventure.util.Constants.NAME_SIZE;
import static adventure.util.Constants.TEXT_SIZE;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "RACE")
public class Race implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE, generator = "SEQ_RACE")
	@SequenceGenerator(name = "SEQ_RACE", sequenceName = "SEQ_RACE", allocationSize = 1)
	private Integer id;

	@NotEmpty
	@Size(max = NAME_SIZE)
	@Column(name = "NAME")
	@Index(name = "IDX_RACE_NAME")
	private String name;

	@Size(max = TEXT_SIZE)
	@Column(name = "DESCRIPTION")
	private String description;

	@NotNull
	@Temporal(DATE)
	@Column(name = "DATE")
	@Index(name = "IDX_RACE_DATE")
	private Date date;

	@ManyToOne(optional = true)
	@JoinColumn(name = "CITY_ID")
	@ForeignKey(name = "FK_RACE_CITY")
	@Index(name = "IDX_RACE_CITY")
	private City city;

	@Lob
	@Column(name = "BANNER")
	private byte[] banner;

	@Lob
	@Column(name = "LOGO")
	private byte[] logo;

	@NotNull
	@Enumerated(STRING)
	@Column(name = "PAYMENT_TYPE", length = ENUM_SIZE)
	@Index(name = "IDX_RACE_PAYMENT_TYPE")
	private PaymentType paymentType;

	@Size(max = TEXT_SIZE)
	@Column(name = "PAYMENT_INFO")
	private String paymentInfo;

	@Size(max = EMAIL_SIZE)
	@Column(name = "PAYMENT_ACCOUNT")
	private String paymentAccount;

	@Size(max = HASH_SIZE)
	@Column(name = "PAYMENT_TOKEN")
	private String paymentToken;

	@Transient
	private Period registrationPeriod;

	@Transient
	private RaceStatusType status;

	public Race() {
	}

	public Race(Integer id) {
		this.id = id;
	}

	public Race(Integer id, String name, String description, Date date, String paymentAccount, String paymentToken,
			Integer cityId, String cityName, Integer stateId, String stateName, String stateAbbreviation,
			Date registrationBeginning, Date registrationEnd) {
		setId(id);
		setName(name);
		setDescription(description);
		setDate(date);
		setPaymentAccount(paymentAccount);
		setPaymentToken(paymentToken);
		setCity(new City());
		getCity().setId(cityId);
		getCity().setName(cityName);
		getCity().setState(new State());
		getCity().getState().setId(stateId);
		getCity().getState().setName(stateName);
		getCity().getState().setAbbreviation(stateAbbreviation);
		setRegistrationPeriod(new Period());
		getRegistrationPeriod().setBeginning(registrationBeginning);
		getRegistrationPeriod().setEnd(registrationEnd);
	}

	public RaceStatusType getRaceStatusType() {
		RaceStatusType result = null;

		if (registrationPeriod != null && registrationPeriod.getBeginning() != null
				&& registrationPeriod.getEnd() != null && this.date != null) {
			Date now = new Date();

			if (now.before(registrationPeriod.getBeginning())) {
				result = SOON;
			} else if (now.after(registrationPeriod.getBeginning()) && now.before(registrationPeriod.getEnd())) {
				result = OPEN;
			} else if (now.after(registrationPeriod.getEnd()) && now.before(this.date)) {
				result = CLOSED;
			} else if (now.after(this.date)) {
				result = END;
			} else {
				throw new IllegalStateException("O status da prova " + this.name + " não pôde ser definido");
			}
		}

		return result;
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
		if (!(obj instanceof Race)) {
			return false;
		}
		Race other = (Race) obj;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public byte[] getBanner() {
		return banner;
	}

	public void setBanner(byte[] banner) {
		this.banner = banner;
	}

	public byte[] getLogo() {
		return logo;
	}

	public void setLogo(byte[] logo) {
		this.logo = logo;
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

	public Period getRegistrationPeriod() {
		return registrationPeriod;
	}

	public void setRegistrationPeriod(Period registrationPeriod) {
		this.registrationPeriod = registrationPeriod;
	}
}
