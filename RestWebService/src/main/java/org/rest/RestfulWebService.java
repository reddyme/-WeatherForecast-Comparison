package org.rest;

import java.util.ArrayList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.HashMap;
import java.util.Iterator;

import javax.ws.rs.GET;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import java.util.Date;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.rest.*;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.PathParam;
import com.google.gson.*;
import java.util.Collections;
import java.util.Comparator;

@Path("/weather")
public class RestfulWebService {
	static ReadFile rf = new ReadFile();
	// public static List<Weather>
	// we=rf.readFile("/Users/macpro/Documents/workspace/RestWebService/src/main/resources/daily.csv");
	public static List<Weather> we = rf.readFile("/daily.csv");

	@GET
	@Path("/historical")
	@Produces(MediaType.APPLICATION_JSON)
	public List<WeatherDate> getHistoricalDates() {
		List<WeatherDate> dates = new ArrayList<WeatherDate>();
		Collections.reverse(we);
		Collections.sort(we, new Comparator<Weather>() {
			public int compare(Weather o1, Weather o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		for (Weather w : we) {
			WeatherDate date = new WeatherDate();
			date.setDate(w.getDate());
			dates.add(date);
		}
		// Gson gson=new Gson();
		// String jsonString=gson.toJson(dates);
		// return
		// Response.status(Response.Status.OK).entity(jsonString).build();
		return dates;
	}

	@GET
	@Path("/historical/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getHistoricalRecord(@PathParam("param") String date) {
		List<WeatherDate> dates = new ArrayList<WeatherDate>();
		Weather record = new Weather();
		for (Weather w : we) {
			if (w.getDate().equalsIgnoreCase(date)) {
				record.setDate(w.getDate());
				record.setTmax(w.getTmax());
				record.setTmin(w.getTmin());
				return Response.ok(record).build();
			}
		}
		return Response.status(404).build();
	}

	@GET
	@Path("/forecast/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Weather> getFutureRecords(@PathParam("param") String date) throws ParseException {
		List<Weather> dates = new ArrayList<Weather>();
		boolean countDown = false;
		int countDays = 0;
		List<String> allDates = new ArrayList<String>();
		Map<String,Weather> dateToRecord=new HashMap<String,Weather>();
		List<Weather> pastRecords=new ArrayList<Weather>();
		for (Weather w : we) {
			allDates.add(w.getDate());
			dateToRecord.put(w.getDate(), w);
		}
		
		if (dateToRecord.keySet().contains(date)) {
			for (Weather w : we) {
				if (w.getDate().equalsIgnoreCase(date)) {
					countDown = true;
				}
				if (countDown) {
					Weather record = new Weather();
					record.setDate(w.getDate());
					record.setTmax(w.getTmax());
					record.setTmin(w.getTmin());
					dates.add(record);
					countDays++;
					if (countDays == 7)
						return dates;
				}
			}
		} else {
				CalculateDate cd=new CalculateDate();
				String []futureDates=new String[7];
				futureDates=cd.findFutureDates(date,futureDates);
				Map<String,List<Weather>> finalMap=new HashMap<String,List<Weather>>();
				for(int i=0;i<futureDates.length;i++)
				{
					List<Weather> pastWeatherList=cd.findPastDates(futureDates[i],dateToRecord);
					finalMap.put(futureDates[i], pastWeatherList);
				}
				dates=cd.calculateFinalJson(finalMap,dates);
		}
		
		if(countDays!=0 && countDays<7)
		{
			SimpleDateFormat df=new SimpleDateFormat("yyyyMMdd");
			String followDate="";
			for(int i=dates.size();i<7;i++)
			{ 
				Weather record = new Weather();
				//dates.get(dates.size()).getDate();
				//Find next date
				 Date nextDate=df.parse(dates.get(dates.size()-1).getDate());
				 Calendar calendar=Calendar.getInstance();
				 calendar.setTime(nextDate);
				 calendar.add(Calendar.DAY_OF_YEAR, 1);
				 followDate=df.format(calendar.getTime());
				
				record.setDate(followDate);
				record.setTmax(dates.get(dates.size()-1).getTmax()+1);
				record.setTmin(dates.get(dates.size()-1).getTmin()+1);
				dates.add(record);
			}
		}
		Collections.reverse(dates);
		Collections.sort(dates, new Comparator<Weather>() {
			public int compare(Weather o1, Weather o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		
		return dates;
	}

	@GET
	@Path("/forecast-api/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Weather> getFutureRecordsFromApi(@PathParam("param") String date) throws ParseException, ClientProtocolException, IOException, org.json.simple.parser.ParseException {
		
		List<Weather> dates = new ArrayList<Weather>();
		CalculateDate cd=new CalculateDate();
		String []futureDates=new String[7];
		futureDates=cd.findFutureDates(date,futureDates);
		for(int i=0;i<futureDates.length;i++)
		{
			 DateFormat dfm = new SimpleDateFormat("yyyyMMdd");  
			 long unixtime;
			 dfm.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
			 unixtime = dfm.parse(futureDates[i]).getTime();  
		     unixtime=unixtime/1000;
	
		 HttpClient client = new DefaultHttpClient();
		 HttpGet request = new HttpGet("https://api.darksky.net/forecast/13843dc03fe3bb29b1260f456723467f/37.8267,-122.4233,"+String.valueOf(unixtime));
		  HttpResponse response = client.execute(request);
		  BufferedReader rd = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
		  String line = "";
		  StringBuilder st=new StringBuilder();
		  while ((line = rd.readLine()) != null) {
			  st.append(line);
		  }
		
		 JSONParser parser = new JSONParser(); 
		 JSONObject json = (JSONObject) parser.parse(st.toString());
		 JSONObject jsonDaily = (JSONObject) parser.parse(json.get("daily").toString());
		 JSONArray jsonDailyData = (JSONArray) parser.parse(jsonDaily.get("data").toString());
		 Iterator<JSONObject> iterator = jsonDailyData.iterator();
		 JSONObject finalJson = null;
		 while (iterator.hasNext()) {
			  finalJson = iterator.next();
         }
		 
		 System.out.println(finalJson.get("temperatureMax")+"\t"+finalJson.get("temperatureMin"));
		 
		 Weather we=new Weather();
		 we.setDate(futureDates[i]);
		 we.setTmax(Float.parseFloat(finalJson.get("temperatureMax").toString()));
		 we.setTmin(Float.parseFloat(finalJson.get("temperatureMin").toString()));
		 dates.add(we);
		}
		return dates;
	}
	
	@DELETE
	@Path("/historical/{param}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteRecord(@PathParam("param") String date) {
	//	List<WeatherDate> dates = new ArrayList<WeatherDate>();
	//	Weather record = new Weather();
		for (Weather w : we) {
			if (w.getDate().equalsIgnoreCase(date)) {
				String st = "Record With Date: " + w.getDate() + " Deleted";
				we.remove(w);
				return Response.ok(st).build();
			}
		}
		return Response.status(404).build();
	}

	@POST
	@Path("/historical")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(Weather weather) {
		we.add(weather);
		Collections.reverse(we);
		Collections.sort(we, new Comparator<Weather>() {
			public int compare(Weather o1, Weather o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
		});
		WeatherDate date = new WeatherDate();
		date.setDate(weather.getDate());
		return Response.status(201).entity(date).build();
	}
	
}
