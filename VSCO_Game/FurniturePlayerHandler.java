package team66.VSCO_Game;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class FurniturePlayerHandler extends CollisionHandler {

	public FurniturePlayerHandler() {
		super(VSCOGameType.PLAYER, VSCOGameType.FURNITURE);
	}

	@Override
	protected void onCollisionBegin(Entity player, Entity furniture) {
		
		Furniture f = furniture.getComponent(Furniture.class);
		
		if (f.isExplorable()) {
			
			int x = ThreadLocalRandom.current().nextInt(225, 491);
			int y = ThreadLocalRandom.current().nextInt(160, 451);
			
			//exploring message
			FXGL.getGameState().setValue("activityMessage", "Exploring " + furniture.getComponent(Furniture.class).getFurnitureName() + "...");
			
			//instantiate goodie; spawnData includes location, itemName, collectible (image file must be itemName.png)
			ArrayList<String> goodieArrayList = FXGL.getGameState().getObject("goodieSpawnOrder");
			int goodieIndexNum = FXGL.getGameState().getInt("goodieSpawnIndex");
			String nextGoodie = goodieArrayList.get(goodieIndexNum);
			
			FXGL.getGameWorld().spawn("Goodie", new SpawnData(x, y).put("name", nextGoodie).put("collectable", true));
			
			//advance the goodieSpawnIndex to be able to call the next Goodie's name
			if (goodieIndexNum < (goodieArrayList.size() - 1)) {
				FXGL.getGameState().increment("goodieSpawnIndex", +1);
			}
			else {
				FXGL.getGameState().setValue("goodieSpawnIndex", 0);
			}
			
			//make sure we only get one goodie from each piece of furniture
			f.setExplorable(false);
		}
		else {
			//non-exploring message
			FXGL.getGameState().setValue("activityMessage", "That's a " + furniture.getComponent(Furniture.class).getFurnitureName() + ".");
		}
	}
	
	@Override
	protected void onCollisionEnd(Entity player, Entity furniture) {
		FXGL.getGameState().setValue("activityMessage", "");
	}
}
