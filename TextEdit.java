import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.*;

public class TextEdit extends JFrame implements ActionListener {
	JTextArea area;
	JFrame frame = this;
	public TextEdit() {
		super("Text Editor v1.0");
		
		//Create MenuBar
		JMenuBar mbar = new JMenuBar();
		JMenu File = new JMenu("File");
		JMenu Edit = new JMenu("Edit");
		JMenu Find = new JMenu("Find");
		final JPopupMenu Popup = new JPopupMenu("Popup");
		
		//Create Text Area
		area = new JTextArea();
		Document doc = area.getDocument();
		
		//Fill in the menus
		JMenuItem item;
		item = makeMenuItem("Open");
		File.add(item);
		item = makeMenuItem("Save");
		File.add(item);
		File.addSeparator();
		item = makeMenuItem("Quit");
		item.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK)); // Add CTRL-Q Quit Shortcut
		File.add(item);
		item = makeMenuItem("Cut");
		Edit.add(item);
		Popup.add(item);
		item = makeMenuItem("Copy");
		Edit.add(item);
		Popup.add(item);
		item = makeMenuItem("Paste");
		Edit.add(item);
		Popup.add(item);
		item = makeMenuItem("Find");
		Find.add(item);
		Popup.add(item);
		
		// Set conditions for right click popup menu
		MouseListener mouselistener = new MouseAdapter() {
			public void mousePressed(MouseEvent e){checkbutton(e);}
			public void mouseReleased(MouseEvent e){checkbutton(e);}
			public void mouseClicked(MouseEvent e){checkbutton(e);}
			public void checkbutton(MouseEvent e) {
				if(e.isPopupTrigger()){
					Popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		};
		// Add mouse listener for popup menu
		frame.addMouseListener(mouselistener);
		area.addMouseListener(mouselistener);
				
		//Add menus to menuBar
		mbar.add(File);
		mbar.add(Edit);
		mbar.add(Find);
		this.setJMenuBar(mbar);
		
		//Add text area
		this.add(new JScrollPane(area));
		
		// Specify frame values
		this.setSize(400,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent ae) {
		String name = ae.getActionCommand();
		new TaskBarItem(name,area,frame).action();
	}
	
	// Creates menu item and adds it to Menu passed
	JMenuItem makeMenuItem(String name) {
		JMenuItem item = new JMenuItem(name);
		item.addActionListener(this);
		return item;
	}
	
	public static void main(String[] args){
		new TextEdit();
	}
}

class TaskBarItem
{
	String taskname;
	Document doc;
	JTextArea area;
	JFrame frame;
	
	TaskBarItem(String taskname, JTextArea area, JFrame frame){
		this.taskname = taskname;
		this.doc = area.getDocument();
		this.area = area;
		this.frame = frame;
	}
	//Executes function corresponding to the menu item
	void action() {
		if(taskname.equals("Open")){
			this.openFile(doc);
		}
		else if(taskname.equals("Save")){
			this.saveFile(doc);
		}
		else if(taskname.equals("Quit")){
			System.exit(0);
		}
		else if(taskname.equals("Cut")){
			area.cut();
		}
		else if(taskname.equals("Copy")){
			area.copy();
		}
		else if(taskname.equals("Paste")){
			area.paste();
		}
		else if(taskname.equals("Find")){
			this.findString(area);
		}
		else {
			System.err.print("TaskName not defined");
		}
	}
	// Opens file from file system and displays in text edit
	void openFile(Document doc) {
		JFileChooser fileExplr = new JFileChooser();
		int result = fileExplr.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION) {return;} // End function if file chooser cancled 
		try {
			File file = fileExplr.getSelectedFile();
			FileInputStream fin = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			String line;
			// Loop through selected file and instet contents to screen
			while((line = br.readLine()) != null){
				line += "\n";
				doc.insertString(doc.getLength(), line, null);
			}
		} catch(IOException e){System.err.print("Error");}
		catch(BadLocationException be) {System.err.print("bad loc");}
	}
	// Saves content in text edit to a user specified file
	void saveFile(Document doc) {
		JFileChooser fileExplr = new JFileChooser();
		int result = fileExplr.showSaveDialog(null);
		if(result == JFileChooser.CANCEL_OPTION) {return;}
		try {
			File file = fileExplr.getSelectedFile();
			FileOutputStream fout = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(fout);
			String line = doc.getText(0, doc.getLength()); // Get the text from the screen
			pw.print(line);
			pw.flush();
		} catch(IOException e) {System.err.print("IOError");}
		catch(BadLocationException bl) {System.err.print("bad location");}
	}
	void findString(JTextArea area) {
		String term;
		term = JOptionPane.showInputDialog(frame, "Please enter the term you are looking for");
		Pattern termPattern = Pattern.compile(term);
		Matcher termMatch = termPattern.matcher(area.getText()); //Match the given input with the text in editor screen
		while(termMatch.find()){
			area.select(termMatch.start(),termMatch.end());
			JOptionPane.showMessageDialog(frame, "Match found at index "+termMatch.start());
		}
	}
}

