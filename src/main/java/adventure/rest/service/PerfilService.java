package adventure.rest.service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Usuario;
import adventure.persistence.UsuarioDAO;

@Path("/api/perfil")
@Produces(APPLICATION_JSON)
public class PerfilService {

	@Inject
	private UsuarioDAO dao;

	@GET
	@Path("/{id}")
	public Usuario obter(@PathParam("id") Long id) {
		return dao.load(id);
	}
}
