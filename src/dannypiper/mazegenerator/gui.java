package dannypiper.mazegenerator;

import java.io.File;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
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
	public static int XMAX = 1920;
	public static int YMAX = 1000;
	public static final int progressBarY = 10;
	public static final String Version = "1.2.1";
	
	//javaFX
	private final static String font = "Lucida Console";
	private static Stage stage;
	private static Scene inputScene;	
	
	private static VBox vBox;		
	private static HBox entranceExitParametersHBOX;
	private static HBox graphParametersHBOX;
	private static VBox detailsBOX;
	private static HBox buttonHBOX;
	private static HBox procedualHBOX;
	
	//Maze render scene
	private static ProgressBar progress;
	private static Scene renderScene;
	private static Canvas canvas;
	@SuppressWarnings("exports")
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
	private static CheckBox procedualCheckBox;

	//Details
	private static Text fileSelectedText;
	private static Text errorsText;
	
	//Buttons
	private static Button selectImageButton;
	private static Button generateButton;
	
	private boolean validateInput() {
		boolean out = false;
		String errorText = "";
		String procedualGenerationSatus = "Using adjacency matrix";
				
		if(imageFile==null) {
			out=true;
			errorText+="Please select a file.";
		}
		if(width <= 0) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid width.";
		}
		if(height <= 0) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid height.";
		}
		if(height >= mazegen.procedualThreshold || width >= mazegen.procedualThreshold) {
			if(out) {
				errorText+="\n";
			}
			errorText+="Procedual generation must be used.";
			procedualCheckBox.setSelected(true);
			procedualCheckBox.setDisable(true);
			procedualGenerationSatus = "Must procedual generation";
		} else {
			procedualCheckBox.setDisable(false);
		}
		if(procedualCheckBox.isSelected()) {
			procedualGenerationSatus = "Using procedual generation";
		}
		if(EntranceY > height || EntranceY < 0) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid entrance Y.";
		}
		if(ExitY > height || ExitY < 0) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid exit Y.";
		}
		
		if(!out) {
			errorText = "All input valid. Maze Width: " + (width * 2 + 1) + " Maze Height: " + (height * 2 + 1);
			generateButton.setText("Generate Maze\n" + procedualGenerationSatus);
		} else {
			generateButton.setText("Invalid Input");
		}
		
		errorsText.setText(errorText);
		if(!out) {
			System.out.println("Input is: valid");			
		} else {
			System.out.println("Input is: not valid");		
		}		
		
		return out;		
	}

	//Methods	
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
			if(validNumericalParameter(entranceYField.getText())) {
				if(entranceYField.getText().length() > 0 && entranceYField.getText().length() < 8) {
					EntranceY = Integer.valueOf(entranceYField.getText());
				} else {
					EntranceY = 0;
				}				
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
			if(validNumericalParameter(exitYField.getText())) {		
				if(exitYField.getText().length() > 0 && exitYField.getText().length() < 8) {
					ExitY = Integer.valueOf(exitYField.getText());
				} else {
					ExitY = 0;
				}		
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
		EntranceY = 5;
		ExitY = 5;
	}

	private void graphParameters() {
	
		widthLabel = new Text("Graph Width:");
		widthLabel.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		widthField = new TextField("50");
		widthField.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		widthField.setOnKeyTyped(e -> {				
			if(validNumericalParameter(widthField.getText())) {		
				if(widthField.getText().length() > 0 && widthField.getText().length() < 8) {
					width = Integer.valueOf(widthField.getText());
				} else {
					width = 0;
				}		
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
			if(validNumericalParameter(heightField.getText())) {		
				if(heightField.getText().length() > 0 && heightField.getText().length() < 8) {
					height = Integer.valueOf(heightField.getText());
				} else {
					height = 0;
				}		
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
		
		width = 50;
		height = 50;
	}

	private void details() {
		fileSelectedText = new Text("No file selected.");
		fileSelectedText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		errorsText = new Text("Please select a file.");
		errorsText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		
		detailsBOX = new VBox(fileSelectedText, errorsText);
		detailsBOX.setPadding(new Insets(20));
	}
	
	private void procedualCheckBox() {	
		procedualCheckBox = new CheckBox("Procedual Generation (Uses less RAM)");
		procedualCheckBox.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		procedualCheckBox.setSelected(true);
	
		procedualCheckBox.setOnAction(e -> {
			validateInput();
		});
		
		procedualHBOX = new HBox(procedualCheckBox);
		procedualHBOX.setPadding(new Insets(20));
	}

	private void buttons() {
		generateButton = new Button("Invalid input.");
		selectImageButton = new Button("Save Maze As.");
		
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
	
	private void initInputScene() {
		entranceExitParameters();
		graphParameters();
		details();
		buttons();
		procedualCheckBox();
		
		vBox = new VBox(entranceExitParametersHBOX,
				graphParametersHBOX,
				detailsBOX,
				procedualHBOX,
				buttonHBOX);
		vBox.setPadding(new Insets(20));
		inputScene = new Scene(vBox, 500, 400);
	}
	
	private void initGenerationScene() {
		progress = new ProgressBar(0);
		progress.setPrefHeight(progressBarY);
		progress.setMinHeight(progressBarY);
		
		if(width* scale * 2 + scale <= XMAX && height* scale * 2 + scale <= (YMAX - progressBarY)) {
			canvas = new Canvas(width* scale * 2 + scale, height* scale * 2 + scale);
			progress.setPrefWidth(width* scale * 2 + scale);
			vBox = new VBox(progress, canvas);	
			renderScene = new Scene(vBox, width* scale * 2 + scale, height* scale * 2 + scale + progressBarY);			
		} else {
			canvas = new Canvas(XMAX, YMAX);	
			vBox = new VBox(progress, canvas);
			progress.setPrefWidth(XMAX);
			renderScene = new Scene(vBox, XMAX, YMAX + progressBarY);		
		}
		canvas.setOnMouseClicked(e -> {
			if(e.getClickCount()>=2) {
				stage.setFullScreen(true);
			}
		});
		
		vBox.setPadding(new Insets(0));	
		
		stage.setScene(renderScene); 
		stage.setResizable(false);
		stage.setX(0);
		stage.setY(0);			
		stage.setFullScreen(true); //Fullscreen!
		stage.setFullScreenExitHint("ESC to exit fullscreen mode"
				+ "\nDouble click to go fullscreen");
		
		graphicsContext = canvas.getGraphicsContext2D();
	}

	private void generateMaze() {
		
		EntranceY = Integer.valueOf(entranceYField.getText());
		ExitY = Integer.valueOf(exitYField.getText());
		height = Integer.valueOf(heightField.getText());	
		width = Integer.valueOf(widthField.getText());
		
		if(!validateInput()) {
			System.out.println("Canvas GUI");
			
			scale = 1f;
			
			float scalex = XMAX / (width * 2 + 1);
			float scaley = (YMAX - 30) / (height * 2 + 1);
			
			if(scalex < 1 || scaley < 1) {
				scale = 1f;
			} else if(scalex <= scaley) {
				scale = scalex;
			} else {
				scale = scaley;
			}
			
			if (scale * width * 2 + 1 > XMAX) {
				scale = 1f;
			} else if (scale * height * 2 + 1 > YMAX) {
				scale = 1f;
			}
			
			initGenerationScene();

			System.out.println("Started Generation, scale: "+scale);
			mazegen generator = new mazegen(width, height, scale, imageFile, EntranceY, ExitY, procedualCheckBox.isSelected());
			Thread generatorThread = new Thread(generator, "Generator Thread");
			generatorThread.start();
		} else {
			System.out.println("Invalid input");
		}
	}
	
	public static void setProgress(double percentage) {
		progress.setProgress(percentage);
	}

	@Override
	public void start(@SuppressWarnings("exports") Stage arg0) throws Exception {
		stage = arg0;
		
		stage.setRenderScaleX(1);
		stage.setRenderScaleY(1);
		
		String memStatus = "";
		if(Runtime.getRuntime().totalMemory()/1024/1024<700) {
			memStatus += "Low RAM! ";
		}
		memStatus+=Runtime.getRuntime().totalMemory()/1024/1024+"MB RAM Default.";
		stage.setTitle("Maze Generator v"+Version+" - "+memStatus);
		initInputScene();
		
		//Get primary screen and adjust canvas size for it.
		if(Screen.getPrimary() != null){
	  	      System.out.println(Screen.getPrimary().getBounds());
	  	      XMAX = (int) Screen.getPrimary().getBounds().getWidth();
		      YMAX = (int) Screen.getPrimary().getBounds().getHeight();
	   	}
		
		stage.setScene(inputScene);
		stage.show();
		stage.centerOnScreen();

		stage.setOnCloseRequest(e -> {
			System.out.println("Closing program by user request.");
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
