package adventure.rest.service;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.persistence.Entity;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.cache.Cache;

import br.gov.frameworkdemoiselle.util.Reflections;

@Cache(maxAge = 10)
@Produces("text/javascript")
@Path("/js/model/{entity}.js")
public class EntityService implements Extension {

	private Map<String, Class<?>> entities = new HashMap<String, Class<?>>();

	protected <T> void vetoCustomContexts(@Observes ProcessAnnotatedType<T> event) {
		Class<T> type = event.getAnnotatedType().getJavaClass();

		if (type.isAnnotationPresent(Entity.class)) {
			entities.put(type.getSimpleName(), type);
		}
	}

	@GET
	public Response getEntityMetadata(@PathParam("entity") String entity) {
		try {
			return Response.ok(parse(entity)).build();
		} catch (ClassNotFoundException cause) {
			return Response.status(NOT_FOUND).build();
		}
	}

	private String parse(String entity) throws ClassNotFoundException {
		Class<?> type = entities.get(entity);

		if (type == null) {
			throw new ClassNotFoundException(entity);
		}

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
