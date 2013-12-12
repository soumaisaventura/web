package adventure;

import static adventure.entity.Sexo.MASCULINO;
import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.util.GenericType;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.Tests;
import adventure.entity.Sexo;
import adventure.entity.Usuario;
import adventure.persistence.ValidationException.Violation;

@RunWith(Arquillian.class)
public class RegistroServiceTest {

	@ArquillianResource
	private URL url;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Tests.createDeployment();
	}

	@Test
	public void cadastroBemSucedidoApenasComCamposObrigatorios() {
		RegistroClient registroClient = Tests.createRegistroClient(this.url);
		PerfilClient perfilClient = Tests.createPerfilClient(this.url);

		String nome = "Cleverson Sacramento";
		String email = "cleverson.sacramento@gmail.com";
		String senha = "segredo";
		Date nascimento = Tests.createDate(1980, 12, 18);
		Sexo sexo = MASCULINO;

		Usuario usuario;

		usuario = new Usuario();
		usuario.setNome(nome);
		usuario.setEmail(email);
		usuario.setSenha(senha);
		usuario.setNascimento(nascimento);
		usuario.setSexo(sexo);
		Long id = registroClient.registrar(usuario);
		assertNotNull(id);

		usuario = perfilClient.obter(id);
		assertNotNull(usuario);
		assertEquals(nome, usuario.getNome());
		assertEquals(email, usuario.getEmail());
		assertEquals(nascimento, usuario.getNascimento());
		assertEquals(sexo, usuario.getSexo());

		registroClient.desregistrar();
		usuario = perfilClient.obter(id);
		assertNull(usuario);
	}

	@Test
	public void falhaAoTentarInserirComCamposObrigatoriosNulos() {
		RegistroClient registroClient = Tests.createRegistroClient(this.url);

		try {
			registroClient.registrar(new Usuario());
			fail("Deveria ter ocorrido erro ao tentar inserir");

		} catch (ClientResponseFailure cause) {
			assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());

			@SuppressWarnings("unchecked")
			List<Violation> validations = (List<Violation>) cause.getResponse().getEntity(
					new GenericType<List<Violation>>() {
					});

			List<Violation> expected = new ArrayList<Violation>();
			expected.add(new Violation("nome", "campo obrigatório"));
			expected.add(new Violation("email", "campo obrigatório"));
			expected.add(new Violation("nascimento", "campo obrigatório"));
			expected.add(new Violation("sexo", "campo obrigatório"));
			expected.add(new Violation("senha", "campo obrigatório"));

			assertEquals(new HashSet<Violation>(expected), new HashSet<Violation>(validations));
		}
	}
}
