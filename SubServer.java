import java.util.*;
import java.net.*;
import java.io.*;

public class SubServer implements Runnable{
	private final static String serverIP="127.0.0.1";
	private int filePort;
	private Socket clientSoc,clientFileSoc;
	private ServerSocket servFileSoc;
	private boolean isLogout;
	private String userName="";

	public SubServer(Socket client){
		clientSoc=client;
		isLogout=false;
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
			System.out.println("subserver read fail!");
			String s="";
			System.exit(0);
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
			System.out.println("subserver write fail!");
			System.exit(0);
		}
	}

	public void run(){
		try{
			filePort=Server.nextPort;
			if(Server.nextPort < Server.MAXPORT){
				Server.nextPort++;
			}
			servFileSoc=new ServerSocket(filePort);
			write(clientSoc,Integer.toString(filePort));
			clientFileSoc=servFileSoc.accept();
		}catch(Exception e){
			println("failed in create file server socket");
		}
		while(!isLogout){
			String comm=read(clientSoc);
			switch(comm){
				case "login":
					login();
					break;
				case "register":
					register();
					break;
				case "knock":
					knock();
					break;
				case "send_message":
					send_message();
					break;
				case "send_file":
					send_file();
					break;
				case "create_chatroom":
					create_chatroom();
					break;
				case "logout":
					logout();
					break;
				case "receive_message":
					receive_message();
					break;
				case "receive_chatroomMessage":
					receive_chatroomMessage();
					break;
				case "receive_file":
					receive_file();
					break;
				case "exist":
					exist();
					break;
				case "chat":
					chat();
					break;
				default:
					break;
			}
		}
	}

	public void login(){
		String name,password;
		
		try{
			name=read(clientSoc);
			password=read(clientSoc);
			if(Server.user_password_map.containsKey(name) == true && Server.user_password_map.get(name).equals(password)){
				write(clientSoc,"valid");
				Server.user_socket_map.put(name,clientSoc);
				userName=name;
			}else{
				write(clientSoc,"invalid");
				return;
			}
		}catch(Exception e){
			System.out.println("failed in login in subserver");
		}
	}

	public void register(){
		String name,password;

		try{
			name=read(clientSoc);
			password=read(clientSoc);
			if(name==null || password==null || Server.user_password_map.containsKey(name)==true){
				write(clientSoc,"invalid");
				return;
			}else{
				write(clientSoc,"valid");
				Server.user_password_map.put(name,password);
				Server.user_socket_map.put(name,clientSoc);
				userName=name;
			}
		}catch(Exception e){
			System.out.println("failed in register in subserver");
		}
	}

	public void knock(){
		String name;

		try{
			name=read(clientSoc);

			//check if user exists
			if(Server.user_password_map.containsKey(name)==true){
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
				return;
			}

			if(Server.user_socket_map.containsKey(name)==true){
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
				return;
			}
		}catch(Exception e){
			println("failed in knock in subserver");
		}
	}

	public void exist(){
		String name;

		try{
			name=read(clientSoc);
			if(Server.user_password_map.containsKey(name)){
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
			}
		}catch(Exception e){
			println("failed in exist in subserver");
		}
	}

	public boolean userExist(String name){
		if(Server.user_password_map.containsKey(name)){
			return true;
		}else{
			return false;
		}
	}

	public boolean userInChatroom(String chatroom,String user){
		return Server.chatroom_user_map.get(chatroom).contains(user);
	}

	public void send_message(){
		String receiverName,message;

		try{
			receiverName=read(clientSoc);
			System.out.println("receiverName = "+ receiverName);
			//check if user exists
			if(Server.user_password_map.containsKey(receiverName)){
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
				return;
			}

			//if no message at beginning
			if(!Server.user_message_map.containsKey(receiverName)){
				Server.user_message_map.put(receiverName,new ArrayList<String>());
			}

			ArrayList<String> arr=Server.user_message_map.get(receiverName);
			while(!(message=read(clientSoc)).equals("end")){
				arr.add(userName+":"+message);
			}
			Server.user_message_map.remove(receiverName);
			Server.user_message_map.put(receiverName,arr);
		}catch(Exception e){
			println("failed in send_message in subserver");
		}
	}

	public void send_chatroomMessage(String chatroom){
		String message;

		try{
			//if no message at beginning
			if(!Server.chatroom_message_map.containsKey(chatroom)){
				Server.chatroom_message_map.put(chatroom,new ArrayList<String>());
			}

			ArrayList<String> arr=Server.chatroom_message_map.get(chatroom);
			while(!(message=read(clientSoc)).equals("end")){
				arr.add(userName+":"+message);
			}
			Server.user_message_map.remove(chatroom);
			Server.user_message_map.put(chatroom,arr);
		}catch(Exception e){
			println("failed in send_chatroomMessage in subserver");
		}
	}

	public void send_file(){
		String receiverName,fileName;
		int fileLength;

		try{
			receiverName=read(clientSoc);

			//check if user exists
			if(Server.user_password_map.containsKey(receiverName)){
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
				return;
			}

			fileName=read(clientSoc);
			if(fileName.equals("invalid_file")){
				return;
			}
			Thread.sleep(100);
			fileLength=Integer.valueOf(read(clientSoc));

			byte[] fileBuf=new byte[fileLength];
			InputStream is=clientFileSoc.getInputStream();
			File file=new File(Server.FILEPATH+"\\"+fileName);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fos=new FileOutputStream(file);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			int bytesRead=is.read(fileBuf,0,fileBuf.length);
			Thread.sleep(100);
			bos.write(fileBuf,0,bytesRead);
			bos.flush();
		
			//if receiver has no pending file at beginning
			if(!Server.user_file_map.containsKey(receiverName)){
				Server.user_file_map.put(receiverName,new ArrayList<String[]>());
			}
			
			ArrayList<String[]> arr=Server.user_file_map.get(receiverName);
			String[] receiveInfo=new String[2];
			receiveInfo[0]=userName;
			receiveInfo[1]=fileName;
			arr.add(receiveInfo);
			Server.user_file_map.remove(receiverName);
			Server.user_file_map.put(receiverName,arr);
		}catch(Exception e){
			println("failed in send_file in subserver");
		}
	}

	public void send_chatroomFile(String chatroom){
		String fileName;
		int fileLength;

		try{
			fileName=read(clientSoc);
			Thread.sleep(100);
			fileLength=Integer.valueOf(read(clientSoc));

			byte[] fileBuf=new byte[fileLength];
			InputStream is=clientFileSoc.getInputStream();
			File file=new File(Server.FILEPATH+"\\"+fileName);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fos=new FileOutputStream(file);
			BufferedOutputStream bos=new BufferedOutputStream(fos);
			int bytesRead=is.read(fileBuf,0,fileBuf.length);
			Thread.sleep(100);
			bos.write(fileBuf,0,bytesRead);
			bos.flush();
		
			//if receiver has no pending file at beginning
			if(!Server.chatroom_file_map.containsKey(chatroom)){
				Server.chatroom_file_map.put(chatroom,new ArrayList<String[]>());
			}
			
			ArrayList<String[]> arr=Server.chatroom_file_map.get(chatroom);
			String[] receiveInfo=new String[2];
			receiveInfo[0]=userName;
			receiveInfo[1]=fileName;
			arr.add(receiveInfo);
			Server.chatroom_file_map.remove(chatroom);
			Server.chatroom_file_map.put(chatroom,arr);
		}catch(Exception e){
			println("failed in send_chatroomFile in subserver");
		}
	}

	public ArrayList<String> list_users(){
		ArrayList<String> arr=new ArrayList<String>();
		
		Iterator it=Server.user_password_map.entrySet().iterator();
		while(it.hasNext()){
			HashMap.Entry pair=(HashMap.Entry)it.next();
			arr.add((String)pair.getKey());
		}

		return arr;
	}

	public ArrayList<String> list_userChatroom(){
		return Server.user_chatroom_map.get(userName);
	}

	public void create_chatroom(){
		try{
			String chatroomName=read(clientSoc);
			ArrayList<String> chatroomMember=new ArrayList<String>();

			//check if group exists
			if(Server.chatroom_user_map.containsKey(chatroomName)){
				write(clientSoc,"invalid");
				return;
			}else{
				write(clientSoc,"valid");
				ArrayList<String> arr=new ArrayList<String>();
				if(Server.user_chatroom_map.containsKey(userName)){
					arr=Server.user_chatroom_map.get(userName);
				}
				arr.add(chatroomName);
				Server.user_chatroom_map.remove(userName);
				Server.user_chatroom_map.put(userName,arr);
				chatroomMember.add(userName);
			}

			//list all users
			ArrayList<String> userArr=list_users();
			write(clientSoc,Integer.toString(userArr.size()));
			for(int i=0;i<userArr.size();i++){
				Thread.sleep(100);
				write(clientSoc,userArr.get(i));
			}

			//add users to chatroom
			String name;
			while(!(name=read(clientSoc)).equals("end")){
				if(!userExist(name)){
					write(clientSoc,"notExist");
				}else{
					ArrayList<String> arr=new ArrayList<String>();
					if(Server.user_chatroom_map.containsKey(name)){
						arr=Server.user_chatroom_map.get(name);
					}
					arr.add(chatroomName);
					Server.user_chatroom_map.remove(name);
					Server.user_chatroom_map.put(name,arr);
					chatroomMember.add(name);
					write(clientSoc,"valid");
				}
			}

			//update chatroom_user_map
			Server.chatroom_user_map.put(chatroomName,chatroomMember);
		}catch(Exception e){
			println("failed in create_chatroom in subserver");
		}
	}

	public void chat(){
		try{
			ArrayList<String> userChatroom=list_userChatroom();
			write(clientSoc,Integer.toString(userChatroom.size()));
			for(int i=0;i<userChatroom.size();i++){
				Thread.sleep(100);
				write(clientSoc,userChatroom.get(i));
			}
			String chatroomName=read(clientSoc);

			//check if chatroom exists
			if(Server.chatroom_user_map.containsKey(chatroomName)){
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
				return;
			}

			String comm;
			while(!(comm=read(clientSoc)).equals("end")){
				switch(comm){
					case "send_message":
						send_chatroomMessage(chatroomName);
						break;
					case "send_file":
						send_chatroomFile(chatroomName);
						break;
					default:
						break;
				}
			}
		}catch(Exception e){
			println("failed in chat in subserver");
		}
	}

	public void receive_message(){
		try{
			//check if any message
			if(!Server.user_message_map.containsKey(userName)){
				write(clientSoc,Integer.toString(0));
				return;
			}

			ArrayList<String> arr=Server.user_message_map.get(userName);
			int messageNum=arr.size();
			write(clientSoc,Integer.toString(messageNum));
			for(int i=0;i<messageNum;i++){
				Thread.sleep(100);
				write(clientSoc,arr.get(i));
			}
			Server.user_message_map.remove(userName);
			Server.user_message_map.put(userName,new ArrayList<String>());
		}catch(Exception e){
			println("failed in receive_message in subserver");
		}
	}

	public void receive_chatroomMessage(){
		try{
			if(!Server.user_chatroom_map.containsKey(userName)){
				ArrayList<String> arr=new ArrayList<String>();
				Server.user_chatroom_map.put(userName,arr);
			}
			ArrayList<String> chatroomList=Server.user_chatroom_map.get(userName);
			write(clientSoc,Integer.toString(chatroomList.size()));
			for(int i=0;i<chatroomList.size();i++){
				String chatroomName=chatroomList.get(i);

				write(clientSoc,chatroomName);
				ArrayList<String> arr=Server.chatroom_message_map.get(chatroomName);
				int messageNum=arr.size();
				write(clientSoc,Integer.toString(messageNum));
				for(int j=0;j<messageNum;j++){
					Thread.sleep(100);
					write(clientSoc,arr.get(j));
				}
			}
		}catch(Exception e){
			println("failed in receive_message in subserver");
		}
	}

	public void receive_file(){
		try{
			//check if any file
			if(!Server.user_file_map.containsKey(userName)){
				write(clientSoc,Integer.toString(0));
				return;
			}

			ArrayList<String[]> arr=Server.user_file_map.get(userName);
			int fileNum=arr.size();
			write(clientSoc,Integer.toString(fileNum));
			for(int i=0;i<fileNum;i++){
				String[] fileInfo=new String[2];
				fileInfo=arr.get(i);
				write(clientSoc,fileInfo[0]);//sender name
				Thread.sleep(100);
				File file=new File(Server.FILEPATH+"\\"+fileInfo[1]);//file path

				//check if file exists
				if(!file.exists() || file.isDirectory()){
					println("File does not exist or it is a directory");
					return;
				}

				//send file name and length
				write(clientSoc,file.getName());
				Thread.sleep(100);
				write(clientSoc,Integer.toString((int)file.length()));

				//send file
				int fileLength=(int)file.length();
				byte[] fileBuf=new byte[fileLength];
				BufferedInputStream bis=new BufferedInputStream(new FileInputStream(file));
				bis.read(fileBuf,0,fileBuf.length);
				OutputStream outStr=clientFileSoc.getOutputStream();
				outStr.write(fileBuf,0,fileBuf.length);
				outStr.flush();
			}

		}catch(Exception e){
			println("failed in receive_file in subserver");
		}
	}

	public void logout(){
		try{
			if(Server.user_socket_map.containsKey(userName)==true){
				Server.user_socket_map.remove(userName);
				isLogout=true;
				write(clientSoc,"valid");
			}else{
				write(clientSoc,"invalid");
			}
		}catch(Exception e){
			println("failed in logout in subserver");
		}
	}
}
