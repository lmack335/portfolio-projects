package datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import logging.Logger;

public class PopnReader {
	
	protected String filename;
	protected Logger logger;
	
	public PopnReader(String filename) {
		this.filename = filename;
		logger = Logger.getInstance();
	}
	
	
	public Map<String, Integer> read() {
		
		Map<String, Integer> popnByZip = new HashMap<String, Integer>();
		
		try {
			//log input file opened for reading
			logger.log(System.currentTimeMillis() + " " + filename);

			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line = "";
			String[] temp;
			
			while ((line = br.readLine()) != null) {
				temp = line.split(" ");
				if (temp.length == 2) {
					String zip = temp[0];
					int pop = Integer.parseInt(temp[1]); 
				
					popnByZip.put(zip, pop);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("The states input file does not exist");
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("The states input file cannot be read");
			System.exit(-1);
		}
		
		return popnByZip;
	}
}
