package com.sl.mdb04.utils;

import java.util.Arrays;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mandalas")
public class Mandala {
	@Id
	private String id;

	private PublicUser[] publicUsers = new PublicUser[15];
	private int userQuantity = 0;
	private Date timeOut = null;

	public Mandala() {
		Arrays.fill(publicUsers, new PublicUser());
	}

	public Mandala(String id, PublicUser[] publicUsers, Date timeOut) {
		this.id = id;
		this.publicUsers = publicUsers;
		this.timeOut = timeOut;
	}

	public void addUser(PublicUser publicUser, int index) {
		if (index > -1 && index < 15) {
			this.publicUsers[index] = publicUser;
			this.userQuantity++;
		}
	}

	public void removeUser(int index) {
		if (index > -1 && index < 15) {
			this.publicUsers[index] = new PublicUser();
			this.userQuantity--;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public PublicUser[] getPublicUsers() {
		return publicUsers;
	}

	public void setPublicUsers(PublicUser[] publicUsers) {
		this.publicUsers = publicUsers;
	}

	public int getUserQuantity() {
		return userQuantity;
	}

	public void setUserQuantity(int userQuantity) {
		this.userQuantity = userQuantity;
	}

	public Date getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Date timeOut) {
		this.timeOut = timeOut;
	}

	@Override
	public String toString() {
		return "Mandala [id=" + id + ", publicUsers=" + Arrays.toString(publicUsers) + ", userQuantity=" + userQuantity
				+ ", timeOut=" + timeOut + "]";
	}

}
