///*
// * Demoiselle Framework
// * Copyright (C) 2010 SERPRO
// * ----------------------------------------------------------------------------
// * This file is part of Demoiselle Framework.
// * 
// * Demoiselle Framework is free software; you can redistribute it and/or
// * modify it under the terms of the GNU Lesser General Public License version 3
// * as published by the Free Software Foundation.
// * 
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// * 
// * You should have received a copy of the GNU Lesser General Public License version 3
// * along with this program; if not,  see <http://www.gnu.org/licenses/>
// * or write to the Free Software Foundation, Inc., 51 Franklin Street,
// * Fifth Floor, Boston, MA  02110-1301, USA.
// * ----------------------------------------------------------------------------
// * Este arquivo é parte do Framework Demoiselle.
// * 
// * O Framework Demoiselle é um software livre; você pode redistribuí-lo e/ou
// * modificá-lo dentro dos termos da GNU LGPL versão 3 como publicada pela Fundação
// * do Software Livre (FSF).
// * 
// * Este programa é distribuído na esperança que possa ser útil, mas SEM NENHUMA
// * GARANTIA; sem uma garantia implícita de ADEQUAÇÃO a qualquer MERCADO ou
// * APLICAÇÃO EM PARTICULAR. Veja a Licença Pública Geral GNU/LGPL em português
// * para maiores detalhes.
// * 
// * Você deve ter recebido uma cópia da GNU LGPL versão 3, sob o título
// * "LICENCA.txt", junto com esse programa. Se não, acesse <http://www.gnu.org/licenses/>
// * ou escreva para a Fundação do Software Livre (FSF) Inc.,
// * 51 Franklin St, Fifth Floor, Boston, MA 02111-1301, USA.
// */
//package test;
//
//import java.io.File;
//import java.net.URL;
//import java.util.Calendar;
//import java.util.Date;
//
//import org.apache.commons.lang.RandomStringUtils;
//import org.jboss.resteasy.client.ProxyFactory;
//import org.jboss.resteasy.spi.ResteasyProviderFactory;
//import org.jboss.shrinkwrap.api.ShrinkWrap;
//import org.jboss.shrinkwrap.api.spec.WebArchive;
//import org.jboss.shrinkwrap.resolver.api.maven.Maven;
//import org.junit.Ignore;
//
//import temp.client.AuthClient;
//import temp.client.ProfileClient;
//import temp.client.SignUpClient;
//
//@Ignore
//public final class Tests {
//
//	{
//		ResteasyProviderFactory.setRegistrationBuiltinByDefault(false);
//		ResteasyProviderFactory.getInstance().registrationProvider(JacksonJsonProvider.class);
//	}
//
//	private Tests() {
//	}
//
//	public static WebArchive createDeployment() {
//		File[] libs = Maven.resolver().offline().loadPomFromFile("pom.xml", "arquillian-test")
//				.importCompileAndRuntimeDependencies().resolve().withTransitivity().asFile();
//
//		return ShrinkWrap.create(WebArchive.class)
//				.addPackages(true, "temp.entity")
//				.addPackages(true, "temp.persistence")
//				.addPackages(true, "temp.rest")
//				.addPackages(true, "temp.security")
//				.addPackages(true, "temp.validator")
//				.addAsWebInfResource(new File("src/main/webapp/WEB-INF/beans.xml"))
//				.addAsWebInfResource(new File("src/main/webapp/WEB-INF/urlrewrite.xml"))
//				.addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"))
//				.addAsWebInfResource(new File("src/main/webapp/WEB-INF/jboss-deployment-structure.xml"))
//				.addAsResource(new File("src/main/resources/META-INF/persistence.xml"), "META-INF/persistence.xml")
//				.addAsResource(new File("src/main/resources/demoiselle.properties"))
//				.addAsResource(new File("src/main/resources/messages.properties"))
//				.addAsResource(new File("src/main/resources/ValidationMessages.properties"))
//				.addAsLibraries(libs);
//	}
//
//	public static SignUpClient createSignUpClient(URL base) {
//		return ProxyFactory.create(SignUpClient.class, base.toString());
//	}
//
//	public static ProfileClient createPerfilClient(URL base) {
//		return ProxyFactory.create(ProfileClient.class, base.toString());
//	}
//	
//	public static AuthClient createAuthClient(URL base) {
//		return ProxyFactory.create(AuthClient.class, base.toString());
//	}
//	
//
//	public static Date createDate(int year, int month, int day) {
//		Calendar calendar = Calendar.getInstance();
//		calendar.set(year, month - 1, day);
//		calendar.set(Calendar.HOUR_OF_DAY, 0);
//		calendar.set(Calendar.MINUTE, 0);
//		calendar.set(Calendar.SECOND, 0);
//		calendar.set(Calendar.MILLISECOND, 0);
//
//		return calendar.getTime();
//	}
//
//	public static String generateRandomEmail() {
//		return RandomStringUtils.randomAlphabetic(10) + "@" + RandomStringUtils.randomAlphabetic(10) + ".com";
//	}
//}
