package assignment5;

import assignment5.Critter.TestCritter;

public class Critter1 extends TestCritter {
	/**
	 * always has enough energy to reproduce 3 times
	 */
	@Override
	public void doTimeStep() {			
		walk(getRandomInt(8));
		if(getEnergy() < 65) {
			setEnergy(65);
		}
	}
	/**
	 * whenever this critter fights, it reproduces 3 times after running away
	 */
	@Override
	public boolean fight(String opponent) {
		Critter1 crit = new Critter1();
		int direction = getRandomInt(8);
		walk(direction);
		reproduce(crit,direction);
		reproduce(crit,direction);
		reproduce(crit,direction);
		return false;
	}

	@Override
	public String toString () {
		return "1";
	}
	@Override
	public CritterShape viewShape() {
		// TODO Auto-generated method stub
		return null;
	}
}
