package com.tripPortal.Model;

public abstract class User {

	private int id;
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String email;

	User(int id, String firstName, String lastName, String username, String password, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.email = email;
	}

}