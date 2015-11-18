package adventure.entity;

import static adventure.entity.RaceStatusType.CLOSED;
import static adventure.entity.RaceStatusType.END;
import static adventure.entity.RaceStatusType.OPEN;
import static adventure.entity.RaceStatusType.SOON;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.DATE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.time.DateUtils;

@Entity
@Table(name = "race")
public class Race {

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "event_id")
	private Event event;

	private String slug;

	@Deprecated
	private String name;

	@Deprecated
	private String description;

	@Column(name = "name_2")
	private String name2;

	@Column(name = "description_2")
	private String description2;

	@ManyToOne
	@JoinColumn(name = "sport_id")
	private Sport sport;

	@Deprecated
	@Temporal(DATE)
	private Date date;

	@Temporal(DATE)
	private Date beginning;

	@NotNull
	@Temporal(DATE)
	@Column(name = "ending")
	private Date end;

	@ManyToOne
	@Deprecated
	@JoinColumn(name = "city_id")
	private City city;

	@Deprecated
	private String site;

	@Lob
	@Deprecated
	private byte[] banner;

	@Deprecated
	@Enumerated(STRING)
	@Column(name = "payment_type")
	private PaymentType paymentType;

	@Deprecated
	@Column(name = "payment_info")
	private String paymentInfo;

	@Deprecated
	@Column(name = "payment_account")
	private String paymentAccount;

	@Deprecated
	@Column(name = "payment_token")
	private String paymentToken;

	@Transient
	@Deprecated
	private Period registrationPeriod;

	public Race() {
	}

	public Race(Integer id) {
		this.id = id;
	}

	public Race(Integer id, String name, String slug, Integer sportId, String sportName, String sportAcronym,
			Integer eventId, String eventName, String eventSlug, Date beginning, Date end) {
		setId(id);
		setName2(name);
		setSlug(slug);
		setSport(new Sport());
		getSport().setId(sportId);
		getSport().setName(sportName);
		getSport().setAcronym(sportAcronym);
		setEvent(new Event());
		getEvent().setId(eventId);
		getEvent().setName(eventName);
		getEvent().setSlug(eventSlug);
		getEvent().setCoords(new Coords());
		setBeginning(beginning);
		setEnd(end);
	}

	public Race(Integer id, String slug, String name, String description, Integer sportId, String sportName,
			String sportAcronym, Date beginning, Date end) {
		setId(id);
		setSlug(slug);
		setName(name);
		setDescription(description);
		setSport(new Sport());
		getSport().setId(sportId);
		getSport().setName(sportName);
		getSport().setAcronym(sportAcronym);
		setBeginning(beginning);
		setEnd(end);

		// setEvent(new Event());
		// getEvent().setId(eventId);
		// getEvent().setCoords(new Coords());
		// getEvent().getCoords().setLatitude(coordLatitude);
		// getEvent().getCoords().setLongitude(coordLongitude);
		//
		// getEvent().setCity(new City());
		// getEvent().getCity().setId(cityId);
		// getEvent().getCity().setName(cityName);
		// getEvent().getCity().setState(new State());
		// getEvent().getCity().getState().setId(stateId);
		// getEvent().getCity().getState().setName(stateName);
		// getEvent().getCity().getState().setAbbreviation(stateAbbreviation);
	}

	// TODO: OLD

	public Race(Integer id, String name, String description, Date date, String site, String paymentAccount,
			String paymentToken, Integer cityId, String cityName, Integer stateId, String stateName,
			String stateAbbreviation, Date registrationBeginning, Date registrationEnd) {
		setId(id);
		setName(name);
		setDescription(description);
		setDate(date);
		setSite(site);
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

	@Deprecated
	public RaceStatusType getStatus() {
		RaceStatusType result = null;

		if (registrationPeriod != null && registrationPeriod.getBeginning() != null
				&& registrationPeriod.getEnd() != null && this.date != null) {
			Date now = new Date();

			if (now.before(registrationPeriod.getBeginning())) {
				result = SOON;
			} else if (now.after(registrationPeriod.getBeginning()) && now.before(registrationPeriod.getEnd())
					|| DateUtils.isSameDay(now, registrationPeriod.getEnd())) {
				result = OPEN;
			} else if (now.after(registrationPeriod.getEnd()) && now.before(this.date)
					|| DateUtils.isSameDay(now, this.date)) {
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

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getDescription2() {
		return description2;
	}

	public void setDescription2(String description2) {
		this.description2 = description2;
	}

	public Sport getSport() {
		return sport;
	}

	public void setSport(Sport sport) {
		this.sport = sport;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

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

	@Deprecated
	public City getCity() {
		return city;
	}

	@Deprecated
	public void setCity(City city) {
		this.city = city;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public byte[] getBanner() {
		return banner;
	}

	public void setBanner(byte[] banner) {
		this.banner = banner;
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
