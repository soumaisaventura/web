package adventure.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "event")
public class Event {

    @Id
    private Integer id;

    private String alias;

    private String name;

    private String description;

    @Lob
    private byte[] banner;

    @Column(name = "_banner_hash")
    private String bannerHash;

    private String site;

    @Embedded
    private EventPayment payment;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    private Boolean show;

    @Transient
    private List<Race> races;

    @Temporal(DATE)
    @Column(name = "_beginning")
    private Date beginning;

    @Temporal(DATE)
    @Column(name = "_ending")
    private Date end;

    @ManyToOne
    @JoinColumn(name = "_status_id")
    private Status status;

    @Transient
    private List<User> organizers;

    public Event() {
    }

    public Event(Integer id) {
        this.id = id;
    }

    public Event(Integer id, String name, String alias, String description, Date beginning, Date end) {
        setId(id);
        setName(name);
        setAlias(alias);
        setDescription(description);
        setBeginning(beginning);
        setEnd(end);
    }

    public Event(Integer id, byte[] banner, String bannerHash) {
        setId(id);
        setBanner(banner);
        setBannerHash(bannerHash);
    }

    public Event(Integer id, String alias, String name, String description, String site,
                 Integer cityId, String cityName, Integer stateId, String stateName, String stateAbbreviation, Integer countryId, String countryName, String countryAbbreviation,
                 Date beginnig, Date end, Status status) {
        setId(id);
        setAlias(alias);
        setName(name);
        setDescription(description);
        setSite(site);

        setCity(new City());
        getCity().setId(cityId);
        getCity().setName(cityName);
        getCity().setState(new State());
        getCity().getState().setId(stateId);
        getCity().getState().setName(stateName);
        getCity().getState().setAbbreviation(stateAbbreviation);
        getCity().getState().setCountry(new Country());
        getCity().getState().getCountry().setId(countryId);
        getCity().getState().getCountry().setName(countryName);
        getCity().getState().getCountry().setAbbreviation(countryAbbreviation);

        setBeginning(beginnig);
        setEnd(end);

        setStatus(status);
    }

    public boolean isTest() {
        return id < 1;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public byte[] getBanner() {
        return banner;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerHash() {
        return bannerHash;
    }

    public void setBannerHash(String bannerHash) {
        this.bannerHash = bannerHash;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public EventPayment getPayment() {
        return payment;
    }

    public void setPayment(EventPayment payment) {
        this.payment = payment;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public List<Race> getRaces() {
        return races;
    }

    public void setRaces(List<Race> races) {
        this.races = races;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<User> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<User> organizers) {
        this.organizers = organizers;
    }
}
