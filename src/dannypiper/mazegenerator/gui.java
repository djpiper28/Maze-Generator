package dannypiper.mazegenerator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class gui {
	
	public static File imageFile;
	
	//parameters
	public static int width;
	public static int height;
	public static int EntranceX;
	public static int EntranceY;
	public static int ExitX;
	public static int ExitY;
	public static BufferedImage mazeImage;
	
	//gui vars
	private static JFrame jframe;
	private static JPanel jpanel;
	private static JTextArea widthTitle;
	private static JTextArea heightTitle;
	private static JTextField widthIn;
	private static JTextField heightIn;
	private static JButton generateBtn;
	private static JButton cancelGenerationBtn;
	private static ActionListener numericalInput = new ActionListener() {		
		@Override
		/*
		 * Does the following:
		 * 	Whenever an input event occurs this purges non-numerical characters from the apt
		 *  input fields and allows up to 7 digits to be entered. The max 32 bit int has 8
		 *  digits so this stops errors on str to int conversion.
		 */
		public void actionPerformed(ActionEvent e) {
			String temp = "";
			int i = 0;
			for (String c: widthIn.getText().split("")) {
				if(validNumericalParameter(c) && i<=7) {
					temp+=c;
					i++;
				}
			}
			widthIn.setText(temp);
			
			temp = "";
			i = 0;
			for (String c: heightIn.getText().split("")) {
				if(validNumericalParameter(c) && i<=7) {
					temp+=c;
					i++;
				}
			}
			heightIn.setText(temp);
		}
	};
	
	//UI Methods
	private static void InitJFrame() {
		jframe = new JFrame("Maze Generator");
		jframe.setSize(1900, 1000);
		jframe.setResizable(false);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private static void initInputFieldsAndCaptions() {
		widthTitle = new JTextArea("Graph width: ");
		widthTitle.setEditable(false);
		jframe.add(widthTitle);
		widthTitle.setVisible(true);
		
		widthIn = new JTextField("5");
		widthIn.addActionListener(numericalInput);	//numerical input only.
		widthIn.setEditable(true);
		jframe.add(widthIn);
		
		heightTitle = new JTextArea("Graph height: ");
		heightTitle.setEditable(false);
		jframe.add(heightTitle);
		heightTitle.setVisible(true);	
		
		heightIn = new JTextField("5");
		heightIn.addActionListener(numericalInput);	//numerical input only.
		widthIn.setEditable(true);
		jframe.add(heightIn);
			
	}
	
	private static void initButtons() {
		generateBtn = new JButton("Generate Maze");
		cancelGenerationBtn = new JButton("Cancel");
	}
	
	public static void InitUI() {
		InitJFrame();
		initInputFieldsAndCaptions();
		initButtons();
	}
	
	public boolean valuesForAllParameters() {
		return (width>0 && height>0) 
				&& (EntranceY>0 && EntranceX>0)
				&& (ExitX>0 && ExitX>0)
				&& imageFile!=null;
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
}
