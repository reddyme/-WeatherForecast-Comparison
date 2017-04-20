package org.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

 class Timeconversion
{
    DateFormat dfm = new SimpleDateFormat("yyyyMMdd");  

    long unixtime;
    public long timeConversion(String time)
    {
    dfm.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));//Specify your timezone 
    try
    {
        unixtime = dfm.parse(time).getTime();  
        unixtime=unixtime/1000;
    } 
    catch (ParseException e) 
    {
        e.printStackTrace();
    }
    return unixtime;
    }

}


 class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Timeconversion timeconversion=new Timeconversion();
		System.out.println(timeconversion.timeConversion("20130104"));
	}

}
