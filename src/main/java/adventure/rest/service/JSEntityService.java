package adventure.rest.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.lang.reflect.Field;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.cache.Cache;

import br.gov.frameworkdemoiselle.util.Reflections;

@Path("/js/model/{entity}.js")
@Produces("text/javascript")
@Cache(maxAge = 10)
public class JSEntityService {

	@GET
	public Response getEntityMetadata(@PathParam("entity") String entity) {
		try {
			return Response.ok(parse(entity)).build();
		} catch (ClassNotFoundException cause) {
			return Response.status(NOT_FOUND).build();
		}
	}

	private static String parse(String entity) throws ClassNotFoundException {
		Class<?> type = Class.forName("adventure.entity." + entity);

		StringBuffer buffer = new StringBuffer();
		buffer.append("var ");
		buffer.append(type.getSimpleName());
		buffer.append("=function(){");

		for (Field field : Reflections.getNonStaticFields(type)) {
			buffer.append("this.");
			buffer.append(field.getName());
			buffer.append(";");
		}

		buffer.append("};");
		return buffer.toString();
	}
}
