import info.gridworld.actor.Bug;
import java.awt.Color;

public class HiBug extends Bug
{
  public HiBug()
  {
    setColor(Color.PINK);
  }

  public HiBug(Color bugColor)
  {
    setColor(bugColor);
  }

  public void turnRandom()
  {
    int angle = 45 * (int)(Math.random() * 8);
    setDirection(getDirection() + angle);
  }
  // Overrides Bug's act method
  public void act()
  {
	  
    if (canMove())
        move();
    
    turnRandom();
  }
}
