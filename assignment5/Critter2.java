package assignment5;

import java.util.*;

public class Critter2 extends Critter.TestCritter {
	/**
	 * runs in random directions
	 */
	@Override
	public void doTimeStep() {
		run(getRandomInt(8));
	}
	/**
	 * only fights other critters of the same class, otherwise runs away
	 */
	@Override
	public boolean fight(String opponent) {
		if(opponent.equals("Cannibal")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public String toString() {
		return "2";
	}
	
	public void test (List<Critter> l) {
		
	}
	@Override
	public CritterShape viewShape() {
		// TODO Auto-generated method stub
		return null;
	}
}
