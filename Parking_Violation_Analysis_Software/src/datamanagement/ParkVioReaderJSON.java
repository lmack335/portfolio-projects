package datamanagement;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import data.ParkingViolation;
import logging.Logger;

public class ParkVioReaderJSON implements Reader<ParkingViolation> {
	
	protected String filename;
	protected Logger logger;
	
	public ParkVioReaderJSON(String filename) {
		this.filename = filename;
		logger = Logger.getInstance();
	}
	
	@Override
	public List<ParkingViolation> read() {
		
		//adapted from Assignment 11 starter code
		JSONParser parser = new JSONParser();
		List<ParkingViolation> violations = new ArrayList<ParkingViolation>();
		
		try {
			//log input file opened for reading
			logger.log(System.currentTimeMillis() + " " + filename);
			
			JSONArray violationsJSONArr = (JSONArray) parser.parse(new FileReader(filename));
			
			Iterator iter = violationsJSONArr.iterator();
			 
			 while (iter.hasNext()) {
				 JSONObject violJO = (JSONObject) iter.next();
				 
				 long fine = (long) violJO.get("fine");
				 String state = (String) violJO.get("state");
				 String zip = (String) violJO.get("zip_code");

				 violations.add(new ParkingViolation( (double) fine, state, zip));
			 }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("The parking violation JSON input file does not exist");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("The parking violation JSON input file cannot be read");
			System.exit(-1);
		} catch (ParseException e) {
			e.printStackTrace();
			System.err.println("The parking violation JSON input file cannot be parsed");
			System.exit(-1);
		}
		return violations;
		
	}

}
