package adventure.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.inject.Inject;
import javax.naming.NamingException;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jboss.resteasy.spi.validation.ValidateRequest;

import adventure.entity.User;
import adventure.persistence.UserDAO;
import br.gov.frameworkdemoiselle.transaction.Transactional;

@ValidateRequest
@Path("/api/profile")
@Produces(APPLICATION_JSON)
public class ProfileService {

	@Inject
	private UserDAO dao;

	@GET
	@Path("/{id}")
	public User load(@NotNull @PathParam("id") Long id) {
		return dao.load(id);
	}
	
	@GET
	@Path("/{email}")
	public User loadByEmail(@NotNull @PathParam("email") String email){
		return dao.loadByEmail(email);
	}

	@DELETE
	@Path("/{id}")
	@Transactional
	public void delete(@NotNull @PathParam("id") Long id) {
		dao.delete(id);
	}

	@GET
	public List<User> search() throws NamingException {
		return dao.findAll();
	}
}
