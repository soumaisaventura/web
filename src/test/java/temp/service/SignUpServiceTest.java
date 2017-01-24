//package adventure.service;
//
//import static temp.entity.Gender.FEMALE;
//import static temp.entity.Gender.MALE;
//import static javax.servlet.http.HttpServletResponse.SC_PRECONDITION_FAILED;
//import static junit.framework.Assert.assertEquals;
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertNull;
//import static org.junit.Assert.fail;
//
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.test.api.ArquillianResource;
//import org.jboss.resteasy.client.ClientResponseFailure;
//import org.jboss.resteasy.util.GenericType;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import test.Tests;
//import temp.client.ProfileClient;
//import temp.client.SignUpClient;
//import temp.entity.Gender;
//import temp.entity.User;
//import temp.service.SignUpService.SignUpData;
//import br.gov.frameworkdemoiselle.HttpViolationException.Violation;
//
//@RunWith(Arquillian.class)
//public class SignUpServiceTest {
//
//	@ArquillianResource
//	private URL url;
//
//	@Deployment(testable = false)
//	public static WebArchive createDeployment() {
//		return Tests.createDeployment();
//	}
//
//	@Test
//	public void cadastroBemSucedidoApenasComCamposObrigatorios() {
//		SignUpClient signUpClient = Tests.createSignUpClient(this.url);
//		ProfileClient profileClient = Tests.createPerfilClient(this.url);
//
//		String fullName = "User Full Name";
//		String email = Tests.generateRandomEmail();
//		String password = "secret";
//		Date birthday = Tests.createDate(1980, 12, 18);
//		Gender gender = MALE;
//
//		SignUpData signUpData;
//
//		signUpData = new SignUpData();
//		signUpData.name = fullName;
//		signUpData.email = email;
//		signUpData.password = password;
//		signUpData.birthday = birthday;
//		signUpData.gender = gender;
//		Long id = signUpClient.signup(signUpData);
//		assertNotNull(id);
//
//		User user = profileClient.load(id);
//		assertNotNull(user);
//		assertEquals(fullName, user.getName());
//		assertEquals(email, user.getEmail());
//		// assertEquals(nascimento, usuario.getNascimento());
//		assertEquals(gender, user.getGender());
//
//		signUpClient.quit();
//		user = profileClient.load(id);
//		assertNull(user);
//	}
//
//	@Test
//	public void falhaAoTentarRegistrarComCamposObrigatoriosNulos() {
//		SignUpClient SignUpClient = Tests.createSignUpClient(this.url);
//
//		try {
//			SignUpClient.signup(new SignUpData());
//			fail("Deveria ter ocorrido erro ao tentar inserir");
//
//		} catch (ClientResponseFailure cause) {
//			assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());
//
//			@SuppressWarnings("unchecked")
//			List<Violation> validations = (List<Violation>) cause.getResponse().getEntity(
//					new GenericType<List<Violation>>() {
//					});
//
//			List<Violation> expected = new ArrayList<Violation>();
//			expected.add(new Violation("fullName", "campo obrigatório"));
//			expected.add(new Violation("email", "campo obrigatório"));
//			expected.add(new Violation("birthday", "campo obrigatório"));
//			expected.add(new Violation("gender", "campo obrigatório"));
//			expected.add(new Violation("password", "campo obrigatório"));
//
//			assertEquals(new HashSet<Violation>(expected), new HashSet<Violation>(validations));
//		}
//	}
//
//	@Test
//	public void falhaAoTentarRegistrarEmailJaExistente() {
//		SignUpData signUpForm = new SignUpData();
//		signUpForm.name = "User Full Name";
//		signUpForm.email = Tests.generateRandomEmail();
//		signUpForm.password = "secret";
//		signUpForm.birthday = Tests.createDate(1980, 12, 18);
//		signUpForm.gender = FEMALE;
//
//		SignUpClient SignUpClient = Tests.createSignUpClient(this.url);
//		SignUpClient.signup(signUpForm);
//
//		try {
//			SignUpClient.signup(signUpForm);
//			fail("Deveria ter ocorrido erro ao tentar inserir");
//
//		} catch (ClientResponseFailure cause) {
//			assertEquals(SC_PRECONDITION_FAILED, cause.getResponse().getStatus());
//
//			@SuppressWarnings("unchecked")
//			List<Violation> validations = (List<Violation>) cause.getResponse().getEntity(
//					new GenericType<List<Violation>>() {
//					});
//
//			List<Violation> expected = new ArrayList<Violation>();
//			expected.add(new Violation("email", "e-mail já associado a outra conta"));
//
//			assertEquals(new HashSet<Violation>(expected), new HashSet<Violation>(validations));
//		}
//	}
//}
