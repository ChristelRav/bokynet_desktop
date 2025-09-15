import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    
    public Window() { this.loadDefault();}

	public  void loadSign() {
		setTitle("Bokynet Desktop"); 
        setSize(300, 300); 
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	public void loadDefault(){
		setTitle("Bokynet Desktop"); 
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
	}

}
