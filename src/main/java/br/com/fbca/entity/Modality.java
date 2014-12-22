package br.com.fbca.entity;

import static javax.persistence.GenerationType.SEQUENCE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Entity
@JSEntity
public class Modality {

	@Id
	@GeneratedValue(strategy = SEQUENCE)
	private Long id;

	private String name;
	
	public Modality(){}
	
	public Modality(String name){
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
