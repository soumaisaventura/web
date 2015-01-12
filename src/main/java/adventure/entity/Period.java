package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@Table(name = "PERIOD", uniqueConstraints = { @UniqueConstraint(name = "UK_PERIOD_BEGINNING", columnNames = {
		"RACE_ID", "BEGINNING" }) })
// @Table(name = "PERIOD", uniqueConstraints = { @UniqueConstraint(columnNames = { "RACE_ID", "BEGINNING" }),
// @UniqueConstraint(columnNames = { "RACE_ID", "ENDING" }) })
// @org.hibernate.annotations.Table(appliesTo = "PERIOD", indexes = {
// @Index(name = "IDX_PERIOD_BEGINNING", columnNames = { "RACE_ID" }),
// @Index(name = "IDX_PERIOD_ENDING", columnNames = { "RACE_ID" }) })
public class Period implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_PERIOD_RACE")
	@Index(name = "IDX_PERIOD_RACE")
	private Race race;

	@NotNull
	@Temporal(DATE)
	@Column(name = "BEGINNING")
	@Index(name = "IDX_PERIOD_BEGINNING")
	private Date beginning;

	@NotNull
	@Temporal(DATE)
	@Column(name = "ENDING")
	@Index(name = "IDX_PERIOD_ENDING")
	private Date end;

	@NotNull
	@Column(name = "PRICE")
	private BigDecimal price;

	public Period(Race race) {
		this.race = race;
	}

	public Period() {
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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
