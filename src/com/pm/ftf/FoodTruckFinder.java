package com.pm.ftf;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


public class FoodTruckFinder {
	
	public static void main(String[] args) {

		//Using time as constraint requires using $query param as '>=' not accepted in simple $where param
		final String SVC_URL = "https://data.sfgov.org/resource/bbb8-hzi6.json?$query=";
		
		//No way to use $limit or $offset when using $query - either in $query OR as $param
		String q = "select applicant, location where dayorder=%d and (start24<='%s' and '%s'<=end24) order by applicant";

		StringBuilder result = new StringBuilder();
		List<Truck> listTrucks = new ArrayList<Truck>();
		LocalDateTime timePoint = LocalDateTime.now(); 
		DateTimeFormatter format24Time = DateTimeFormatter.ofPattern("HH:mm");

		String query = String.format(q, timePoint.getDayOfWeek().getValue(), timePoint.format(format24Time), timePoint.format(format24Time));


   		try {

   			//URLEncoder encodes space as '+' and socrata doesn't like so replace with '%20'
   			URL url = new URL(SVC_URL + URLEncoder.encode(query, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20"));

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			rd.close();
			
			//Jackson ObjectMapper to fill collection from returned JSON payload
			ObjectMapper objectMapper = new ObjectMapper();
			listTrucks = objectMapper.readValue(result.toString(), new TypeReference<List<Truck>>() {});
			display(listTrucks);
			
		} catch (Exception e) {
			System.out.println("System Error occured. Details : " + e.getMessage());
		}			
			

	}
	
	
	public static void display(List<Truck> listTrucks) throws IOException {
		
		//Accept input from command line
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	    String s;
	    int offset = 0;
	    int limit = 9;
	    int start = 0;
	    
	    do {
		      System.out.println(String.format("%-50s |", "NAME") + String.format("%-30s", "ADDRESS"));
		      start = offset;
		      for (int i=start; i<= start+limit; i++) {
		    	 
		    	  if (listTrucks.get(i).getApplicant().length() > 50) {
		    		  System.out.println(String.format("%-80s", listTrucks.get(i).getApplicant()));
		    		  System.out.println(String.format("%-50s |", "") + String.format(" %-30s", listTrucks.get(i).getLocation()));
		    	  }else
		    		  System.out.println(String.format("%-50s |", listTrucks.get(i).getApplicant()) 
		    			  			+ String.format(" %-30s", listTrucks.get(i).getLocation())); 
		    	 
		    	  if (offset < listTrucks.size() -1 )
		    		  offset++;
		    	  else
		    		  System.exit(0);
		    	  
		      }		      
		      System.out.println(String.format("%-40s", "Hit Return to view more trucks, or enter anything else to exit : "));
		}while ((s = in.readLine()) != null && s.length() == 0) ;
		
	}
}


