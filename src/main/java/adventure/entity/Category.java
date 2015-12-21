package adventure.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category")
public class Category {

	@Id
	private Integer id;

	private String name;

	private String description;

	@NotNull
	@Column(name = "team_size")
	private Integer teamSize;

	@Column(name = "min_male_members")
	private Integer minMaleMembers;

	@Column(name = "min_female_members")
	private Integer minFemaleMembers;

	public Category() {
	}

	public Category(Integer id, String name, String description, Integer teamSize, Integer minMaleMembers,
			Integer minFemaleMembers) {
		setId(id);
		setName(name);
		setDescription(description);
		setTeamSize(teamSize);
		setMinMaleMembers(minMaleMembers);
		setMinFemaleMembers(minFemaleMembers);
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
		if (!(obj instanceof Category)) {
			return false;
		}
		Category other = (Category) obj;
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

	public Integer getTeamSize() {
		return teamSize;
	}

	public void setTeamSize(Integer teamSize) {
		this.teamSize = teamSize;
	}

	public Integer getMinMaleMembers() {
		return minMaleMembers;
	}

	public void setMinMaleMembers(Integer minMaleMembers) {
		this.minMaleMembers = minMaleMembers;
	}

	public Integer getMinFemaleMembers() {
		return minFemaleMembers;
	}

	public void setMinFemaleMembers(Integer minFemaleMembers) {
		this.minFemaleMembers = minFemaleMembers;
	}
}
