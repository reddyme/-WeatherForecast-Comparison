package org.rest;
import java.util.*;
import java.io.*;
import org.rest.Weather;

public class ReadFile {
	public List<Weather> readFile(String filePath)
	{
		List<Weather> list=new ArrayList<Weather>();
		try {
			//BufferedReader br=new BufferedReader(new FileReader(filePath));
			BufferedReader br=new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filePath)));
			String st=br.readLine();
			
			while(st!=null)
			{
				Weather wea=new Weather();
				String att[]=st.split(",");
				wea.setDate(att[0]);
				wea.setTmax(Float.parseFloat(att[1]));
				wea.setTmin(Float.parseFloat(att[2]));
				list.add(wea);
				st=br.readLine();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}

