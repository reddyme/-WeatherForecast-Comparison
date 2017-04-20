package org.rest;

import java.util.*;
public class Test {

	public static void main(String[] args) {
		ReadFile rf=new ReadFile();
		List<Weather> we=rf.readFile("/Users/macpro/Documents/workspace/RestWebService/src/main/resources/daily.csv");
		
		for(Weather w:we)
		{
			System.out.print(w.getDate()+"\t"+w.getTmax()+"\t"+w.getTmin());
			System.out.println();
		}
	}

}
