package com.revature.models;

import java.util.Objects;

public class Location {
	
	private String street;
	private String city;
	private State state;
	private String zipcode;
	
	
	public Location(String street, String city, State state, String zipcode) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}


	public String getStreet() {
		return street;
	}


	public void setStreet(String street) {
		this.street = street;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public State getState() {
		return state;
	}


	public void setState(State state) {
		this.state = state;
	}


	public String getZipcode() {
		return zipcode;
	}


	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}


	@Override
	public int hashCode() {
		return Objects.hash(city, state, street, zipcode);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(city, other.city) && state == other.state && Objects.equals(street, other.street)
				&& Objects.equals(zipcode, other.zipcode);
	}


	@Override
	public String toString() {
		return "Location [street=" + street + ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + "]";
	}

}
