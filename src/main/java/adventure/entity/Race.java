package adventure.entity;

import static javax.persistence.TemporalType.DATE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

@Entity
@Table(name = "race")
public class Race {

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;

	private String slug;

	private String name;

	private String description;

	private Integer distance;

	@ManyToOne
	@JoinColumn(name = "sport_id")
	private Sport sport;

	// @Deprecated
	// @Temporal(DATE)
	// private Date date;

	@Temporal(DATE)
	private Date beginning;

	@Temporal(DATE)
	@Column(name = "ending")
	private Date end;

	@ManyToOne
	@JoinColumn(name = "_status_id")
	private Status status;

	// @ManyToOne
	// @Deprecated
	// @JoinColumn(name = "city_id")
	// private City city;

	// @Deprecated
	// private String site;

	// @Lob
	// @Deprecated
	// private byte[] banner;

	// @Deprecated
	// @Enumerated(STRING)
	// @Column(name = "payment_type")
	// private PaymentType paymentType;

	// @Deprecated
	// @Column(name = "payment_info")
	// private String paymentInfo;

	// @Deprecated
	// @Column(name = "payment_account")
	// private String paymentAccount;

	// @Deprecated
	// @Column(name = "payment_token")
	// private String paymentToken;

	@Transient
	private Period registrationPeriod;

	public Race() {
	}

	public Race(Integer id) {
		this.id = id;
	}

	public Race(Integer id, String slug, String name, String description, Integer distance, Integer sportId,
			String sportName, String sportAcronym, Date beginning, Date end, Status status) {
		setId(id);
		setSlug(slug);
		setName(name);
		setDistance(distance);
		setDescription(description);
		setSport(new Sport());
		getSport().setId(sportId);
		getSport().setName(sportName);
		getSport().setAcronym(sportAcronym);
		setBeginning(beginning);
		setEnd(end);
		setStatus(status);
	}

	// TODO: OLD

	public Race(Integer id, String name, String description, Date date, String site, String paymentAccount,
			String paymentToken, Integer cityId, String cityName, Integer stateId, String stateName,
			String stateAbbreviation, Date registrationBeginning, Date registrationEnd, Status status) {
		setId(id);
		setName(name);
		setDescription(description);
		// setDate(date);
		// setSite(site);
		// setPaymentAccount(paymentAccount);
		// setPaymentToken(paymentToken);
		// setCity(new City());
		// getCity().setId(cityId);
		// getCity().setName(cityName);
		// getCity().setState(new State());
		// getCity().getState().setId(stateId);
		// getCity().getState().setName(stateName);
		// getCity().getState().setAbbreviation(stateAbbreviation);
		setRegistrationPeriod(new Period());
		getRegistrationPeriod().setBeginning(registrationBeginning);
		getRegistrationPeriod().setEnd(registrationEnd);
		setStatus(status);
	}

	// @Deprecated
	// public StatusType getStatus() {
	// StatusType result = null;
	//
	// if (registrationPeriod != null && registrationPeriod.getBeginning() != null
	// && registrationPeriod.getEnd() != null && this.date != null) {
	// Date now = new Date();
	//
	// if (now.before(registrationPeriod.getBeginning())) {
	// result = SOON;
	// } else if (now.after(registrationPeriod.getBeginning()) && now.before(registrationPeriod.getEnd())
	// || DateUtils.isSameDay(now, registrationPeriod.getEnd())) {
	// result = OPEN;
	// } else if (now.after(registrationPeriod.getEnd()) && now.before(this.date)
	// || DateUtils.isSameDay(now, this.date)) {
	// result = CLOSED;
	// } else if (now.after(this.date)) {
	// result = END;
	// } else {
	// throw new IllegalStateException("O status da prova " + this.name + " não pôde ser definido");
	// }
	// }
	//
	// return result;
	// }

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

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
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

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	// @Deprecated
	// public Date getDate() {
	// return date;
	// }

	// @Deprecated
	// public void setDate(Date date) {
	// this.date = date;
	// }

	public Date getBeginning() {
		return beginning;
	}

	public void setBeginning(Date beginning) {
		this.beginning = beginning;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	// @Deprecated
	// public City getCity() {
	// return city;
	// }

	// @Deprecated
	// public void setCity(City city) {
	// this.city = city;
	// }

	// @Deprecated
	// public String getSite() {
	// return site;
	// }

	// @Deprecated
	// public void setSite(String site) {
	// this.site = site;
	// }

	// @Deprecated
	// public byte[] getBanner() {
	// return banner;
	// }

	// @Deprecated
	// public void setBanner(byte[] banner) {
	// this.banner = banner;
	// }

	// @Deprecated
	// public PaymentType getPaymentType() {
	// return paymentType;
	// }

	// @Deprecated
	// public void setPaymentType(PaymentType paymentType) {
	// this.paymentType = paymentType;
	// }

	// @Deprecated
	// public String getPaymentInfo() {
	// return paymentInfo;
	// }

	// @Deprecated
	// public void setPaymentInfo(String paymentInfo) {
	// this.paymentInfo = paymentInfo;
	// }

	// @Deprecated
	// public String getPaymentAccount() {
	// return paymentAccount;
	// }

	// @Deprecated
	// public void setPaymentAccount(String paymentAccount) {
	// this.paymentAccount = paymentAccount;
	// }

	// @Deprecated
	// public String getPaymentToken() {
	// return paymentToken;
	// }

	// @Deprecated
	// public void setPaymentToken(String paymentToken) {
	// this.paymentToken = paymentToken;
	// }

	public Period getRegistrationPeriod() {
		return registrationPeriod;
	}

	public void setRegistrationPeriod(Period registrationPeriod) {
		this.registrationPeriod = registrationPeriod;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
