import java.awt.Color;

import info.gridworld.actor.*;

public class NewBugs {

	
	public static void main(String[] args) {
	Actor a1ice1 = new Actor();
	Actor alice2 = new Bug();
	Actor alice3 = new Flower();
	Bug bob1 = (Bug) new Actor();
	Flower rose1 = (Flower) new Actor();
	Flower rose2 = new Flower(Color.RED);
	BoxBug boxy1 = new BoxBug(0);
	BoxBug boxy2 = new BoxBug(5);
	}

}
