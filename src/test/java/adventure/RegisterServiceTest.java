package adventure;

import static adventure.entity.Gender.FEMALE;
import static adventure.entity.Gender.MALE;
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
import adventure.entity.Gender;
import adventure.entity.User;
import adventure.persistence.ValidationException.Violation;
import adventure.rest.service.Registration;

@RunWith(Arquillian.class)
public class RegisterServiceTest {

	@ArquillianResource
	private URL url;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Tests.createDeployment();
	}

	@Test
	public void cadastroBemSucedidoApenasComCamposObrigatorios() {
		RegisterClient registerClient = Tests.createRegisterClient(this.url);
		PerfilClient perfilClient = Tests.createPerfilClient(this.url);

		String fullName = "User Full Name";
		String email = Tests.generateRandomEmail();
		String password = "secret";
		Date birthday = Tests.createDate(1980, 12, 18);
		Gender gender = MALE;

		Registration registration;

		registration = new Registration();
		registration.setFullName(fullName);
		registration.setEmail(email);
		registration.setPassword(password);
		registration.setBirthday(birthday);
		registration.setGender(gender);
		Long id = registerClient.register(registration);
		assertNotNull(id);

		User user = perfilClient.obter(id);
		assertNotNull(user);
		assertEquals(fullName, user.getFullName());
		assertEquals(email, user.getEmail());
		// assertEquals(nascimento, usuario.getNascimento());
		assertEquals(gender, user.getGender());

		registerClient.desregistrar();
		user = perfilClient.obter(id);
		assertNull(user);
	}

	@Test
	public void falhaAoTentarRegistrarComCamposObrigatoriosNulos() {
		RegisterClient registerClient = Tests.createRegisterClient(this.url);

		try {
			registerClient.register(new Registration());
			fail("Deveria ter ocorrido erro ao tentar inserir");

		} catch (ClientResponseFailure cause) {
			assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());

			@SuppressWarnings("unchecked")
			List<Violation> validations = (List<Violation>) cause.getResponse().getEntity(
					new GenericType<List<Violation>>() {
					});

			List<Violation> expected = new ArrayList<Violation>();
			expected.add(new Violation("fullName", "campo obrigatório"));
			expected.add(new Violation("email", "campo obrigatório"));
			expected.add(new Violation("birthday", "campo obrigatório"));
			expected.add(new Violation("gender", "campo obrigatório"));
			expected.add(new Violation("password", "campo obrigatório"));

			assertEquals(new HashSet<Violation>(expected), new HashSet<Violation>(validations));
		}
	}

	@Test
	public void falhaAoTentarRegistrarEmailJaExistente() {
		Registration registration = new Registration();
		registration.setFullName("User Full Name");
		registration.setEmail(Tests.generateRandomEmail());
		registration.setPassword("secret");
		registration.setBirthday(Tests.createDate(1980, 12, 18));
		registration.setGender(FEMALE);

		RegisterClient registerClient = Tests.createRegisterClient(this.url);
		registerClient.register(registration);

		try {
			registerClient.register(registration);
			fail("Deveria ter ocorrido erro ao tentar inserir");

		} catch (ClientResponseFailure cause) {
			assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());

			@SuppressWarnings("unchecked")
			List<Violation> validations = (List<Violation>) cause.getResponse().getEntity(
					new GenericType<List<Violation>>() {
					});

			List<Violation> expected = new ArrayList<Violation>();
			expected.add(new Violation("email", "e-mail já associado a outra conta"));

			assertEquals(new HashSet<Violation>(expected), new HashSet<Violation>(validations));
		}
	}
}