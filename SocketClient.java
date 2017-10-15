//package bdn;
import java.util.*;
/*  The java.net package contains the basics needed for network operations. */
import java.net.*;
/* The java.io package contains the basics needed for IO operations. */
import java.io.*;
/** The SocketClient class is a simple example of a TCP/IP Socket Client.
 *
 */
public class SocketClient {
	public static Scanner sc = new Scanner(System.in);
	String _request;
	String _msg;

	public StringBuffer read(Socket connection){
		try {
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");
			StringBuffer instr = new StringBuffer();
			int c;
	     		while ( (c = isr.read()) != 13){   //change this reading from server
	     	  		instr.append( (char) c);
	     	  	}
			return instr;
	       	}catch(Exception e){ 
	       		System.out.println("input reader fail.");
	       		return (new StringBuffer());
	       	}

	}
	public void write(Socket connection){	
		BufferedOutputStream bos;
		OutputStreamWriter osw;
 		String buf;
 		buf = sc.nextLine() + (char) 13;
 		try { 
 			bos = new BufferedOutputStream(connection.getOutputStream());
 		 	osw = new OutputStreamWriter(bos, "US-ASCII");
 		 	osw.write(buf);
      			osw.flush();
		}catch(Exception e){}
	}
	public Socket initConnect(String host, int port) {
		try {
			InetAddress address = InetAddress.getByName(host);
		 	Socket connection = new Socket(address, port);
			return connection;
		}catch(Exception e){
			System.out.println("initial Connection fail!");
			return (new Socket());
		}
	}
	public static void main(String[] args){
		String host = "localhost"; //change the host connect to
		int port = 20012;

		StringBuffer instr = new StringBuffer();
		String TimeStamp;
		System.out.println("SocketClient initialized");
		SocketClient client = new SocketClient();
		try {
  			Socket connection = client.initConnect(host, port);
		  	System.out.println(connection);
		  	boolean close = true;
	  		instr =	client.read(connection);
	  		System.out.println(instr);
		  	while(close) {

		  		instr =	client.read(connection);
		  		System.out.println(instr);
		 		client.write(connection);
		  	}

		   	connection.close();
     		}catch (IOException f) {
     			 System.out.println("IOException: " + f);
   		}catch (Exception g) {
    	  		System.out.println("Exception: " + g);
 		}
  
	}
}
