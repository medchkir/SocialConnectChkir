package tn.chkir.socialconnect.model;

import java.io.Serializable;

public class Connected implements Serializable{

	public String id;
	public String firstname;
	public String lastname;
	public String image;
	public int socialnetwork;  //1 for facebook; 2 for google;
	
	public Connected(String id, String firstname, String lastname, String image) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.image = image;
	}
}
