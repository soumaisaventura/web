package adventure;

import static adventure.entity.Sexo.MASCULINO;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.util.GenericType;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.Tests;
import adventure.entity.Atleta;
import adventure.entity.Telefone;
import adventure.service.Validation;

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
		atletaLocal.setSexo(MASCULINO);

		Atleta atletaRemote;
		atletaRemote = obterPorEmail(atletaLocal.getEmail());

		if (atletaRemote != null) {
			fail("O atleta já está cadastrado e não deveria");
		}

		AtletaServiceClient client = createClient();
		client.criar(atletaLocal);

		atletaRemote = obterPorEmail(atletaLocal.getEmail());
		assertEquals(atletaLocal.getEmail(), atletaRemote.getEmail());

		client.excluir(atletaRemote.getId());
		atletaRemote = obterPorEmail(atletaLocal.getEmail());
		assertNull(atletaRemote);
	}

	@Test
	public void falhaAoTentarInserirComCamposObrigatoriosNulos() {
		AtletaServiceClient client = createClient();

		Atleta atleta = new Atleta();
		atleta.setTelefoneComercial(new Telefone());

		try {
			client.criar(atleta);
			fail("Deveria ter ocorrido erro ao tentar inserir");

		} catch (ClientResponseFailure cause) {
			assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());

			@SuppressWarnings("unchecked")
			List<Validation> validations = (List<Validation>) cause.getResponse().getEntity(
					new GenericType<List<Validation>>() {
					});

			List<Validation> expected = new ArrayList<Validation>();
			expected.add(new Validation("nome", "Não pode ser vazio."));
			expected.add(new Validation("email", "Não pode ser vazio."));
			expected.add(new Validation("telefoneComercial.area", "Não pode ser vazio."));
			expected.add(new Validation("telefoneComercial.numero", "Não pode ser vazio."));

			assertEquals(new HashSet<Validation>(expected), new HashSet<Validation>(validations));
		}
	}

	private Atleta obterPorEmail(String email) {
		Atleta result = null;
		AtletaServiceClient client = createClient();

		for (Atleta aux : client.obterTodos()) {
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
