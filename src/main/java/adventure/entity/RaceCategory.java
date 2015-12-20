package adventure.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(RaceCategoryPk.class)
@Table(name = "race_category")
public class RaceCategory {

	@Id
	@ManyToOne
	@JoinColumn(name = "race_id")
	private Race race;

	// @Id
	// @ManyToOne
	// @JoinColumn(name = "course_id")
	// private Course course;

	@Id
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public RaceCategory() {
	}

	public RaceCategory(Race race, /* Course course, */Category category) {
		this.race = race;
		// this.course = course;
		this.category = category;
	}

	public RaceCategory(Integer categoryId, String categoryName, String categoryDescription, Integer categoryTeamSize,
			Integer categoryMinMaleMembers, Integer categoryMinFemaleMembers, /* Integer courseId, String courseName, */
			Integer raceId, String raceName /* , Date raceDate */) {
		setCategory(new Category());
		getCategory().setId(categoryId);
		getCategory().setName(categoryName);
		getCategory().setDescription(categoryDescription);
		getCategory().setTeamSize(categoryTeamSize);
		getCategory().setMinMaleMembers(categoryMinMaleMembers);
		getCategory().setMinFemaleMembers(categoryMinFemaleMembers);

		// setCourse(new Course());
		// getCourse().setId(courseId);
		// getCourse().setName(courseName);

		setRace(new Race());
		getRace().setId(raceId);
		getRace().setName(raceName);
		// getRace().setDate(raceDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((category == null) ? 0 : category.hashCode());
		// result = prime * result + ((course == null) ? 0 : course.hashCode());
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
		// if (course == null) {
		// if (other.course != null) {
		// return false;
		// }
		// } else if (!course.equals(other.course)) {
		// return false;
		// }
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

	// public Course getCourse() {
	// return course;
	// }
	//
	// public void setCourse(Course course) {
	// this.course = course;
	// }

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
