/****************************************************************************
* Name:        Tom Takes to the Sky!
* Author:      Sara Subedi & Becca Pettigrew
* Date:        Oct. 2, 2020
* Purpose:     Jump and collect fruit while avoiding hitting an
*              enemy or falling to the ground.      
*****************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.image.BufferedImage;  

public class Game extends Canvas {

		protected int gr = 5;// gravity
	
      	private BufferStrategy strategy;   // take advantage of accelerated graphics
      	
        private boolean waitingForKeyPress = true;  // true if game held up until
        private boolean gameRunning = true; // determine if game loop is running
        private boolean logicRequiredThisLoop = false; // true if logic needs to be applied
          
        // a key is pressed
        private boolean firstRun = false; // false if the instructions have already showed
        private boolean leftPressed = false; // true if left arrow key currently pressed
        private boolean rightPressed = false; // true if right arrow key currently pressed
        private boolean firePressed = false; // true if boost activated
        private boolean upPressed = false; // true if up has been pressed
        private boolean downPressed = false; // true if down has been pressed

        // complete list of entities and entities to remove
        private ArrayList entities = new ArrayList(); // list of entities in game                                             
        private ArrayList removeEntities = new ArrayList(); // remove entities                                         

        private Entity ship; // the ship
        protected String shipState = ""; // what colour he is based on hearts left
        protected String shipDirection = "L"; // what direction he is going
        private double moveSpeed = 90; // hor. vel. of ship (px/s)
        
        private ArrayList pe = new ArrayList(); // list platform entities
        private Entity platform; // platform entity 
        private int randX = 0; // the platforms random X coordinate
        private int randY = 0; // the platforms random X coordinate
        
        // background images and start/end screen
        private ArrayList bg = new ArrayList(); // list of background entities
        private Entity bgImage; // first background image
        private Entity bgImage2; // tiled background image
        private Entity instructions;
        private Entity start; // latter screen
        private Entity startScreen;
        private Entity endScreen;

        private Entity bird; // bird enemy
        protected long lastTouch = 0; 
        protected long immuneInterval = 2000; //immunity from bird based on last touch
        
        private ArrayList fruitList = new ArrayList(); // list of fruit
        private Entity fruit; // fruit entity
        protected int fruitCount = 0; // points

        protected int lifeCounter = 0; // how many lives left
        protected Entity heart;
        protected Entity heart2;
        protected Entity heart3;
        
        //points
        protected Entity point;
        protected Entity point2;
        protected Entity point3;
        private String currentPoint = "";
        private String currentPoint2 = "";
        
        // boost when space bar touched
        private long lastFire = 0; // time last shot fired
        private long firingInterval = 2000; // interval between shots (ms)

        public String message = ""; // message to display while waiting

    	
    	//  Game - construct our game and set it running.
    	 
    	public Game() {
    		// create a frame to contain game
    		//JFrame container = new JFrame("Commodore 64 Space Invaders");
    		JFrame container = new JFrame("Tom Takes to the Sky!");
    		
    		// get hold the content of the frame
    		JPanel panel = (JPanel) container.getContentPane();
 
    		// set up the resolution of the game
    		panel.setPreferredSize(new Dimension(800,600));
    		panel.setLayout(null);
    		
    		// set up canvas size (this) and add to frame
    		setBounds(0,0,800,600);
    		panel.add(this);
    
    		// Tell AWT not to bother repainting canvas since that will
            // be done using graphics acceleration
    		setIgnoreRepaint(true);
    
    		// make the window visible
    		container.pack();
    		container.setResizable(false);
    		container.setVisible(true);
    
            // if user closes window, shutdown game and jre
    		container.addWindowListener(new WindowAdapter() {
    			public void windowClosing(WindowEvent e) {
    				System.exit(0);
    			} // windowClosing
    		});
    
    		// add key listener to this canvas
    		addKeyListener(new KeyInputHandler());
    
    		// request focus so key events are handled by this canvas
    		requestFocus();

    		// create buffer strategy to take advantage of accelerated graphics
    		createBufferStrategy(2);
    		strategy = getBufferStrategy();
    
    		// initialize entities
    		initEntities();
    
    		// start the game loop
    		gameLoop();
    		
        } // constructor
    
    

    	
    
    	/* initEntities - initialize the starting state of the ship, bird, 
    	   points, hearts, background and platform entities. 
    	   Each of these entities will be added to array lists. */
    	
    	private void initEntities() {
    		
    		// variables that are reset each time entities are initialized
            shipState = ""; 
   	        lifeCounter = 0;
   	        shipDirection = "L";
   	        fruitCount = 0;
   	        
   	        
   	        
   	        
    		// create the left bush entities randomly
        	 for(int i = 0; i < 3; i++) {
        		 randX = 1+(int)(Math.random()*(135 - 5 + 1) + 5); // random x coordinate
        		 randY = i*200; // separate the y coordinates
        		 randY -= 450; // don't initialize under the start screen
        		  
        		 platform = new BushEntity(this, "sprites/bush.png", randX, randY + (i * 50));
                 pe.add(platform); 
                 entities.add(platform);

        	 }//for 
        	 
        	 
        	// create the right bush entities randomly
        	 for(int i = 0; i < 3; i++) {
        		 randX = (int)(Math.random()*(695 - 535 + 1) + 535); // random x coordinate
        		 randY = i*200;
        		 randY -= 450;
        		 
        		 platform = new BushEntity(this, "sprites/bush.png", randX, randY - (i * 50));
                 pe.add(platform);
                 entities.add(platform);

        	 }//for 
   
        	 
        	 //create the left branch entities randomly
        	 for(int i = 0; i < 3; i++) {
        		 randX = 1+(int)(Math.random()*(335 - 215 + 1) + 215);
        		 randY = -i*200;
        		 randY -= 425;
        		 
        		 platform = new BranchLEntity(this, "sprites/branchL.png", randX, randY - (i * 65) - 80);
                 pe.add(platform);
                 entities.add(platform);
                 
        	 }//for 
      
        	 
         	// create the right branch entities randomly
        	 for(int i = 0; i < 3; i++) {
        		 randX = 1+(int)(Math.random()*(439 - 395 + 1) + 395); // random x coordinate
        		 randY = -i*200; 
        		 randY -= 425;
        		 
        		 platform = new BranchREntity(this, "sprites/branchR.png", randX, randY + (i * 50) - 100);
        		 pe.add(platform);
                 entities.add(platform);

        	 }//for 
        	 

        	// initialize the tiled screens
            bgImage = new BgEntity(this, "sprites/bg.png", 0, 0);
            bg.add(bgImage);  
            bgImage2 = new BgEntity(this, "sprites/bg.png", 0, -800);
            bg.add(bgImage2); 
    		
            
            //initialize the bird
        	bird = new EnemyEntity(this, "sprites/bird.png", 50, -300);
          	entities.add(bird);
          	
          	
          	//initialize the hearts
          	heart = new HeartEntity(this, "sprites/life.png", -20, -25);
          	entities.add(heart);
          	
          	heart2 = new HeartEntity(this, "sprites/life.png", 15, -25);
          	entities.add(heart2);
          	
          	heart3 = new HeartEntity(this, "sprites/life.png", 50, -25);
          	entities.add(heart3);
          	
          	
          	// initialize the points
          	point = new HeartEntity(this, "sprites/0.png", 735, -25);
          	entities.add(point);
          	
         	point2 = new HeartEntity(this, "sprites/0.png", 710, -25);
          	entities.add(point2);
          	
         	point3 = new HeartEntity(this, "sprites/0.png",685, -25);
          	entities.add(point3);
          	
        	
          	// the moving latter screen
          	start = new BgEntity(this, "sprites/start.png", 0, -200);
           	entities.add(start);
           	
   	        // initalize the ship entity 
            ship = new ShipEntity(this, "sprites/ship" + shipDirection + shipState + ".png", 425, 499);
            entities.add(ship);
            
            // instruction screen
            instructions = new BgEntity(this, "sprites/instructions.png", 0, -200);
           	
            // start screen
            startScreen = new BgEntity(this, "sprites/startScreen.png", 0, -200);
           	entities.add(startScreen);

    	} // initEntities

    	
    	
    	
    	
    	/* randomly decides what fruit image will be generated */
    	
    	public String randFruit() {
    		String fruit = "";
    		int rand = (int)(Math.random()*(3-1+1) + 1);
    		
    		switch(rand) {
    			case 1: 
    				fruit = "sprites/mango.png";
    				break;
    			case 2: 
    				fruit = "sprites/cherry.png";
    				break;
    			default:
    				fruit = "sprites/orange.png";
    				break;
    		}//switch
    		return fruit;
    	}// randFruit
    	
    	
    	
    	
    	
    	/* randomly decides if fruit will be generated on that platform or not */
    	
       	public boolean isFruit() {
    		boolean fruit = false;
    		int rand = (int)(Math.random()*(3-1+1) + 1);
    		
    		switch(rand) {
    			case 1: 
    				fruit = false;
    				break;
    			case 2: 
    				fruit = true;
    				break;
    			default:
    				fruit = true;
    		}
    		return fruit;
    	}// isFruit
       	
       	
       
       	
       	
       	/* puts the fruit entity on top of the platform */
       	
       	public void createFruit(Entity e){
       		
	       	 if(isFruit()) {
	        	 fruit = new FruitEntity(this, randFruit(), (int)e.x,(int) e.y - 40);
	        	 fruitList.add(fruit);
	        	 pe.add(fruit);
	             entities.add(fruit);
	         }
       		
       	} //createFruit
    	
    	
       	
       	
       	
        /* Notification from a game entity that the logic of the game
          should be run at the next opportunity */
       	
         public void updateLogic() {
           logicRequiredThisLoop = true;
         } // updateLogic

         
         
         /* Remove an entity from the game.  It will no longer be
           moved or drawn.*/
         
         public void removeEntity(Entity entity) {
           removeEntities.add(entity);
         } // removeEntity

         
         
         /* Notification that the player has died.*/
         
         public void notifyDeath() {
           message = "Well that was an utter failure!  Try again? Your final score was "
           		+ fruitCount;
           waitingForKeyPress = true;
           
           // add the end screen
           endScreen = new BgEntity(this, "sprites/endScreen.png", 0, -200);
           bg.add(endScreen);

           point = new HeartEntity(this, "sprites/" + fruitCount%10 + ".png", 375, 225);
         	bg.add(point);
         	
        	point2 = new HeartEntity(this, "sprites/" + (int)fruitCount/10 + ".png", 345, 225);
         	bg.add(point2);
         	
        	point3 = new HeartEntity(this, "sprites/0.png", 315, 225);
         	bg.add(point3);
         	
         	
           
           // clear everything if tom dies
           entities.clear();
           pe.clear();
           fruitList.clear();
           entities.removeAll(removeEntities);
           removeEntities.clear();
         } // notifyDeath

   
         
         /* Notification that the play has collected enough points to win*/
         
         public void notifyWin() {
           waitingForKeyPress = true;
           
           // add the end screen
           endScreen = new BgEntity(this, "sprites/winScreen.png", 0, -200);
           bg.add(endScreen); 
           
           // clear everything if tom wins
           entities.clear();
           pe.clear();
           fruitList.clear();
           entities.removeAll(removeEntities);
           removeEntities.clear();
         } // notifyWin
         


        /* Attempt to boost Tom*/
         
        public void tryToFire() {
          // check that we've waited long enough to jump
          if ((System.currentTimeMillis() - lastFire) < firingInterval){
            return;
          } // if

          // otherwise add jump
          lastFire = System.currentTimeMillis();
        
          // reset 
          ship.setFloor(ship.y);
          ship.y -= 80;
          ship.setFloor(500);
        } // tryToFire

        
        
        
        
	/* gameLoop - Main game loop. Runs throughout game play. Responsible for the following activities:
	            - calculates speed of the game loop to update moves
	            - moves the game entities
	            - draws the screen contents (entities, text)
	            - updates game events
	            - checks input  */
      
	public void gameLoop() {
		
          long lastLoopTime = System.currentTimeMillis();
          
	          while (gameRunning) {
	        	  
	        	// if 25 fruit notify win
	        	if(fruitCount == 10) {
	        		notifyWin();
	        	}//if
	        	 
	        	// tile the background images
	        	bgImage.y += gr;
	        	bgImage2.y += gr;
	        	  
	        	
	        	// if the background touches the ground bring it to the top
	          	 if(bgImage.y == 800) {
	         		 bgImage.y = -800;
	         	 }else if(bgImage2.y == 800) {
	         		 bgImage2.y = -800;
	         	 }
	        	  
	          	 
	        	  // number of hearts to show
	        	  if (lifeCounter == 1) {
	        		  heart.changeHearts(lifeCounter);
	        	  } else if (lifeCounter == 2) {
	        		  heart2.changeHearts(lifeCounter);
	        	  } else if (lifeCounter == 3) {
	        		  heart3.changeHearts(lifeCounter);
	        	  } else {
	        		  heart.changeHearts(lifeCounter);
	        		  heart2.changeHearts(lifeCounter);
	        		  heart3.changeHearts(lifeCounter);
	        	  }
	        	  
	        	  
	        	 // number of points to print
     			 point.changeImage("sprites/" + fruitCount%10 + ".png");
     			 point2.changeImage("sprites/" + (int)(fruitCount/10) + ".png");
     			 point3.changeImage("sprites/" + (int)(fruitCount/100) + ".png");
     			 
     		
     			 
	        	  // ensures that you can't touch the ground twice within two seconds
	        	  if (!((System.currentTimeMillis() - lastTouch) < immuneInterval)){
			        			
		                // otherwise lose a life
	        			 if (ship.y >= 500) {
		        			 lastTouch = System.currentTimeMillis();
		        			 
		        			 if (lifeCounter == 2) {
		        	    		 notifyDeath();
		        	    		 lifeCounter++;
		        	    		 shipState = "";
		        	    	     lifeCounter = 0;
		        	    	 } else if (lifeCounter == 1) {
		        	    		 shipState = "T";
		        	    		 lifeCounter++;
		        	    	 } else {
		        	    		 shipState = "ST";
		        	    	     lifeCounter++;
		        	    	 }//else
		        			 
		        		 }// if ship is on the ground
	        			 
	                }// if immuneInterval
	        		
	        	  // change the ship image based on the state
	        	  ship.changeImage("sprites/ship"+ shipDirection + shipState + ".png");
	        			
	        		
	        	  // remove the fruit when it touches the ground
	        	  for(int i = 0; i<fruitList.size(); i++) {
	        		  Entity entity = (Entity) fruitList.get(i);
	        		  
	        		  	// remove the fruit if they touch the ground
	        			  if(entity.y >= 545){ 	
	        				 entities.remove(entity);
	        				 fruitList.remove(entity);
	        				 pe.remove(entity);
	        			 }// if
	        			  
	        		 }//for
	        		 
	        		 
	        		 
	        		 for(int i = 0; i<pe.size(); i++) {
	        			 Entity entity = (Entity) pe.get(i);
	       
	        			 // adds gravity
	        			 entity.y += gr;
	        			 
	        			 //  if the platforms touch the ground
	        			 if(entity.y >= 550){ 
	        				 
	        				 //bring them to the top
	        				 entity.y -= 600;
	        				 
	        				//decides what image to show
	        				 if (entity instanceof BushEntity) {
	        					 entity.changeImage("sprites/bush.png");
	            				 entity.counter = 0;
	        				 } else if (entity instanceof BranchLEntity) {
	        					 entity.changeImage("sprites/branchL.png");
	            				 entity.counter = 0;
	            				 createFruit(entity); // adds the fruit
	        				 } else if (entity instanceof BranchREntity) {
	        					 entity.changeImage("sprites/branchR.png");
	            				 entity.counter = 0;
	            				 createFruit(entity); // adds the fruit
	        				 }// else if
	        				 
	        			 }//if
	        			 
	        		 }//for
	        		
	        	 
	            // calc. time since last update, will be used to calculate
	            // entities movement
	            long delta = System.currentTimeMillis() - lastLoopTime;
	            lastLoopTime = System.currentTimeMillis();
	
	            // get graphics context for the accelerated surface and make it black
	            Graphics g = (Graphics2D) strategy.getDrawGraphics();
	    	       
	           // draw all entities
	            for (int i = 0; i < bg.size(); i++) {
	               Entity entity = (Entity) bg.get(i);
	               entity.draw(g);
	            } // for
	
	            // move each entity
	            if (!waitingForKeyPress) {
	            	
	            	// remove the start screen and instructions
	            	entities.remove(startScreen);
	            	entities.remove(instructions);
	            	bg.remove(point);
	            	bg.remove(point2);
	            	bg.remove(point3);

	            	// gets tom off the latter and into the game
	            	if(start.y == 605) {
	            		entities.remove(start);
	            		gr = 5;
	            		ship.floor = 500;
	            	} // if
	            	
	            	// starts the game once the start screen reaches a certain point
	            	if (start.y <= 375){
	            		ship.setJumped(true);
	            		gr = 0;
	            		start.y+=5;
	            		ship.y-=5;
	            		leftPressed = false;
	            		rightPressed = false;
	            		firePressed = false;
	            		ship.y-=5;
	            	} else if (start.y >= 375) {
	            		start.y+=5;
	            	}//else
	            	
	              // move entities
	              for (int i = 0; i < entities.size(); i++) {
	                Entity entity = (Entity) entities.get(i);
	                entity.move(delta);
	                gr = 5;
	                g.fillRect(0,0,0,0);
	              } // for
	            } // if
	
	            // draw all entities
	            for (int i = 0; i < entities.size(); i++) {
	               Entity entity = (Entity) entities.get(i);
	               entity.draw(g);
	            } // for
	            
	            
	
	            // brute force collisions, compare every entity
	            // against every other entity.  If any collisions
	            // are detected notify both entities that it has
	            // occurred
	           for (int i = 0; i < entities.size(); i++) {
	             for (int j = i + 1; j < entities.size(); j++) {
	                Entity me = (Entity)entities.get(i);
	                Entity him = (Entity)entities.get(j);
	
	                if (me.collidesWith(him)) {
	                  me.collidedWith(him);
	                  him.collidedWith(me);
	                } // if
	             } // inner for
	           } // outer for
	
	           // remove dead entities
	           entities.removeAll(removeEntities);
	           removeEntities.clear();
	
	           // run logic if required
	           if (logicRequiredThisLoop) {
	             for (int i = 0; i < entities.size(); i++) {
	               Entity entity = (Entity) entities.get(i);
	               entity.doLogic();
	             } // for
	             logicRequiredThisLoop = false;
	           } // if
	
	           // if waiting for "any key press", stop jump
	           if (waitingForKeyPress) { 
	        	 gr = 0;
	             ship.setJumped(true);
	           }  // if
	
	            // clear graphics and flip buffer
	            g.dispose();
	            strategy.show();
	
	            // ship should not move without user input
	            ship.setHorizontalMovement(0);
	
	            // respond to user moving ship
	            if ((leftPressed) && (!rightPressed)) {
	              ship.setHorizontalMovement(-moveSpeed);
	            } else if ((rightPressed) && (!leftPressed)) {
	              ship.setHorizontalMovement(moveSpeed);
	            }
	            
	            //make the ship jump/fall
	            ship.jump(ship);
	            ship.fall(ship);
	         
	            // if spacebar pressed, try to fire
	            if (firePressed) {
	              tryToFire();
	            } // if
	
	            // pause
	            try { Thread.sleep(100); } catch (Exception e) {}
	
	          } // while
          

		} // gameLoop

	
	

	
        /* startGame - start a fresh game, clear old data */
	
         private void startGame() {
            // clear out any existing entities and initalize a new set
            entities.clear();
            initEntities();
            
            // blank out any keyboard settings that might exist
            leftPressed = false;
            rightPressed = false;
            firePressed = false;
            upPressed = false;
            downPressed = false;
         } // startGame


         
         
         
        /* inner class KeyInputHandler - handles keyboard input from the user */
         
         private class KeyInputHandler extends KeyAdapter {
                 
        	 private int pressCount = 1;  // the number of key presses since
                                              // waiting for 'any' key press

                /* The following methods are required
                   for any class that extends the abstract
                   class KeyAdapter. They handle keyPressed,
                   keyReleased and keyTyped events. */
        	 public void keyPressed(KeyEvent e) {

                  // if waiting for keypress to start game, do nothing
        		 if (waitingForKeyPress) {
                    return;
                  } // if
                  
                  // respond to move left, right or fire
                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = true;
                    shipDirection = "L";
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = true;
                    shipDirection = "R";
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    firePressed = true;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_UP) {
                      upPressed = true;
                    } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                      downPressed = true;
                  }

		} // keyPressed

        	 
        	 
        	 
        	 
		public void keyReleased(KeyEvent e) {
			
                  // if waiting for keypress to start game, do nothing
                  if (waitingForKeyPress) { 
                    return;
                  } // if
                  
                  // respond to move left, right or fire
                  if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    leftPressed = false;
                  } // if

                  if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    rightPressed = false;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    firePressed = false;
                  } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_UP) {
                      upPressed = false;
                    } // if
                  
                  if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                      downPressed = false;
                    } // if
                  
		} // keyReleased

		
		
		
		
 	        public void keyTyped(KeyEvent e) {

                   // if waiting for key press to start game
 	           if (waitingForKeyPress) {
 	        	   
 	        	   
	 	        	if(pressCount == 1 && !firstRun) {
	 	        		
	 	        		// if key pressed once and it's your first run of the game
	 	        		entities.add(instructions);
	 	        		entities.remove(startScreen);
	 	        		waitingForKeyPress = true;
	 	        		
	 	        		firstRun = true;
	 	        		return;
	 	        		
	 	           } else if (pressCount == 2 && firstRun)  {
	 	        	   
	 	        	  // start the game
	                  waitingForKeyPress = false;
	                  startGame();
	                  
	 	           } else if (pressCount == 1) {
	 	           
	 	        	   // remove instructions and start the game
	 	        	   waitingForKeyPress = false;
	 	        	   entities.remove(instructions);
	 	        	   startGame();
	 	        	   pressCount++;
	                     
	 	           } else {
	 	        	   pressCount++;
	 	           } // else
	 	        	
 	           } // if waitingForKeyPress
	
		  // if escape is pressed, end game
 	           if (e.getKeyChar() == 27) {
 	        	   System.exit(0);
 	           } // if escape pressed

		} // keyTyped

	}// class KeyInputHandler


	/* Main Program*/
	public static void main(String [] args) {
        // instantiate this object
		new Game();
	} // main
} // Game