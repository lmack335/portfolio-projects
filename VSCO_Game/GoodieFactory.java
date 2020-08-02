/**
 * This class is responsible for spawning new Entities for EntityTypes
 * FURNITURE and GOODIE
 * 
 * See https://github.com/AlmasB/FXGL/wiki/Game-Architecture-and-Workflow for
 * more details
 * 
 * @author ljmack, csantoma
 */
package team66.VSCO_Game;

import java.util.ArrayList;
import java.util.Collections;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.*;
import com.almasb.fxgl.entity.view.*;
import com.almasb.fxgl.physics.*;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.*;
import javafx.scene.shape.*;

public final class GoodieFactory implements EntityFactory {

	//  If we decide we want a background image, use this:
	
	@Spawns("Background")
	public Entity newBackground(SpawnData data) {
        return Entities.builder()
                .viewFromNode(FXGL.getAssetLoader().loadTexture("background.png", 800, 600))
                .renderLayer(RenderLayer.BACKGROUND)
                .build();
    } 
	
	/**
	 * Spawns new Furniture entities in the game world.
	 * 
	 * Explorable Furniture will eventually include:
	 * Dresser, Bed, Closet, Desk, Chair, 
	 * Clothes Hamper / Pile of Clothes,
	 * 
	 * Non-Explorable Furniture will eventually include:
	 * Mirror, Window, TV, Door, Stuffed Animal
	 */
    @Spawns("Furniture")
    public Entity newFurniture(SpawnData data) {
        String name = data.get("name");
        String fileName = name + ".png";
        boolean explorable = data.get("explorable");
    	
    	return Entities.builder()
                .from(data)
                .type(VSCOGameType.FURNITURE)
    			.with(new Furniture(name, fileName, explorable))
    			.with(new CollidableComponent(true))
                .build();
    }
	
	/*
	 * Spawns new Goodie entities in the game world.
	 * 
	 * Collectible Goodies include:
	 * Birkenstock sandals, Scrunchy, Croptop shirt,
	 * Friendship bracelets, iPhone, Burt's Bees and Carmax lip balm,
	 * Oversized t-shirt, Puka shell necklace, Hydroflash,
	 * Short shorts.
	 */
    @Spawns("Goodie")
    public Entity newGoodie(SpawnData data) {
    	String name = data.get("name");
        String fileName = name + ".png";
        boolean collectible = data.get("collectable");
    	
    	return Entities.builder()
                .from(data)
                .type(VSCOGameType.GOODIE)
//                .at(500, 200)
    			.with(new Goodie(name, fileName, collectible))
    			.with(new CollidableComponent(true))
                .build();
    }
    
    /**
     * This method spawns the bedroom furniture needed for the game.
     * Includes both explorable and non-explorable furniture.
     * @param world
     */
    public void spawnFurniture(GameWorld world) {
		//instantiate explorable furniture; spawnData includes location, itemName, explorable (image file must be itemName.png)
		world.spawn("Furniture", new SpawnData(470, 40).put("name", "dresser").put("explorable", true));
		world.spawn("Furniture", new SpawnData(10, 300).put("name", "bed").put("explorable", true));
		world.spawn("Furniture", new SpawnData(675, 75).put("name", "closet").put("explorable", true));
		world.spawn("Furniture", new SpawnData(30, 40).put("name", "desk").put("explorable", true));
		world.spawn("Furniture", new SpawnData(580, 150).put("name", "laundrypile").put("explorable", true));
		world.spawn("Furniture", new SpawnData(700, 295).put("name", "giantteddybear").put("explorable", true));
		
		//instantiate non-explorable furniture; spawnData includes location, itemName, explorable (image file must be itemName.png)
		world.spawn("Furniture", new SpawnData(365, 10).put("name", "mirror").put("explorable", false));
		world.spawn("Furniture", new SpawnData(215, 120).put("name", "deskchair").put("explorable", false));
		world.spawn("Furniture", new SpawnData(10, 10).put("name", "window").put("explorable", false));
    }
    
	
    public ArrayList<String> allGoodiesList() {
    	ArrayList<String> allGoodies = new ArrayList<String>();
		allGoodies.add("birkenstocksandals");
		allGoodies.add("bluescrunchy");
		allGoodies.add("butterflycroptop");
		allGoodies.add("friendshipbracelet");
		allGoodies.add("iphone");
		allGoodies.add("lipbalm");
		allGoodies.add("oversizedtee");
		allGoodies.add("pukashells");
		allGoodies.add("rainbowhydroflask");
		allGoodies.add("shortshorts");
		
		return allGoodies;
    }
    
	/**
	 * This method selects the required goodies that the player must collect
	 * to win the game
	 * @param allGoodies - master list of all available goodies
	 * @param numCorrect - how many goodies the player must collect
	 * @return
	 */
	public ArrayList<String> requiredGoodiesList(ArrayList<String> allGoodies, int numRequired) {
		
		ArrayList<String> correctGoodies = new ArrayList<String>();
		
		for (int i = 0; i < numRequired; i++) {
			correctGoodies.add(allGoodies.get(i));
		}
		
		return correctGoodies;
		
	}
	
	/**
	 * This method selects the goodies that will spawn during the game.  It includes all
	 * required goodies needed to win the game as well as additional unnecessary goodies.
	 * @param requiredGoodies
	 * @param allGoodies
	 * @param totalNumGoodies
	 * @return
	 */
	public ArrayList<String> goodiesToSpawnList(ArrayList<String> requiredGoodies, 
			ArrayList<String> allGoodies, int totalNumGoodies) {
		
		ArrayList<String> goodiesToSpawn = requiredGoodies;
		
		int i = 0;
		
		while(goodiesToSpawn.size() < totalNumGoodies) {
			if (!goodiesToSpawn.contains(allGoodies.get(i))) {
				goodiesToSpawn.add(allGoodies.get(i));
			}
			
			i++;
		}
		
		return randomize(goodiesToSpawn);
	}
	
	/**
	 * This method randomizes the order of an ArrayList of strings.
	 * @param arr - ArrayList of strings
	 * @return randomized ArrayList
	 */
	public ArrayList<String> randomize(ArrayList<String> arr) {
		Collections.shuffle(arr);
		return arr;
	}
	
	/**
	 * This method sorts an ArrayList of strings in alphabetical order.
	 * @param arr - ArrayList of strings
	 * @return randomized ArrayList
	 */
	public ArrayList<String> alphabetize(ArrayList<String> arr) {
		Collections.sort(arr);
		return arr;
	}
	
	
	public String printoutList(ArrayList<String> arrayToPrint) {
    	String output = arrayToPrint.get(0);
    	        	
    	for (int i = 1; i < arrayToPrint.size(); i++) {
    		output += "\n" + arrayToPrint.get(i);
    	}
    	
    	return output;
    }
	
}
