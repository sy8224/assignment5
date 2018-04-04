package assignment4;

import assignment4.Critter.TestCritter;

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
		return true;
	}
	/** 
	 * outputs onto screen if apex predator kills another critter
	 * @param Critter14
	 */
	public static void runStats(java.util.List<Critter> Critter14) {
		int number = 0;
		for (Object obj : Critter14) {
			number++;
		}
		System.out.println(number + " apexs on the board");
		System.out.println("Has killed " + Kills + " other Critters");
	}
	@Override
	public String toString () {
		return "4";
	}
}
