package assignment5;

import assignment5.Critter.CritterShape;
import assignment5.Critter.TestCritter;

public class Critter4 extends TestCritter {
	private static int Kills = 0;
	/**
	 * this critter always runs and has a high amount of energy
	 */
	@Override
	public void doTimeStep() {
		if(this.getEnergy() > (1000 - Params.run_energy_cost)) {
			Kills++;
		}
		this.setEnergy(1000);
		this.run(Critter.getRandomInt(9));
	}
	/**
	 * always fights other critters
	 */
	@Override
	public boolean fight(String opponent) {
		this.look(Critter.getRandomInt(9),true);
		return true;
	}
	/** 
	 * outputs onto screen if apex predator kills another critter
	 * @param Critter14
	 */
	public static String runStats(java.util.List<Critter> Critter14) {
		int number = 0;
		for (Object obj : Critter14) {
			number++;
		}
		String st = "";
		st += number + " apexx on the board";
		st += "Has killed " + Kills + " other Critters";
		return st;
	}
	@Override
	public String toString () {
		return "4";
	}
	@Override
	public CritterShape viewShape() {
		return CritterShape.DIAMOND;
	}
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { 
		return javafx.scene.paint.Color.NAVY; 
	}
	@Override
	public javafx.scene.paint.Color viewFillColor() { 
		return javafx.scene.paint.Color.NAVY; 
	}
	
}
