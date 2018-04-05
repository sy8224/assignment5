package assignment5;

import java.util.Iterator;
import java.util.List;

import assignment5.Algae;
import assignment5.Critter;
import assignment5.InvalidCritterException;
import assignment5.Params;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.WHITE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static List<Critter> remove = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected final String look(int direction, boolean steps) {
		int x_origin = this.x_coord;
		int y_origin = this.y_coord;
		if(steps == false) {
			movement(direction);
		}else if(steps == true) {
			movement(direction);
			movement(direction);
		}
		for(int i = 0;i < population.size();i++) {
				if(detectCollision(this,population.get(i)) && this != population.get(i)) {
					this.x_coord = x_origin;
					this.y_coord = y_origin;
					return population.get(i).toString();
				}
		}
		this.x_coord = x_origin;
		this.y_coord = y_origin;
		return null;

	}
	
	/* rest is unchanged from Project 4 */
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean moved;

	/**
	 * walks in a straight line that is the current critter's current coordinates
	 * @param direction
	 */
	protected final void movement(int direction) {
		switch(direction) {
		case 0: this.x_coord += 1;
				break;
		case 1: this.x_coord += 1;
				this.y_coord -= 1;
				break;
		case 2: this.y_coord -= 1;
				break;
		case 3: this.x_coord -= 1;
				this.y_coord -= 1;
				break;
		case 4: this.x_coord -= 1;
				break;
		case 5: this.x_coord -= 1;
				this.y_coord += 1;
				break;
		case 6: this.y_coord += 1;
				break;
		case 7: this.x_coord += 1;
				this.y_coord += 1;
				break;
		}
	}
	protected final void walk(int direction) {
		this.energy -= Params.walk_energy_cost;
		if(this.moved == true) {

			return;
		}
		if(getEnergy() < Params.walk_energy_cost) {
			remove.add(this);
			return;
		}
		this.moved = true;
		movement(direction);
		this.x_coord = (this.x_coord % Params.world_width);
		this.y_coord = (this.y_coord % Params.world_height);
		if(this.x_coord < 0) {
			this.x_coord += Params.world_width;
		}
		if(this.y_coord < 0) {
			this.y_coord += Params.world_height;
		}
	}
	/**
	 * similar to walk, but the direction is extended to distance of two. Can only walk a straight path
	 * @param direction
	 */
	protected final void run(int direction) {
		if(getEnergy() < Params.run_energy_cost) {
			remove.add(this);
			return;
		}
		this.energy -= Params.run_energy_cost;
		if(this.moved == true) {
			return;
		}
		this.moved = true;
		movement(direction);
		movement(direction);
		this.x_coord = (this.x_coord % Params.world_width);
		this.y_coord = (this.y_coord % Params.world_height);
		if(this.x_coord < 0) {
			this.x_coord += Params.world_width;
		}
		if(this.y_coord < 0) {
			this.y_coord += Params.world_height;
		}if(this.fightRun == false) {
			boolean check = false;;
			for(int i = 0;i < population.size();i++) {
				for(int j = 0;j<i;j++) {
					if(detectCollision(population.get(i),population.get(j))) {
						check = true;
						break;
					}
				}
			}
			if(check == true) {//you cannot fast travel when enemies are near by
				movement(direction);
				movement(direction);
			this.x_coord = (this.x_coord % Params.world_width);
			this.y_coord = (this.y_coord % Params.world_height);
			if(this.x_coord < 0) {
				this.x_coord += Params.world_width;
			}
			if(this.y_coord < 0) {
				this.y_coord += Params.world_height;
			}
			}
		}
	}
	/**
	 * passes the baby offspring and creates the new critter in the specified direction, updates energy of both parent and new baby critter
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(getEnergy() < Params.min_reproduce_energy) {
			return;
		}
		offspring.x_coord = this.x_coord;
		offspring.y_coord = this.y_coord;
		offspring.fightRun = true;
		if(this.energy %2==1) {
			offspring.energy = (int)((this.energy+1)*0.5);
		}
		else {
			offspring.energy = (int)((this.energy)*0.5);
		}
		this.energy = (int)(0.5*this.energy);
		offspring.walk(direction);
		babies.add(offspring);
	}


	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			String pack = Critter.class.getPackage().getName() + ".";
			pack += critter_class_name;
			Class C = Class.forName(pack);
			Critter crit = (Critter) C.newInstance();
			crit.energy = Params.start_energy;
			crit.x_coord = getRandomInt(Params.world_width);
			crit.y_coord = getRandomInt(Params.world_height);
			crit.fightRun = true;
			population.add(crit);
		}	catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		try {
		List<Critter> result = new java.util.ArrayList<Critter>();
		String classed;
		String pack = Critter.class.getPackage().getName() + ".";
		String pack2 = pack + critter_class_name;
		Class C = Class.forName(pack2);
		for(Iterator iter = population.iterator(); iter.hasNext();) {
			Critter crit = (Critter) iter.next();			
			classed = crit.getClass().toString();
			classed = classed.replaceFirst("class ", "");
			classed = classed.replaceFirst(pack, "");
			if(classed.equals(critter_class_name)) {
				result.add(crit);
			}
		}
		return result;
		}catch(Exception e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static String runStats(List<Critter> critters) {
		String statstics = "";
		//System.out.print("" + critters.size() + " critters as follows -- ");
		statstics += critters.size() + " critters as follows -- ";
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			statstics += prefix + s + ":" + critter_count.get(s);
			//prefix = ", ";
			statstics += ", ";
		}
		return statstics;
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	private boolean fightRun;
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		// Complete this method.
		population.clear();
		babies.clear();
		remove.clear();
	}	
	/**
	 * given two critters, detects whether they are in the same location
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static boolean detectCollision(Critter c1, Critter c2) {
		if((population.contains(c1)==false)||(population.contains(c2))==false) {
			return false;
		}
		if((c1.x_coord == c2.x_coord)&&(c1.y_coord == c2.y_coord)) {
			return true;
		}
		return false;
	}
	/**
	 * given that two characters have the same location, the fight functions must be called and resolved
	 * @param c1
	 * @param c2
	 */
	public static void resolveEncounter(Critter c1, Critter c2) {
		boolean c1Fight = c1.fight(c2.toString());
		c1.fightRun = c1Fight;
		cullDead();
		if(!detectCollision(c1,c2)) {
			c1.fightRun = true;
			return;
		}
		boolean c2Fight = c2.fight(c1.toString());
		c2.fightRun = c2Fight;
		if(!detectCollision(c1,c2)) {
			c2.fightRun = true;
			return;
		}
		cullDead();
		if(detectCollision(c1,c2)) {
			int result1 = getRandomInt(c1.getEnergy());
			int result2 = getRandomInt(c2.getEnergy());
			if((result1 > result2)||(result1==result2)) {
				c1.energy = (int) (c1.energy + 0.5 * c2.energy);
				population.remove(c2);
			}
			else {
				c2.energy = (int)(c1.energy + 0.5*c2.energy);
				population.remove(c1);
			}
		}
		else {
			return;
		}
	}
	/**
	 * function that handles all encounters - an encounter occurs if a critter shares the same location with another critter
	 * @return
	 */
	//removing a critter during the loop my cause the size of the population to become less than i
	public static boolean doEncounters() {
		boolean check = false;;
		for(int i = 0;i < population.size();i++) {
			for(int j = 0;j<i;j++) {
				if(detectCollision(population.get(i),population.get(j))) {
					check = true;
					resolveEncounter(population.get(i),population.get(j));
					i = 0;
					j = 0;
				}
			}
		}
		return check;
	}
	/**
	 * scans through all the critters, removing any critters that have < 0 energy, meaning they are dead
	 */
	public static void cullDead() {
		for(int i = 0;i<population.size();i++) {
			if(population.get(i).getEnergy()<=0) {
				population.remove(population.get(i));
				i = 0;
			}
		}
	}
	/**
	 * called at the end of every worldTimeStep to update all critters energy at the end of each turn
	 */
	public static void updateRestEnergy() {
		for(int i = 0;i < population.size();i++) {
			population.get(i).energy = population.get(i).energy - Params.rest_energy_cost;
		}
	}
	/**
	 * generates random algae on the map
	 */
	public static void genAlgae() {
		for(int i = 0;i<Params.refresh_algae_count;i++) {
			Algae food = new Algae();
			food.setX_coord(getRandomInt(Params.world_width-1));
			food.setY_coord(getRandomInt(Params.world_height-1));
			population.add(food);
		}
	}

	public static void worldTimeStep() {
		// Complete this method.
		for(Iterator<Critter> iter = population.iterator(); iter.hasNext();) {
			Critter current = iter.next();
			current.moved = false;
			current.doTimeStep();
			if(remove.contains(current)) {
				iter.remove();
			}
		}
		while(doEncounters()==true) {	
		}
		updateRestEnergy();
		cullDead();
		//genAlgae();
		//Encounters here
		//check for death after each encounter? or in the fight functions
		population.addAll(babies);
		babies.clear();
	}
	/**
	 * function that displays the critters and borders of the world
	 */
	public static void displayWorld(GridPane grid) {
		// Complete this method.
		grid.getChildren().clear();
		
		Main.paintGridLines(grid);
				for(Critter crit : population) {
						switch(crit.viewShape()) {
						case CIRCLE:
							Circle CritCircle = new Circle(552/Params.world_height,552/Params.world_height,276/Params.world_height);
							CritCircle.setStroke(crit.viewFillColor());
							CritCircle.setFill(crit.viewFillColor());
							//edit creation to fit the size of the grid square
							Main.grid.add(CritCircle, crit.y_coord, crit.x_coord);
								break;
						case SQUARE:
							Rectangle CritSquare = new Rectangle(552/Params.world_height,552/Params.world_height);
							//CritSquare.setStroke(crit.viewOutlineColor());
							//CritSquare.setFill(crit.viewFillColor());
							Main.grid.add(CritSquare, crit.y_coord, crit.x_coord);
							break;
						case TRIANGLE:
							Polygon polygon = new Polygon();
							polygon.getPoints().addAll(new Double[]{
								    12.5, 0.0,
								    0.0, 25.0,
								    26.0, 25.0 });
							Main.grid.add(polygon, crit.y_coord, crit.x_coord);
							break;
						case DIAMOND:
							Polygon polygon1 = new Polygon();
							polygon1.getPoints().addAll(new Double[]{
								    13.5,0.0,
								    27.0,13.5,
								    13.5,27.0,
								    0.0,13.5});
							Main.grid.add(polygon1, crit.y_coord, crit.x_coord);
							break;
						case STAR:
							Polygon polygon2 = new Polygon();
							polygon2.getPoints().addAll(new Double[]{
								    13.5,0.0,
								   	18.0,9.0,
								    27.0,10.0,
								    19.0,17.0,
								    22.0,27.0,
								    13.5,19.0,
								    5.0,27.0,
								    7.5,17.0,
								    0.0,10.0,
								    9.0,9.0});
							Main.grid.add(polygon2, crit.y_coord, crit.x_coord);
							break;
							//Add shape to grid
						}
				}
	}

	
	/*public static void displayWorld() {
		// Complete this method.
		WorldEdge(Params.world_width);
		for(int y= 0; y < Params.world_height;y++) {
			System.out.print("|");
			//print contents of the world
			for(int x = 0; x < Params.world_width;x++) {
				String loc = " ";
				for(Critter crit : population) {
					if(crit.x_coord == x && crit.y_coord == y) {
						loc = crit.toString();
					}
				}
			System.out.print(loc);
			}
		System.out.println("|");
		}
		WorldEdge(Params.world_width);
	}*/
	
	
}
