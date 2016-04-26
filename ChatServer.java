import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.*;

public class ChatServer{
	public static void main(String args[]) throws IOException {
		Executor executor = Executors.newFixedThreadPool(3);
		Integer port = Integer.parseInt(args[0]);
		ServerSocket ss = new ServerSocket(port);
		StringBuffer log = new StringBuffer("");
		
		while(true) {
			executor.execute(new ChatServerConnection( ss.accept(), log ));
		}
	}
}

// ChatUpdate is used to create an alternate thread to refresh the user's chatroom view
class ChatUpdate implements Runnable {
	PrintWriter userscrn;
	StringBuffer log;
	
	ChatUpdate(PrintWriter userscrn, StringBuffer log) {
		this.userscrn = userscrn;
		this.log = log;
	}
	
	void clearscreen(PrintWriter pw, int linecnt) {
		for(int i = 0;i<linecnt;i++) {
			pw.print("\n\r");
		}
	}
	
	public void run() {
		while(true) {
			// Print updated Chat room log
			clearscreen(userscrn,23);
			userscrn.print(log.toString());
			userscrn.print(">> ");
			userscrn.flush();
			try {
				Thread.sleep(7500);
			} catch(InterruptedException e) {
				System.err.print("Thread interrupted");
			}
			
		}
	}
}

class ChatServerConnection implements Runnable {
	Socket sock;
	StringBuffer log;
	
	ChatServerConnection(Socket sock, StringBuffer log) {
		this.sock = sock;
		this.log = log;
	}
	
	// Used to clear the previous view of chat log
	void clearscreen(PrintWriter pw, int linecnt) {
		for(int i = 0;i<linecnt;i++) {
			pw.print("\n\r");
		}
	}
	
	public void run() {
		try {
			// Set up input and output streams with the connection
			InputStream in = sock.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			PrintWriter pw = new PrintWriter(sock.getOutputStream());
			
			// Get user informtation and display to users
			pw.print("Welcome, please give your username: ");
			pw.flush();
			String name = br.readLine();
			System.out.println(name+" logged in."); // Log user sign in
			log.append("*** "+name+" has entered the chatroom ***\n\r");
			clearscreen(pw,23);
			pw.print(log.toString());
			pw.flush();
			pw.print(">> ");
			pw.flush();
			
			String line;
			new Thread(new ChatUpdate(pw,log)).start();
			// Output text entered to chat room
			while(!( (line = br.readLine()).equals("logout") )){
					log.append(name+": "+line+"\n\r");
					clearscreen(pw,22);
					pw.print(log.toString());
					pw.print(">> ");
					pw.flush();

				}
			log.append("\n");
			log.append("*** "+name+" has left the chatroom ***\n\r");
			System.out.println(name+" logged out.");
			sock.close();
		} catch (IOException e) {
			System.err.println("You done goofed");
		}
	} 
}