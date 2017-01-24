package core.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "race")
public class Race {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private String alias;

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

    public Race(Integer id, String alias, String name, String description, Integer distance, Integer sportId,
                String sportName, String sportAcronym, Date periodBeginning, Date periodEnd, Status status) {
        setId(id);
        setAlias(alias);
        setName(name);
        setDistance(distance);
        setDescription(description);
        setSport(new Sport());
        getSport().setId(sportId);
        getSport().setName(sportName);
        getSport().setAlias(sportAcronym);
        setPeriod(new Period());
        getPeriod().setBeginning(periodBeginning);
        getPeriod().setEnd(periodEnd);
        setStatus(status);
    }

    // Meta OGG

    public Race(Integer id, String name, String alias, String description, Integer eventId, String eventName,
                String eventAlias, String eventDescription) {
        setId(id);
        setName(name);
        setAlias(alias);
        setDescription(description);

        setEvent(new Event());
        getEvent().setId(eventId);
        getEvent().setName(eventName);
        getEvent().setAlias(eventAlias);
        getEvent().setDescription(eventDescription);
    }

    // Detail

    public Race(Integer id, String name, String alias, String description, Date periodBeginning, Date periodEnd,
                Integer eventId, String eventName, String eventAlias, String eventDescription, String eventSite,
                String eventPaymentAccount, String eventPaymentToken,
                Integer cityId, String cityName, Integer stateId, String stateName, String stateAbbreviation, Integer countryId, String countryName, String countryAbbreviation,
                Integer statusId, String statusName) {
        setId(id);
        setName(name);
        setAlias(alias);
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
        getEvent().setAlias(eventAlias);
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
        getEvent().getCity().getState().setCountry(new Country());
        getEvent().getCity().getState().getCountry().setId(countryId);
        getEvent().getCity().getState().getCountry().setName(countryName);
        getEvent().getCity().getState().getCountry().setAbbreviation(countryAbbreviation);
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
