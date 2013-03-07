/*
 * A subclass of Bug that adds the turnAround method and
 * redefines Bug's act method so that this bug
 * makes a U-turn when it can't move
 */

import info.gridworld.actor.Bug;
import java.awt.Color;

public class UTurnBug extends Bug
{
  public UTurnBug()
  {
    setColor(Color.YELLOW);
  }

  public UTurnBug(Color bugColor)
  {
    setColor(bugColor);
  }

  public void turnAround()
  {
    turn(); turn(); turn(); turn(); // Or: setDirection(getDirection() + 180);
  }

  // Overrides Bug's act method
  public void act()
  {
    if (canMove())
        move();
    else
        turnAround();
  }
}
