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

public class Gui extends Application {

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
	public static final String Version = "1.3.1";

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
	private static HBox proceduralHBOX;

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
	private static final double greyConstant = 0.20d;
	private static final double greyConstantAccent = 0.3d;

	public static void main ( final String [ ] args ) {
		Gui.darkModeToggle = true;

		for ( final String arg : args ) {

			if ( arg.toLowerCase ( ) == "lightMode" ) {
				Gui.darkModeToggle = false;
				System.out.println ( "light mode" );
				break;
			}

		}

		System.out.println ( "GUI Initialising..." );

		Application.launch ( );

		System.out.println ( "GUI Initialisation finished." );
		System.out.println ( "Wait for gui events." );
	}

	public static void setProgress ( final double percentage ) {
		Gui.progress.setProgress ( percentage );
	}

	public static void showError ( final String errorMessage ) {
		final Text errorText = new Text ( errorMessage );
		errorText.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		errorText.setFill ( Color.DARKRED );

		final Button okayButton = new Button ( "Okay" );

		okayButton.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		okayButton.setOnAction ( e -> {
			System.exit ( 13 );
		} );

		final VBox errorVbox = new VBox ( errorText, okayButton );
		errorVbox.setPadding ( new Insets ( 20 ) );

		final Scene errorScene = new Scene ( errorVbox, Color.BLACK );

		Gui.stage.setScene ( errorScene );
		Gui.stage.sizeToScene ( );
		Gui.stage.show ( );
		Gui.stage.centerOnScreen ( );
		Gui.stage.setResizable ( false );
	}

	private void applyDarkMode ( ) {
		this.darkModeify ( Gui.entranceExitParametersHBOX, Gui.graphParametersHBOX, Gui.proceduralHBOX, Gui.buttonHBOX,
		        Gui.vBox );

		this.darkModeify ( Gui.widthLabel, Gui.heightLabel, Gui.entranceYLabel, Gui.exitYLabel );

		Gui.proceduralHBOX.setPadding ( new Insets ( 10 ) );

		this.darkModeAccent ( Gui.primmsCheckBox, Gui.primmsTypeCheckBox, Gui.entranceYField, Gui.detailsBOX,
		        Gui.exitYField, Gui.heightField, Gui.widthField, Gui.generateButton, Gui.selectImageButton );
	}

	private void buttons ( ) {
		Gui.generateButton = new Button ( "Invalid input." );
		Gui.selectImageButton = new Button ( "Save Maze As." );

		Gui.generateButton.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.selectImageButton.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.generateButton.setOnAction ( e -> {

			if ( ! this.validateInput ( ) ) {
				this.generateMaze ( );
			}

		} );

		Gui.selectImageButton.setOnAction ( e -> {
			this.openFileChooser ( );
		} );

		Gui.buttonHBOX = new HBox ( Gui.selectImageButton, Gui.generateButton );
		Gui.buttonHBOX.setPadding ( new Insets ( 20 ) );
	}

	// Primms
	private void checkBoxes ( ) {
		Gui.primmsTypeCheckBox = new CheckBox ( "Procedural Generation (Uses less RAM)" );
		Gui.primmsTypeCheckBox.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.primmsTypeCheckBox.setSelected ( true );

		Gui.primmsTypeCheckBox.setOnAction ( e -> {
			this.validateInput ( );
		} );

		Gui.primmsCheckBox = new CheckBox ( "Use Primm's Algorithm" );
		Gui.primmsCheckBox.setStyle ( "-fx-text-fill: white;" );
		Gui.primmsCheckBox.setSelected ( false );
		Gui.primmsCheckBox.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.primmsCheckBox.setOnMouseClicked ( e -> {
			this.updateGUI ( );
		} );

		Gui.proceduralHBOX = new HBox ( Gui.primmsTypeCheckBox );
		Gui.proceduralHBOX.setPadding ( new Insets ( 20 ) );
	}

	private void darkModeAccent ( final Region... nodes ) {
		final Background darkModeAccent = new Background ( new BackgroundFill (
		        new Color ( Gui.greyConstantAccent, Gui.greyConstantAccent, Gui.greyConstantAccent, 1d ),
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
		        new BackgroundFill ( new Color ( Gui.greyConstant, Gui.greyConstant, Gui.greyConstant, 1d ),
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
		Gui.fileSelectedText = new Text ( "No file selected." );
		Gui.fileSelectedText.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.fileSelectedText.setFill ( Color.DARKORANGE );

		Gui.errorsText = new Text ( "Please select a file." );
		Gui.errorsText.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.errorsText.setFill ( Color.DARKRED );

		Gui.detailsBOX = new VBox ( Gui.fileSelectedText, Gui.errorsText );
		Gui.detailsBOX.setPadding ( new Insets ( 20 ) );
	}

	private void entranceExitParameters ( ) {
		Gui.entranceYLabel = new Text ( "Entrance Y: " );
		Gui.entranceYLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.entranceYField = new TextField ( "5" );
		Gui.entranceYField.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.entranceYField.setOnKeyTyped ( e -> {

			if ( Validation.validNumericalParameter ( Gui.entranceYField.getText ( ) ) ) {

				if ( ( Gui.entranceYField.getText ( ).length ( ) > 0 )
				        && ( Gui.entranceYField.getText ( ).length ( ) < 8 ) ) {
					Gui.EntranceY = Integer.valueOf ( Gui.entranceYField.getText ( ) );
				}
				else {
					Gui.EntranceY = 0;
				}

			}
			else {
				Gui.EntranceY = 0;
			}

			this.validateInput ( );
		} );

		Gui.exitYLabel = new Text ( "  Exit Y :" );
		Gui.exitYLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.exitYField = new TextField ( "5" );
		Gui.exitYField.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.exitYField.setOnKeyTyped ( e -> {

			if ( Validation.validNumericalParameter ( Gui.exitYField.getText ( ) ) ) {

				if ( ( Gui.exitYField.getText ( ).length ( ) > 0 ) && ( Gui.exitYField.getText ( ).length ( ) < 8 ) ) {
					Gui.ExitY = Integer.valueOf ( Gui.exitYField.getText ( ) );
				}
				else {
					Gui.ExitY = 0;
				}

			}
			else {
				Gui.ExitY = 0;
			}

			this.validateInput ( );
		} );
		Gui.entranceExitParametersHBOX = new HBox ( Gui.entranceYLabel, Gui.entranceYField, Gui.exitYLabel,
		        Gui.exitYField );
		Gui.entranceExitParametersHBOX.setPadding ( new Insets ( 20 ) );
		Gui.EntranceY = 5;
		Gui.ExitY = 5;
	}

	private void generateMaze ( ) {

		Gui.EntranceY = Integer.valueOf ( Gui.entranceYField.getText ( ) );
		Gui.ExitY = Integer.valueOf ( Gui.exitYField.getText ( ) );
		Gui.height = Integer.valueOf ( Gui.heightField.getText ( ) );
		Gui.width = Integer.valueOf ( Gui.widthField.getText ( ) );

		if ( ! this.validateInput ( ) ) {
			System.out.println ( "Canvas GUI" );

			Gui.scale = 1f;

			final float scalex = Gui.XMAX / ( ( Gui.width * 2 ) + 1 );
			final float scaley = Gui.YMAX / ( ( Gui.height * 2 ) + 1 );

			if ( ( scalex < 1 ) || ( scaley < 1 ) ) {
				Gui.scale = 1f;
			}
			else if ( scalex <= scaley ) {
				Gui.scale = scalex;
			}
			else {
				Gui.scale = scaley;
			}

			if ( ( ( Gui.scale * Gui.width * 2 ) + 1 ) > Gui.XMAX ) {
				Gui.scale = 1f;
			}
			else if ( ( ( Gui.scale * Gui.height * 2 ) + 1 ) > Gui.YMAX ) {
				Gui.scale = 1f;
			}

			this.initGenerationScene ( );

			System.out.println ( "Started Generation, scale: " + Gui.scale );

			try {
				sortType type = sortType.COUNTINGSORT;

				if ( Gui.bubbleSort.isSelected ( ) ) {
					type = sortType.BUBBLESORT;
				}
				else if ( Gui.insersionSort.isSelected ( ) ) {
					type = sortType.INSERTIONSORT;
				}
				else if ( Gui.quickSort.isSelected ( ) ) {
					type = sortType.QUICKSORT;
				}
				else if ( Gui.countingSort.isSelected ( ) ) {
					type = sortType.COUNTINGSORT;
				}

				final MazeGen generator = new MazeGen ( Gui.width, Gui.height, Gui.scale, Gui.imageFile, Gui.EntranceY,
				        Gui.ExitY, Gui.primmsTypeCheckBox.isSelected ( ), Gui.XMAX, Gui.YMAX,
				        Gui.primmsCheckBox.isSelected ( ), type );
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

		Gui.widthLabel = new Text ( "Graph Width:" );
		Gui.widthLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.widthField = new TextField ( "50" );
		Gui.widthField.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.widthField.setOnKeyTyped ( e -> {

			if ( Validation.validNumericalParameter ( Gui.widthField.getText ( ) ) ) {

				if ( ( Gui.widthField.getText ( ).length ( ) > 0 ) && ( Gui.widthField.getText ( ).length ( ) < 8 ) ) {
					Gui.width = Integer.valueOf ( Gui.widthField.getText ( ) );
				}
				else {
					Gui.width = 0;
				}

			}
			else {
				Gui.width = 0;
			}

			this.validateInput ( );
		} );

		Gui.heightLabel = new Text ( "  Graph Height:" );
		Gui.heightLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.heightField = new TextField ( "50" );
		Gui.heightField.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.heightField.setOnKeyTyped ( e -> {

			if ( Validation.validNumericalParameter ( Gui.heightField.getText ( ) ) ) {

				if ( ( Gui.heightField.getText ( ).length ( ) > 0 )
				        && ( Gui.heightField.getText ( ).length ( ) < 8 ) ) {
					Gui.height = Integer.valueOf ( Gui.heightField.getText ( ) );
				}
				else {
					Gui.height = 0;
				}

			}
			else {
				Gui.height = 0;
			}

			this.validateInput ( );
		} );

		Gui.graphParametersHBOX = new HBox ( Gui.widthLabel, Gui.widthField, Gui.heightLabel, Gui.heightField );
		Gui.graphParametersHBOX.setPadding ( new Insets ( 20 ) );

		Gui.width = 50;
		Gui.height = 50;
	}

	private void initGenerationScene ( ) {
		Gui.progress = new ProgressBar ( 0 );
		Gui.progress.setPrefHeight ( Gui.progressBarY );
		Gui.progress.setMinHeight ( Gui.progressBarY );
		Gui.progress.setPadding ( new Insets ( 0 ) );
		Gui.progress.setBackground ( new Background (
		        new BackgroundFill ( new Color ( 0d, 0d, 0d, 1d ), CornerRadii.EMPTY, Insets.EMPTY ) ) );

		if ( ( ( Gui.width * 2 + 1 ) <= ( Gui.XMAX * 2 ) )
		        && ( Gui.height * 2 + 1 <= ( ( Gui.YMAX - Gui.progressBarY ) * 2 ) ) ) {
			Gui.canvas = new Canvas ( ( Gui.width * Gui.scale * 2 ) + Gui.scale,
			        ( Gui.height * Gui.scale * 2 ) + Gui.scale );
			Gui.progress.setPrefWidth ( ( Gui.width * Gui.scale * 2 ) + Gui.scale );
			Gui.vBox = new VBox ( Gui.progress, Gui.canvas );
			Gui.renderScene = new Scene ( Gui.vBox, ( Gui.width * Gui.scale * 2 ) + Gui.scale,
			        ( Gui.height * Gui.scale * 2 ) + Gui.scale + Gui.progressBarY );
		}
		else {
			Gui.canvas = new Canvas ( Gui.XMAX, Gui.YMAX - Gui.progressBarY );
			Gui.vBox = new VBox ( Gui.progress, Gui.canvas );
			Gui.progress.setPrefWidth ( Gui.XMAX );

			if ( ( Gui.width > Gui.XMAX ) || ( Gui.height > Gui.YMAX ) ) {
				Gui.renderScene = new Scene ( Gui.vBox, Gui.XMAX, 30 + Gui.progressBarY );
			}
			else {
				Gui.renderScene = new Scene ( Gui.vBox, Gui.XMAX, Gui.YMAX + Gui.progressBarY );
			}

		}

		Gui.canvas.setOnMouseClicked ( e -> {

			if ( e.getClickCount ( ) >= 2 ) {
				Gui.stage.setFullScreen ( true );
			}

		} );

		Gui.vBox.setPadding ( new Insets ( 0 ) );

		Gui.stage.setScene ( Gui.renderScene );
		Gui.stage.setResizable ( false );
		Gui.stage.setX ( 0 );
		Gui.stage.setY ( 0 );
		Gui.stage.setFullScreenExitHint ( "ESC to exit fullscreen mode" + "\nDouble click to go fullscreen" );

		Gui.graphicsContext = Gui.canvas.getGraphicsContext2D ( );
	}

	private void initInputScene ( ) {
		this.entranceExitParameters ( );
		this.graphParameters ( );
		this.details ( );
		this.buttons ( );
		this.checkBoxes ( );
		this.setUpRadioButtons ( );

		Gui.vBox = new VBox ( Gui.primmsCheckBox, Gui.entranceExitParametersHBOX, Gui.graphParametersHBOX,
		        Gui.detailsBOX, Gui.proceduralHBOX, Gui.kruskalsRadioBoxes, Gui.buttonHBOX );
		Gui.vBox.setPadding ( new Insets ( 20 ) );

		if ( Gui.darkModeToggle ) {
			this.applyDarkMode ( );
		}

		Gui.inputScene = new Scene ( Gui.vBox, 500, 450, Color.BLACK );

		this.updateGUI ( );
	}

	private void openFileChooser ( ) {
		final FileChooser fileChooser = new FileChooser ( );

		fileChooser.getExtensionFilters ( ).addAll ( new FileChooser.ExtensionFilter ( "Maze Image", "*.png" ),
		        new FileChooser.ExtensionFilter ( "Maze Image", "*.jpeg" ) );

		try {
			final File selectedFile = fileChooser.showSaveDialog ( Gui.stage );

			if ( selectedFile != null ) {
				Gui.imageFile = selectedFile; // Keeps old file if dialog is closed
			}

			if ( Gui.imageFile != null ) {
				Gui.fileSelectedText.setText ( "Selected '" + Gui.imageFile.getName ( ) + "'" );
			}

		}
		catch ( final Exception e ) {
			e.printStackTrace ( );
		}

		this.validateInput ( );
	}

	// Kruskals Methods
	private void setUpRadioButtons ( ) {
		Gui.group = new ToggleGroup ( );

		Gui.radioButtonLabels = new Text ( "Select Sorting Type" );
		Gui.bubbleSort = new RadioButton ( "Bubble Sort (slowest)" );
		Gui.insersionSort = new RadioButton ( "Insersion Sort" );
		Gui.quickSort = new RadioButton ( "Quick Sort" );
		Gui.countingSort = new RadioButton ( "Counting Sort (fastest)" );

		Gui.bubbleSort.setToggleGroup ( Gui.group );
		Gui.insersionSort.setToggleGroup ( Gui.group );
		Gui.quickSort.setToggleGroup ( Gui.group );
		Gui.countingSort.setToggleGroup ( Gui.group );
		Gui.countingSort.setSelected ( true );

		Gui.radioButtonLabels.setFill ( Color.WHITE );
		Gui.radioButtonLabels.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.bubbleSort.setStyle ( "-fx-text-fill: white;" );
		Gui.bubbleSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.insersionSort.setStyle ( "-fx-text-fill: white;" );
		Gui.insersionSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.quickSort.setStyle ( "-fx-text-fill: white;" );
		Gui.quickSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.countingSort.setStyle ( "-fx-text-fill: white;" );
		Gui.countingSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.kruskalsRadioBoxes = new VBox ( Gui.radioButtonLabels, Gui.bubbleSort, Gui.insersionSort, Gui.quickSort,
		        Gui.countingSort );
		Gui.kruskalsRadioBoxes.setPadding ( new Insets ( 5 ) );
	}

	@Override
	public void start ( @SuppressWarnings ( "exports" ) final Stage arg0 ) throws Exception {
		Gui.stage = arg0;

		Gui.stage.setRenderScaleX ( 1 );
		Gui.stage.setRenderScaleY ( 1 );

		String memStatus = "";

		if ( ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) < 700 ) {
			memStatus += "Low RAM! ";
		}

		memStatus += ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) + " MB of RAM";
		Gui.stage.setTitle ( "Maze Generator v" + Gui.Version + " - " + memStatus + "." );
		this.initInputScene ( );

		// Get primary screen and adjust canvas size for it.
		if ( Screen.getPrimary ( ) != null ) {
			System.out.println ( Screen.getPrimary ( ).getBounds ( ) );
			Gui.XMAX = ( int ) Screen.getPrimary ( ).getBounds ( ).getWidth ( );
			Gui.YMAX = ( int ) Screen.getPrimary ( ).getBounds ( ).getHeight ( );
		}

		Gui.stage.setScene ( Gui.inputScene );
		Gui.stage.show ( );
		Gui.stage.centerOnScreen ( );

		Gui.stage.setOnCloseRequest ( e -> {
			System.out.println ( "Closing program by user request." );
			System.exit ( 1 ); // User closed program
		} );
	}

	private void updateGUI ( ) {
		Gui.kruskalsRadioBoxes.setVisible ( ! Gui.primmsCheckBox.isSelected ( ) );
		Gui.primmsTypeCheckBox.setVisible ( Gui.primmsCheckBox.isSelected ( ) );
	}

	// Primms Methods
	@SuppressWarnings ( "unused" )
	private boolean validateInput ( ) {
		boolean out = false;
		String errorText = "";
		String proceduralGenerationStatus = "Using adjacency matrix";

		if ( Gui.imageFile == null ) {
			out = true;
			errorText += "Please select a file.";
			Gui.fileSelectedText.setFill ( Color.DARKORANGE );
		}
		else {
			Gui.fileSelectedText.setFill ( Color.LIGHTGREEN );
		}

		if ( Gui.width <= 1 ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid width.";
		}

		if ( Gui.height <= 1 ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid height.";
		}

		if ( ( ( Gui.height >= MazeGen.proceduralThreshold ) || ( Gui.width >= MazeGen.proceduralThreshold ) )
		        && Gui.primmsCheckBox.isSelected ( ) ) {

			if ( out ) {
				errorText += "\n";
			}

			errorText += "Procedural generation must be used.";
			Gui.primmsTypeCheckBox.setSelected ( true );
			Gui.primmsTypeCheckBox.setDisable ( true );
			proceduralGenerationStatus = "Must procedural generation";
		}
		else {
			Gui.primmsTypeCheckBox.setDisable ( false );
		}

		if ( Gui.primmsTypeCheckBox.isSelected ( ) && Gui.primmsCheckBox.isSelected ( ) ) {
			proceduralGenerationStatus = "Using procedural generation";
		}

		if ( ( Gui.EntranceY > Gui.height ) || ( Gui.EntranceY < 0 ) ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid entrance Y.";
		}

		if ( ( Gui.ExitY > Gui.height ) || ( Gui.ExitY < 0 ) ) {

			if ( out ) {
				errorText += "\n";
			}

			out = true;
			errorText += "Invalid exit Y.";
		}

		if ( ! out ) {
			errorText = "All input valid. Maze Width: " + ( ( Gui.width * 2 ) + 1 ) + " Maze Height: "
			        + ( ( Gui.height * 2 ) + 1 );
			Gui.errorsText.setFill ( Color.LIGHTGREEN );
			Gui.generateButton.setText ( "Generate Maze" );
			Gui.generateButton.setTextFill ( Color.LIGHTGREEN );
		}
		else {
			Gui.generateButton.setText ( "Invalid Input" );
			Gui.errorsText.setFill ( Color.DARKRED );
			Gui.generateButton.setTextFill ( Color.WHITE );
		}

		Gui.errorsText.setText ( errorText );

		if ( ! out ) {
			System.out.println ( "Input is: valid" );
		}
		else {
			System.out.println ( "Input is: not valid" );
		}

		return out;
	}
}
