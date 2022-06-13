package com.sl.mdb04.utils;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "startPage")
public class StartPage {
	@Id
	private String id = "0";
	private String h1 = "ברוכה הבאה לעולם המנדלות";

	public StartPage() {
	}

	public StartPage(String h1) {
		this.h1 = h1;
	}

	public String getH1() {
		return h1;
	}

	public void setH1(String h1) {
		this.h1 = h1;
	}

	@Override
	public String toString() {
		return "StartPage [h1=" + h1 + "]";
	}

}
