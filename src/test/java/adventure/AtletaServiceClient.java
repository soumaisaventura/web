//package adventure;
//
//import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
//
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//
//import adventure.entity.Atleta;
//
//@Path("/api/atleta")
//@Consumes(APPLICATION_JSON)
//public interface AtletaServiceClient {
//
//	@POST
//	Long criar(Atleta atleta);
//
//	@DELETE
//	@Path("/{id}")
//	void excluir(@PathParam("id") Long id);
//
//	@GET
//	List<Atleta> obterTodos();
//
//	@GET
//	@Path("/{id}")
//	Atleta obter(@PathParam("id") Long id);
//}
