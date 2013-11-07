package adventure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.Tests;
import adventure.entity.Atleta;

@RunWith(Arquillian.class)
public class AtletaServiceTest {

	@ArquillianResource
	private URL url;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Tests.createDeployment();
	}

	private AtletaServiceClient createClient() {
		return ProxyFactory.create(AtletaServiceClient.class, url.toString());
	}

	@Test
	public void crudBemSucedidoApenasComCamposObrigatorios() {
		Atleta atletaLocal = new Atleta();
		atletaLocal.setNome("Cleverson Sacramento");
		atletaLocal.setEmail("cleverson.sacramento@gmail.com");

		Atleta atletaRemote;
		atletaRemote = obterPorEmail(atletaLocal.getEmail());

		if (atletaRemote != null) {
			fail("O atleta já está cadastrado e não deveria");
		}

		AtletaServiceClient client = createClient();
		client.create(atletaLocal);

		atletaRemote = obterPorEmail(atletaLocal.getEmail());
		assertEquals(atletaLocal.getEmail(), atletaRemote.getEmail());

		client.delete(atletaRemote.getId());
		atletaRemote = obterPorEmail(atletaLocal.getEmail());
		assertNull(atletaRemote);
	}

	private Atleta obterPorEmail(String email) {
		Atleta result = null;
		AtletaServiceClient client = createClient();

		for (Atleta aux : client.findAll()) {
			if (email.equals(aux.getEmail())) {
				if (result != null) {
					throw new IllegalStateException("E-mail duplicado: " + email);
				} else {
					result = aux;
				}
			}
		}

		return result;
	}
}
