package adventure.entity;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.google.api.services.oauth2.model.Userinfo;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	@NotEmpty
	private String fullName;

	@Email
	@NotEmpty
	@Column(unique = true)
	private String email;

	@NotEmpty
	private String password;

	@Past
//	@NotNull
	private Date birthday;

	@NotNull
	@Enumerated(STRING)
	private Gender gender;

	public User() {
	}

	public User(br.gov.frameworkdemoiselle.security.User user) {
		this.id = (Long) user.getAttribute("id");
		this.email = user.getId();
	}

	public User(Userinfo userInfo) {
		this.fullName = userInfo.getName();
		this.email = userInfo.getEmail();

		if (userInfo.getGender() != null) {
			this.gender = Gender.valueOf(userInfo.getGender().toUpperCase());
		}
	}

	public br.gov.frameworkdemoiselle.security.User parse() {
		br.gov.frameworkdemoiselle.security.User user = new br.gov.frameworkdemoiselle.security.User() {

			private static final long serialVersionUID = 1L;

			private Map<Object, Object> attrs = new HashMap<Object, Object>();

			@Override
			public String getId() {
				return fullName;
			}

			@Override
			public Object getAttribute(Object key) {
				return this.attrs.get(key);
			}

			@Override
			public void setAttribute(Object key, Object value) {
				this.attrs.put(key, value);
			}

			@Override
			public String toString() {
				return this.getId();
			}
		};
		user.setAttribute("id", this.id);

		return user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}
