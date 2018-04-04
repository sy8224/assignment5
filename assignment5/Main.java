package assignment5;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;



public class Main extends Application {
	static GridPane grid = new GridPane();
	@Override
		public void start(Stage primaryStage) {
			try {			

				grid.setGridLinesVisible(true);

				Scene scene = new Scene(grid, 500, 500);
				primaryStage.setScene(scene);
				
				primaryStage.show();
				
				// Paints the icons.
				paint();
				
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