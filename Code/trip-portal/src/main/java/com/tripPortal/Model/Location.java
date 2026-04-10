package com.tripPortal.Model;
import java.util.Random;
public abstract class Location {
	private String id;
	private String city;

	public Location(String city) {
		this.id = randomGenerateID();
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String randomGenerateID() {
		String id = "";
		Random rand = new Random();
		for (int i = 0; i < 3; i++) {
			char letter = (char) (rand.nextInt(26) + 'A');
			id += letter;
		}

		// complete verification ...

		return id;

	}
}