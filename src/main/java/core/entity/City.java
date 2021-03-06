package core.entity;

import javax.persistence.*;

@Entity
@Table(name = "city")
public class City {

    @Id
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "state_id")
    private State state;

    public City() {
    }

    public City(Integer id, String name, Integer stateId, String stateName, String stateAbbreviation, Integer countryId, String countryName, String countryAbbreviation) {
        setId(id);
        setName(name);
        setState(new State());
        getState().setId(stateId);
        getState().setName(stateName);
        getState().setAbbreviation(stateAbbreviation);
        getState().setCountry(new Country());
        getState().getCountry().setId(countryId);
        getState().getCountry().setName(countryName);
        getState().getCountry().setAbbreviation(countryAbbreviation);
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
        if (!(obj instanceof City)) {
            return false;
        }
        City other = (City) obj;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
