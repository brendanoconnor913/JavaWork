import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
// get layout worked out before moving on to functionality

public class GUICalc {
	GridBagConstraints constraints = new GridBagConstraints();
	JFrame frame;
	
	void startCalc() {
		frame = new JFrame();
		JPanel display = new JPanel();
		JPanel buttons = new JPanel();
		JPanel row = new JPanel();
		
		// Create display
		JLabel out = new JLabel("");
		out.setBorder(BorderFactory.createCompoundBorder());
		out.setBackground(Color.WHITE);
		display.setPreferredSize(new Dimension(200,40));
		display.add(out);
		
		
		//create buttons
		buttons.setLayout(new GridLayout(0,3));
		JButton number;
		number = makeButton("C");
		buttons.add(number);
		number = makeButton("DEL");
		buttons.add(number);
		number = makeButton("ENTER");
		buttons.add(number);
		number = makeButton("9");
		buttons.add(number);
		number = makeButton("8");
		buttons.add(number);
		number = makeButton("7");
		buttons.add(number);
		number = makeButton("6");
		buttons.add(number);
		number = makeButton("5");
		buttons.add(number);
		number = makeButton("4");
		buttons.add(number);
		number = makeButton("3");
		buttons.add(number);
		number = makeButton("2");
		buttons.add(number);
		number = makeButton("1");
		buttons.add(number);
		number = makeButton("0");
		buttons.add(number);
		number = makeButton(".");
		buttons.add(number);
		number = makeButton("(-)");
		buttons.add(number);
		
		GridBagLayout grid = new GridBagLayout();
		
		frame.setLayout(grid);
		
		addGB(display,1,0);
		addGB(buttons,1,1);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	// Used to add components to frame with constraints passed
	void addGB(Component comp, int x, int y) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		frame.add(comp, constraints);
	}
	
	JButton makeButton(String name) {
		JButton button = new JButton(name);
		//button.addActionListener(this);
		return button;
	}
	
	public static void main(String[] args) {
		GUICalc calculator = new GUICalc();
		calculator.startCalc();
	}
}
