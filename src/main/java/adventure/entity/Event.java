package adventure.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

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

	public Event(Integer id, byte[] banner) {
		setId(id);
		setBanner(banner);
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

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}
}
