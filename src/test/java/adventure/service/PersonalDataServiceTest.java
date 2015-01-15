//package adventure.service;
//
//import static junit.framework.Assert.assertNotNull;
//import static junit.framework.Assert.assertTrue;
//import static org.junit.Assert.fail;
//
//import java.net.URL;
//
//import javax.inject.Inject;
//
//import org.codehaus.jackson.map.ObjectMapper;
//import org.jboss.arquillian.container.test.api.Deployment;
//import org.jboss.arquillian.junit.Arquillian;
//import org.jboss.arquillian.test.api.ArquillianResource;
//import org.jboss.resteasy.client.ClientResponse;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import test.Tests;
//import adventure.client.AuthClient;
//import adventure.entity.Athlete;
//import adventure.entity.User;
//import adventure.rest.service.RegisterService;
//import adventure.security.Credentials;
//import br.gov.frameworkdemoiselle.security.SecurityContext;
//
//@RunWith(Arquillian.class)
//public class PersonalDataServiceTest {
//
//	@ArquillianResource
//	private URL url;
//
//	@Inject
//	SecurityContext securityContext;
//	
//	@Inject
//	RegisterService service;
//
//	@Deployment(testable = false)
//	public static WebArchive createDeployment() {
//		return Tests.createDeployment();
//	}
//
//	@Test
//	public void cadastroBemSucedido() {
//		String email = "urtzi.iglesias@vidaraid.com";
//		String password = "abcde";
//		String rg = "760876109";
//		String cpf = "04680662626";
//		String endereco = "Rua X";
//		String bairro = "Bairro Y";
//		String municipio = "Salvador";
//		String uf = "BA";
//
//		AuthClient authClient = Tests.createAuthClient(this.url);
//
//		Credentials credentials = new Credentials();
//		credentials.setEmail(email);
//		credentials.setPassword(password);
//
//		// http://meera-subbarao.blogspot.com.br/2011/03/resteasy-connection-release-problems.html
//		ClientResponse response = (ClientResponse) authClient.login(credentials);
//		response.releaseConnection();
//
//		User user = authClient.getAuthenticatedUser();
//		assertNotNull(user);
//		
//		Athlete personalData = new Athlete();
//
//		personalData.setUser(user);
//		personalData.setRg(rg);
//		personalData.setCpf(cpf);
//		personalData.setEndereco(endereco);
//		personalData.setBairro(bairro);
//		personalData.setMunicipio(municipio);
//		personalData.setUf(uf);
//
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			System.out.println(mapper.writeValueAsString(personalData));
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
//		
//		try {
//			service.insert(personalData);
//			assertTrue(service.all().size() == 1);
//		} catch (Exception e) {
//			fail("Não está conseguindo verificar se a inserção de dados pessoais.");
//		}
//
//	}
//
//}
