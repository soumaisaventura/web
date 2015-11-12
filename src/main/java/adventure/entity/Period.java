package adventure.entity;

import static javax.persistence.TemporalType.DATE;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "period")
public class Period {

	@Id
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "race_id")
	private Race race;

	@Temporal(DATE)
	private Date beginning;

	@Temporal(DATE)
	@Column(name = "ending")
	private Date end;

	@Column(name = "price")
	private BigDecimal price;

	public Period() {
	}

	public Period(Date beginning, Date end, BigDecimal price) {
		setBeginning(beginning);
		setEnd(end);
		setPrice(price);
	}

	// TODO: OLD

	public Period(Race race) {
		this.race = race;
	}

	public Period(Date beginning, Date end) {
		setBeginning(beginning);
		setEnd(end);
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
		if (!(obj instanceof Period)) {
			return false;
		}
		Period other = (Period) obj;
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

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
