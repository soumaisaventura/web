package adventure.security;

import java.security.Principal;

public class User implements Principal {

	private Long id;

	private String name;

	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}
}
