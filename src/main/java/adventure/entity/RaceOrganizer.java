package adventure.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

@Entity
@IdClass(RaceCategoryPk.class)
@Table(name = "RACE_ORGANIZER")
public class RaceOrganizer implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_RACE_ORGANIZER_RACE")
	@Index(name = "IDX_RACE_ORGANIZER_RACE")
	private Race race;
}
