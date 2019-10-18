package dannypiper.mazegenerator;

import java.io.File;

import javax.swing.GroupLayout.Alignment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class gui extends Application {
	
	//Objects to parse
	public static File imageFile;
	
	//parameters
	private static int width;
	private static int height;
	private static int EntranceY;
	private static int ExitY;
	private static float scale = 1;
	public static final int XMAX = 1920;
	public static final int YMAX = 1050;
	public static final String Version = "1.1.2";
	
	//javaFX
	private final static String font = "Lucida Console";
	private static Stage stage;
	private static Scene inputScene;	
	
	private static VBox vBox;		
	private static HBox entranceExitParametersHBOX;
	private static HBox graphParametersHBOX;
	private static VBox detailsBOX;
	private static HBox buttonHBOX;
	
	private static Scene renderScene;
	private static Canvas canvas;
	public static GraphicsContext graphicsContext;
	
	//Entrance and Exit Parameters
	private static Text entranceYLabel;	
	private static TextField entranceYField;
	private static Text exitYLabel;
	private static TextField exitYField;

	//Graph Parameters
	private static Text widthLabel;
	private static TextField widthField;
	private static Text heightLabel;
	private static TextField heightField;

	//Details
	private static Text fileSelectedText;
	private static Text errorsText;
	
	//Buttons
	private static Button selectImageButton;
	private static Button generateButton;
	
	private boolean validateInput() {
		boolean out = false;
		String errorText = "";
				
		if(imageFile==null) {
			out=true;
			errorText+="Please select a file.";
		}
		if(width < 0) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid width.";
		}
		if(height < 0) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid height";
		}
		if(!(EntranceY <= height)) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid entrance Y.";
		}
		if(!(ExitY <= height)) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid exit Y.";
		}
		
		if(!out) {
			errorText = "All input valid.";
			generateButton.setText("Generate Maze");
		} else {
			generateButton.setText("Invalid Input");
		}
		
		errorsText.setText(errorText);
		
		return out;		
	}

	//Methods	
	private String numOnly(String input) {		
		String temp = "";
		int i = 0;
		for (String c: input.split("")) {
			if(validNumericalParameter(c) && i<=7) {
				temp+=c;
				i++;
			}
		}
		return temp;
	}
	
	private static boolean validNumericalParameter(String parameterStr) {
		String validChars = "0123456789";
		String[] parameterStrArray = parameterStr.split(""); //split after each char
		for (int i = 0; i<parameterStrArray.length; i++) {
			if (!validChars.contains(parameterStrArray[i])) {
				return false;
			}
		}
		return true;
	}
	
	private void openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("Maze Image", "*.png")
			 );
		try {
			File selectedFile = fileChooser.showSaveDialog(stage);
			imageFile = selectedFile;
			if(imageFile!=null) {
				fileSelectedText.setText("Selected '"+imageFile.getName()+"'");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}	
		validateInput();
	}	
	
	private void entranceExitParameters() {
		entranceYLabel = new Text("Entrance Y: ");
		entranceYLabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		entranceYField = new TextField("5");
		entranceYField.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		entranceYField.setOnKeyTyped(e -> {
			int pos = entranceYField.getCaretPosition();
			
			entranceYField.setText(numOnly(entranceYField.getText()));
			
			if(entranceYField.getText().length() > pos+1) {
				entranceYField.positionCaret(pos + 1);
			} else if (entranceYField.getText().length() > pos){
				entranceYField.positionCaret(pos);
			}
			
			if(entranceYField.getText().length() > 0) {
				EntranceY = Integer.valueOf(entranceYField.getText());
			} else {
				EntranceY = 0;
			}
			validateInput();
		});
		
		exitYLabel = new Text("  Exit Y :");
		exitYLabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		exitYField = new TextField("5");	
		exitYField.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		exitYField.setOnKeyTyped(e -> {
			int pos = exitYField.getCaretPosition();
			
			exitYField.setText(numOnly(exitYField.getText()));
			
			if(exitYField.getText().length() > pos+1) {
				exitYField.positionCaret(pos + 1);
			} else if (exitYField.getText().length() > pos){
				exitYField.positionCaret(pos);
			} 
			
			if(exitYField.getText().length() > 0) {
				ExitY = Integer.valueOf(exitYField.getText());
			} else {
				ExitY = 0;
			}
			validateInput();
		});
		entranceExitParametersHBOX = new HBox(entranceYLabel,
				entranceYField,
				exitYLabel,
				exitYField);
		entranceExitParametersHBOX.setPadding(new Insets(20));
	}

	private void graphParameters() {
	
		widthLabel = new Text("Graph Width:");
		widthLabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		widthField = new TextField("50");
		widthField.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		widthField.setOnKeyTyped(e -> {
			int pos = widthField.getCaretPosition();
			
			widthField.setText(numOnly(widthField.getText()));
			
			if(widthField.getText().length() > pos+1) {
				widthField.positionCaret(pos + 1);
			} else if (widthField.getText().length() > pos){
				widthField.positionCaret(pos);
			} 
				
			if(widthField.getText().length() > 0) {
				width = Integer.valueOf(widthField.getText());
			} else {
				width = 0;
			}
			validateInput();
		});
		
		heightLabel = new Text("  Graph Height:");
		heightLabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		heightField = new TextField("50");	
		heightField.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		heightField.setOnKeyTyped(e -> {
			int pos = heightField.getCaretPosition();
			
			heightField.setText(numOnly(heightField.getText()));
			
			if(heightField.getText().length() > pos+1) {
				heightField.positionCaret(pos + 1);
			} else if (heightField.getText().length() > pos){
				heightField.positionCaret(pos);
			} 	
				
			if(heightField.getText().length() > 0) {
				height = Integer.valueOf(heightField.getText());	
			} else {
				height = 0;
			}
			validateInput();
		});
		
		graphParametersHBOX = new HBox(widthLabel,	
				widthField,
				heightLabel,
				heightField);
		graphParametersHBOX.setPadding(new Insets(20));
	}

	private void details() {
		fileSelectedText = new Text("No file selected.");
		fileSelectedText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		errorsText = new Text("Please select a file.");
		errorsText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		detailsBOX = new VBox(fileSelectedText, errorsText);
		detailsBOX.setPadding(new Insets(20));
	}
	
	private void buttons() {
		generateButton = new Button("Invalid input");
		selectImageButton = new Button("Save Maze As");
		
		generateButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		selectImageButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		generateButton.setOnAction(e -> {
			if(!validateInput()) {
				generateMaze();
			}
		});
		
        selectImageButton.setOnAction(e -> {
        	openFileChooser();
        });
        
        buttonHBOX = new HBox(selectImageButton, generateButton);
        buttonHBOX.setPadding(new Insets(20));
	}
	
	private void initScene() {
		entranceExitParameters();
		graphParameters();
		details();
		buttons();
		vBox = new VBox(entranceExitParametersHBOX,
				graphParametersHBOX,
				detailsBOX,
				buttonHBOX);
		vBox.setPadding(new Insets(20));
	}
	
	private void generationGUI() {
		canvas = new Canvas(XMAX, YMAX);			
		
		vBox = new VBox(canvas);
		vBox.setPadding(new Insets(0));
		
		renderScene = new Scene(vBox, XMAX, YMAX+30);
		
		stage.setScene(renderScene); 
		stage.setResizable(false);
		stage.setX(0);
		stage.setY(0);			
		stage.setFullScreen(true); //Fullscreen!
		
		graphicsContext = canvas.getGraphicsContext2D();
	}

	private void generateMaze() {
		
		EntranceY = Integer.valueOf(entranceYField.getText());
		ExitY = Integer.valueOf(exitYField.getText());
		height = Integer.valueOf(heightField.getText());	
		width = Integer.valueOf(widthField.getText());
		
		if(!validateInput()) {
			System.out.println("Canvas GUI");
			
			float scalex = XMAX / (width * 2 + 1);
			float scaley = YMAX / (height * 2 + 1);
			
			if(scalex < 1 || scaley < 1) {
				scale = 1f;
			} else if(scalex <= scaley) {
				scale = scalex;
			} else {
				scale = scaley;
			}
			
			if(scale > 20) {
				scale = 20f;
			}
			
			generationGUI();

			System.out.println("Started Generation, scale: "+scale);
			mazegen generator = new mazegen(width, height, scale, imageFile, EntranceY, ExitY);
			Thread generatorThread = new Thread(generator, "Generator Thread");
			generatorThread.start();
		} else {
			System.out.println("Invalid input");
		}
	}

	@Override
	public void start(Stage arg0) throws Exception {
		stage = arg0;
		
		String memStatus = "";
		if(Runtime.getRuntime().totalMemory()/1024/1024<1500) {
			memStatus += "Low RAM! ";
		}
		memStatus+=Runtime.getRuntime().totalMemory()/1024/1024/1024+"GB RAM Default.";
		stage.setTitle("Maze Generator v"+Version+" - "+memStatus);
		initScene();
		
		inputScene = new Scene(vBox, 500, 300);
		stage.setScene(inputScene);
		stage.show();

		stage.setOnCloseRequest(e -> {
			System.exit(1); //User closed program
		});
	}
	
	public static void main(String[] args) {
		System.out.println("GUI Initialising...");

		launch();
		
		System.out.println("GUI Initialisation finished.");
		System.out.println("Wait for gui events.");
	}
}
