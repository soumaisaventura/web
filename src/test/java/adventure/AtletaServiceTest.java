package adventure;

import static adventure.entity.Sexo.MASCULINO;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
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
import adventure.entity.Usuario;
import adventure.persistence.ValidationException;

@RunWith(Arquillian.class)
public class AtletaServiceTest {

	@ArquillianResource
	private URL url;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Tests.createDeployment();
	}

	private RegistroServiceClient createClient() {
		return ProxyFactory.create(RegistroServiceClient.class, url.toString());
	}

	private Date createDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);

		return calendar.getTime();
	}

	@Test
	@SuppressWarnings("unchecked")
	public void cadastroBemSucedidoApenasComCamposObrigatorios() {
		RegistroServiceClient client = createClient();

		Usuario usuario = new Usuario();
		usuario.setNome("Cleverson Sacramento");
		usuario.setEmail("cleverson.sacramento@gmail.com");
		usuario.setSenha("segredo");
		usuario.setNascimento(createDate(1980, 12, 18));
		usuario.setSexo(MASCULINO);

		try {
			client.criar(usuario);
		} catch (ClientResponseFailure cause) {
			System.out.println(cause.getResponse().getEntity(new GenericType<List<ValidationException.Violation>>() {
			}));
		}
	}

	// @Test
	// public void falhaAoTentarInserirComCamposObrigatoriosNulos() {
	// RegistroServiceClient client = createClient();
	//
	// Atleta atleta = new Atleta();
	//
	// try {
	// client.criar(atleta);
	// fail("Deveria ter ocorrido erro ao tentar inserir");
	//
	// } catch (ClientResponseFailure cause) {
	// assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());
	//
	// @SuppressWarnings("unchecked")
	// List<Violation> validations = (List<Violation>) cause.getResponse().getEntity(
	// new GenericType<List<Violation>>() {
	// });
	//
	// List<Violation> expected = new ArrayList<Violation>();
	// expected.add(new Violation("nome", "Não pode ser vazio."));
	// expected.add(new Violation("email", "Não pode ser vazio."));
	// expected.add(new Violation("cpf", "Não pode ser vazio."));
	// expected.add(new Violation("rg", "Não pode ser vazio."));
	// expected.add(new Violation("nascimento", "Não pode ser nulo."));
	// expected.add(new Violation("sexo", "Não pode ser nulo."));
	// expected.add(new Violation("telefoneCelular", "Não pode ser vazio."));
	//
	// assertEquals(new HashSet<Violation>(expected), new HashSet<Violation>(validations));
	// }
	// }
	//
	// private Atleta obterPorEmail(String email) {
	// Atleta result = null;
	// RegistroServiceClient client = createClient();
	//
	// for (Atleta aux : client.obterTodos()) {
	// if (email.equals(aux.getEmail())) {
	// if (result != null) {
	// throw new IllegalStateException("E-mail duplicado: " + email);
	// } else {
	// result = aux;
	// }
	// }
	// }
	//
	// return result;
	// }
}
