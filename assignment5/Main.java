package assignment5;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Main extends Application {
	static GridPane grid = new GridPane();
	@Override
		public void start(Stage primaryStage) {
			
			try {			

				grid.setGridLinesVisible(false);

				Button create = new Button("Create");
				Button step = new Button("Step");
				Button quit = new Button("Quit");
				Button seed = new Button("Seed");
				Button stats = new Button("Statistics");
				Button show = new Button("Show");
				
				TextField statstxt = new TextField();
				TextField seedtxt = new TextField();
				TextField createtxt = new TextField();
				TextField steptxt = new TextField();				
				steptxt.textProperty().addListener((observable,oldValue,newValue) -> {
					if(!newValue.matches("\\d")) {
						steptxt.setText(newValue.replaceAll("[^\\d]",  ""));
					}
				});
				seedtxt.textProperty().addListener((observable,oldValue,newValue) -> {
					if(!newValue.matches("\\d")) {
						seedtxt.setText(newValue.replaceAll("[^\\d]",  ""));
					}
				});	
				int size = 600;
				Scene scene = new Scene(grid, size+225, size+110);
				grid.add(create, 0, Params.world_height);
				grid.add(createtxt,1,Params.world_height);
				grid.add(step,0,Params.world_height+1);
				grid.add(steptxt, 1, Params.world_height+1);
				grid.add(quit, 0, Params.world_height+2);
				grid.add(show,0,Params.world_height+3);	//debug func
				paintGridLines(grid);

				
				
				Timeline tl = new Timeline();
				tl.setCycleCount(Animation.INDEFINITE);
		        KeyFrame stepframe = new KeyFrame(Duration.seconds(1),
		                new EventHandler<ActionEvent>() {
		                    public void handle(ActionEvent event) {
		                    		//animation stuff
		                    		Critter.displayWorld(grid);
		            				grid.add(create, Params.world_width, Params.world_height);
		            				grid.add(createtxt,Params.world_width+1,Params.world_height);
		            				grid.add(step,Params.world_width,Params.world_height+1);
		            				grid.add(steptxt, Params.world_width+1, Params.world_height+1);
		            				grid.add(quit, Params.world_width, Params.world_height+2);
		            				grid.add(show,Params.world_width,Params.world_height+3);	//debug func
		                    }
		                });


		        tl.getKeyFrames().add(stepframe);
		        tl.play();
				
				primaryStage.setScene(scene);
				primaryStage.show();

				// Paints the icons.
				//paint();
				show.setOnAction(new EventHandler<ActionEvent>() {	//debug function
				    @Override public void handle(ActionEvent e) {
				    		Critter.displayWorld(grid);

				    }
				});
				seed.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				        int seed = Integer.parseInt(seedtxt.getText());
				        Critter.setSeed(seed);
				    }
				});

				create.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	try {
				    		String critClass = createtxt.getText();
				    		critClass.trim();
							Critter.makeCritter(critClass);
						} catch (InvalidCritterException e1) {
							create.setText("InvalidCritterClass");
						}
				    	
				    }
				});
				stats.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	try {
				    	String critClass = statstxt.getText();
				    	critClass.trim();
				    	List<Critter> result;
						result = Critter.getInstances(critClass);
		    			String pack = Critter.class.getPackage().getName() + ".";
		    			pack += critClass;
		    			Class C = Class.forName(pack);
		    			Critter crit = (Critter) C.newInstance();
		    			Method method = crit.getClass().getMethod("runStats",List.class);
		    			method.invoke(crit, result);
						}
		    			 catch (InvalidCritterException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (InstantiationException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalAccessException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (NoSuchMethodException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (SecurityException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (IllegalArgumentException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				    }
				    
				    
				});
				step.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	int steping = Integer.parseInt(steptxt.getText());
				    	for(int i = 0; i < steping; i++) {
				    		Critter.worldTimeStep();
				    	}
				    	step.setText(steptxt.getText());	//steps
				    }
				});
				quit.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				        System.exit(0);
				    }
				});
			} catch(Exception e) {
				e.printStackTrace();		
			}
		}
		
		public static void main(String[] args) {
			launch(args);
		}


	public static void paint() {
		Main.grid.getChildren().clear(); // clean up grid.
		for (int i = 0; i <= 1; i++) {
			Shape s = getIcon(i);	// convert the index to an icon.
			Main.grid.add(s, i, i); // add the shape to the grid.
		}
		
	}
	
	protected static void paintGridLines(GridPane grid) {
		int size = 552;
		for (int r = 0; r < Params.world_width; r++)
			for (int c = 0; c < Params.world_height; c++) {
				Shape s = new Rectangle(size/Params.world_height, size/Params.world_height);
				s.setFill(null);
				s.setStroke(Color.BLUEVIOLET);
				grid.add(s, c, r);
			}

	}
	
	static Shape getIcon(int shapeIndex) {
		Shape s = null;
		int size = 100;
		
		switch(shapeIndex) {
		case 0: s = new Rectangle(size, size); 
			s.setFill(javafx.scene.paint.Color.RED); break;
		case 1: s = new Circle(size/2); 
			s.setFill(javafx.scene.paint.Color.GREEN); break;
		}
		// set the outline of the shape
		s.setStroke(javafx.scene.paint.Color.BLUE); // outline
		return s;
	}
}
