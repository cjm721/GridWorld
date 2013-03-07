import java.awt.Color;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Critter;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


public class Jumper extends Critter{
	
	//CJ
	public Jumper(Color c){
		setColor(c);
	}
	
	//Vinay
	public Jumper(){
		setColor(Color.black);
		setDirection(0);
	}
	
	//CJ
	@Override
	public void act(){
        Grid<Actor> grid = getGrid();
		if (grid == null) return;
        Location loc = getLocation().getAdjacentLocation(getDirection()).getAdjacentLocation(getDirection());
        if(canJump()){
        	jump();
        }else{
           	loc = getLocation().getAdjacentLocation(getDirection());
           	if(grid.isValid(loc) && grid.get(loc) == null){
        		makeMove(loc);
        	}else{
        		turn();
        	}
        }
        
        
	}
	
	//Vinay
	public boolean canJump() 
	{ 
		Grid<Actor> gr = getGrid(); 
		if (gr == null) return false; 
		Location loc = getLocation(); 
		Location next = loc.getAdjacentLocation(getDirection()); 
		Location twoAway = next.getAdjacentLocation(getDirection()); 
		if (!gr.isValid(twoAway)) 
			return false; 
		Actor neighbor = gr.get(twoAway); 
		return (neighbor == null);
	} 
	
	
	//CJ
	void jump(){
		Location loc = getLocation().getAdjacentLocation(getDirection()).getAdjacentLocation(getDirection());        	
		moveTo(loc);
	}
	
	//CJ
	void turn(){
		setDirection(getDirection() + 45);
	}
}
