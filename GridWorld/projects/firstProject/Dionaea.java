import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Flower;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;


public class Dionaea extends Flower{
	
    public Dionaea()
    {
    	super(null);
    }

    public void act(){
    	if(canEat()){
    		eat();
    	}else{
    		turn();
    	}
    }
    
    public void turn(){
    	setDirection(getDirection() + 45);
    }
    
    public boolean canEat(){    	
    	Location front = getLocation().getAdjacentLocation(getDirection());
    	Grid<Actor> grid = getGrid();
    	if(grid.isValid(front) && grid.get(front) != null){
    		Actor a = getGrid().get(front);
    		if(a instanceof Bug){
    			return true;
    		}
    	}  	
    	return false;
    }
    
    public void eat(){
    	Location front = getLocation().getAdjacentLocation(getDirection());
    	Actor bug = getGrid().get(front);
    	bug.removeSelfFromGrid();
    }
    
}
