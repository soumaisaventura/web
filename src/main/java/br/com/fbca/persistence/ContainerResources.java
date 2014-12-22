package br.com.fbca.persistence;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.mail.Session;

import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

@Singleton
public class ContainerResources {

	@Resource(mappedName = "java:/mail/Adventure")
	private Session session;

	@Resource(mappedName = "java:/infinispan/Adventure")
	private CacheContainer container;

	public Session getSession() {
		return this.session;
	}

	public Cache<String, String> getPasswordResetCache() {
		return this.container.getCache("password-reset");
	}
}
