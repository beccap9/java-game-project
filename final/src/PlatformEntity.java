import java.awt.AlphaComposite;

public abstract class PlatformEntity extends Entity {

	private Game game;
	
	PlatformEntity (Game g, String r, int newX, int newY) {
		super(r, newX, newY);  // calls the constructor in Entity
	    game = g;
	    jumped = false;
	    floor = y;
	} // constructor
	
	
	
	public void collidedWith(Entity other) {

	}//collidedWith
	
	
	
}//PlatformEntity