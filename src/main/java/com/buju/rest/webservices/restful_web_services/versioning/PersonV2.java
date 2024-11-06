package com.buju.rest.webservices.restful_web_services.versioning;

import org.springframework.beans.factory.annotation.Autowired;

public class PersonV2 {
	
	@Autowired
	private Name name;

	public PersonV2(Name name) {
		super();
		this.name = name;
	}
	
	public Name getName() {
		return name;
	}

	@Override
	public String toString() {
		return "PersonV2 [name=" + name + "]";
	}
	
	

}
