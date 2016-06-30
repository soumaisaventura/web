package adventure.rest;

import adventure.entity.City;
import adventure.entity.Country;
import adventure.entity.State;
import adventure.persistence.CityDAO;
import adventure.persistence.CountryDAO;
import adventure.persistence.StateDAO;
import adventure.rest.data.CityData;
import adventure.rest.data.CountryData;
import adventure.rest.data.StateData;
import br.gov.frameworkdemoiselle.NotFoundException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Path("location")
public class LocationREST {

    @GET
    @Path("countries")
    @Produces("application/json")
    public List<CountryData> findCoutries() throws Exception {
        List<CountryData> result = new ArrayList();

        for (Country country : CountryDAO.getInstance().findAll()) {
            result.add(new CountryData(country));
        }

        return result.isEmpty() ? null : result;
    }

    @GET
    @Produces("application/json")
    @Path("countries/{id}/states")
    public List<StateData> findStates(@PathParam("id") String id) throws Exception {
        List<StateData> result = new ArrayList();
        Country country = loadCountry(id);

        for (State state : StateDAO.getInstance().find(country)) {
            StateData data = new StateData(state);
            data.country = null;
            result.add(data);
        }

        return result.isEmpty() ? null : result;
    }

    @GET
    @Produces("application/json")
    @Path("countries/{countryId}/states/{stateId}/cities")
    public List<CityData> findCities(@PathParam("countryId") String countryId, @PathParam("stateId") String stateId) throws Exception {
        List<CityData> result = new ArrayList();
        State state = loadState(stateId, countryId);

        for (City city : CityDAO.getInstance().find(state)) {
            CityData data = new CityData(city);
            data.state = null;
            result.add(data);
        }

        return result.isEmpty() ? null : result;
    }

    private Country loadCountry(String abbreviation) throws Exception {
        Country result = CountryDAO.getInstance().load(abbreviation);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }

    private State loadState(String abbreviation, String countryAbbreviation) throws Exception {
        State result = StateDAO.getInstance().load(abbreviation, countryAbbreviation);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }
}
