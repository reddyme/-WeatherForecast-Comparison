package org.rest;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Weather {
	
	private String weatherDate;
	private float tmin;
	private float tmax;
	
	public String getDate() {
		return weatherDate;
	}
	@XmlElement(name="DATE") 
	public void setDate(String weatherDate) {
		this.weatherDate = weatherDate;
	}
	public float getTmin() {
		return tmin;
	}
	@XmlElement(name="TMIN") 
	public void setTmin(float tmin) {
		this.tmin = tmin;
	}
	public float getTmax() {
		return tmax;
	}
	@XmlElement(name="TMAX") 
	public void setTmax(float tmax) {
		this.tmax = tmax;
	}

	public String toString()
	{
		return "Date: "+weatherDate+"Tmax: "+tmax+"Tmin: "+tmin;
	}
}
