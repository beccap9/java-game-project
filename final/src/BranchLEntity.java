

public class BranchLEntity extends PlatformEntity {

private Game game;
	
	private Entity platform;
	protected Sprite sprite; // this entity's sprite
	
	
	BranchLEntity (Game g, String r, int newX, int newY) {
		super(g, r, newX, newY);  // calls the constructor in Entity
	    game = g;
	    jumped = false;
	    floor = y;
	} // constructor
	
	
	public void collidedWith(Entity other) {
	     if (other instanceof ShipEntity) {

	    	 //only sets other.y if it has landed
	    	 if(!jumped){
	    		 other.y = y - 100;
	    	 }
	    	 
	    	 jump(other);
	         fall(other);

		           // remove affect entities from the Entity list
		        	 if(counter == 1) {
		        		 
		        		 other.y -= 70;
		        		this.y -= 600;
		        		this.changeImage("sprites/branchL.png");
		        		counter = 0;
		        		
		        	 }else if(counter == 0) {
		        		
		        		this.changeImage("sprites/branchLT.png");
		        		counter++;
		        		
		        	 }else {
		        		 counter++;
		        	 }


	     } // if
	         
	    	 
	}//collidedWith
	
	
} //BranchLEntity
