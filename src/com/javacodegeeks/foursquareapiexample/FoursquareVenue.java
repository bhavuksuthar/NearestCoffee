package com.javacodegeeks.foursquareapiexample;


public class FoursquareVenue implements Comparable<FoursquareVenue>{
	private String name;
	private String city;
	private String address;
	private int distance;

	public FoursquareVenue() {
		this.name = "";
		this.city = "";
		this.setAddress("");
	}

	public String getCity() {
		if (city.length() > 0) {
			return city;
		}
		return city;
	}

	public void setCity(String city) {
		this.city= city;
		
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public int getDistance(){
		return distance;
	}
	
	public void setDistance(int distance){
		this.distance = distance;
	}	
	
	@Override
	public int compareTo(FoursquareVenue lhs) {
		
		int compareDistance = ((FoursquareVenue) lhs).getDistance(); 
		 
		//ascending order
		return this.distance - compareDistance;
		
	}
}
