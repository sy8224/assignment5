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

				
				TextField statstxt = new TextField();
				TextField statsres = new TextField();
				TextField seedtxt = new TextField();
				TextField createtxt = new TextField();
				TextField steptxt = new TextField();			
				statsres.setDisable(true);

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

				paintGridLines(grid);

				
				Timeline tl = new Timeline();
				tl.setCycleCount(Animation.INDEFINITE);
		        KeyFrame stepframe = new KeyFrame(Duration.seconds(0.75),
		                new EventHandler<ActionEvent>() {
		                    public void handle(ActionEvent event) {
		                    		//animation stuff
		                    		Critter.displayWorld(grid);
		            				grid.add(create, Params.world_width, Params.world_height);
		            				grid.add(createtxt,Params.world_width+1,Params.world_height);
		            				grid.add(step,Params.world_width,Params.world_height+1);
		            				grid.add(steptxt, Params.world_width+1, Params.world_height+1);
		            				grid.add(stats,Params.world_width,Params.world_height+2);
		            				grid.add(statstxt, Params.world_width+1, Params.world_height+2);
		            				grid.add(statsres, Params.world_width+1, Params.world_height+3);
		            				grid.add(seed, Params.world_width, Params.world_height+4);
		            				grid.add(seedtxt, Params.world_width+1, Params.world_height+4);
		            				grid.add(quit, Params.world_width, Params.world_height+5);
		                    }
		                });

		        tl.getKeyFrames().add(stepframe);
		        tl.play();
		       
				primaryStage.setScene(scene);
				
				
				
				primaryStage.show();

				// Paints the icons.
				//paint();

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
				    		String txtresult;
				    	String critClass = statstxt.getText();
				    	critClass.trim();
				    	List<Critter> result;
						result = Critter.getInstances(critClass);
		    			String pack = Critter.class.getPackage().getName() + ".";
		    			pack += critClass;
		    			Class C = Class.forName(pack);
		    			Critter crit = (Critter) C.newInstance();
		    			Method method = crit.getClass().getMethod("runStats",List.class);
		    			txtresult = (String) method.invoke(crit, result);
		    			statsres.setText(txtresult);
						}
		    			 catch (InvalidCritterException e1) {
								stats.setText("InvalidCritterClass");
							} catch (ClassNotFoundException e1) {
								stats.setText("InvalidCritterClass");
						} catch (InstantiationException e1) {
							stats.setText("InvalidCritterClass");
							} catch (IllegalAccessException e1) {
								stats.setText("InvalidCritterClass");
							} catch (NoSuchMethodException e1) {
								stats.setText("InvalidCritterClass");
							} catch (SecurityException e1) {
								stats.setText("InvalidCritterClass");
							} catch (IllegalArgumentException e1) {
								stats.setText("InvalidCritterClass");
							} catch (InvocationTargetException e1) {
								stats.setText("InvalidCritterClass");
							}
				    }
				    
				    
				});
				step.setOnAction(new EventHandler<ActionEvent>() {
				    @Override public void handle(ActionEvent e) {
				    	int steping = Integer.parseInt(steptxt.getText());
				    	for(int i = 0; i < steping; i++) {
			    		Critter.worldTimeStep();
			    		Critter.displayWorld(grid);
				    	}

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
	
}