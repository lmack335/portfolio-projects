package logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
		//reused code from Homework 11
	
		private PrintWriter out;
		private boolean init;
		
		//private constructor
		private Logger() {
			init = false;
		}
		
		//singleton instance
		private static Logger instance = new Logger();
		
		//singleton accessor method
		public static Logger getInstance() { return instance; }
		
		//public logging method
		public void log(String msg) {
			if (init) {
				out.println(msg);
				out.flush();
			}
		}
		
		//public method to pass log filename parameter
		public void init(File file) {
			if (!init) {
				if (!file.exists()) {
					try { file.createNewFile(); }
					catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.err.println("The log file cannot be created");
						System.exit(-1);
					}
				}
				
				try {out = new PrintWriter(new FileOutputStream(file, true)); }
				catch (Exception e) {}
				init = true;
			}
		}
}
