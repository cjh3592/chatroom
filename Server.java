import java.net.*;
import java.io.*;
import java.util.*;

public class Server{
	//Socket connection;
	public final static int serverPort=20012;
	public final static String serverIP="127.0.0.1";
	public final static String USER_PASS_PATH="./data/user_password.txt";
	public final static String FILEPATH="./file";
	public static ServerSocket serverSocket;
	public static int nextPort;
	public final static int MAXPORT=65000;
	public static HashMap<String,String> user_password_map;
	public static HashMap<String,Socket> user_socket_map;
	public static HashMap<String,ArrayList<String>> user_message_map;//save message offline
	public static HashMap<String,ArrayList<String[]>> user_file_map;//save file offline
	public static HashMap<String,ArrayList<String>> chatroom_message_map;
	public static HashMap<String,ArrayList<String[]>> chatroom_file_map;
	public static HashMap<String,ArrayList<String>> user_chatroom_map;
	public static HashMap<String,ArrayList<String>> chatroom_user_map;

	public Server(){
		user_password_map=new HashMap<String,String>();
		user_socket_map=new HashMap<String,Socket>();
		user_message_map=new HashMap<String,ArrayList<String>>();
		user_file_map=new HashMap<String,ArrayList<String[]>>();
		chatroom_message_map=new HashMap<String,ArrayList<String>>();
		chatroom_file_map=new HashMap<String,ArrayList<String[]>>();
		user_chatroom_map=new HashMap<String,ArrayList<String>>();
		chatroom_user_map=new HashMap<String,ArrayList<String>>();
		nextPort=20013;
		read_user_password_file();
		setup_dir();
	}

	public static void main(String argv[]){
		Server server=new Server();
		server.wait_request();
	}

	public void println(String s){
		System.out.println(s);
	}

	//set up user-password map
	public void read_user_password_file(){
		File file=new File(USER_PASS_PATH);
		if(file.exists() && !file.isDirectory()){
			try{
				BufferedReader br=new BufferedReader(new FileReader(USER_PASS_PATH));
				String line;
				while((line=br.readLine())!=null){
					String[] user_info=line.split(",");
					user_password_map.put(user_info[0],user_info[1]);
				}	
			}catch(Exception e){
				System.out.println("failed to create file reader of user password data");
			}
		}else{
			try{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}catch(Exception e){
				println("failed to create user_password file");
			}
		}
	}

	public void setup_dir(){
		File file=new File(FILEPATH);

		if(!file.exists()){
			try{
				file.getParentFile().mkdirs();
			}catch(Exception e){
				println("failed in creating file path in server");
			}
		}
	}

	public void wait_request(){
		try {
			serverSocket = new ServerSocket(serverPort);
			System.out.println("server starts");
			while(true) {
				Socket newClient = serverSocket.accept();
				System.out.println("new connection comes");
				Runnable runnable = new SubServer(newClient);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		}catch(Exception e){
			System.out.println("failed in wait_request");
		}
	}

	public String read(Socket connection) {
		try {
			BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
			InputStreamReader isr = new InputStreamReader(bis);
			int character;
			StringBuffer buf = new StringBuffer();
			while((character = isr.read())!=13) {
				buf.append((char)character);
			}
			return buf.toString();
		}catch(Exception e) { 
			System.out.println("server read fail!");
			String s="";
			return (s);
		}
	}

	public void write(Socket connection, String data) {
		try {
			BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
			OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");
			osw.write(data+(char)13);
			osw.flush();	
		}catch(Exception e) { 
			System.out.println("server write fail!");
		}
	}
}
