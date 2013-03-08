import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Flower;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.actor.Rock;

public class Bettle extends Rock{
	
	static int id = 1;
	static ArrayList<Bettle> team;
	
	private ArrayList<Location> actors;
	private ArrayList<Location> nearbyActors;
	private ArrayList<Location>	nearbyEmpty;
	private Grid<Actor> grid;
	private int personalID;
	
	private static Actor targetActor;
	
	public static boolean debug = false; //Warning this in true state is testing only, do not use while true in competition.
	
	public Bettle(){
		setColor(Color.cyan);
		setDirection(45);
		personalID = id;
		if(id < 2) setupTeam();
		team.add(this);
		id++;
	}
	
	private void setupTeam(){
		team = new ArrayList(1);
	}
	
	public int getId(){
		return personalID;
	}
	
	@Override
	public void act(){
		AI();
	}
	
	private void AI(){
		grid = getGrid();
		actors = grid.getOccupiedLocations();
		nearbyActors = grid.getOccupiedAdjacentLocations(getLocation());
		nearbyEmpty = grid.getEmptyAdjacentLocations(getLocation());
		
		
		
	}
	
	private void surroundSelf(){
		if(nearbyEmpty.size() < 1  || nearbyEmpty == null) return;
		Rock j = new Rock();
		j.putSelfInGrid(grid, nearbyEmpty.get(0));
	}
	
	//@return all valid locations 2 away from initial location;
	public ArrayList<Location> x5outer(Location loc){
		Location[] locs = {loc.getAdjacentLocation(45), loc.getAdjacentLocation(45+90), loc.getAdjacentLocation(45+180), loc.getAdjacentLocation(45+270)}; 
		
		ArrayList<Location> result = new ArrayList<Location>();
		for(int i = 0; i < locs.length; i++){
			result.addAll(grid.getValidAdjacentLocations(locs[i]));
		}
		
		ArrayList<Location> tempNear = grid.getValidAdjacentLocations(loc);
		
		for(int i = 0; i < result.size(); i++){
			Location A = result.get(i);
			for(int j = i+1; j < result.size(); j++){
				if(result.get(j).equals(A)){
					result.remove(j);
					j--;
				}
			}
		}
		
		result.removeAll(tempNear);
		result.remove(loc);
		
		//Old Method
		/*
		for(int i = 0; i < tempNear.size(); i++){
			if(result.contains(tempNear.get(i))) result.remove(tempNear.get(i));
		}
		*/
		
		
		if(debug){
			System.out.println("x5outer: result--> " + result + " (size = " + result.size() + ")");
			for(int i = 0; i < result.size(); i++){
				Flower f = new Flower();
				f.putSelfInGrid(grid, result.get(i));
			}
		}
		
		return result;
	}
	
	//@return Arraylist of Locations of all targets within two blocks of location supplied;
	private ArrayList<Location> x5Targets(Location loc){
		ArrayList<Location> spaces = x5outer(loc);
		ArrayList<Location> result = new ArrayList<Location>();
		spaces.addAll(grid.getOccupiedAdjacentLocations(loc));
		for(int i = 0; i < spaces.size(); i++){
			if((grid.get(spaces.get(i)) instanceof Actor) && !(grid.get(spaces.get(i)) instanceof Bettle)){
				result.add(spaces.get(i));
			}
		}
		
		if(debug)System.out.println("x5Targets: Input--> " + loc.toString() + "\t result--> " + result + " (size = " + result.size() +")");
		return result;
	}
	
	
	//@return nearestTarget location or -1 if none found
	private Location nearestTarget(Location loc){
		ArrayList<Location> actors = grid.getOccupiedLocations();
		int result = -1;
		double lastResult = 10000000;
		for(int i = 0; i < actors.size(); i++){
			if(grid.get(actors.get(i)) instanceof Bettle){
				continue;
			}
			int col = actors.get(i).getCol() - loc.getCol();
			int row = actors.get(i).getRow() - loc.getRow();
			double length = Math.sqrt(col*col + row*row);
			if( length < lastResult){
				lastResult = length;
				result = i;
			}
		}
		return actors.get(result);
	}
	
	public String nearestTargetTest(){
		Location loc = nearestTarget(getLocation());
		return loc.getCol() + ", " + loc.getRow();
	}
	
	
	public int inKillZone(){
		ArrayList<Location> enimies = x5Targets(getLocation());
		if(enimies.size() == 0 || enimies == null) return -1;
		int four5Off = -1;
		for(int i = 0; i < enimies.size(); i++){
			int direction = (grid.get(enimies.get(i))).getDirection();
			Location loc = enimies.get(i).getAdjacentLocation(direction);
			if( loc.equals(getLocation())){
				return 1;
			}else if (enimies.get(i).getAdjacentLocation(direction+45) == getLocation() || 
					enimies.get(i).getAdjacentLocation(direction+90) == getLocation() || 
					enimies.get(i).getAdjacentLocation(direction+270) == getLocation() || 
					enimies.get(i).getAdjacentLocation(direction+360-45) == getLocation()
					){
				four5Off = 0;
			}
			
			//enimies.get(i).getAdjacentLocation(direction+45) == getLocation() || enimies.get(i).getAdjacentLocation(direction+90) == getLocation() || enimies.get(i).getAdjacentLocation(direction+270) == getLocation() || enimies.get(i).getAdjacentLocation(direction+360-45) == getLocation()
		}
		return four5Off;
	}
	
	private void kill(Actor target){
		target.removeSelfFromGrid();
		targetActor = grid.get(nearestTarget(getLocation()));
	}
	
	
	
	
	
	
	//Illegal but cool
	/*
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
	*/

}
