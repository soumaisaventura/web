package br.com.fbca.rest_;

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

import br.com.fbca.entity_.Modality;
import br.com.fbca.persistence_.ModalityDAO;
import br.gov.frameworkdemoiselle.util.ValidatePayload;

@Path("modality")
public class ModalityREST {

	@Inject
	private ModalityDAO dao;

	@GET
	@Produces("application/json")
	public List<Modality> findAll() throws Exception {
		return dao.findAll();
	}

	@GET
	@Path("/{id}")
	@Produces("application/json")
	public Modality findById(@PathParam("id") Long id) throws Exception {
		return dao.load(id);
	}

	@POST
	@ValidatePayload
	@Consumes("application/json")
	public void insert(Modality modality) throws Exception {
		dao.insert(modality);
	}

	@PUT
	@ValidatePayload
	@Consumes("application/json")
	public void update(Modality modality) throws Exception {
		dao.update(modality);
	}

	@DELETE
	@Path("/{id}")
	public void delete(@PathParam("id") Long id) throws Exception {
		dao.delete(id);
	}
}
