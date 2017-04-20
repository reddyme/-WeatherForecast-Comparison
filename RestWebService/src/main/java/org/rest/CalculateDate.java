package org.rest;
import java.text.*;
import java.util.*;

public class CalculateDate {
	
	public String[] findFutureDates(String givenDate, String[] futureDates) throws ParseException {
		
		 SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		 for(int i=0;i<futureDates.length;i++)
		 {
		 futureDates[i]=givenDate;
		 Date date=df.parse(givenDate);
		 Calendar calendar=Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.DAY_OF_YEAR, 1);
		 givenDate=df.format(calendar.getTime());
		 }
		return futureDates;
	}

	public List<Weather> findPastDates(String stringDate,Map<String, Weather> dateToRecord) throws ParseException {
		Weather we=new Weather();
		 SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
		 List<Weather> weather=new ArrayList<Weather>();
		//checking last 100 years records
		for(int i=0;i<100;i++)
		{
			 Date date=df.parse(stringDate);
			 Calendar calendar=Calendar.getInstance();
			 calendar.setTime(date);
			 calendar.add(Calendar.YEAR, -1);
			 stringDate=df.format(calendar.getTime());
			 if(dateToRecord.keySet().contains(stringDate))
				 weather.add(dateToRecord.get(stringDate));
		}
		return weather;
	}

	public List<Weather> calculateFinalJson(Map<String,List<Weather>> finalMap,List<Weather> dates) {
		for(Map.Entry<String, List<Weather>> entry:finalMap.entrySet())
		{
			Weather we=new Weather();
			we.setDate(entry.getKey());
			we.setTmax(findFinalMaxTemp(entry.getValue()));
			we.setTmin(findFinalMinTemp(entry.getValue()));
			dates.add(we);
		}
		return dates;
	}

	private float findFinalMinTemp(List<Weather> value) {
		// TODO Auto-generated method stub
		float totalMinTemp=0;
		for(Weather we:value)
		{
			totalMinTemp=totalMinTemp+we.getTmin();
		}
		return totalMinTemp/value.size();
	}

	private float findFinalMaxTemp(List<Weather> value) {
		float totalMaxTemp=0;
		for(Weather we:value)
		{
			totalMaxTemp=totalMaxTemp+we.getTmax();
		}
		return totalMaxTemp/value.size();
	}


}
