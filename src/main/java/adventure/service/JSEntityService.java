package adventure.service;

import java.lang.reflect.Field;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import adventure.entity.Pessoa;
import br.gov.frameworkdemoiselle.util.Reflections;

@Path("/js/entity/{entity}.js")
@Produces("text/javascript")
public class JSEntityService {

	@GET
	public String getEntityMetadata(@PathParam("entity") String entity) throws ClassNotFoundException {
		Class<?> type = Class.forName(Pessoa.class.getPackage().getName() + "." + entity);

		for (Field field : Reflections.getNonStaticFields(type)) {
			System.out.println("..... " + field.getName());
		}

		return null;
	}
}
