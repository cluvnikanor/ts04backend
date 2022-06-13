package com.sl.mdb04.utils;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
	@Id
	private String id;

	private String name = "";
	private String site = "";
	private String phone = "";
	private String email = "";
	private String password = "";
	private String mandalaId = "";
	private int mandalaIndex = -1;

	public User() {
	}

	public User(String id, String name, String site, String phone, String email, String password) {
		this.id = id;
		this.name = name;
		this.site = site;
		this.phone = phone;
		this.email = email;
		this.password = password;
	}
	
	public User(DeletedUser deletedUser) {
		this.id = deletedUser.getId();
		this.name = deletedUser.getName();
		this.site = deletedUser.getSite();
		this.phone = deletedUser.getPhone();
		this.email = deletedUser.getEmail();
	}

	public User(String id, String name, String site, String phone, String email, String password, String mandalaId,
			int mandalaIndex) {
		this.id = id;
		this.name = name;
		this.site = site;
		this.phone = phone;
		this.email = email;
		this.password = password;
		this.mandalaId = mandalaId;
		this.mandalaIndex = mandalaIndex;
	}

	public PublicUser publicCasting() {
		return new PublicUser(id, name, site, mandalaId, mandalaIndex);
	}

	public DeletedUser deletedCasting() {
		return new DeletedUser(id, name, site, email, phone);
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMandalaId() {
		return mandalaId;
	}

	public void setMandalaId(String mandalaId) {
		this.mandalaId = mandalaId;
	}

	public int getMandalaIndex() {
		return mandalaIndex;
	}

	public void setMandalaIndex(int roll) {
		this.mandalaIndex = roll;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", site=" + site + ", email=" + email + ", phone=" + phone
				+ ", password=" + password + ", mandalaId=" + mandalaId + ", mandalaIndex=" + mandalaIndex + "]";
	}

}
