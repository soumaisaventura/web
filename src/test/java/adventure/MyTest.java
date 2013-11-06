package adventure;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.Tests;

@RunWith(Arquillian.class)
public class MyTest {

	// private static final String PATH = "src/test/resources/producer/response";

	@ArquillianResource
	private URL deploymentUrl;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return Tests.createDeployment();
	}

	@Test
	public void x() {

	}

}
