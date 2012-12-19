//TestEngine.java
//Rolph Recto

import java.util.*;
import engine.*;
import engine.util.*;
import engine.msgtype.*;

class Player extends Entity {
	
	private int health, maxHealth;
	
	private int ammo, maxAmmo;
	
	public Player() { //Assumes max health and ammo are 100 and 500 respectively
		
		maxHealth = 100; //Maximum Value for Health
		health = maxHealth; //Sets health to maximum value
		
		maxAmmo = 500; //Maximum amount of ammo player can hold.
		ammo = maxAmmo; //Fills ammo.
	}
	
	public Player(int maxHealth_, int maxAmmo_){
		maxHealth = maxHealth_; //Maximum Value for Health
		health = maxHealth; //Sets health to maximum value
		
		maxAmmo = maxAmmo_; //Maximum amount of ammo player can hold.
		ammo = maxAmmo; //Fills ammo.
	}
	
	//Returns health
	public int getHealth(){
		return health;
	}
	//Set Health to a specific value
	public void setHealth(int health_){
		health = health_;
	}
	//Reduce Health by a specific value
	public void reduceHealth(int amt){
		health -= amt;
		if(health < 0)
			health = 0;
	}
	//Adds a specific value of health.
	public void addHealth(int amt){
		health += amt;
		if(health > maxHealth)
			health = maxHealth;
	}
	//Returns maximum health
	public int getMaxHealth(){
		return maxHealth;
	}
	//Returns amount of missing health - dunno if we'll ever use this but couldnt hurt.
	public int getMissingHealth(){
		return maxHealth-health;
	}
	
	
	//All the same functions that health has but for ammo
	public int getAmmo(){
		return ammo;
	}
	public void setAmmo(int ammo_){
		ammo = ammo_;
	}
	public void reduceAmmo(int amt){
		ammo -= amt;
		if (ammo<0)
			ammo=0;
	}
	public void addAmmo(int amt){
		ammo += amt;
		if (ammo>maxAmmo)
			ammo=maxAmmo;
	}
	public int getMaxAmmo(){
		return maxAmmo;
	}
	public int getMissingAmmo(){
		return maxAmmo-ammo;
	}
	
}

class PlayerController extends Controller {
	private Player player;
	
	public PlayerController() {
		super();
	}
	
	public PlayerController(Level l, Entity player) {
		super(l, player);
		this.player = (Player)player;
	}
	
	/* Override addEntity method
	 * set added entity as this.player
	 */
	@Override
	public boolean addEntity(Entity e) {
		boolean added = super.addEntity(e);
		
		if (added) {
			this.player = (Player)e;
			return true;
		}
		else return false;
	}
	
	public void onMessage(Message msg) {
		EntityMoveMessage move_msg;
		switch (msg.getType()) {
		case LEVEL_UPDATE:
			move_msg = new EntityMoveMessage(MsgType.ENTITY_COMMAND_MOVE, this.player, -5.0, 0.0, 0.0);
			this.player.onMessage(move_msg);
			break;
		case ENTITY_MOVE:
			move_msg = (EntityMoveMessage)msg;
			System.out.println("Player moved: "+move_msg.getX()+" "+move_msg.getY()+" "+move_msg.getZ());
		default:
			break;
		}
	}
}

class PlayerType extends EntityType {
	private static int MAX_HEALTH = 100;
	private static int MAX_AMMO = 500;
	private static final PlayerType instance = new PlayerType();
	
	private PlayerType() {
		this.name = "PlayerType";
		this.controller_class = PlayerController.class;
		this.hivemind = false;
	}
	
	public static EntityType getInstance() {
		return instance;
	}
	
	public Entity createEntity() {
		return new Player();
	}
	
	public int getMaxHealth(){
		return MAX_HEALTH;
	}
}

public class TestEngine {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Level level = new Level();
		long id = level.createEntity(PlayerType.getInstance(), (float)0.0, (float)0.0, (float)0.0);
		Entity player = level.getEntityById(id);
		
		boolean done = false;
		while (!done) {
			System.out.println("POS "+player.getPosX()+" "+player.getPosY()+" "+player.getPosZ());
			System.out.println("VEL "+player.getVelX()+" "+player.getVelY()+" "+player.getVelZ());
			System.out.println("ACCEL "+player.getAccelX()+" "+player.getAccelY()+" "+player.getAccelZ());
			System.out.println("What do you want to do?");
			String input = s.nextLine();
			if (input.equals("quit")) done = true;
			level.update();
		}
	} 
}
