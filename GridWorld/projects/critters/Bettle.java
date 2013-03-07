import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.actor.Rock;

public class Bettle extends Rock{
	
	private ArrayList<Location> actors;
	private ArrayList<Location> nearbyActors;
	private ArrayList<Location>	nearbyEmpty;
	private Grid<Actor> grid;
	
	
	public Bettle(){
		setColor(Color.cyan);
		setDirection(45);
	}
	
	public void act(){
		AI();
		surroundSelf();
	}
	
	public void AI(){
		grid = getGrid();
		actors = grid.getOccupiedLocations();
		nearbyActors = grid.getOccupiedAdjacentLocations(getLocation());
		nearbyEmpty = grid.getEmptyAdjacentLocations(getLocation());
		
		
		
	}
	
	void surroundSelf(){
		if(nearbyEmpty.size() < 1  || nearbyEmpty == null) return;
		Rock j = new Rock();
		j.putSelfInGrid(grid, nearbyEmpty.get(0));
	}
	
	void surroundEnemies(){
		for(int i = 0; i < actors.size(); i++){
			Location loc = actors.get(i);
			Actor target = grid.get(actors.get(i)); 
			if(target instanceof Bettle){
				continue;
			}else{
				ArrayList<Location> spaces = grid.getEmptyAdjacentLocations(loc);
				Rock[] rocks = new Rock[spaces.size()];
				for(int j = 0; j < spaces.size(); j++){
					rocks[j]  = new Rock(Color.red);
					rocks[j].putSelfInGrid(grid, spaces.get(j));
				}
			}
			
		}
	}

}
