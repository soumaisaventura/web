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

	@Embedded
	private Period period;

	@ManyToOne
	@JoinColumn(name = "_status_id")
	private Status status;

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

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

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
