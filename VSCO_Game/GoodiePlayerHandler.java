package team66.VSCO_Game;

import java.util.ArrayList;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GoodiePlayerHandler extends CollisionHandler {
	
	public GoodiePlayerHandler() {
		super(VSCOGameType.PLAYER, VSCOGameType.GOODIE);
	}

	@Override
	protected void onCollisionBegin(Entity player, Entity goodie) {
		
		Player p = player.getComponent(Player.class);
		Goodie g = goodie.getComponent(Goodie.class);

		
		if (g.isCollectible()) {
			
			FXGL.getGameState().setValue("activityMessage", "Picked up a " + g.getGoodieName() + "...");
			FXGL.getGameState().increment("collectedObjects", +1);
			FXGL.getGameState().increment("remainingObjects", -1);
			
			p.addToInventory(g);
			
			//commented out so that I could see the list of required goodies instead of the inventory. 
//			FXGL.getGameState().setValue("checklistMessage", p.getInventory().printInventory());
			
			goodie.removeFromWorld();
			
			
			//checks if all the required goodies are in the inventory (ends the game)
			
			if (p.getInventory().isFull()) {		//modify this to use checkHasReqGoodies
				
			    //FXGL.getGameState().setValue("activityMessage", "GAME OVER!  YOU WON!");
			    System.exit(0);
			    //FXGL.getApp().stop();
			    
			}
			
		} else {
			FXGL.getGameState().setValue("activityMessage", "That's a " + goodie.getComponent(Goodie.class).getGoodieName() + ".");
		}
	}
	
	
	/**
	 * This method compares elements from the requiredGoodies list with the elements in the player's
	 * inventory.  Returns true if all elements from the requiredGoodies list are present in the
	 * player's inventory.
	 * @param requiredGoodies
	 * @param inventory
	 * @return
	 */
	public boolean checkHasReqGoodies(ArrayList<String> requiredGoodies, ArrayList<String> inventory) {
		
		
		
		return true;
	}
	
}
