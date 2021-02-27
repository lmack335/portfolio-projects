package ui;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map;
import java.util.Scanner;

import logging.Logger;
import processor.Processor;

public class CommandLineUserInterface {
	//adapted from 594 "Monolith vs Modularity" notes
	
	protected Processor processor;
	protected Scanner in;
	protected Logger logger;
	
	public CommandLineUserInterface(Processor processor) {
		this.processor = processor;
		in = new Scanner(System.in);
		logger = Logger.getInstance();
	}
	
	
	public void start() {
			
		int choice = 0;
		
		while (true) {
			System.out.println("Enter 0 to exit the program,"
				+ " 1 to show the total population for all ZIP Codes,"
				+ " 2 to show the total parking fines per capita for each ZIP Code,"
				+ " 3 to show the average market value for residences in a specified ZIP Code,"
				+ " 4 to show the average total livable area for residences in a specified ZIP Code,"
				+ " 5 to show the total residential market value per capita for a specified ZIP Code,"
				+ " or 6 to show the total parking fines as a percentage of the average residential market value per capita for a specified ZIP Code.");  //IN PROGRESS
			System.out.print("Enter an integer between 0-6: ");
		
			choice = in.nextInt();
			
			//log user's choice
			logger.log(System.currentTimeMillis() + " " + choice);
			
			if (choice == 0) {
				//exit the program
				System.exit(0);
			}
			else if (choice == 1) {
				//show the total population for all ZIP Codes
				doTotalPopAllZIPCodes();
			}
			else if (choice == 2) {
				//show the total parking fines per capita for each ZIP Code
				doTotalFinesPerCapitaByZIP();
			}
			else if (choice == 3 || choice == 4 || choice == 5) {
				//show the average market value (3), average total livable area (4),
				//  total residential market value per capita (5) in a specified ZIP Code

				doValueByZIP(choice);
			}
			
			else if (choice == 6) {
				//the total parking fines as a percentage of the total residential market value per capita for a specified ZIP Code.
				doPercentageByZIP();
			}
			else {
				System.err.println("You must enter an integer between 0-6.");
				System.exit(-1);
			}
		}
	}
	
	protected void doTotalPopAllZIPCodes() {
		System.out.println(processor.totalPopnAllZip());
	}
	
	protected void doTotalFinesPerCapitaByZIP() {
		
		Map<String, Double> finesPerCapita = processor.totalFinesPerCapita();
		
		DecimalFormat df = new DecimalFormat("0.0000");
		df.setRoundingMode(RoundingMode.DOWN);
		
		for (Map.Entry<String, Double> entry : finesPerCapita.entrySet()) {
			System.out.println(entry.getKey() + " " + df.format(entry.getValue()));
		}
		
	}
	
	protected void doValueByZIP(int choice) {
		System.out.print("Enter a 5 digit ZIP Code: ");
		in.nextLine();
		String zip = in.nextLine();
		
		//log entered zip code
		logger.log(System.currentTimeMillis() + " " + zip);
		
		if (zip == null || zip.length() != 5 || !(zip.matches("[0-9]+"))) {
			System.err.println("A valid ZIP Code must be five digits, 0-9");
			System.exit(-1);
		}
		
		double value = -1;
		
		if (choice == 3) {
			value = processor.avgMarketValInZIP(zip, choice);
		}
		else if (choice == 4) {
			value = processor.avgLivableAreaInZIP(zip, choice);
		}
		else if (choice == 5) {
			value = processor.totalResMarketValPerCapitaByZIP(zip);
		}
		
		DecimalFormat df = new DecimalFormat("0");
		df.setRoundingMode(RoundingMode.DOWN);
		
		System.out.println(df.format(value));
	}	
	
	protected void doPercentageByZIP() {
		System.out.print("Enter a 5 digit ZIP Code: ");
		in.nextLine();
		String zip = in.nextLine();
		
		//logging is not needed for option 6
		
		if (zip == null || zip.length() != 5 || !(zip.matches("[0-9]+"))) {
			System.err.println("A valid ZIP Code must be five digits, 0-9");
			System.exit(-1);
		}
		
		double percentage = -1;
		percentage = processor.finesPerCapitaPercentageTotalMV(zip);
		
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		
		System.out.println(nf.format(percentage));
	}
}
