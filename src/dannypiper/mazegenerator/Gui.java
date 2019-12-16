package dannypiper.mazegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
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
	public static final int maxScale = 4;

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
	public static final String Version = Messages.getString ( "Gui.version" ); //$NON-NLS-1$

	// javaFX
	private final static String font = "Lucida Console"; //$NON-NLS-1$
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

	// Test parameters
	public static boolean test = false;

	public static void main ( final String [ ] args ) {
		Gui.darkModeToggle = true;
		String testFileName = "test.csv";

		for ( final String arg : args ) {

			switch ( arg.toLowerCase ( ) ) {
				case "testmode" : {
					System.out.println ( "Testing generation..." );
					test = true;
					break;
				}

				case "help" : {
					System.out.println ( "Add testMode as a parameter to execute a generation test."
					        + "\nUse the parameter filename-<filename> to store the log to <filename>." );
					break;
				}
			}

			if ( arg.contains ( "filename-" ) ) {
				testFileName = arg.split ( "filename-" ) [ 1 ];
			}

		}

		if ( ! test ) {
			System.out.println ( "GUI Initialising..." ); //$NON-NLS-1$

			Application.launch ( );

			System.out.println ( "GUI Initialisation finished." ); //$NON-NLS-1$
			System.out.println ( "Wait for gui events." ); //$NON-NLS-1$
		}
		else {
			int n = 5;
			long primmsTime = 0;
			long kruskalsTime = 0;
			final long max = 60000;

			File makeTESTFolder = new File ( "TEST" );

			if ( ! makeTESTFolder.exists ( ) && ! makeTESTFolder.isDirectory ( ) ) {
				makeTESTFolder.mkdir ( );
			}

			File logFile = new File ( testFileName );

			if ( logFile.exists ( ) ) {
				logFile.delete ( );
			}

			try {
				System.out.println ( "Filename: " + testFileName );
				PrintWriter fileWriter = new PrintWriter ( logFile );

				fileWriter.println ( "n,primmsTime,KruskalsTime" );

				while ( primmsTime < max || kruskalsTime < max ) {

					System.out.println ( "[TEST]: n=" + n + "," );
					fileWriter.print ( n + "," );

					if ( primmsTime < max ) {
						MazeGenTest testMazeGenPrimms = new MazeGenTest ( n, n,
						        new File ( "TEST/Primms Test For ." + testFileName + ". n - " + n + ".png" ), true );
						primmsTime = testMazeGenPrimms.runTest ( );

						fileWriter.print ( primmsTime );
						System.out.println ( "[TEST]: p=" + primmsTime );
					}

					fileWriter.print ( "," );

					if ( kruskalsTime < max ) {
						MazeGenTest testMazeGenKruskals = new MazeGenTest ( n, n,
						        new File ( "TEST/" + "Kruskals Test For ." + testFileName + ". n is " + n + ".png" ),
						        false );
						kruskalsTime = testMazeGenKruskals.runTest ( );

						fileWriter.print ( kruskalsTime );
						System.out.println ( "[TEST]: k=" + kruskalsTime );
					}

					n += 5;

					fileWriter.println ( );

				}

				fileWriter.close ( );
				System.out.println ( "Finished Tests." );
				System.exit ( 0 );

			}
			catch ( Exception exception ) {
				exception.printStackTrace ( );
			}

		}

	}

	public static void setProgress ( final double percentage ) {

		if ( ! test ) {
			Gui.progress.setProgress ( percentage );
		}

	}

	public static void showError ( final String errorMessage ) {
		final Text errorText = new Text ( errorMessage );
		errorText.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		errorText.setFill ( Color.DARKRED );

		final Button okayButton = new Button ( "Okay" ); //$NON-NLS-1$

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
		Gui.generateButton = new Button ( "Invalid input." ); //$NON-NLS-1$
		Gui.selectImageButton = new Button ( "Save Maze As." ); //$NON-NLS-1$

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
		Gui.primmsTypeCheckBox = new CheckBox ( "Procedural Generation (Uses less RAM)" ); //$NON-NLS-1$
		Gui.primmsTypeCheckBox.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.primmsTypeCheckBox.setSelected ( true );

		Gui.primmsTypeCheckBox.setOnAction ( e -> {
			this.validateInput ( );
		} );

		Gui.primmsCheckBox = new CheckBox ( "Use Primm's Algorithm" ); //$NON-NLS-1$
		Gui.primmsCheckBox.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
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
			n.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
		}

	}

	@SuppressWarnings ( "unused" )
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
			n.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
		}

	}

	private void darkModeify ( final Text... nodes ) {

		for ( final Text n : nodes ) {
			n.setFill ( Color.WHITE );
		}

	}

	private void details ( ) {
		Gui.fileSelectedText = new Text ( "No file selected." ); //$NON-NLS-1$
		Gui.fileSelectedText.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.fileSelectedText.setFill ( Color.DARKORANGE );

		Gui.errorsText = new Text ( "Please select a file." ); //$NON-NLS-1$
		Gui.errorsText.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );
		Gui.errorsText.setFill ( Color.DARKRED );

		Gui.detailsBOX = new VBox ( Gui.fileSelectedText, Gui.errorsText );
		Gui.detailsBOX.setPadding ( new Insets ( 20 ) );
	}

	private void entranceExitParameters ( ) {
		Gui.entranceYLabel = new Text ( "Entrance Y: " ); //$NON-NLS-1$
		Gui.entranceYLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.entranceYField = new TextField ( "5" ); //$NON-NLS-1$
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

		Gui.exitYLabel = new Text ( "  Exit Y :" ); //$NON-NLS-1$
		Gui.exitYLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.exitYField = new TextField ( "5" ); //$NON-NLS-1$
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
			System.out.println ( "Canvas GUI" ); //$NON-NLS-1$

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

			System.out.println ( "Started Generation, scale: " + Gui.scale ); //$NON-NLS-1$

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
				final Thread generatorThread = new Thread ( generator, "Generator Thread" ); //$NON-NLS-1$
				generatorThread.start ( );
			}
			catch ( final Exception e ) {
				e.printStackTrace ( );
			}

		}
		else {
			System.out.println ( "Invalid input" ); //$NON-NLS-1$
		}

	}

	private void graphParameters ( ) {

		Gui.widthLabel = new Text ( "Graph Width:" ); //$NON-NLS-1$
		Gui.widthLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.widthField = new TextField ( "50" ); //$NON-NLS-1$
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

		Gui.heightLabel = new Text ( "  Graph Height:" ); //$NON-NLS-1$
		Gui.heightLabel.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.heightField = new TextField ( "50" ); //$NON-NLS-1$
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

		if ( ( ( Gui.width * 2 + 1 ) <= ( Gui.XMAX * Gui.maxScale ) )
		        && ( Gui.height * 2 + 1 <= ( Gui.YMAX * Gui.maxScale ) ) ) {
			Gui.canvas = new Canvas ( ( Gui.width * Gui.scale * 2 ) + Gui.scale,
			        ( Gui.height * Gui.scale * 2 ) + Gui.scale );
			Gui.progress.setPrefWidth ( ( Gui.width * Gui.scale * 2 ) + Gui.scale );
			ScrollPane scrollPane = new ScrollPane ( );

			scrollPane = new ScrollPane ( );
			scrollPane.setContent ( canvas );

			scrollPane.setVbarPolicy ( ScrollBarPolicy.AS_NEEDED );
			scrollPane.setHbarPolicy ( ScrollBarPolicy.AS_NEEDED );

			Gui.vBox = new VBox ( Gui.progress, scrollPane );
			Gui.renderScene = new Scene ( Gui.vBox, ( Gui.width * Gui.scale * 2 ) + Gui.scale,
			        ( Gui.height * Gui.scale * 2 ) + Gui.scale );
		}
		else {
			Gui.canvas = new Canvas ( Gui.XMAX, Gui.YMAX );

			ScrollPane scrollPane = new ScrollPane ( );

			scrollPane = new ScrollPane ( );
			scrollPane.setContent ( canvas );

			scrollPane.setVbarPolicy ( ScrollBarPolicy.AS_NEEDED );
			scrollPane.setHbarPolicy ( ScrollBarPolicy.AS_NEEDED );

			Gui.vBox = new VBox ( Gui.progress, scrollPane );

			Gui.progress.setPrefWidth ( Gui.XMAX );

			if ( ( Gui.width > Gui.XMAX ) || ( Gui.height > Gui.YMAX ) ) {
				Gui.renderScene = new Scene ( Gui.vBox, Gui.XMAX, 30 );
			}
			else {
				Gui.renderScene = new Scene ( Gui.vBox, Gui.XMAX, Gui.YMAX );
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
		Gui.stage.setFullScreenExitHint ( "ESC to exit fullscreen mode" + "\nDouble click to go fullscreen" ); //$NON-NLS-1$ //$NON-NLS-2$

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

		fileChooser.getExtensionFilters ( ).addAll ( new FileChooser.ExtensionFilter ( "Maze Image", "*.png" ), //$NON-NLS-1$ //$NON-NLS-2$
		        new FileChooser.ExtensionFilter ( "Maze Image", "*.jpeg" ) ); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			final File selectedFile = fileChooser.showSaveDialog ( Gui.stage );

			if ( selectedFile != null ) {
				Gui.imageFile = selectedFile; // Keeps old file if dialog is closed
			}

			if ( Gui.imageFile != null ) {
				Gui.fileSelectedText.setText ( "Selected '" + Gui.imageFile.getName ( ) + "'" ); //$NON-NLS-1$ //$NON-NLS-2$
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

		Gui.radioButtonLabels = new Text ( "Select Sorting Type" ); //$NON-NLS-1$
		Gui.bubbleSort = new RadioButton ( "Bubble Sort (slowest)" ); //$NON-NLS-1$
		Gui.insersionSort = new RadioButton ( "Insersion Sort" ); //$NON-NLS-1$
		Gui.quickSort = new RadioButton ( "Quick Sort" ); //$NON-NLS-1$
		Gui.countingSort = new RadioButton ( "Counting Sort (fastest)" ); //$NON-NLS-1$

		Gui.bubbleSort.setToggleGroup ( Gui.group );
		Gui.insersionSort.setToggleGroup ( Gui.group );
		Gui.quickSort.setToggleGroup ( Gui.group );
		Gui.countingSort.setToggleGroup ( Gui.group );
		Gui.countingSort.setSelected ( true );

		Gui.radioButtonLabels.setFill ( Color.WHITE );
		Gui.radioButtonLabels.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.bubbleSort.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
		Gui.bubbleSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.insersionSort.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
		Gui.insersionSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.quickSort.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
		Gui.quickSort.setFont ( Font.font ( Gui.font, FontWeight.BOLD, FontPosture.REGULAR, 14 ) );

		Gui.countingSort.setStyle ( "-fx-text-fill: white;" ); //$NON-NLS-1$
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

		String memStatus = ""; //$NON-NLS-1$

		if ( ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) < 700 ) {
			memStatus += "Low RAM! "; //$NON-NLS-1$
		}

		memStatus += ( Runtime.getRuntime ( ).totalMemory ( ) / 1024 / 1024 ) + " MB of RAM"; //$NON-NLS-1$
		Gui.stage.setTitle (
		        "Maze Generator v" + Gui.Version + " - " + memStatus + ". Built on: " + GetBuildDate.GetBuildDate ( ) ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
			System.out.println ( "Closing program by user request." ); //$NON-NLS-1$
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
		String errorText = ""; //$NON-NLS-1$
		String proceduralGenerationStatus = "Using adjacency matrix"; //$NON-NLS-1$

		if ( Gui.imageFile == null ) {
			out = true;
			errorText += "Please select a file."; //$NON-NLS-1$
			Gui.fileSelectedText.setFill ( Color.DARKORANGE );
		}
		else {
			Gui.fileSelectedText.setFill ( Color.LIGHTGREEN );
		}

		if ( Gui.width <= 1 ) {

			if ( out ) {
				errorText += "\n"; //$NON-NLS-1$
			}

			out = true;
			errorText += "Invalid width."; //$NON-NLS-1$
		}

		if ( Gui.height <= 1 ) {

			if ( out ) {
				errorText += "\n"; //$NON-NLS-1$
			}

			out = true;
			errorText += "Invalid height."; //$NON-NLS-1$
		}

		if ( ( ( Gui.height >= MazeGen.proceduralThreshold ) || ( Gui.width >= MazeGen.proceduralThreshold ) )
		        && Gui.primmsCheckBox.isSelected ( ) ) {

			if ( out ) {
				errorText += "\n"; //$NON-NLS-1$
			}

			errorText += "Procedural generation must be used."; //$NON-NLS-1$
			Gui.primmsTypeCheckBox.setSelected ( true );
			Gui.primmsTypeCheckBox.setDisable ( true );
			proceduralGenerationStatus = "Must procedural generation"; //$NON-NLS-1$
		}
		else {
			Gui.primmsTypeCheckBox.setDisable ( false );
		}

		if ( Gui.primmsTypeCheckBox.isSelected ( ) && Gui.primmsCheckBox.isSelected ( ) ) {
			proceduralGenerationStatus = "Using procedural generation"; //$NON-NLS-1$
		}

		if ( ( Gui.EntranceY > Gui.height ) || ( Gui.EntranceY < 0 ) ) {

			if ( out ) {
				errorText += "\n"; //$NON-NLS-1$
			}

			out = true;
			errorText += "Invalid entrance Y."; //$NON-NLS-1$
		}

		if ( ( Gui.ExitY > Gui.height ) || ( Gui.ExitY < 0 ) ) {

			if ( out ) {
				errorText += "\n"; //$NON-NLS-1$
			}

			out = true;
			errorText += "Invalid exit Y."; //$NON-NLS-1$
		}

		if ( ! out ) {
			errorText = "All input valid. Maze Width: " + ( ( Gui.width * 2 ) + 1 ) + " Maze Height: " //$NON-NLS-1$ //$NON-NLS-2$
			        + ( ( Gui.height * 2 ) + 1 );
			Gui.errorsText.setFill ( Color.LIGHTGREEN );
			Gui.generateButton.setText ( "Generate Maze" ); //$NON-NLS-1$
			Gui.generateButton.setTextFill ( Color.LIGHTGREEN );
		}
		else {
			Gui.generateButton.setText ( "Invalid Input" ); //$NON-NLS-1$
			Gui.errorsText.setFill ( Color.DARKRED );
			Gui.generateButton.setTextFill ( Color.WHITE );
		}

		Gui.errorsText.setText ( errorText );

		if ( ! out ) {
			System.out.println ( "Input is: valid" ); //$NON-NLS-1$
		}
		else {
			System.out.println ( "Input is: not valid" ); //$NON-NLS-1$
		}

		return out;
	}
}
