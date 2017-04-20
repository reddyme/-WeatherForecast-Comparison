package org.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class WeatherDate {
	private String DATE;

	public String getDate() {
		return DATE;
	}
	@XmlElement(name="DATE") 
	public void setDate(String date) {
		this.DATE = date;
	}
	
	@Override
	public String toString()
	{
		return "DATE:"+DATE;
	}
	
}
