package dannypiper.mazegenerator;

import java.io.File;

import dannypiper.mazegenerator.kuskals.sorting.sortType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class gui extends Application {
	
	private static CheckBox primmsCheckBox;
	
	//Objects to parse
	public static File imageFile;
	public static boolean darkModeToggle;
	
	//parameters
	private static int width;
	private static int height;
	private static int EntranceY;
	private static int ExitY;
	private static float scale = 1;
	public static int XMAX = 1920;
	public static int YMAX = 1000;
	public static final int progressBarY = 10;
	public static final String Version = "1.2.2";
	
	//javaFX
	private final static String font = "Lucida Console";
	private static Stage stage;
	private static Scene inputScene;	
	
	private static VBox vBox;	
	private static VBox kruskalsRadioBoxes;		
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
	private static CheckBox primmsTypeCheckBox;

	//Details
	private static Text fileSelectedText;
	private static Text errorsText;
	
	//Buttons
	private static Button selectImageButton;
	private static Button generateButton;
	
	//Kruskals radioButtons
	private static Text radioButtonLabels;
	private static RadioButton bubbleSort; 
	private static RadioButton insersionSort;
	private static RadioButton quickSort; 
	private static RadioButton countingSort; 
	private static ToggleGroup group;
	
	//Kruskals Methods
	private void setUpRadioButtons() {
		group = new ToggleGroup();
		
		radioButtonLabels = new Text("Select Sorting Type");
		bubbleSort = new RadioButton("Bubble Sort");
		insersionSort = new RadioButton("Insersion Sort");
		quickSort = new RadioButton("Quick Sort");		
		countingSort = new RadioButton("Couting Sort");
		
		bubbleSort.setToggleGroup(group);
		insersionSort.setToggleGroup(group);
		quickSort.setToggleGroup(group);
		countingSort.setToggleGroup(group);
		countingSort.setSelected(true);

		radioButtonLabels.setFill(Color.WHITE);
		radioButtonLabels.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));

		bubbleSort.setStyle("-fx-text-fill: white;");	
		bubbleSort.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));

		insersionSort.setStyle("-fx-text-fill: white;");	
		insersionSort.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));

		quickSort.setStyle("-fx-text-fill: white;");	
		quickSort.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));

		countingSort.setStyle("-fx-text-fill: white;");	
		countingSort.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));		
		
		kruskalsRadioBoxes = new VBox(radioButtonLabels, bubbleSort, insersionSort, quickSort, countingSort);
		kruskalsRadioBoxes.setPadding(new Insets(5));
	}
	
	private void updateGUI() {
		kruskalsRadioBoxes.setVisible(!primmsCheckBox.isSelected());
		primmsTypeCheckBox.setVisible(primmsCheckBox.isSelected());
	}	
	
	//Primms Methods
	@SuppressWarnings("unused")
	private boolean validateInput() {
		boolean out = false;
		String errorText = "";
		String procedualGenerationStatus = "Using adjacency matrix";
				
		if(imageFile==null) {
			out=true;
			errorText+="Please select a file.";
			fileSelectedText.setFill(Color.DARKORANGE);
		} else {
			fileSelectedText.setFill(Color.LIGHTGREEN);
		}
		if(width <= 1) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid width.";
		}
		if(height <= 1) {
			if(out) {
				errorText+="\n";
			}
			out = true;
			errorText+="Invalid height.";
		}
		if((height >= mazegen.procedualThreshold || width >= mazegen.procedualThreshold) && primmsCheckBox.isSelected()) {
			if(out) {
				errorText+="\n";
			}
			errorText+="Procedual generation must be used.";
			primmsTypeCheckBox.setSelected(true);
			primmsTypeCheckBox.setDisable(true);
			procedualGenerationStatus = "Must procedual generation";
		} else {
			primmsTypeCheckBox.setDisable(false);
		}
		if(primmsTypeCheckBox.isSelected() && primmsCheckBox.isSelected()) {
			procedualGenerationStatus = "Using procedual generation";
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
			errorsText.setFill(Color.LIGHTGREEN);	
			generateButton.setText("Generate Maze");
			generateButton.setTextFill(Color.LIGHTGREEN);
		} else {
			generateButton.setText("Invalid Input");
			errorsText.setFill(Color.DARKRED);		
			generateButton.setTextFill(Color.WHITE);	
		}
		
		errorsText.setText(errorText);
		if(!out) {
			System.out.println("Input is: valid");		
		} else {
			System.out.println("Input is: not valid");	
		}		
		
		return out;		
	}
	
	private void openFileChooser() {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.getExtensionFilters().addAll(
			     new FileChooser.ExtensionFilter("Maze Image", "*.png"),
			     new FileChooser.ExtensionFilter("Maze Image", "*.jpeg")
			 );
		try {
			File selectedFile = fileChooser.showSaveDialog(stage);
			if(selectedFile != null) {
				imageFile = selectedFile;	//Keeps old file if dialog is closed
			}
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
			if(validation.validNumericalParameter(entranceYField.getText())) {
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
			if(validation.validNumericalParameter(exitYField.getText())) {		
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
			if(validation.validNumericalParameter(widthField.getText())) {		
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
			if(validation.validNumericalParameter(heightField.getText())) {		
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
		fileSelectedText.setFill(Color.DARKORANGE);
		
		errorsText = new Text("Please select a file.");
		errorsText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		errorsText.setFill(Color.DARKRED);
		
		detailsBOX = new VBox(fileSelectedText, errorsText);
		detailsBOX.setPadding(new Insets(20));
	}

	//Primms
	private void checkBoxes() {	
		primmsTypeCheckBox = new CheckBox("Procedual Generation (Uses less RAM)");
		primmsTypeCheckBox.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		primmsTypeCheckBox.setSelected(true);
	
		primmsTypeCheckBox.setOnAction(e -> {
			validateInput();
		});
		
		primmsCheckBox = new CheckBox("Use Primm's Algorithm");
		primmsCheckBox.setStyle("-fx-text-fill: white;");	
		primmsCheckBox.setSelected ( false );
		primmsCheckBox.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		primmsCheckBox.setOnMouseClicked(e -> {
			updateGUI();
		});
		
		procedualHBOX = new HBox(primmsTypeCheckBox);
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
	
	private void darkMode() {
		double greyConstant = 0.20d;
		double greyConstantAccent = 0.3d;
		
		Background darkMode = new Background(new BackgroundFill(new Color(greyConstant, greyConstant, greyConstant ,1d)
				, CornerRadii.EMPTY, Insets.EMPTY));
		
		Background darkModeAccent = new Background(new BackgroundFill(new Color(greyConstantAccent, greyConstantAccent
				, greyConstantAccent ,1d), new CornerRadii(8d), Insets.EMPTY));
		
		
		entranceExitParametersHBOX.setBackground(darkMode);
		graphParametersHBOX.setBackground(darkMode);
		detailsBOX.setBackground(darkModeAccent);
		procedualHBOX.setBackground(darkMode);
		buttonHBOX.setBackground(darkMode);	
		vBox.setBackground(darkMode);
		
		widthField.setBackground(darkModeAccent);
		widthField.setStyle("-fx-text-fill: white;");
		
		heightField.setBackground(darkModeAccent);
		heightField.setStyle("-fx-text-fill: white;");
		
		exitYField.setBackground(darkModeAccent);
		exitYField.setStyle("-fx-text-fill: white;");
		
		entranceYField.setBackground(darkModeAccent);
		entranceYField.setStyle("-fx-text-fill: white;");
		
		primmsTypeCheckBox.setTextFill(Color.WHITE);
		widthLabel.setFill(Color.WHITE);
		heightLabel.setFill(Color.WHITE);
		entranceYLabel.setFill(Color.WHITE);
		exitYLabel.setFill(Color.WHITE);
		
		primmsTypeCheckBox.setBackground(darkModeAccent);
		procedualHBOX.setPadding(new Insets(10));
		
		primmsCheckBox.setBackground(darkModeAccent);
		
		
		generateButton.setBackground(darkModeAccent);
		generateButton.setTextFill(Color.WHITE);
		
		selectImageButton.setBackground(darkModeAccent);
		selectImageButton.setTextFill(Color.WHITE);
	}
	
	private void initInputScene() {		
		entranceExitParameters();
		graphParameters();
		details();
		buttons();
		checkBoxes();
		setUpRadioButtons();
		
		vBox = new VBox(primmsCheckBox,
				entranceExitParametersHBOX,
				graphParametersHBOX,
				detailsBOX,
				procedualHBOX,
				kruskalsRadioBoxes,
				buttonHBOX);
		vBox.setPadding(new Insets(20));
		
		if(darkModeToggle) {
			darkMode();
		}	
		
		inputScene = new Scene(vBox, 500, 450, Color.BLACK);

		updateGUI();
	}
	
	private void initGenerationScene() {
		progress = new ProgressBar(0);
		progress.setPrefHeight(progressBarY);
		progress.setMinHeight(progressBarY);
		progress.setPadding(new Insets(0));
		progress.setBackground(new Background(new BackgroundFill(new Color(0d, 0d, 0d ,1d)
				, CornerRadii.EMPTY, Insets.EMPTY)));
		
		
		if(width* scale * 2 + scale <= XMAX * 2 && height* scale * 2 + scale <= (YMAX - progressBarY) * 2) {
			canvas = new Canvas(width* scale * 2 + scale, height* scale * 2 + scale);
			progress.setPrefWidth(width* scale * 2 + scale);
			vBox = new VBox(progress, canvas);	
			renderScene = new Scene(vBox, width* scale * 2 + scale, height* scale * 2 + scale + progressBarY);			
		} else {
			canvas = new Canvas(XMAX, YMAX - progressBarY);	
			vBox = new VBox(progress, canvas);
			progress.setPrefWidth(XMAX);
			if(width > XMAX || height > YMAX) {
				renderScene = new Scene(vBox, XMAX, 30 + progressBarY);	
			} else {
				renderScene = new Scene(vBox, XMAX, YMAX + progressBarY);
			}
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
		stage.setFullScreenExitHint("ESC to exit fullscreen mode"
				+ "\nDouble click to go fullscreen");		
		stage.setFullScreen(true); 
		
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
			float scaley = YMAX / (height * 2 + 1);
			
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
			try {
				sortType type = sortType.countingSort;
				if(bubbleSort.isSelected()) {
					type=sortType.bubbleSort;
				} else if(insersionSort.isSelected()) {
					type=sortType.insersionSort;
				} else if(quickSort.isSelected()) {
					type=sortType.quickSort;
				} else if(countingSort.isSelected()) {
					type=sortType.countingSort;
				}
				mazegen generator = new mazegen(width, height, scale, imageFile, EntranceY, ExitY, primmsTypeCheckBox.isSelected(), XMAX, YMAX, primmsCheckBox.isSelected(), type);
				Thread generatorThread = new Thread(generator, "Generator Thread");
				generatorThread.start();
			} catch(Exception e ) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Invalid input");
		}
	}
	
	public static void showError(String errorMessage) {
		Text errorText = new Text(errorMessage);
		errorText.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		errorText.setFill(Color.DARKRED);

		Button okayButton = new Button("Okay");
		
		okayButton.setFont(Font.font(font, FontWeight.BOLD, FontPosture.REGULAR, 14));
		okayButton.setOnAction(e -> {
			System.exit(13);
		});
		
		VBox errorVbox = new VBox(errorText, okayButton);
		errorVbox.setPadding(new Insets(20));
		
		Scene errorScene = new Scene(errorVbox, Color.BLACK);
				
		stage.setScene(errorScene);
		stage.sizeToScene();
		stage.show();
		stage.centerOnScreen();
		stage.setResizable(false);
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
		darkModeToggle = true;
		for(String arg: args) {
			if(arg == "lightMode") {
				darkModeToggle = false;
				break;
			}
		}
		
		System.out.println("GUI Initialising...");

		launch();
		
		System.out.println("GUI Initialisation finished.");
		System.out.println("Wait for gui events.");
	}
}