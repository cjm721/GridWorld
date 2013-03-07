import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;
import java.awt.Color;

//Vinay
public class JumperRunner {
  public static void main(String[] args)
   {
       ActorWorld world = new ActorWorld();
       Jumper bob = new Jumper();
       world.add(new Location(0, 0), bob);
       world.add(new Location(7, 8), new Rock());
       world.add(new Location(3, 3), new Rock());
       world.show();
       world.add(new Location (5,5), new Bettle());
   }
}