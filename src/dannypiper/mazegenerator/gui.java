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
import javafx.scene.control.Labeled;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
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

	// Objects to parse
	public static File imageFile;
	public static boolean darkModeToggle;

	// parameters
	private static int width;
	private static int height;
	private static int EntranceY;
	private static int ExitY;
	private static float scale = 1;
	public static int XMAX = 1920;
	public static int YMAX = 1000;
	public static final int progressBarY = 10;
	public static final String Version = "1.3.0";

	// javaFX
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

	// Maze render scene
	private static ProgressBar progress;
	private static Scene renderScene;
	private static Canvas canvas;
	@SuppressWarnings ( "exports" )
	public static GraphicsContext graphicsContext;

	// Entrance and Exit Parameters
	private static Text entranceYLabel;
	private static TextField entranceYField;
	private static Text exitYLabel;
	private static TextField exitYField;

	// Graph Parameters
	private static Text widthLabel;
	private static TextField widthField;
	private static Text heightLabel;
	private static TextField heightField;
	private static CheckBox primmsTypeCheckBox;

	// Details
	private static Text fileSelectedText;
	private static Text errorsText;

	// Buttons
	private static Button selectImageButton;
	private static Button generateButton;

	// Kruskals radioButtons
	private static Text radioButtonLabels;
	private static RadioButton bubbleSort;
	private static RadioButton insersionSort;
	private static RadioButton quickSort;
	private static RadioButton countingSort;
	private static ToggleGroup group;
	private final static double greyConstant = 0.20d;
	private final static double greyConstantAccent = 0.3d;

	public static void main ( final String [ ] args ) {
		gui.darkModeToggle = true;

		for ( final String arg : args ) {

			if ( arg == "lightMode" ) {
				gui.darkModeToggle = false;
				break;
			}

		}

		System.out.println ( "GUI Initialising..." );

		Application.launch ( );

		System.out.println ( "GUI Initialisation finished." );
		System.out.println ( "Wait for gui events." );
	}

	public static void setProgress ( final double percentage ) {
		gui.progress.setProgress ( percentage );
	}

	public static void showError ( final String errorMessage ) {
		final Text errorText = new Text ( errorMessage );
		errorText.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		errorText.setFill ( Color.DARKRED );

		final Button okayButton = new Button ( "Okay" );

		okayButton.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		okayButton.setOnAction ( e -> {
			System.exit ( 13 );
		} );

		final VBox errorVbox = new VBox ( errorText, okayButton );
		errorVbox.setPadding ( new Insets ( 20 ) );

		final Scene errorScene = new Scene ( errorVbox, Color.BLACK );

		gui.stage.setScene ( errorScene );
		gui.stage.sizeToScene ( );
		gui.stage.show ( );
		gui.stage.centerOnScreen ( );
		gui.stage.setResizable ( false );
	}

	private void applyDarkMode ( ) {
		this.darkModeify ( gui.entranceExitParametersHBOX, gui.graphParametersHBOX, gui.procedualHBOX, gui.buttonHBOX,
		        gui.vBox );

		this.darkModeify ( gui.widthLabel, gui.heightLabel, gui.entranceYLabel, gui.exitYLabel );

		gui.procedualHBOX.setPadding ( new Insets ( 10 ) );

		this.darkModeAccent ( gui.primmsCheckBox, gui.primmsTypeCheckBox, gui.entranceYField, gui.detailsBOX,
		        gui.exitYField, gui.heightField, gui.widthField, gui.generateButton, gui.selectImageButton );
	}

	private void buttons ( ) {
		gui.generateButton = new Button ( "Invalid input." );
		gui.selectImageButton = new Button ( "Save Maze As." );

		gui.generateButton.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.selectImageButton.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.generateButton.setOnAction ( e -> {

			if ( ! this.validateInput ( ) ) {
				this.generateMaze ( );
			}

		} );

		gui.selectImageButton.setOnAction ( e -> {
			this.openFileChooser ( );
		} );

		gui.buttonHBOX = new HBox ( gui.selectImageButton, gui.generateButton );
		gui.buttonHBOX.setPadding ( new Insets ( 20 ) );
	}

	// Primms
	private void checkBoxes ( ) {
		gui.primmsTypeCheckBox = new CheckBox ( "Procedual Generation (Uses less RAM)" );
		gui.primmsTypeCheckBox.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.primmsTypeCheckBox.setSelected ( true );

		gui.primmsTypeCheckBox.setOnAction ( e -> {
			this.validateInput ( );
		} );

		gui.primmsCheckBox = new CheckBox ( "Use Primm's Algorithm" );
		gui.primmsCheckBox.setStyle ( "-fx-text-fill: white;" );
		gui.primmsCheckBox.setSelected ( false );
		gui.primmsCheckBox.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.primmsCheckBox.setOnMouseClicked ( e -> {
			this.updateGUI ( );
		} );

		gui.procedualHBOX = new HBox ( gui.primmsTypeCheckBox );
		gui.procedualHBOX.setPadding ( new Insets ( 20 ) );
	}

	private void darkModeAccent ( final Region... nodes ) {
		final Background darkModeAccent = new Background ( new BackgroundFill (
		        new Color ( gui.greyConstantAccent, gui.greyConstantAccent, gui.greyConstantAccent, 1d ),
		        new CornerRadii ( 8d ), Insets.EMPTY ) );

		for ( final Region n : nodes ) {
			n.setBackground ( darkModeAccent );
			n.setStyle ( "-fx-text-fill: white;" );
		}

	}

	private void darkModeify ( final Labeled... nodes ) {

		for ( final Labeled n : nodes ) {
			n.setTextFill ( Color.WHITE );
		}

	}

	private void darkModeify ( final Region... nodes ) {
		final Background darkMode = new Background (
		        new BackgroundFill ( new Color ( gui.greyConstant, gui.greyConstant, gui.greyConstant, 1d ),
		                CornerRadii.EMPTY, Insets.EMPTY ) );

		for ( final Region n : nodes ) {
			n.setBackground ( darkMode );
			n.setStyle ( "-fx-text-fill: white;" );
		}

	}

	private void darkModeify ( final Text... nodes ) {

		for ( final Text n : nodes ) {
			n.setFill ( Color.WHITE );
		}

	}

	private void details ( ) {
		gui.fileSelectedText = new Text ( "No file selected." );
		gui.fileSelectedText.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.fileSelectedText.setFill ( Color.DARKORANGE );

		gui.errorsText = new Text ( "Please select a file." );
		gui.errorsText.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.errorsText.setFill ( Color.DARKRED );

		gui.detailsBOX = new VBox ( gui.fileSelectedText, gui.errorsText );
		gui.detailsBOX.setPadding ( new Insets ( 20 ) );
	}

	private void entranceExitParameters ( ) {
		gui.entranceYLabel = new Text ( "Entrance Y: " );
		gui.entranceYLabel.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.entranceYField = new TextField ( "5" );
		gui.entranceYField.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.entranceYField.setOnKeyTyped ( e -> {

			if ( validation.validNumericalParameter ( gui.entranceYField.getText ( ) ) ) {

				if ( ( gui.entranceYField.getText ( ).length ( ) > 0 )
				        && ( gui.entranceYField.getText ( ).length ( ) < 8 ) ) {
					gui.EntranceY = Integer.valueOf ( gui.entranceYField.getText ( ) );
				}
				else {
					gui.EntranceY = 0;
				}

			}
			else {
				gui.EntranceY = 0;
			}

			this.validateInput ( );
		} );

		gui.exitYLabel = new Text ( "  Exit Y :" );
		gui.exitYLabel.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.exitYField = new TextField ( "5" );
		gui.exitYField.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.exitYField.setOnKeyTyped ( e -> {

			if ( validation.validNumericalParameter ( gui.exitYField.getText ( ) ) ) {

				if ( ( gui.exitYField.getText ( ).length ( ) > 0 ) && ( gui.exitYField.getText ( ).length ( ) < 8 ) ) {
					gui.ExitY = Integer.valueOf ( gui.exitYField.getText ( ) );
				}
				else {
					gui.ExitY = 0;
				}

			}
			else {
				gui.ExitY = 0;
			}

			this.validateInput ( );
		} );
		gui.entranceExitParametersHBOX = new HBox ( gui.entranceYLabel, gui.entranceYField, gui.exitYLabel,
		        gui.exitYField );
		gui.entranceExitParametersHBOX.setPadding ( new Insets ( 20 ) );
		gui.EntranceY = 5;
		gui.ExitY = 5;
	}

	private void generateMaze ( ) {

		gui.EntranceY = Integer.valueOf ( gui.entranceYField.getText ( ) );
		gui.ExitY = Integer.valueOf ( gui.exitYField.getText ( ) );
		gui.height = Integer.valueOf ( gui.heightField.getText ( ) );
		gui.width = Integer.valueOf ( gui.widthField.getText ( ) );

		if ( ! this.validateInput ( ) ) {
			System.out.println ( "Canvas GUI" );

			gui.scale = 1f;

			final float scalex = gui.XMAX / ( ( gui.width * 2 ) + 1 );
			final float scaley = gui.YMAX / ( ( gui.height * 2 ) + 1 );

			if ( ( scalex < 1 ) || ( scaley < 1 ) ) {
				gui.scale = 1f;
			}
			else if ( scalex <= scaley ) {
				gui.scale = scalex;
			}
			else {
				gui.scale = scaley;
			}

			if ( ( ( gui.scale * gui.width * 2 ) + 1 ) > gui.XMAX ) {
				gui.scale = 1f;
			}
			else if ( ( ( gui.scale * gui.height * 2 ) + 1 ) > gui.YMAX ) {
				gui.scale = 1f;
			}

			this.initGenerationScene ( );

			System.out.println ( "Started Generation, scale: " + gui.scale );

			try {
				sortType type = sortType.countingSort;

				if ( gui.bubbleSort.isSelected ( ) ) {
					type = sortType.bubbleSort;
				}
				else if ( gui.insersionSort.isSelected ( ) ) {
					type = sortType.insersionSort;
				}
				else if ( gui.quickSort.isSelected ( ) ) {
					type = sortType.quickSort;
				}
				else if ( gui.countingSort.isSelected ( ) ) {
					type = sortType.countingSort;
				}

				final mazegen generator = new mazegen ( gui.width, gui.height, gui.scale, gui.imageFile, gui.EntranceY,
				        gui.ExitY, gui.primmsTypeCheckBox.isSelected ( ), gui.XMAX, gui.YMAX,
				        gui.primmsCheckBox.isSelected ( ), type );
				final Thread generatorThread = new Thread ( generator, "Generator Thread" );
				generatorThread.start ( );
			}
			catch ( final Exception e ) {
				e.printStackTrace ( );
			}

		}
		else {
			System.out.println ( "Invalid input" );
		}

	}

	private void graphParameters ( ) {

		gui.widthLabel = new Text ( "Graph Width:" );
		gui.widthLabel.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.widthField = new TextField ( "50" );
		gui.widthField.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.widthField.setOnKeyTyped ( e -> {

			if ( validation.validNumericalParameter ( gui.widthField.getText ( ) ) ) {

				if ( ( gui.widthField.getText ( ).length ( ) > 0 ) && ( gui.widthField.getText ( ).length ( ) < 8 ) ) {
					gui.width = Integer.valueOf ( gui.widthField.getText ( ) );
				}
				else {
					gui.width = 0;
				}

			}
			else {
				gui.width = 0;
			}

			this.validateInput ( );
		} );

		gui.heightLabel = new Text ( "  Graph Height:" );
		gui.heightLabel.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.heightField = new TextField ( "50" );
		gui.heightField.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		gui.heightField.setOnKeyTyped ( e -> {

			if ( validation.validNumericalParameter ( gui.heightField.getText ( ) ) ) {

				if ( ( gui.heightField.getText ( ).length ( ) > 0 )
				        && ( gui.heightField.getText ( ).length ( ) < 8 ) ) {
					gui.height = Integer.valueOf ( gui.heightField.getText ( ) );
				}
				else {
					gui.height = 0;
				}

			}
			else {
				gui.height = 0;
			}

			this.validateInput ( );
		} );

		gui.graphParametersHBOX = new HBox ( gui.widthLabel, gui.widthField, gui.heightLabel, gui.heightField );
		gui.graphParametersHBOX.setPadding ( new Insets ( 20 ) );

		gui.width = 50;
		gui.height = 50;
	}

	private void initGenerationScene ( ) {
		gui.progress = new ProgressBar ( 0 );
		gui.progress.setPrefHeight ( gui.progressBarY );
		gui.progress.setMinHeight ( gui.progressBarY );
		gui.progress.setPadding ( new Insets ( 0 ) );
		gui.progress.setBackground ( new Background (
		        new BackgroundFill ( new Color ( 0d, 0d, 0d, 1d ), CornerRadii.EMPTY, Insets.EMPTY ) ) );

		if ( ( ( ( gui.width * gui.scale * 2 ) + gui.scale ) <= ( gui.XMAX * 2 ) )
		        && ( ( ( gui.height * gui.scale * 2 ) + gui.scale ) <= ( ( gui.YMAX - gui.progressBarY ) * 2 ) ) ) {
			gui.canvas = new Canvas ( ( gui.width * gui.scale * 2 ) + gui.scale,
			        ( gui.height * gui.scale * 2 ) + gui.scale );
			gui.progress.setPrefWidth ( ( gui.width * gui.scale * 2 ) + gui.scale );
			gui.vBox = new VBox ( gui.progress, gui.canvas );
			gui.renderScene = new Scene ( gui.vBox, ( gui.width * gui.scale * 2 ) + gui.scale,
			        ( gui.height * gui.scale * 2 ) + gui.scale + gui.progressBarY );
		}
		else {
			gui.canvas = new Canvas ( gui.XMAX, gui.YMAX - gui.progressBarY );
			gui.vBox = new VBox ( gui.progress, gui.canvas );
			gui.progress.setPrefWidth ( gui.XMAX );

			if ( ( gui.width > gui.XMAX ) || ( gui.height > gui.YMAX ) ) {
				gui.renderScene = new Scene ( gui.vBox, gui.XMAX, 30 + gui.progressBarY );
			}
			else {
				gui.renderScene = new Scene ( gui.vBox, gui.XMAX, gui.YMAX + gui.progressBarY );
			}

		}

		gui.canvas.setOnMouseClicked ( e -> {

			if ( e.getClickCount ( ) >= 2 ) {
				gui.stage.setFullScreen ( true );
			}

		} );

		gui.vBox.setPadding ( new Insets ( 0 ) );

		gui.stage.setScene ( gui.renderScene );
		gui.stage.setResizable ( false );
		gui.stage.setX ( 0 );
		gui.stage.setY ( 0 );
		gui.stage.setFullScreenExitHint ( "ESC to exit fullscreen mode" + "\nDouble click to go fullscreen" );

		gui.graphicsContext = gui.canvas.getGraphicsContext2D ( );
	}

	private void initInputScene ( ) {
		this.entranceExitParameters ( );
		this.graphParameters ( );
		this.details ( );
		this.buttons ( );
		this.checkBoxes ( );
		this.setUpRadioButtons ( );

		gui.vBox = new VBox ( gui.primmsCheckBox, gui.entranceExitParametersHBOX, gui.graphParametersHBOX,
		        gui.detailsBOX, gui.procedualHBOX, gui.kruskalsRadioBoxes, gui.buttonHBOX );
		gui.vBox.setPadding ( new Insets ( 20 ) );

		if ( gui.darkModeToggle ) {
			this.applyDarkMode ( );
		}

		gui.inputScene = new Scene ( gui.vBox, 500, 450, Color.BLACK );

		this.updateGUI ( );
	}

	private void openFileChooser ( ) {
		final FileChooser fileChooser = new FileChooser ( );

		fileChooser.getExtensionFilters ( ).addAll ( new FileChooser.ExtensionFilter ( "Maze Image", "*.png" ),
		        new FileChooser.ExtensionFilter ( "Maze Image", "*.jpeg" ) );

		try {
			final File selectedFile = fileChooser.showSaveDialog ( gui.stage );

			if ( selectedFile != null ) {
				gui.imageFile = selectedFile; // Keeps old file if dialog is closed
			}

			if ( gui.imageFile != null ) {
				gui.fileSelectedText.setText ( "Selected '" + gui.imageFile.getName ( ) + "'" );
			}

		}
		catch ( final Exception e ) {
			e.printStackTrace ( );
		}

		this.validateInput ( );
	}

	// Kruskals Methods
	private void setUpRadioButtons ( ) {
		gui.group = new ToggleGroup ( );

		gui.radioButtonLabels = new Text ( "Select Sorting Type" );
		gui.bubbleSort = new RadioButton ( "Bubble Sort" );
		gui.insersionSort = new RadioButton ( "Insersion Sort" );
		gui.quickSort = new RadioButton ( "Quick Sort" );
		gui.countingSort = new RadioButton ( "Couting Sort" );

		gui.bubbleSort.setToggleGroup ( gui.group );
		gui.insersionSort.setToggleGroup ( gui.group );
		gui.quickSort.setToggleGroup ( gui.group );
		gui.countingSort.setToggleGroup ( gui.group );
		gui.countingSort.setSelected ( true );

		gui.radioButtonLabels.setFill ( Color.WHITE );
		gui.radioButtonLabels.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.bubbleSort.setStyle ( "-fx-text-fill: white;" );
		gui.bubbleSort.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.insersionSort.setStyle ( "-fx-text-fill: white;" );
		gui.insersionSort.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.quickSort.setStyle ( "-fx-text-fill: white;" );
		gui.quickSort.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.countingSort.setStyle ( "-fx-text-fill: white;" );
		gui.countingSort.setFont ( Font.font ( gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		gui.kruskalsRadioBoxes = new VBox ( gui.radioButtonLabels, gui.bubbleSort, gui.insersionSort, gui.quickSort,
		        gui.countingSort );
		gui.kruskalsRadioBoxes.setPadding ( new Insets ( 5 ) );
	}

	@Override
	public void start ( @SuppressWarnings ( "exports" ) final Stage arg0 ) throws Exception {
		gui.stage = arg0;

		gui.stage.setRenderScaleX ( 1 );
		gui.stage.setRenderScaleY ( 1 );

		String memStatus = "";

		if ( ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) < 700 ) {
			memStatus += "Low RAM! ";
		}

		memStatus += ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) + " MB of RAM";
		gui.stage.setTitle ( "Maze Generator v" + gui.Version + " - " + memStatus + "." );
		this.initInputScene ( );

		// Get primary screen and adjust canvas size for it.
		if ( Screen.getPrimary ( ) != null ) {
			System.out.println ( Screen.getPrimary ( ).getBounds ( ) );
			gui.XMAX = ( int ) Screen.getPrimary ( ).getBounds ( ).getWidth ( );
			gui.YMAX = ( int ) Screen.getPrimary ( ).getBounds ( ).getHeight ( );
		}

		gui.stage.setScene ( gui.inputScene );
		gui.stage.show ( );
		gui.stage.centerOnScreen ( );

		gui.stage.setOnCloseRequest ( e -> {
			System.out.println ( "Closing program by user request." );
			System.exit ( 1 ); // User closed program
		} );
	}

	private void updateGUI ( ) {
		gui.kruskalsRadioBoxes.setVisible ( ! gui.primmsCheckBox.isSelected ( ) );
		gui.primmsTypeCheckBox.setVisible ( gui.primmsCheckBox.isSelected ( ) );
	}

	// Primms Methods
	@SuppressWarnings ( "unused" )
	private boolean validateInput ( ) {
		boolean out = false;
		String errorText = "";
		String procedualGenerationStatus = "Using adjacency matrix";

		if ( gui.imageFile == null ) {
			out = true;
			errorText += "Please select a file.";
			gui.fileSelectedText.setFill ( Color.DARKORANGE );
		}
		else {
			gui.fileSelectedText.setFill ( Color.LIGHTGREEN );
		}

		if ( gui.width <= 1 ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid width.";
		}

		if ( gui.height <= 1 ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid height.";
		}

		if ( ( ( gui.height >= mazegen.procedualThreshold ) || ( gui.width >= mazegen.procedualThreshold ) )
		        && gui.primmsCheckBox.isSelected ( ) ) {

			if ( out ) {
				errorText += "\n";
			}

			errorText += "Procedual generation must be used.";
			gui.primmsTypeCheckBox.setSelected ( true );
			gui.primmsTypeCheckBox.setDisable ( true );
			procedualGenerationStatus = "Must procedual generation";
		}
		else {
			gui.primmsTypeCheckBox.setDisable ( false );
		}

		if ( gui.primmsTypeCheckBox.isSelected ( ) && gui.primmsCheckBox.isSelected ( ) ) {
			procedualGenerationStatus = "Using procedual generation";
		}

		if ( ( gui.EntranceY > gui.height ) || ( gui.EntranceY < 0 ) ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid entrance Y.";
		}

		if ( ( gui.ExitY > gui.height ) || ( gui.ExitY < 0 ) ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid exit Y.";
		}

		if ( ! out ) {
			errorText = "All input valid. Maze Width: " + ( ( gui.width * 2 ) + 1 ) + " Maze Height: "
			        + ( ( gui.height * 2 ) + 1 );
			gui.errorsText.setFill ( Color.LIGHTGREEN );
			gui.generateButton.setText ( "Generate Maze" );
			gui.generateButton.setTextFill ( Color.LIGHTGREEN );
		}
		else {
			gui.generateButton.setText ( "Invalid Input" );
			gui.errorsText.setFill ( Color.DARKRED );
			gui.generateButton.setTextFill ( Color.WHITE );
		}

		gui.errorsText.setText ( errorText );

		if ( ! out ) {
			System.out.println ( "Input is: valid" );
		}
		else {
			System.out.println ( "Input is: not valid" );
		}

		return out;
	}
}
