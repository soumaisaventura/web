package adventure;

import java.net.URL;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.Tests;

// Os testes não estão funcionando pois o Resteasy não está funcionando no Glassfish Embedded
//@Ignore
@RunWith(Arquillian.class)
public class MyTest {

	@ArquillianResource
	private URL url;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Tests.createDeployment();
	}

	@Test
	public void x() {
		 ResteasyProviderFactory.setRegisterBuiltinByDefault(false);
		 ResteasyProviderFactory.getInstance().registerProvider(JacksonJsonProvider.class);

		System.out.println(url.toString());

		AtletaServiceClient client = ProxyFactory.create(AtletaServiceClient.class, url.toString());
		System.out.println(client.search());
	}
}
