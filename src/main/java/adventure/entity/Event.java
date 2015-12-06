package adventure.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "event")
public class Event {

	@Id
	private Integer id;

	private String name;

	private String description;

	private String slug;

	@Lob
	private byte[] banner;

	private String site;

	@Embedded
	private Payment payment;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	@Embedded
	private Coords coords;

	@Embedded
	private Layout layout;

	@Transient
	private List<Race> races;

	@ManyToOne
	@JoinColumn(name = "_status_id")
	private Status status;

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

	public Event(Integer id, byte[] banner) {
		setId(id);
		setBanner(banner);
	}

	public Event(Integer id, String name, String slug, BigDecimal coordLatitude, BigDecimal coordLongitude) {
		setId(id);
		setName(name);
		setSlug(slug);

		setCoords(new Coords());
		getCoords().setLatitude(coordLatitude);
		getCoords().setLongitude(coordLongitude);
	}

	public Event(Integer id, String slug, String name, String description, String site, Integer cityId,
			String cityName, Integer stateId, String stateName, String stateAbbreviation, BigDecimal coordLatitude,
			BigDecimal coordLongitude, String layoutTextColor, String layoutBackgroundColor, String layoutButtonColor,
			Status status) {
		setId(id);
		setSlug(slug);
		setName(name);
		setDescription(description);
		setSite(site);
		setLayout(new Layout());
		getLayout().setTextColor(layoutTextColor);
		getLayout().setBackgroundColor(layoutBackgroundColor);
		getLayout().setButtonColor(layoutButtonColor);

		setCoords(new Coords());
		getCoords().setLatitude(coordLatitude);
		getCoords().setLongitude(coordLongitude);

		setCity(new City());
		getCity().setId(cityId);
		getCity().setName(cityName);
		getCity().setState(new State());
		getCity().getState().setId(stateId);
		getCity().getState().setName(stateName);
		getCity().getState().setAbbreviation(stateAbbreviation);

		setStatus(status);
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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Coords getCoords() {
		return coords;
	}

	public void setCoords(Coords coords) {
		this.coords = coords;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public List<Race> getRaces() {
		return races;
	}

	public void setRaces(List<Race> races) {
		this.races = races;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
