package datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.Residence;
import logging.Logger;

public class PropReader {
	protected String filename;
	protected Logger logger;
	
	public PropReader(String filename) {
		this.filename = filename;
		logger = Logger.getInstance();
	}
	
	public List<Residence> read() {
		
		ArrayList<Residence> residences = new ArrayList<Residence>();
		
		try {
			//log input file opened for reading
			logger.log(System.currentTimeMillis() + " " + filename);
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			
			//use the header row to locate desired fields: market_value, total_livable_area, and zip_code 
			String line = br.readLine();
			String[] temp = line.split(",");
			
			HashMap<String, Integer> valuePositions = new HashMap<String, Integer>();
			
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].equals("market_value")) {
					valuePositions.put("market_value", i);
				}
				else if (temp[i].equals("total_livable_area")) {
					valuePositions.put("total_livable_area", i);
				}
				else if (temp[i].equals("zip_code")) {
					valuePositions.put("zip_code", i);
				}
			}
			
			//parse records, checking for fields in quotes that contain commas
			while ((line = br.readLine()) != null) {
			
				temp = line.split(",");
				boolean lookingForEndQuote = false;
				ArrayList<String> residenceRecord = new ArrayList<String>();
				String fieldBuilder = "";
				
				for (int i = 0; i < temp.length; i++) {
					
					if (lookingForEndQuote) {
						
						if (temp[i].contains("\"")) {
							lookingForEndQuote = false;
							fieldBuilder += temp[i];
							residenceRecord.add("fieldBuilder complete: " + fieldBuilder);
							fieldBuilder = "";
						}
						else {
							fieldBuilder += (temp[i] + ",");
						}
					}
					else {
						if (temp[i].contains("\"")) {
							
							int count = 0;
							for (char c : temp[i].toCharArray()) {
								if (c == '"') {
									count++;
								}
							}
							
							if (count%2 == 0) {
								residenceRecord.add(temp[i]);
							}
							else {
								lookingForEndQuote = true;
								fieldBuilder += (temp[i] + ",");
							}
						}
						else {
							residenceRecord.add(temp[i]);
						}
					}
				}
				
				//handle records with incorrect/missing inputs
				String s = null;
				double mv = -1;
				double la = -1;
				String zip = null;
				
				zip = residenceRecord.get(valuePositions.get("zip_code"));
				
				//truncate zip code to first five characters
				if (zip.length() > 5) {
					zip = zip.substring(0,5);
				}
				
				//ignore record if invalid zip code
				if (zip.length() < 5) {
					continue;
				}
				
				for (int i = 0; i < 5; i++) {
					if (!Character.isDigit(zip.charAt(i))) {
						continue;
					}
				}
				
				//parse market value & livable area
				s = residenceRecord.get(valuePositions.get("market_value"));
				if (!s.isEmpty()) {
					mv = Double.parseDouble(s);
				}
				
				s = residenceRecord.get(valuePositions.get("total_livable_area"));
				if (!s.isEmpty()) {
					la = Double.parseDouble(s);
				}
				
				//ignore record if both market value & livable area are invalid
				if (mv < 0 && la < 0) {
					continue;
				}
				else {
					residences.add(new Residence(mv, la, zip));
				}
				
			}
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("The parking violation CSV input file does not exist");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("The parking violation CSV input file cannot be read");
			System.exit(-1);
		}
		
		return residences;
	}
}
