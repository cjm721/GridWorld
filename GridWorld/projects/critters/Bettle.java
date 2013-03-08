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
		
		
		if(deadMan()){
			kill();
		}else if(inKillZone() == 1){
			move();
		}else if(inKillZone() == 0){
			fight();
		}else{
			Location target = nearestTarget(getLocation());
			if(getLocation().getDirectionToward(target) == getDirection()){
				move();
			}else{
				turn(target);
			}
		}
		
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
		if (result < 0) return null;
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
			Location current = enimies.get(i);
			int direction = (grid.get(current)).getDirection();
			Location loc = current.getAdjacentLocation(direction);
			if( loc.equals(getLocation())){
				return 1;
			}else if (current.getAdjacentLocation(direction+45).equals(getLocation()) || 
					current.getAdjacentLocation(direction+90).equals(getLocation()) || 
					current.getAdjacentLocation(direction+270).equals(getLocation()) || 
					current.getAdjacentLocation(direction+360-45).equals(getLocation())
					){
				four5Off = 0;
			}
			
			//enimies.get(i).getAdjacentLocation(direction+45) == getLocation() || enimies.get(i).getAdjacentLocation(direction+90) == getLocation() || enimies.get(i).getAdjacentLocation(direction+270) == getLocation() || enimies.get(i).getAdjacentLocation(direction+360-45) == getLocation()
		}
		return four5Off;
	}
	
	public Location nearEnemy(){
		ArrayList<Location> enimies = x5Targets(getLocation());
		if(enimies.size() == 0 || enimies == null) return null;
		for(int i = 0; i < enimies.size(); i++){
			Location current = enimies.get(i);
			int direction = (grid.get(current)).getDirection();
			Location loc = current.getAdjacentLocation(direction);
			if (current.getAdjacentLocation(direction+45).equals(getLocation()) || 
					current.getAdjacentLocation(direction+90).equals(getLocation()) || 
					current.getAdjacentLocation(direction+270).equals(getLocation()) || 
					current.getAdjacentLocation(direction+360-45).equals(getLocation())
					){
				return current;
			}
			
			//enimies.get(i).getAdjacentLocation(direction+45) == getLocation() || enimies.get(i).getAdjacentLocation(direction+90) == getLocation() || enimies.get(i).getAdjacentLocation(direction+270) == getLocation() || enimies.get(i).getAdjacentLocation(direction+360-45) == getLocation()
		}
		return null;
	}
	
	public ArrayList<Location> killZone(){
		ArrayList<Location> enimies = x5Targets(getLocation());
		if(enimies.size() == 0 || enimies == null) return null;
		
		ArrayList<Location> result = new ArrayList<Location>();
		
		for(int i = 0; i < enimies.size(); i++){
			Location current = enimies.get(i);
			int direction = (grid.get(current)).getDirection();
			Location loc = current.getAdjacentLocation(direction);
			
			result.add(loc);
		}
		return result;
	}
	
	
	private void avoid(){		
		if(killZone().contains(getLocation().getAdjacentLocation(getDirection()))){
			Rock r = new Rock();
			r.putSelfInGrid(grid, getLocation().getAdjacentLocation(getDirection()));
		}
	}
	
	private void fight(){
		if((getLocation().getDirectionToward(nearEnemy())- getDirection() + 360) % 180 > 90) avoid(); 
		turn(nearEnemy());
	}
	
	private void turn(Location loc){
		int direction = getLocation().getDirectionToward(loc);
		int diffrence = direction - getDirection();
		if(diffrence < 0) diffrence += 360;
		System.out.println("direction: " + direction + "   diffrence: " + diffrence);
		//Right
		if(diffrence <= 180){
			if(diffrence < 90){
				setDirection(getDirection() + 45);
			}else{
				setDirection(getDirection() + 90);
			}
		}
		//Left
		else{
			if(diffrence > 270){
				setDirection(getDirection() - 45);
			}else{
				setDirection(getDirection() - 90);
			}
		}
		
	}
	
	private void move(){
		moveTo(getLocation().getAdjacentLocation(getDirection()));
	}
	
	private boolean deadMan(){
		if(!grid.isValid(getLocation().getAdjacentLocation(getDirection()))) return false;
		Actor a = grid.get(getLocation().getAdjacentLocation(getDirection()));
		if (a != null && !(a instanceof Bettle)) return true;
		else return false;
	}
	
	private void kill(Actor target){
		target.removeSelfFromGrid();
		targetActor = grid.get(nearestTarget(getLocation()));
	}
	
	private void kill(){
		Actor target = grid.get(getLocation().getAdjacentLocation(getDirection()));
		target.removeSelfFromGrid();
		try{
			targetActor = grid.get(nearestTarget(getLocation()));
		}catch(Exception e){
			System.out.println("Nothing Left to Hunt.");
		}
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
