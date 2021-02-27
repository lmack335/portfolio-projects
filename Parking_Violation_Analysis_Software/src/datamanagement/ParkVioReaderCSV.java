package datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import data.ParkingViolation;
import logging.Logger;

public class ParkVioReaderCSV implements Reader<ParkingViolation> {
	
	protected String filename;
	protected Logger logger;
	
	public ParkVioReaderCSV(String filename) {
		this.filename = filename;
		logger = Logger.getInstance();
	}
	
	public List<ParkingViolation> read() {
		
		List<ParkingViolation> violations = new ArrayList<ParkingViolation>();
		
		try {
			//log input file opened for reading
			logger.log(System.currentTimeMillis() + " " + filename);
			
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			String[] temp;
					
			while ((line = br.readLine()) != null) {
				temp = line.split(",");
				double fine = 0;
				String state = null;
				String zip = null;
				
				if (temp.length >= 2) {
					fine = Double.parseDouble(temp[1]);
					
					if (temp.length >= 5) {
						state = temp[4];
						
						if (temp.length >= 7) {
							zip = temp[6];
						}
					}
				
					violations.add(new ParkingViolation(fine, state, zip));
				
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
		return violations;
	}
}
