package com.sl.mdb04.utils;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "deletedUsers")
public class DeletedUser {
	@Id
	private String id;

	private String name;
	private String site;
	private String email;
	private String phone;

	public DeletedUser() {
	}

	public DeletedUser(String id, String name, String site, String email, String phone) {
		this.id = id;
		this.name = name;
		this.site = site;
		this.email = email;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "SavedUser [id=" + id + ", name=" + name + ", site=" + site + ", email=" + email + ", phone=" + phone
				+ "]";
	}

}
