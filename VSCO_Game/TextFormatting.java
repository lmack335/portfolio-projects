package team66.VSCO_Game;

import java.util.ArrayList;

public class TextFormatting {
	
	public String printVerticalList(ArrayList<String> arrayToPrint) {
    	String output = arrayToPrint.get(0);
    	        	
    	for (int i = 1; i < arrayToPrint.size(); i++) {
    		output += "\n" + arrayToPrint.get(i);
    	}
    	
    	return output;
    }
	
	public String printHorizontalList(ArrayList<String> arrayToPrint) {
    	String output = arrayToPrint.get(0);
    	        	
    	for (int i = 1; i < arrayToPrint.size(); i++) {
    		output += "\n" + arrayToPrint.get(i);
    	}
    	
    	return output;
    }
}
