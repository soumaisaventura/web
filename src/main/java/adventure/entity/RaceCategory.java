package adventure.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

@Entity
@IdClass(RaceCategoryPk.class)
@Table(name = "RACE_CATEGORY")
public class RaceCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "RACE_ID")
	@ForeignKey(name = "FK_RACE_CATEGORY_RACE")
	private Race race;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "COURSE_ID")
	@ForeignKey(name = "FK_RACE_CATEGORY_COURSE")
	private Course course;

	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "CATEGORY_ID")
	@ForeignKey(name = "FK_RACE_CATEGORY_CATEGORY")
	private Category category;

	public RaceCategory() {
	}

	public RaceCategory(Race race, Course course, Category category) {
		this.race = race;
		this.course = course;
		this.category = category;
	}

	public RaceCategory(Integer categoryId, String categoryName, String categoryDescription, Integer categoryTeamSize,
			Integer categoryMinMaleMembers, Integer categoryMinFemaleMembers, Integer courseId, String courseName,
			Boolean courseAnnualFee, Integer raceId) {
		setCategory(new Category());
		getCategory().setId(categoryId);
		getCategory().setName(categoryName);
		getCategory().setDescription(categoryDescription);
		getCategory().setTeamSize(categoryTeamSize);
		getCategory().setMinMaleMembers(categoryMinMaleMembers);
		getCategory().setMinFemaleMembers(categoryMinFemaleMembers);

		setCourse(new Course());
		setCourse(new Course());
		getCourse().setId(courseId);
		getCourse().setName(courseName);
		getCourse().setAnnualFee(courseAnnualFee);

		setRace(new Race());
		getRace().setId(raceId);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((race == null) ? 0 : race.hashCode());
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
		if (!(obj instanceof RaceCategory)) {
			return false;
		}
		RaceCategory other = (RaceCategory) obj;
		if (category == null) {
			if (other.category != null) {
				return false;
			}
		} else if (!category.equals(other.category)) {
			return false;
		}
		if (course == null) {
			if (other.course != null) {
				return false;
			}
		} else if (!course.equals(other.course)) {
			return false;
		}
		if (race == null) {
			if (other.race != null) {
				return false;
			}
		} else if (!race.equals(other.race)) {
			return false;
		}
		return true;
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
