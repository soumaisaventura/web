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
@IdClass(AvailableCategoryPk.class)
@Table(name = "AVAILABLE_CATEGORY")
public class AvailableCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_AVAILABLE_CATEGORY_RACE")
	@Index(name = "IDX_AVAILABLE_CATEGORY_RACE")
	private Race race;

	@Id
	@ManyToOne(optional = true)
	@JoinColumn(name = "COURSE_ID")
	@ForeignKey(name = "FK_AVAILABLE_CATEGORY_COURSE")
	@Index(name = "IDX_AVAILABLE_CATEGORY_COURSE")
	private Course course;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "CATEGORY_ID")
	@ForeignKey(name = "FK_AVAILABLE_CATEGORY_CATEGORY")
	@Index(name = "IDX_AVAILABLE_CATEGORY_CATEGORY")
	private Category category;

	public AvailableCategory() {
	}

	public AvailableCategory(Race race, Course course, Category category) {
		this.race = race;
		this.course = course;
		this.category = category;
	}

	public Race getRace() {
		return race;
	}

	public void setRace(Race race) {
		this.race = race;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
