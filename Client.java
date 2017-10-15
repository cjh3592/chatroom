import java.io.*;
import java.net.*;
import java.util.*;

public class Client{
	private final String serverIP="127.0.0.1";
	private final int servPort=20012;
	private int filePort;
	private Socket subServSoc,fileSoc;
	private BufferedReader userInput;
	private String userName;
	private boolean isLogout,isLogin;
	private final static String  FILEPATH="./receive file";

	public static void main(String argv[]){
		Client client=new Client();
		client.idle();
		
		client.end();
	}

	public Client(){
		try{
			subServSoc=new Socket(serverIP,servPort);
			Thread.sleep(100);
			filePort=Integer.valueOf(read(subServSoc));
			fileSoc=new Socket(serverIP,filePort);
			userInput=new BufferedReader(new InputStreamReader(System.in));
			isLogout=false;
			isLogin=false;
		}catch(Exception e){
			System.out.println("Initialize client_s socket to server error");
		}
	}

	public void println(String s){
		System.out.println(s);
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
		//	System.exit(0);
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
		//	System.exit(0);
		}
	}

	//terminal idle
	public void idle(){
		try{
		//	if(isLogin && !isLogout) {
			Thread r = new ReceiveMessage();
			Thread f = new ReceiveFile();
		//	}
			while(isLogin==false){
				println("What do you want to do (login,register) ?");
				String comm=userInput.readLine();
				switch(comm){
					case "login":
						login_terminal();
						break;
					case "register":
						register_terminal();
						break;
					default:
						println("Please enter a valid command");
						break;
				}
			}
			while(isLogout==false){
			//	receive_message_terminal();
				//receive_chatroomMessage();
			//	receive_file_terminal();
				//receive_chatroomFile();
				r.start();
				f.start();

				println("What do you want to do (knock,send message,send file,create chatroom,logout) ?");

				String comm=userInput.readLine();
				switch(comm){
					case "knock":
						knock_terminal();
						break;
					case "send message":
						send_message_terminal();
						break;
					case "send file":
						send_file_terminal();
						break;
					case "create chatroom":
						create_chatroom();
						break;
					case "chat":
						chat();
						break;
					case "logout":
						logout();
						break;
					default:
						println("Please enter a valid command");
						break;
				}
			}
		}catch(Exception e){
			println("failed in idle");
		}
	}

	//GUI login
	public boolean login(String account, String password) {
		if(isLogin){
			println("You have already logged in");
			return false;
		}

		try {
			write(subServSoc, "login");
			Thread.sleep(100);
			write(subServSoc, account);
			Thread.sleep(100);
			write(subServSoc, password);
			if(read(subServSoc).equals("valid")) {
				userName=account;
				println("Successfully log in");
				isLogin=true;
				return true;
			}else{
				println("Please try again");
				return false;
			}
		}catch(Exception e){
			System.out.println("failed in login_gui in client");
			return false;
		}

	}

	//terminal login
	public void login_terminal(){
		if(isLogin){
			println("You have already logged in");
			return;
		}

		try{
			String name,password;

			write(subServSoc,"login");
			println("Please enter your user id:");
			name=userInput.readLine();
			write(subServSoc,name);
			println("Please enter your password:");
			password=userInput.readLine();
			write(subServSoc,password);
			if(read(subServSoc).equals("valid")){
				println("Successfully log in");
				isLogin=true;
				userName=name;
			}else{
				println("Please try again");
			}
		}catch(Exception e){
			println("failed in login_terminal in client");
		}
	}

	//GUI register
	public boolean register(String account,String password){
		boolean flag=false;

		if(isLogin){
			println("You have already logged in");
			return false;
		}
		if(account.length()==0 || password.length()==0){
			return false;
		}
		try{
			write(subServSoc,"register");
			Thread.sleep(100);
			write(subServSoc,account);
			Thread.sleep(100);
			write(subServSoc,password);
			if(read(subServSoc).equals("valid")){
				flag=true;
				isLogin=true;
				userName=account;
			}
		}catch(Exception e){
			println("failed in register_gui in client");
		}

		return flag;
	}

	//terminal register
	public void register_terminal(){
		if(isLogin){
			println("You have already logged in");
			return;
		}
		try{
			String name,password;

			write(subServSoc,"register");
			println("Please enter your user id:");
			name=userInput.readLine();
			write(subServSoc,name);
			println("Please enter your password:");
			password=userInput.readLine();
			write(subServSoc,password);
			if(read(subServSoc).equals("valid")){
				println("Successfully register");
				userName=name;
				isLogin=true;
			}else{
				println("Please try another user name");
			}
		}catch(Exception e){
			println("failed in register_terminal in client");
		}
	}

	//terminal knock
	public void knock_terminal(){
		try{
			String name;

			write(subServSoc,"knock");
			println("Who do you want to knock?");
			name=userInput.readLine();
			write(subServSoc,name);

			//if user doesnt exist
			if(read(subServSoc).equals("invalid")){
				println("User does not exist");
				return;
			}

			if(read(subServSoc).equals("valid")){
				println(name+" is online");
			}else{
				println(name+" is offline");
			}
		}catch(Exception e){
			println("failed in knock_terminal in client");
		}
	}

	//GUI knock
	public boolean isUserOnline(String name){
		try{
			write(subServSoc,"knock");
			Thread.sleep(100);
			write(subServSoc,name);

			//if user doesnt exist
			if(read(subServSoc).equals("invalid")){
				println("User does not exist");
				return false;
			}

			if(read(subServSoc).equals("valid")){
				println(name+" is online");
				return true;
			}else{
				println(name+" is not online");
				return false;
			}
		}catch(Exception e){
			println("failed in isUserOnline");
		}

		return false;
	}

	public boolean isUserExist(String name){
		try{
			write(subServSoc,"exist");
			Thread.sleep(100);
			write(subServSoc,name);
			if(read(subServSoc).equals("valid")){
				println("User exists");
				return true;
			}else{
				println("User does not exist");
				return false;
			}
		}catch(Exception e){
			println("failed in isUserExist");
		}

		return false;
	}

	//terminal send_message
	public void send_message_terminal(){
		println("Please type \"end\" when you have entered all your message");
		try{
			String receiverName,message;

			write(subServSoc,"send_message");
			println("Who do you want to send to?");
			receiverName=userInput.readLine();
			write(subServSoc,receiverName);

			//check if user exists
			if(read(subServSoc).equals("invalid")){
				println("User does not exist");
				return;
			}

			while(!(message=userInput.readLine()).equals("end")){
				write(subServSoc,message);
			}
			write(subServSoc,"end");
			println("Successfully send message");
		}catch(Exception e){
			println("failed in send_message_terminal in client");
		}
	}

	//GUI send_message
	public boolean send_message(String receiverName,String message){
		boolean flag=false;

		try{
			write(subServSoc,"send_message");
			Thread.sleep(100);
			write(subServSoc,receiverName);

			//check if user exists
			if(read(subServSoc).equals("invalid")){
				println("User does not exist");
				return flag;
			}

			//send message
			write(subServSoc,message);
			Thread.sleep(100);
			write(subServSoc,"end");
			println("Successfully send message");
			flag=true;
		}catch(Exception e){
			println("failed in send_message_terminal in client");
		}

		return flag;
	}

	//terminal send_file
	public void send_file_terminal(){
		try{
			String name,filePath;

			write(subServSoc,"send_file");
			Thread.sleep(100);
			println("Who do you want to send to?");
			name=userInput.readLine();
			write(subServSoc,name);

			//check if user exists
			if(read(subServSoc).equals("invalid")){
				println("User does not exist");
				return;
			}

			println("Which file do you want to send?");
			filePath=userInput.readLine();

			//check if file exists
			File file=new File(filePath);
			if(!file.exists() || file.isDirectory()){
				write(subServSoc,"invalid_file");
				println("File does not exist or it is a directory");
				return;
			}

			//send file name and length
			write(subServSoc,file.getName());
			write(subServSoc,Integer.toString((int)file.length()));

			//send file
			int fileLength=(int)file.length();
			byte[] fileBuf=new byte[fileLength];
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
			bis.read(fileBuf,0,fileBuf.length);
			OutputStream outStr=fileSoc.getOutputStream();
			outStr.write(fileBuf,0,fileBuf.length);
			Thread.sleep(100);
			outStr.flush();
			println("Successfully transfer file");
		}catch(Exception e){
			println("failed in send_file_terminal");
		}
	}

	//GUI send_file
	public boolean send_file(String name,String filePath){
		boolean flag=false;
		try{
			write(subServSoc,"send_file");
			Thread.sleep(100);
			write(subServSoc,name);

			//check if user exists
			if(read(subServSoc).equals("invalid")){
				println("User does not exist");
				return flag;
			}

			//check if file exists
			File file=new File(filePath);
			if(!file.exists() || file.isDirectory()){
				write(subServSoc,"invalid_file");
				println("File does not exist or it is a directory");
				return flag;
			}

			//send file name and length
			write(subServSoc,file.getName());
			Thread.sleep(100);
			write(subServSoc,Integer.toString((int)file.length()));

			//send file
			int fileLength=(int)file.length();
			byte[] fileBuf=new byte[fileLength];
			BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
			bis.read(fileBuf,0,fileBuf.length);
			OutputStream outStr=fileSoc.getOutputStream();
			outStr.write(fileBuf,0,fileBuf.length);
			Thread.sleep(100);
			outStr.flush();
			println("Successfully transfer file");
			flag=true;
		}catch(Exception e){
			println("failed in send_file_terminal");
		}

		return false;
	}

	//terminal create chatroom
	public void create_chatroom(){
		try{
			write(subServSoc,"create_chatroom");
			println("Please enter the name of the new chatroom:");
			String chatroomName=userInput.readLine();
			write(subServSoc,chatroomName);

			//check if chatroom exists
			if(read(subServSoc).equals("invalid")){
				println("Chatroom already exists");
				return;
			}

			println("Who do you want to add? (Enter \"end\" when you are finished)");
			println("Here is a list of all users:");
			int userNum=Integer.valueOf(read(subServSoc));
			for(int i=0;i<userNum;i++){
				System.out.print(read(subServSoc));
				if(i!=userNum-1){
					System.out.print(",");
				}
			}
			println("");

			//user add members to chatroom
			String name,state;
			while(!(name=userInput.readLine()).equals("end")){
				write(subServSoc,name);
				if((state=read(subServSoc)).equals("notExist")){
					println("User does not exist");
				}else if(state.equals("inGroup")){
					println("User is already in the chatroom");
				}else{
					println("You add "+name+" to "+chatroomName);
				}
			}
			write(subServSoc,"end");
			println("Successfully add chatroom");
		}catch(Exception e){
			println("failed in create_chatroom in client");
		}
	}

	public void chat(){
		try{
			write(subServSoc,"chat");
			println("Which chatroom do you want to enter?");
			int chatroomNum=Integer.valueOf(read(subServSoc));
			for(int i=0;i<chatroomNum;i++){
				System.out.print(read(subServSoc));
				if(i!=chatroomNum-1){
					System.out.print(",");
				}
			}
			String chatroomName=userInput.readLine();
			write(subServSoc,chatroomName);

			//check if chatroom exists
			if(!read(subServSoc).equals("valid")){
				println("Invalid chatroom");
				return;
			}

			println("What do you want to do?(send message,send file)");
			println("Enter \"end\" when you want to quit chatroom");
			String comm;
			while(!(comm=userInput.readLine()).equals("end")){
				if(comm.equals("send message")){
					println("Enter \"end\" when you want to quit sending message");
					String message;
					while(!(message=userInput.readLine()).equals("end")){
						Thread.sleep(100);
						write(subServSoc,"send_message");
						Thread.sleep(100);
						write(subServSoc,message);
						Thread.sleep(100);
						write(subServSoc,"end");
					}
				}else if(comm.equals("send file")){
					println("Which file do you want to transfer?");
					String filePath=userInput.readLine();

					//check if file exists
					File file=new File(filePath);
					if(!file.exists() || file.isDirectory()){
						println("File does not exist or it is a directory");
						return;
					}

					//send file name and length
					write(subServSoc,"send_file");
					Thread.sleep(100);
					write(subServSoc,file.getName());
					Thread.sleep(100);
					write(subServSoc,Integer.toString((int)file.length()));

					//send file
					int fileLength=(int)file.length();
					byte[] fileBuf=new byte[fileLength];
					BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
					bis.read(fileBuf,0,fileBuf.length);
					OutputStream outStr=fileSoc.getOutputStream();
					outStr.write(fileBuf,0,fileBuf.length);
					Thread.sleep(100);
					outStr.flush();
				}else{
					println("Invalid command");
				}
			}
		}catch(Exception e){
			println("failed in chat in client");
		}
	}

	//terminal receive offline messages from users and chatrooms
/*
	public void receive_message_terminal(){
		try{
			write(subServSoc,"receive_message");
			int messageNum=Integer.valueOf(read(subServSoc));
			if(messageNum > 0){
				println("You have "+messageNum+" unread messages");
			}
			for(int i=0;i<messageNum;i++){
				Thread.sleep(100);
				println(read(subServSoc));
			}
		}catch(Exception e){
			println("failed in receive_message in client");
		}
	}
*/
	public void receive_chatroomMessage(){
		try{
			write(subServSoc,"receive_chatroomMessage");
			int chatroomNum=Integer.valueOf(read(subServSoc));
			for(int i=0;i<chatroomNum;i++){
				String chatroomName=read(subServSoc);
				Thread.sleep(100);
				int messageNum=Integer.valueOf(read(subServSoc));
				if(messageNum > 0){
					println("You have "+messageNum+" unread messages from "+chatroomName);
				}
				for(int j=0;j<messageNum;j++){
					Thread.sleep(100);
					println(read(subServSoc));
				}
			}
		}catch(Exception e){
			println("failed in receive_chatroomMessage");
		}
	}

	//GUI receive message
	public ArrayList<String> receive_message(){
		ArrayList<String> arr=new ArrayList<String>();
		int messageNum=0;
		
		try{
			write(subServSoc,"receive_message");
			String temp_ssss = read(subServSoc);
			System.out.println(temp_ssss);
			messageNum=Integer.valueOf(temp_ssss);
			for(int i=0;i<messageNum;i++){
				Thread.sleep(100);
				String message=read(subServSoc);
				println("message="+message);//debug
				arr.add(message);
			}
		}catch(Exception e){
			println("failed in receive_message in client");
		}

		if(messageNum==0){
			return null;
		}else{
			return arr;
		}
	}

	//terminal receive offline files from user and chatrooms
	/*
	public void receive_file_terminal(){
		try{
			write(subServSoc,"receive_file");
			int fileNum=Integer.valueOf(read(subServSoc));
			if(fileNum > 0){
				println("You received "+fileNum+" file(s)");
			}
			for(int i=0;i<fileNum;i++){
				String senderName=read(subServSoc);
				Thread.sleep(100);
				String fileName=read(subServSoc);
				Thread.sleep(100);
				int fileLength=Integer.valueOf(read(subServSoc));
				byte[] fileBuf=new byte[fileLength];
				InputStream is=fileSoc.getInputStream();
				File file=new File(FILEPATH+"\\"+fileName);
				if(!file.exists()){
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream fos=new FileOutputStream(file);
				BufferedOutputStream bos=new BufferedOutputStream(fos);
				int bytesRead=is.read(fileBuf,0,fileBuf.length);
				bos.write(fileBuf,0,bytesRead);
				Thread.sleep(100);
				bos.flush();
			}
		}catch(Exception e){
			println("failed in receive_file_terminal in client");
		}
	}
	*/
	public void receive_chatroomFile(){
		//
	}

	//GUI receive_file
	public ArrayList<String[]> receive_file(){
		ArrayList<String[]> arr=new ArrayList<String[]>();
		int fileNum=0;

		try{
			write(subServSoc,"receive_file");
			fileNum=Integer.valueOf(read(subServSoc));
			for(int i=0;i<fileNum;i++){
				String senderName=read(subServSoc);
				Thread.sleep(100);
				String fileName=read(subServSoc);
				String[] str=new String[2];
				str[0]=senderName;
				str[1]=fileName;
				arr.add(str);
				int fileLength=Integer.valueOf(read(subServSoc));
				byte[] fileBuf=new byte[fileLength];
				InputStream is=fileSoc.getInputStream();
				File file=new File(FILEPATH+"\\"+fileName);
				if(!file.exists()){
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				FileOutputStream fos=new FileOutputStream(file);
				BufferedOutputStream bos=new BufferedOutputStream(fos);
				int bytesRead=is.read(fileBuf,0,fileBuf.length);
				bos.write(fileBuf,0,bytesRead);
				Thread.sleep(100);
				bos.flush();
			}
		}catch(Exception e){
			println("failed in receive_file in client");
		}

		if(fileNum==0){
			return null;
		}else{
			return arr;
		}
	}

	//terminal logout
	public boolean logout(){
		boolean flag=false;

		try{
			write(subServSoc,"logout");
			if(read(subServSoc).equals("valid")){
				isLogout=true;
				subServSoc.close();
				println("Successfully log out");
			}else{
				println("failed to log out and should not be here");
			}
		}catch(Exception e){
			println("failed in logout in client");
		}

		return flag;
	}
	public void end(){
		println("Hope to see you back soon!");
	}
	class ReceiveMessage extends Thread {
		public void run() {
		while(true) {	
			//System.out.println("client" +userName+ " poi");
			try{
				write(subServSoc,"receive_message");
				int messageNum=Integer.valueOf(read(subServSoc));
				if(messageNum > 0){
					println("You have "+messageNum+" unread messages");
				}
				for(int i=0;i<messageNum;i++){
					Thread.sleep(100);
					println(read(subServSoc));
				}
			}catch(Exception e){
				println("failed in receive_message in client");
			}
		}
		}
	}
	class ReceiveFile extends Thread {
		public void run() {
			while(true) {	
				try{
					write(subServSoc,"receive_file");
					int fileNum=Integer.valueOf(read(subServSoc));
					if(fileNum > 0){
						println("You received "+fileNum+" file(s)");
					}
				for(int i=0;i<fileNum;i++){
					String senderName=read(subServSoc);
					Thread.sleep(100);
					String fileName=read(subServSoc);
					Thread.sleep(100);
					int fileLength=Integer.valueOf(read(subServSoc));
					byte[] fileBuf=new byte[fileLength];
					InputStream is=fileSoc.getInputStream();
					File file=new File(FILEPATH+"\\"+fileName);
					if(!file.exists()){
						file.getParentFile().mkdirs();
						file.createNewFile();
					}
					FileOutputStream fos=new FileOutputStream(file);
					BufferedOutputStream bos=new BufferedOutputStream(fos);
					int bytesRead=is.read(fileBuf,0,fileBuf.length);
					bos.write(fileBuf,0,bytesRead);
					Thread.sleep(100);
					bos.flush();
				}
				}catch(Exception e){
					println("failed in receive_file_terminal in client");
				}
			}
		}
	}
}
