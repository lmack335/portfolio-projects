

import java.io.File;
import java.util.HashMap;
import java.util.List;

import data.Residence;
import datamanagement.ParkVioReaderCSV;
import datamanagement.ParkVioReaderJSON;
import datamanagement.PopnReader;
import datamanagement.PropReader;
import datamanagement.Reader;
import logging.Logger;
import processor.Processor;
import ui.CommandLineUserInterface;

public class Main {

	public static void main(String[] args) {
		
		//args order: Parking format (CSV/JSON), Parking filename, Property filename,
		//               Population filename, log filename
		
		//check for correct number of runtime arguments
		if (args.length != 5) {
			System.err.println("The correct use is: java Main parkingViolationsFormat "
				+ " parkingViolationsFilename PropertyValuesFilename PopulationFilename logFilename");
			System.exit(-1);
		}
		
		//initialize singleton Logger
		Logger logger = Logger.getInstance();
		logger.init(new File(args[4]));
		
		logger.log(System.currentTimeMillis() + " " + args[0] + " " + args[1] + " " 
				+ args[2]  + " " + args[3]  + " " + args[4]);
		
		//initialize correct type of parking violations input file reader
		String parkingInputFileType = args[0];
		String parkingInputFileName = args[1];

		Reader parkingReader = null;
		
		if (parkingInputFileType.compareTo("json") == 0) {
			parkingReader = new ParkVioReaderJSON(parkingInputFileName);
		}
		else if (parkingInputFileType.compareTo("csv") == 0) {
			parkingReader = new ParkVioReaderCSV(parkingInputFileName);
		}
		else {
			//error & exit for unsupported formats
			System.err.println("The parking violations input file format must be csv or json (case-sensitive)");
			System.exit(-1);
		}
		
		//initialize other readers
		PropReader propReader = new PropReader(args[2]);
		PopnReader popnReader = new PopnReader(args[3]);
		
		//initialize processor
		Processor processor = new Processor(parkingReader, popnReader, propReader);
		
		//initialize & run user interface
		CommandLineUserInterface ui = new CommandLineUserInterface(processor);
		ui.start();
	}

}
