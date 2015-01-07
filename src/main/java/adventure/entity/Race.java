package adventure.entity;

import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.DATE;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "RACE")
public class Race implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@NotEmpty
	@Index(name = "IDX_RACE_NAME")
	private String name;

	@NotNull
	@Temporal(DATE)
	@Index(name = "IDX_RACE_DATE")
	private Date date;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
