package adventure.rest.service;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import adventure.entity.Modality;
import adventure.persistence.ModalityDAO;

@Path("/api/modality")
public class ModalityService {

	@Inject
	ModalityDAO dao;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Modality> findAll() throws Exception {
		return dao.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Modality findById(@PathParam("id") Long id) throws Exception {
		return dao.load(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void insert(Modality modality) throws Exception {
		dao.insert(modality);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void update(Modality modality) throws Exception {
		dao.update(modality);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long id) throws Exception {
		dao.delete(id);
	}
}
