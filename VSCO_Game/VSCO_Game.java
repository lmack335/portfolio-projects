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

import java.util.*;

import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.event.EventBus;
import com.almasb.fxgl.event.Handles;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.settings.GameSettings;
import com.almasb.fxgl.texture.Texture;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import javafx.beans.property.*;
import javafx.event.*;

import team66.VSCO_Game.VSCOGameEvent;

/* There is a JavaFX Convention to append App after the class containing
 * main -- should this be VSCO_Game_App?
 */
public class VSCO_Game extends GameApplication {

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
	
	//initializes Player game object
	private Entity player;
	
	GoodieFactory goodieFactory = new GoodieFactory();
	
	/**
	 * This method initializes the Entity objects in the game world, using the
	 * EntityFactory GoodieFactory for Goodies and Furniture objects, and using
	 * Entities.builder() and the Player constructor for the Player object.
	 * 
	 */
	@Override
	protected void initGame() {
		
		getGameWorld().addEntityFactory(goodieFactory);
		
		getGameWorld().spawn("Background");
		
		//initialize the bedroom furniture
		goodieFactory.spawnFurniture(getGameWorld());
		
		//initialize the Player
		player = Entities.builder()
				.type(VSCOGameType.PLAYER)
				.at(400, 300)
				.with(new Player())
				.bbox(new HitBox(BoundingShape.box(41, 43)))
//				.viewFromTextureWithBBox("vsco_00.png")
				.with(new CollidableComponent(true))
				.buildAndAttach(getGameWorld());
		
	}
	
	
	/**
	 * This method connects keyboard input with methods in the Player class
	 * that control the player's movement and animation.
	 */
	@Override
	protected void initInput() {
		super.initInput();
		
		getInput().addAction(new UserAction("Right") {
			@Override
			protected void onAction() {
				player.getComponent(Player.class).moveRight();
			}
		}, KeyCode.RIGHT);
		
		getInput().addAction(new UserAction("Left") {
			@Override
			protected void onAction() {
				player.getComponent(Player.class).moveLeft();
			}
		}, KeyCode.LEFT);
		
		getInput().addAction(new UserAction("Up") {
			@Override
			protected void onAction() {
				player.getComponent(Player.class).moveUp();
			}
		}, KeyCode.UP);
		
		getInput().addAction(new UserAction("Down") {
			@Override
			protected void onAction() {
				player.getComponent(Player.class).moveDown();
			}
		}, KeyCode.DOWN);
	}
	
	
	/**
	 * This method is used to declare global game variables so they can be accessed
	 * by other methods 
	 */
	@Override
	protected void initGameVars(Map<String, Object> vars) {
//		super.initGameVars(vars);
		
		int NUM_CORRECT_GOODIES = 3;
		int NUM_EXPLORABLE_FURNITURE = 6;
		
		ArrayList<String> ALL_GOODIES = goodieFactory.randomize(goodieFactory.allGoodiesList());
		
		ArrayList<String> requiredGoodies = goodieFactory.alphabetize(
				goodieFactory.requiredGoodiesList(ALL_GOODIES, NUM_CORRECT_GOODIES));
		String printoutReqGoodies = goodieFactory.printoutList(requiredGoodies);
		
		ArrayList<String> goodiesToSpawn = goodieFactory.randomize(
				goodieFactory.goodiesToSpawnList(requiredGoodies, ALL_GOODIES, NUM_EXPLORABLE_FURNITURE));
		
		vars.put("collectedObjects", 0);
		vars.put("remainingObjects", NUM_CORRECT_GOODIES);
		
		vars.put("timer", 0.0);  //depending on progress, extend game to include a timer
		
		vars.put("activityMessage", "");
		vars.put("checklistMessage", printoutReqGoodies);
		
		vars.put("requiredGoodies", requiredGoodies);
		
		vars.put("goodieSpawnOrder", goodiesToSpawn);
		vars.put("goodieSpawnIndex", 0);
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
		super.initPhysics();
		
		getPhysicsWorld().addCollisionHandler(new FurniturePlayerHandler());
		getPhysicsWorld().addCollisionHandler(new GoodiePlayerHandler());
		
	}
	
	
	/**
	 * This method initializes all User Interface objects in the game world
	 * at specific positions, binds relevant GameVars to Text objects,
	 * 
	 * Adds UI nodes (which makes UI elements visible?)
	 */
	@Override
	protected void initUI() {
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
		Text textChecklistLabel = getUIFactory().newText("Items to Find:", Color.BLACK, 22);
		textChecklistLabel.setTranslateX(600);
		textChecklistLabel.setTranslateY(20);
		
		Text textChecklist = getUIFactory().newText("empty checklist", Color.BLACK, 18);
		textChecklist.setTranslateX(600);
		textChecklist.setTranslateY(40);
		textChecklist.textProperty().bind(getGameState().stringProperty("checklistMessage"));
		
		
		getGameScene().addUINodes(textCollectedObjects, textRemainingObjects, 
				textActivityMessage, textChecklistLabel, textChecklist);
	}

	public static void main(String[] args) {
		launch(args);

	}

}
