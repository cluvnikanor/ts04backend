package com.sl.mdb04.utils;

public class PublicUser {
	private String id = "";
	private String name = "";
	private String site = "";
	private String mandalaId = "";
	private int mandalaIndex = -1;

	public PublicUser() {
	}

	public PublicUser(String id, String name, String site) {
		this.id = id;
		this.name = name;
		this.site = site;
	}

	public PublicUser(String id, String name, String site, String mandalaId, int mandalaIndex) {
		super();
		this.id = id;
		this.name = name;
		this.site = site;
		this.mandalaId = mandalaId;
		this.mandalaIndex = mandalaIndex;
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

	@Override
	public String toString() {
		return "PublicUser [id=" + id + ", name=" + name + ", site=" + site + ", mandalaId=" + mandalaId
				+ ", mandalaIndex=" + mandalaIndex + "]";
	}

}
