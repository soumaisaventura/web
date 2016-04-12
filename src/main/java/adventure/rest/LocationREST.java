package adventure.rest;

import adventure.entity.City;
import adventure.entity.State;
import adventure.persistence.CityDAO;
import adventure.persistence.StateDAO;
import adventure.rest.data.CityData;
import br.gov.frameworkdemoiselle.NotFoundException;
import br.gov.frameworkdemoiselle.UnprocessableEntityException;
import br.gov.frameworkdemoiselle.util.Strings;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Path("location")
public class LocationREST {

    /*
     * TODO Apagar na v2
     */
    @Deprecated
    @GET
    @Path("city")
    @Produces("application/json")
    public List<CityData> searchCityOLD(@QueryParam("q") String q) throws Exception {
        validateOLD(q);
        List<CityData> result = new ArrayList();
        CityData data;

        for (City city : CityDAO.getInstance().search(q)) {
            data = new CityData();
            data.id = city.getId();
            data.name = city.getName();
            data.state = city.getState().getAbbreviation();

            // if (!"brasil".equals(city.getState().getCountry().getName().toLowerCase())) {
            data.country = city.getState().getCountry().getName();
            // }

            result.add(data);
        }

        return result.isEmpty() ? null : result;
    }

    private void validateOLD(String q) throws Exception {
        if (Strings.isEmpty(q)) {
            throw new UnprocessableEntityException().addViolation("q", "parâmetro obrigatório");
        } else if (q.length() < 3) {
            throw new UnprocessableEntityException().addViolation("q", "deve possuir 3 ou mais caracteres");
        }
    }

    @GET
    @Path("uf")
    @Produces("application/json")
    public List<State> loadStates() throws Exception {
        return StateDAO.getInstance().findAll();
    }

    @GET
    @Path("uf/{abbreviation}/cities")
    @Produces("application/json")
    public List<CityData> searchCity(@PathParam("abbreviation") String abbreviation) throws Exception {
        List<CityData> result = new ArrayList();
        CityData data;

        State state = loadState(abbreviation);

        for (City city : CityDAO.getInstance().find(state)) {
            data = new CityData();
            data.id = city.getId();
            data.name = city.getName();
            data.state = null;

            result.add(data);
        }

        return result.isEmpty() ? null : result;
    }

    private State loadState(String abbreviation) throws Exception {
        State result = StateDAO.getInstance().load(abbreviation);

        if (result == null) {
            throw new NotFoundException();
        }

        return result;
    }
}
