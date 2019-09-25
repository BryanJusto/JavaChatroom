import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
	
	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String Message = "";
	private String serverIP;
	private Socket connection;
	private String name1;
	
	//constructor
	public Client(String host, String name){
		super(" Einzig Chat. ");
		serverIP = host;
		name1 = name;
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener( 
			new ActionListener(){
				public void actionPerformed(ActionEvent event){
					sendMessage(event.getActionCommand());
					userText.setText("");
				}
			}
		);
		add(userText,BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	//connect to server
	public  void startRun(){
		try{
			connectToServer();
			setupStreams();
			whilechatting();
		}catch(EOFException eofException){
			showMessage("\n Client Terminated Connection. ");
		}catch(IOException ioException){
			ioException.printStackTrace();
		}finally{
			closeAll();
		}
	}
	//connect to Server
	private void connectToServer() throws IOException{
		showMessage(" Attempting Connection \n");
		connection = new Socket(InetAddress.getByName(serverIP), 2620); //HERE JAIME <------------------ EDIT THAT
		showMessage("Connected to : " + connection.getInetAddress().getHostName());
	}
	//setupStreams
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n Streams Setup \n");
	}
	//while Chatting
	private void whilechatting() throws IOException{
		ableToType(true);
		do{
			try{
				Message = (String) input.readObject();
				showMessage("\n" + Message);
			}catch(ClassNotFoundException classNotFoundException){
				showMessage("\n idk that word.");
			}
			
		}while(!Message.equals("\n" + name1 +" - END"));
	}
	//closes shit
	private void closeAll(){
		showMessage("\nClosing.\n");
		ableToType(false);
		
		try{
		output.close();
		input.close();
		connection.close();
		}catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	//Send message to Server
	private void sendMessage(String Message){
		try{
			output.writeObject(name1 + " - " + Message);
			output.flush();
			showMessage("\n" + name1+ " - "+Message);
			
		}catch(IOException ioException){
			chatWindow.append("\n ERROR: didn't send");
		}
	}
	//showMessage
	private void showMessage(final String text){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					chatWindow.append(text);
				}	
			}
		);
	}
	//able to type
	private void ableToType(final boolean tof){
		SwingUtilities.invokeLater(
			new Runnable(){
				public void run(){
					userText.setEditable(tof);
				}	
			}
		);
	}
	
}
