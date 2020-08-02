/**
 * This class represents the player's inventory and provides
 * methods for adding and removing goodies from the inventory,
 * and checking if the inventory is full (which ends the game)
 * 
 * @author ljmack, csantoma
 */
package team66.VSCO_Game;

import com.almasb.fxgl.entity.component.Component;
import java.util.ArrayList;

public class Inventory extends Component {
    
        private ArrayList<String> inv;
        
        public Inventory() {
        	this.inv = new ArrayList<String>();
        }
        
        
        /*
         * This method checks to see if inventory is full.
         * 
         * @return true when inventory has maximum number of items
         */
        public boolean isFull() {
	    	boolean isFull = false;
	    	if (inv.size() == 2) {
	    	    isFull = true;
	    	    //end game
	    	}
	    	return isFull;
        }
        
        
        /*I don't think we're going to end up needing this method.
         * The check to end the game should be a method in GoodiePlayerHandler.
         * 
         */

//        /*
//         * This method checks to see if all required items have been added to inventory,
//         * which triggers the end of the game.
//         * 
//         * @return true when all require items haven been collected 
//         */
//        public boolean isCorrect() {
//	    	boolean isCorrect = false;
//	    	if (inv.contains("scrunchy") && inv.contains("hydroflask") && inv.contains("necklace")) {
//	    		isCorrect = true;
//	    	}
//	    	return isCorrect;
//        }
        
        
        //methods we need to write 
        
        /*
         * Adds goodie to inventory
         */
        public void addToInventory(Goodie g) {
        	inv.add(g.getGoodieName());
        }
        
        
        /*
         * Removes goodie from inventory
         */
        public void removeFromInventory() {
        	
        }
        
        public String printInventory() {
        	String output = inv.get(0);
        	        	
        	for (int i = 1; i < inv.size(); i++) {
        		output += "\n" + inv.get(i);
        	}
        	
        	return output;
        }

    }
    

