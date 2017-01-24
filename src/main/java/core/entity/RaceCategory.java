package core.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@IdClass(RaceCategoryPk.class)
@Table(name = "race_category")
public class RaceCategory {

    @Id
    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Id
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "_vacant")
    private Boolean vacant;

    public RaceCategory() {
    }

    public RaceCategory(Integer categoryId, String categoryAlias, String categoryName, String categoryDescription, Integer categoryTeamSize, Integer categoryMinMaleMembers, Integer categoryMinFemaleMembers, Integer categoryMinMemberAge, Integer categoryMaxMemberAge, Integer categoryMinTeamAge, Integer categoryMaxTeamAge,
                        Integer raceId, String raceName, Date racePeriodBeginning, Date racePeriodEnd,
                        Integer eventId, String eventAlias, String eventName,
                        Boolean vacant) {
        setCategory(new Category(categoryId, categoryAlias, categoryName, categoryDescription, categoryTeamSize, categoryMinMaleMembers, categoryMinFemaleMembers, categoryMinMemberAge, categoryMaxMemberAge, categoryMinTeamAge, categoryMaxTeamAge));

        setRace(new Race());
        getRace().setId(raceId);
        getRace().setName(raceName);
        getRace().setPeriod(new Period());
        getRace().getPeriod().setBeginning(racePeriodBeginning);
        getRace().getPeriod().setEnd(racePeriodEnd);

        getRace().setEvent(new Event());
        getRace().getEvent().setId(eventId);
        getRace().getEvent().setAlias(eventAlias);
        getRace().getEvent().setName(eventName);

        setVacant(vacant);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getVacant() {
        return vacant;
    }

    public void setVacant(Boolean show) {
        this.vacant = show;
    }
}
