package processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import data.ParkingViolation;
import data.Residence;
import datamanagement.PopnReader;
import datamanagement.PropReader;
import datamanagement.Reader;

public class Processor {
	Reader parkVioReader;
	PopnReader popnReader;
	PropReader propReader;
	
	//variables to store data
	List<ParkingViolation> parkingViolations;
	List<Residence> residences;
	Map<String, Integer> popnByZip;
	
	//variables to store results for memoization
	int totalPopulation = -1;
	Map<String, Double> finesPerZIPCode;
	Map<String, Double> finesPerCapita;
	Map<String, Double> avgMarketValByZIP;
	Map<String, Double> avgLivableAreaByZIP;
	Map<String, Double> totalMarketValPerCapByZIP;
	Map<String, Double> finesTotalMVPerCapByZIP;
	
	public Processor(Reader parkVioReader, PopnReader popnReader, PropReader propReader) {
		this.parkVioReader = parkVioReader;
		this.popnReader = popnReader;
		this.propReader = propReader;
	}
	
	public int totalPopnAllZip() {
		//quick return when value has already been calculated
		if (totalPopulation >= 0) {
			return totalPopulation;
		}
		
		//lazy initialization for popnByZip
		if (popnByZip == null) {
			initPop();
		}
		
		//calculate total population for all zip codes
		for (int n : popnByZip.values()) {
			totalPopulation += n;
		}
		
		//population can't be less than zero
		if (totalPopulation < 0) {
			totalPopulation = 0;
		}
		
		return totalPopulation;
	}
	
	public Map<String, Double> totalFinesPerCapita() {
		//total aggregate fines of a zip code / population of that zip code
		
		//quick return when results have already been calculated
		if (finesPerCapita != null) {
			return finesPerCapita;
		}
		
		//lazy initialization for parkingViolations
		if (parkingViolations == null) {
			initPV();
		}
		
		//find fines by zip code
		finesPerZIPCode = new HashMap<String, Double>();
		
		for (ParkingViolation pv : parkingViolations) {
			
			String zip = pv.getZipCode();
			String state = pv.getPlateState();
			
			//ignore incomplete records
			if (state != null && zip != null) {
				
				//update existing total
				if (state.equals("PA") && finesPerZIPCode.containsKey(zip)) {
					double totalFines = finesPerZIPCode.get(zip) + pv.getFine();
					finesPerZIPCode.put(zip, totalFines);
				}
				//start new total
				else {
					finesPerZIPCode.put(zip, pv.getFine());
				}
			}
		}
		
		//lazy initialization for popnByZip
		if (popnByZip == null) {
			initPop();
		}
		
		//calculate fines per capita in each zip code
		finesPerCapita = new TreeMap<String, Double>();
		
		for (Map.Entry<String, Double> entry : finesPerZIPCode.entrySet()) {
			String zip = entry.getKey();
			
			//only store records that have fines and population > 0
			if (popnByZip.containsKey(zip) && popnByZip.get(zip) > 0) {
				double fine = entry.getValue() / popnByZip.get(zip);
				finesPerCapita.put(zip, fine);
			}
			
		}
		
		return finesPerCapita;
	}
	
	public double avgMarketValInZIP(String zip, int type) {
		//lazy initialization for avgMarketValByZIP
		if (avgMarketValByZIP == null) {
			avgMarketValByZIP = new HashMap<String, Double>();
		}
		//quick return when value has already been calculated
		else if (avgMarketValByZIP.containsKey(zip)) {
			return avgMarketValByZIP.get(zip);
		}
		
		//calculate result using strategy pattern
		double result = avgValInZip(zip, type, new MarketValueAverager());
		avgMarketValByZIP.put(zip, result);
		
		return result;
		
	}
	
	public double avgLivableAreaInZIP(String zip, int type) {
		//lazy initialization for avgLivableAreaByZIP
		if (avgLivableAreaByZIP == null) {
			avgLivableAreaByZIP = new HashMap<String, Double>();
		}
		//quick return when value has already been calculated
		else if (avgLivableAreaByZIP.containsKey(zip)) {
			return avgLivableAreaByZIP.get(zip);
		}
		//calculate result using strategy pattern
		double result = avgValInZip(zip, type, new LivableAreaAverager());
		avgLivableAreaByZIP.put(zip, result);
		
		return result;
	}
	
	
	public double avgValInZip(String desiredZip, int type, Averager avgr) {
		//lazy initialization for residences
		if (residences == null) {
			initRes();
		}
		
		//calculate desired value for each residence
		for (Residence r : residences) {
			String rZIP = r.getZipCode();
			
			//ignore records with missing zip codes
			if (rZIP == null) {
				continue;
			}
			//check that residence is in desired zip code
			else if (rZIP.equals(desiredZip)) {
				double valueToAvg = -1;
				
				if (type == 3) {
					valueToAvg = r.getMarketValue();
				}
				else if (type == 4) {
					valueToAvg = r.getTotalLivableArea();
				}
				
				//ignore records with missing values
				if (valueToAvg < 0) {
					continue;
				}
				else {
					avgr.update(valueToAvg);
				}
			}
		}
		return avgr.average();
	}
	
	public double totalResMarketValPerCapitaByZIP(String desiredZip) {
		//lazy initialization for totalMarketValPerCapByZIP
		if (totalMarketValPerCapByZIP == null) {
			totalMarketValPerCapByZIP = new HashMap<String, Double>();
		}
		//quick return when value has already been calculated
		else if (totalMarketValPerCapByZIP.containsKey(desiredZip)) {
			return totalMarketValPerCapByZIP.get(desiredZip);
		}
		
		//find total population of desired zipcode
		int totalPop = -1;
		
		if (popnByZip == null) {
			initPop();
		}
		
		if (popnByZip.containsKey(desiredZip)) {
			totalPop = popnByZip.get(desiredZip);
			
			//return 0 if total population of that zip code is 0
			if (totalPop <= 0) {
				totalMarketValPerCapByZIP.put(desiredZip, 0.0);
				return 0.0;
			}
		}
		//return 0 if that zip code is not listed in the population input file
		else {
			totalMarketValPerCapByZIP.put(desiredZip, 0.0);
			return 0.0;
		}
		
		
		//find total market value for all residences in the desired zipcode
		if (residences == null) {
			initRes();
		}
		
		double totalResMarketVal = -1;
		
		for (Residence r : residences) {
			String rZIP = r.getZipCode();
			double marketVal = r.getMarketValue();
			
			//ignore records with missing information
			if (rZIP == null || marketVal < 0) {
				continue;
			}
			else if (rZIP.equals(desiredZip)) {
				totalResMarketVal += marketVal;
			}
		}
		
		//handle total residential market value is <= 0
		if (totalResMarketVal <= 0) {
			totalMarketValPerCapByZIP.put(desiredZip, 0.0);
			return 0.0;
		}
		
		//calculate & store result
		double result = totalResMarketVal / (double) totalPop;
		
		totalMarketValPerCapByZIP.put(desiredZip, result);
		
		return result;
	}
	
	
	
	public double finesPerCapitaPercentageTotalMV(String desiredZip) {
		//lazy initialization for finesTotalMVPerCapByZIP
		if (finesTotalMVPerCapByZIP == null) {
			finesTotalMVPerCapByZIP = new HashMap<String, Double>();
		}
		//quick return when value has already been calculated
		else if (finesTotalMVPerCapByZIP.containsKey(desiredZip)) {
			return finesTotalMVPerCapByZIP.get(desiredZip);
		}
		
		//get fines in the zip code (Data Set #1 - Parking Violations)
		if (finesPerZIPCode == null) {
			totalFinesPerCapita();
		}
		
		double fines = -1;
		
		if (finesPerZIPCode.containsKey(desiredZip)) {
			fines = finesPerZIPCode.get(desiredZip);
		}
		
		if (fines <= 0) {
			finesTotalMVPerCapByZIP.put(desiredZip, 0.0);
			return 0;
		}
		
		//get total market value (Data Set #2 - Property Values) per capita (Data Set #3 - Population) of the zip code
		double totalMV = totalResMarketValPerCapitaByZIP(desiredZip);
		
		if (totalMV <= 0) {
			finesTotalMVPerCapByZIP.put(desiredZip, 0.0);
			return 0;
		}
		
		//calculate & store result
		double result = fines / totalMV;

		finesTotalMVPerCapByZIP.put(desiredZip, result);

		return result;
	}
	
	public void initPV() {
		parkingViolations = parkVioReader.read();
	}
	
	public void initRes() {
		residences = propReader.read();
	}
	
	public void initPop() {
		popnByZip = popnReader.read();
	}
}
