import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;


public class Calc2 implements ActionListener {
	GridBagConstraints constraints = new GridBagConstraints();
	JFrame frame;
	JTextArea display;
	CalcOperation co = new CalcOperation(); 
	
	// Creates the GUI for calculator and adds listening for button presses
	void createLayout() {
		frame = new JFrame();
		frame.setLayout(new GridBagLayout());
		
		JPanel operations = new JPanel();
		JPanel btnPad = new JPanel();
		JPanel entryPad = new JPanel(); // holds enter and btnPad
		JPanel top = new JPanel();
		
		// Set up display for calculator output
		display = new JTextArea();
		display.setEditable(false);
		display.setPreferredSize(new Dimension(190,40));
		display.setBorder(BorderFactory.createEtchedBorder());
		top.add(display);
		
		// Add all of the operation Buttons to the operations panel
		JButton button = new JButton();
		operations.setLayout(new GridLayout(5,0));
		button = makeButton("=");
		operations.add(button);
		button = makeButton("+");
		operations.add(button);
		button = makeButton("-");
		operations.add(button);
		button = makeButton("%");
		operations.add(button);
		button = makeButton("x");
		operations.add(button);
		
		// Create the numerical pad
		btnPad.setLayout(new GridBagLayout());
		String [] buttons = {"DEL","C", "9","8","7","6","5","4","3","2","1","0",".",""};
		makeNumPanel(btnPad,3,buttons);
		
		// Add the numerical pad and operations pad to entryPad
		entryPad.add(btnPad);
		entryPad.add(operations);
		
		// Add display and entry to frame
		constraints.gridx = 0;
		constraints.gridy = 0;
		frame.add(top, constraints);
		constraints.gridx = 0;
		constraints.gridy = 1;
		frame.add(entryPad, constraints);
		
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		String bname = ae.getActionCommand();
		display.setText(co.input(bname)); // Use CalcOperations object to handle calculations
	}
	
	void addGB(JPanel panel, JComponent component, int x, int y) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		panel.add(component, constraints);
	}
	
	// Adds buttons sequentially to panel by string array passed in 
	// and formats in number of columns specified
	void makeNumPanel(JPanel panel, int cols, String[] names) {
		JButton button;
		int k = 0;
		int c = 1;
		String name;
		int numButtons = names.length;
		Boolean running = true;
		
		while(running) {
			for(int i = 0;i < cols;i++) {
				// Break once we reached the end of names
				if (k >= numButtons) {
					running = false;
					break;
				}
				
				name = names[k];
				
				// Make the Clear button 2 gridwidths wide
				if (name.equals("C")) {
					button = makeButton(name);
					constraints.gridwidth = 2;
					addGB(panel, button, i, c);
					constraints.gridwidth = 1;
					addGB(panel, makeButton(""),i, c);
					i++;
				}
				else {
					button = makeButton(name);
					addGB(panel, button, i, c);
				}
				
				k++;
			}
			c++;
		}
	}
	
	JButton makeButton(String name) {
		JButton button = new JButton(name);
		button.addActionListener(this);
		return button;
	}
	
	public static void main(String[] args) {
		Calc2 calculator = new Calc2();
		calculator.createLayout();
	}
}

class CalcOperation {
	String calcValue = ""; // Used to keep track of input

	// Takes name of button and performs proper action for button
	String input(String button) {
		if(button.equals("C")) {
			calcValue = "";
			return calcValue;
		}
		else if (button.equals("DEL")) {
			delete();
			return calcValue;
		}
		else if (button.equals("=")) {
			compute();
			return calcValue;
		}
		else if (button.equals("+")) {
			if(!isLastCharOp()){
				calcValue += "+";
			}
			return calcValue;
		}
		else if (button.equals("%")) {
			if(!isLastCharOp()){
				calcValue += "%";
			}
			return calcValue;
		}
		else if (button.equals("-")) {
			if(!isLastCharOp()){
				calcValue += "-";
			}
			return calcValue;
		}
		else if (button.equals("x")) {
			if(!isLastCharOp()){
				calcValue += "x";
			}
			return calcValue;
		}
		else if (button.equals("9")) {
			calcValue += "9";
			return calcValue;
		}
		else if (button.equals("8")) {
			calcValue += "8";
			return calcValue;
		}
		else if (button.equals("7")) {
			calcValue += "7";
			return calcValue;
		}
		else if (button.equals("6")) {
			calcValue += "6";
			return calcValue;
		}
		else if (button.equals("5")) {
			calcValue += "5";
			return calcValue;
		}
		else if (button.equals("4")) {
			calcValue += "4";
			return calcValue;
		}
		else if (button.equals("3")) {
			calcValue += "3";
			return calcValue;
		}
		else if (button.equals("2")) {
			calcValue += "2";
			return calcValue;
		}
		else if (button.equals("1")) {
			calcValue += "1";
			return calcValue;
		}
		else if (button.equals("0")) {
			calcValue += "0";
			return calcValue;
		}
		else if (button.equals(".")) {
			if(!isLastCharOp()) {
				calcValue += ".";
			}
			return calcValue;
		}
		return calcValue;
	}
	
	// Used to check if character is an operation character
	Boolean isOperation(char c) {
		char [] ops = {'+','-','x','%'};
		Boolean isOp = false;
		for (char s : ops) {
			if(c == s) {
				isOp = true;
				break;
			}
		}
		return isOp;
	}
	
	// Checks the input log to see if the last value entered was an operation
	Boolean isLastCharOp() {
		if (calcValue.length() < 1) {
			return false;
		}
		else {
			int lastIndex = calcValue.length()-1;
			if(isOperation((calcValue.charAt(lastIndex)))) {
				return true;
			}
			else {
				return false;
			}
		}
	}
	
	// Deletes the previous character input
	void delete() {
		if(calcValue.length() != 0) {
			int lastIndex = calcValue.length()-1;
			// If character before was an operation don't update the display
			calcValue = calcValue.substring(0,lastIndex);
		}
	}
	
	// Takes the input data, parses it, then performs the calculations then outputs result to display
	void compute() {
		char [] str = calcValue.toCharArray();
		if (isOperation(str[0])) {
			calcValue = "Invalid Input";
		}
		Vector<String> numbers = new Vector(); // Used to store numbers for calculation
		Vector<String> operations = new Vector(); // Used to store the operations between numbers
		String store = "";
		
		// Parse input data and sort into numbers vector or operations vector
		for(int i = 0; i <= calcValue.length()-1;i++) {
			if(isOperation(str[i])) {
				numbers.addElement(store);
				operations.addElement(calcValue.substring(i,i+1));
				store = "";
			}
			else {
				store += calcValue.substring(i,i+1);
			}
			if (i == calcValue.length()-1){
				numbers.addElement(store);
			}
		}
		
		// Loop through resulting numbers and operations and use to calculate and output results
		int j = 0;
		float output = 0;
		for(int i = 1; i < numbers.size();i++) {
			if (i == 1) {
				output = performOperation(numbers.get(0),numbers.get(i),operations.get(j));
			}
			else {
				output = performOperation(output,numbers.get(i),operations.get(j));
			}
			j++;
		}
		calcValue = Float.toString(output);
	}
	
	// Takes two numbers in form of string and performs mathematical operation on numbers
	// Performed in the form n1 operation n2
	float performOperation(String n1, String n2, String op) {
		float first = Float.parseFloat(n1);
		float second = Float.parseFloat(n2);
		if (op.equals("+")){
			return first+second;
		}
		else if (op.equals("-")){
			return first-second;
		}
		else if (op.equals("x")){
			return first*second;
		}
		else if (op.equals("%")){
			return first/second;
		}
		else {
			return (float)0.0;
		}
	}
	
	// Overloaded version that takes a float for first argument instead of string
	float performOperation(float n1, String n2, String op) {
		float first = n1;
		float second = Float.parseFloat(n2);
		if (op.equals("+")){
			return first+second;
		}
		else if (op.equals("-")){
			return first-second;
		}
		else if (op.equals("x")){
			return first*second;
		}
		else if (op.equals("%")){
			return first/second;
		}
		else {
			return (float) 0.000;
		}
	}
}