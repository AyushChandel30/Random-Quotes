package com.example.binding;

import lombok.Data;

@Data
public class Quote { //this class represent one quote
	//we are getting multiple quotes in json array format.
	
	private String text;
	private String author;
	//variable names should match with the json keys provided by 3rd party api
//https://type.fit/api/quotes

}
