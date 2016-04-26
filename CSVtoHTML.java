import java.util.regex.*;
import java.io.*;

public class CSVtoHTML {	
	String csvToHtml(String text) {
		// Patterns for splitting each row (by new line) and column (by ",")
		Pattern r = Pattern.compile("\\n");
		Pattern c = Pattern.compile("\\s*,\\s*");

		String [] rows = r.split(text);
		
		// Get the dimensions of the 2 dimensional string array
		Integer rl = rows.length;
		Integer cl = c.split(rows[0]).length; // assuming all rows have same number of columns
		
		String [][] table = new String[rl][cl];
		
		// Fill the 2d array with contents of inputted string
		for (int i = 0; i < rows.length; i++) {
			String [] tmp = c.split(rows[i]);
			for (int j = 0; j < cl; j++) {
				table[i][j] = tmp[j];
			}
		}
		
		// Output array information in HTML table format
		StringBuffer txt = new StringBuffer();
		txt.append("<table>\n");
		for (int i = 0; i < rl; i++){
			txt.append("\t<tr>\n");
			for (int j = 0; j < cl; j++) {
				txt.append("\t\t<td>");
				txt.append(table[i][j]);
				txt.append("</td>\n");
			}
			txt.append("\t</tr>\n");
		}
		txt.append("</table>");
		
		return txt.toString();
	}
	
	public static void main( String args[]) {
		CSVtoHTML c = new CSVtoHTML();
		try(BufferedReader bf = new BufferedReader( new InputStreamReader (new FileInputStream("input.txt")))) {
			String line;
			StringBuffer sb = new StringBuffer();
			
			while((line = bf.readLine()) != null) {
				sb.append(line);
				sb.append("\n"); // Indicates end of row (gets stripped by bufferedreader)
			}
			
			String out = c.csvToHtml(sb.toString());
			System.out.print(out);
		}
		catch (IOException e) {
			System.err.print("ERROR");
		}
	}
}
