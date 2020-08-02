/**
 * This class extends the GameApplication class to create and
 * manage all aspects of the VSCO_Game including settings, entities,
 * game world, input events, game variables, physics, and user
 * interface elements.
 * 
 * In VSCO_Game, you play as a teenage "VSCO girl" who loves to take
 * selfies with all her trendy 90s themed stuff.  You must 
 * search through your bedroom to collect all of the required goodies
 * so that your next selfie earns plenty of likes on social media!
 * 
 * @author ljmack, csantoma
 */

package team66.VSCO_Game;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.event.Handles;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.beans.property.*;

/* There is a JavaFX Convention to append App after the class containing
 * main -- should this be VSCO_Game_App?
 */
public class VSCO_Game_Chris extends GameApplication {

	/**
	 * This method initializes settings for game window width & height,
	 * title bar in game window.
	 */
	@Override
	protected void initSettings(GameSettings settings) {
		// TODO Auto-generated method stub
		settings.setWidth(800);
		settings.setHeight(600);
		settings.setTitle("VSCO Game");
	}
	
	//declare individual Entity class for each object? (might not need to?) 
	private Entity player;
	private Entity scrunchy, hydroflask, necklace;
	private Entity dresser, mirror, bed;
	
	//declare EntityTypes for CollisionHandler in initPhysics() -- moved to separate class, VSCOGameType
//	public enum EntityType {
//		PLAYER, GOODIES, FURNITURE
//	}
	
	/**
	 * This method initializes the objects in the game world at specific
	 * positions, using an image or animated texture (spritesheet),
	 * and adds relevant components using Entities.builder().
	 */
	@Override
	protected void initGame() {
		// TODO Auto-generated method stub
		super.initGame();
		
		//Each type needs to become its own EntityFactory
		player = Entities.builder()
				.type(VSCOGameType.PLAYER)
				.at(400, 300)  //center of screen?
//				.with(new Player())  //name of script that includes player controls
				.viewFromTextureWithBBox("vsco_00.png")
//				.viewFromTextureWithBBox(new Rectangle(25, 25))
				.with(new CollidableComponent(true))
				.buildAndAttach(getGameWorld());
		
		//instantiate goodies - item name, image file name, collectible
		Entities.builder()
			.type(VSCOGameType.GOODIE)
			.at(500, 200)
			.with(new Goodie("scrunchy", "bluescrunchy.png", true))
			.with(new CollidableComponent(true))
			.buildAndAttach(getGameWorld());
		
		Entities.builder()
			.type(VSCOGameType.GOODIE)
			.at(500, 200)
//			.with(new Goodie("hydroflask", "rainbowhydroflask.png", true))
			.with(new CollidableComponent(true))
			.buildAndAttach(getGameWorld());
		
		//instantiate furniture pieces - item name, image file name, explorable
		Entities.builder()
			.type(VSCOGameType.FURNITURE)
			.at(325, 0)
//			.viewFromTextureWithBBox("dresser.png")
			.with(new Furniture("dresser", "dresser.png", false))
			.with(new CollidableComponent(true))
			.buildAndAttach(getGameWorld());
		
		Entities.builder()
			.type(VSCOGameType.FURNITURE)
			.at(25, 200)
//			.viewFromTextureWithBBox("bed.png")
			.with(new Furniture("bed", "bed.png", false))
			.with(new CollidableComponent(true))
			.buildAndAttach(getGameWorld());
		
		Entities.builder()
		.type(VSCOGameType.FURNITURE)
		.at(250, 0)
//		.viewFromTextureWithBBox("mirror.png")
		.with(new Furniture("mirror", "mirror.png", false))
		.with(new CollidableComponent(true))
		.buildAndAttach(getGameWorld());
		
	}
	
	
	/**
	 * This method connects specific key inputs with methods in the Player class
	 * that control the player's movement and animation.
	 */
	@Override
	protected void initInput() {
		// TODO Auto-generated method stub
		super.initInput();
		
		getInput().addAction(new UserAction("Right") {
			@Override
			protected void onAction() {
//				player.getComponent(Player.class).moveRight();  //name of method that moves player to the right
				player.translateX(5); // move right 5 pixels
			}
		}, KeyCode.RIGHT);
		
		getInput().addAction(new UserAction("Left") {
			@Override
			protected void onAction() {
//				player.getComponent(Player.class).moveLeft();  //name of method that moves player to the left
				player.translateX(-5); // move left 5 pixels
			}
		}, KeyCode.LEFT);
		
		getInput().addAction(new UserAction("Up") {
			@Override
			protected void onAction() {
//				player.getComponent(Player.class).moveUp();  //name of method that moves player vertical up
				player.translateY(-5); // move up 5 pixels
			}
		}, KeyCode.UP);
		
		getInput().addAction(new UserAction("Down") {
			@Override
			protected void onAction() {
//				player.getComponent(Player.class).moveDown();  //name of method that moves player vertical down
				player.translateY(5); // move up 5 pixels
			}
		}, KeyCode.DOWN);
	}
	
	
	/**
	 * This method is used to declare game variables so they can be accessed
	 * by other methods (still learning how to use this aspect of fxgl). 
	 */
	@Override
	protected void initGameVars(Map<String, Object> vars) {
		// TODO Auto-generated method stub
		super.initGameVars(vars);
		
		vars.put("collectedObjects", 0);
		vars.put("remainingObjects", 0);
		
		vars.put("timer", 0.0);  //depending on progress, extend game to include a timer
		
		vars.put("activityMessage", "");
	}

	
	/**
	 * This method is responsible for handling collisions between the player and
	 * different entity types (goodies and furniture).
	 * 
	 * Players are able to add goodies to their inventory by colliding with them.
	 * 
	 * Players are able to explore furniture to reveal goodies by colliding with each object.
	 */
	@Override
	protected void initPhysics() {
		// TODO Auto-generated method stub
		super.initPhysics();
		
		//adds collectible goodies to inventory when player collides with each goody
		//removes goody from game world
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(VSCOGameType.PLAYER, VSCOGameType.GOODIE) {
			
			@Override
			protected void onCollisionBegin(Entity player, Entity goodie) {
//				if (goodie.getRequiredCollectable() == true) {
//					player.found();  //plays found sound
//					player.addToInventory(goodie);  //method needs to be written
//					goodie.removeFromWorld();
//				}
			}
		});
		
		//displays inspection message
		//displays additional furniture / goodies if this furniture is explorable
//		getPhysicsWorld().addCollisionHandler(new FurniturePlayerHandler());
		
		getPhysicsWorld().addCollisionHandler(new CollisionHandler(VSCOGameType.PLAYER, VSCOGameType.FURNITURE) {
			
			@Override
			protected void onCollisionBegin(Entity player, Entity furniture) {
				//furniture.getComponent(Furniture.class).explore();
				getGameState().setValue("activityMessage", "Hit " + furniture.getComponent(Furniture.class).getFurnitureName());
			}
		});
		
	}
	
	@Handles(eventType = "EXPLORE_FURNITURE")
	public void onExploreFurniture(VSCOGameEvent event) {
		getGameState().setValue("activityMessage", "Hit object");
	}
	
	
	/**
	 * This method initializes all User Interface objects in the game world
	 * at specific positions, binds relevant GameVars to Text objects,
	 * 
	 * Adds UI nodes (which makes UI elements visible?)
	 */
	@Override
	protected void initUI() {
		// TODO Auto-generated method stub
		super.initUI();
		
		//declare textboxes for UI & set on-screen positions
		Text textCollectedObjects = getUIFactory().newText("Collected Objects: ", Color.BLACK, 22);
		textCollectedObjects.setTranslateX(10);
		textCollectedObjects.setTranslateY(570);
		textCollectedObjects.textProperty().bind(getGameState().intProperty("collectedObjects").asString("Collected Objects: %d"));
		
		Text textRemainingObjects = getUIFactory().newText("Remaining Objects: ", Color.BLACK, 22);
		textRemainingObjects.setTranslateX(550);
		textRemainingObjects.setTranslateY(570);
		textRemainingObjects.textProperty().bind(getGameState().intProperty("remainingObjects").asString("Remaining Objects: %d"));
		
		Text textActivityMessage = getUIFactory().newText("", Color.BLACK, 22);
		textActivityMessage.setTranslateX(300);
		textActivityMessage.setTranslateY(540);
		textActivityMessage.textProperty().bind(getGameState().stringProperty("activityMessage"));
				
		//Is there another UI element that allows us to mix pictures and text?
		Text textInventory = getUIFactory().newText("Inventory", Color.BLACK, 22);
		textInventory.setTranslateX(300);
		textInventory.setTranslateY(510);
		
		
		
		
		getGameScene().addUINodes(textCollectedObjects, textRemainingObjects, 
				textActivityMessage, textInventory);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);

	}

}
