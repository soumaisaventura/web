package adventure.entity;

import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

	@Embedded
	private Period period;

	// @Temporal(DATE)
	// private Date beginning;
	//
	// @Temporal(DATE)
	// @Column(name = "ending")
	// private Date end;

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
	private RegistrationPeriod registrationPeriod;

	public Race() {
	}

	public Race(Integer id) {
		this.id = id;
	}

	public Race(Integer id, String slug, String name, String description, Integer distance, Integer sportId,
			String sportName, String sportAcronym, Date periodBeginning, Date periodEnd, Status status) {
		setId(id);
		setSlug(slug);
		setName(name);
		setDistance(distance);
		setDescription(description);
		setSport(new Sport());
		getSport().setId(sportId);
		getSport().setName(sportName);
		getSport().setAcronym(sportAcronym);
		setPeriod(new Period());
		getPeriod().setBeginning(periodBeginning);
		getPeriod().setEnd(periodEnd);
		setStatus(status);
	}

	// Meta OGG

	public Race(Integer id, String name, String slug, String description, Integer eventId, String eventName,
			String eventSlug, String eventDescription) {
		setId(id);
		setName(name);
		setSlug(slug);
		setDescription(description);

		setEvent(new Event());
		getEvent().setId(eventId);
		getEvent().setName(eventName);
		getEvent().setSlug(eventSlug);
		getEvent().setDescription(eventDescription);
	}

	// Detail

	public Race(Integer id, String name, String slug, String description, Date periodBeginning, Date periodEnd,
			Integer eventId, String eventName, String eventSlug, String eventDescription, String eventSite,
			String eventPaymentAccount, String eventPaymentToken, Integer cityId, String cityName, Integer stateId,
			String stateName, String stateAbbreviation, Integer statusId, String statusName) {
		setId(id);
		setName(name);
		setSlug(slug);
		setDescription(description);

		setStatus(new Status());
		getStatus().setId(statusId);
		getStatus().setName(statusName);

		setPeriod(new Period());
		getPeriod().setBeginning(periodBeginning);
		getPeriod().setEnd(periodEnd);

		setEvent(new Event());
		getEvent().setId(eventId);
		getEvent().setName(eventName);
		getEvent().setSlug(eventSlug);
		getEvent().setDescription(eventDescription);
		getEvent().setSite(eventSite);

		getEvent().setPayment(new EventPayment());
		getEvent().getPayment().setAccount(eventPaymentAccount);
		getEvent().getPayment().setToken(eventPaymentToken);

		getEvent().setCity(new City());
		getEvent().getCity().setId(cityId);
		getEvent().getCity().setName(cityName);
		getEvent().getCity().setState(new State());
		getEvent().getCity().getState().setId(stateId);
		getEvent().getCity().getState().setName(stateName);
		getEvent().getCity().getState().setAbbreviation(stateAbbreviation);
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

	// public Date getBeginning() {
	// return beginning;
	// }

	// public void setBeginning(Date beginning) {
	// this.beginning = beginning;
	// }

	// public Date getEnd() {
	// return end;
	// }

	// public void setEnd(Date end) {
	// this.end = end;
	// }

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
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

	public RegistrationPeriod getRegistrationPeriod() {
		return registrationPeriod;
	}

	public void setRegistrationPeriod(RegistrationPeriod registrationPeriod) {
		this.registrationPeriod = registrationPeriod;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
