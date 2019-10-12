package dannypiper.mazegenerator;

public class mazegen {
	
	public static void main(String[] args) {
		System.out.println("GUI Initialising...");
		
		gui.InitUI();
		
		System.out.println("GUI Initialisation finished.");
		System.out.println("Wait for gui events.");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
