package assignment5;

import assignment5.Critter.CritterShape;
import assignment5.Critter.TestCritter;

public class Critter3 extends TestCritter {
	/**
	 * this critter will reproduce every timestep until it dies
	 */
	@Override
	public void doTimeStep() {
		Critter3 offspring = new Critter3();
		Critter3 offspring2 = new Critter3();
		if(this.look(Critter.getRandomInt(9),false) == null) {
		int randomDir = Critter.getRandomInt(8);
		int randomDir2 = Critter.getRandomInt(8);
		this.reproduce(offspring, randomDir);
		this.reproduce(offspring2, randomDir2);
		}
	}
	/**
	 * always fights
	 */
	@Override
	public boolean fight(String opponent) {
		return true;
	}
	
	@Override
	public String toString () {
		return "3";
	}
	@Override
	public CritterShape viewShape() {
		return CritterShape.SQUARE;
	}
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { 
		return javafx.scene.paint.Color.BLACK; 
	}
	@Override
	public javafx.scene.paint.Color viewFillColor() { 
		return javafx.scene.paint.Color.BLACK; 
	}
}
