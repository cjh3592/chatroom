//package bdn;
import java.net.*;
import java.io.*;
import java.util.*;

public class MultipleSocketServer implements Runnable {
  public static Socket[] socket = new Socket[50];
  private Socket connection;
  private int ID;
  String msg_to_client;
  String msg_from_client;
  String req_from_client;
  MultipleSocketServer(Socket s, int i) {
    this.connection = s;
    this.ID = i;
  }
  public String read(Socket connection) {
   try{
      BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
      InputStreamReader isr = new InputStreamReader(is);
      int character;
      StringBuffer process = new StringBuffer();
      while((character = isr.read()) != 13) {
        process.append((char)character);
      }
      return process.toString();
    }catch(Exception e) {
      return "server read fail!";
    }
  }
  public void write(Socket connection, String data) {
    try{
      BufferedOutputStream os = new BufferedOutputStream(connection.getOutputStream());
      OutputStreamWriter osw = new OutputStreamWriter(os, "US-ASCII");
      osw.write(data);
      osw.flush(); 
    }catch(Exception e){
      System.out.println("server write fail");
    }
  }
  public void run() {
      try {
          msg_to_client = "your id = " + ID + (char) 13;
          write(connection, msg_to_client);
          Thread.sleep(1000);
          msg_to_client = "chat(M)"+(char) 13;
          write(connection, msg_to_client);  
          req_from_client = read(connection);
          if(req_from_client.equals("M")){
            msg_to_client = "choose a person to chat "+(char) 13;
            write(connection, msg_to_client);
            String id_to_chat;
            id_to_chat = read(connection);
            msg_to_client = "message to him "+(char) 13;
            String chat_msg;
            write(connection, msg_to_client);
            chat_msg = read(connection) + (char) 13;
            System.out.println(id_to_chat);
            System.out.println(socket[1]+" "+ connection);
            write(socket[Integer.parseInt(id_to_chat)], chat_msg);
          }
        }
       catch (Exception e) {
         System.out.println(e);
       }finally {
       try {
          connection.close();
      }catch (IOException e){}
     }
    }
  public static void main(String[] args) {
    int port = 20012;
    int count = 0;
    
    for(int i=0; i<50;i++)
      socket[i] = new Socket();
    try{
      ServerSocket socket1 = new ServerSocket(port);
      System.out.println("MultipleSocketServer Initialized");
      while (true) {
        Socket connection = socket1.accept();
        socket[count] = connection;
        Runnable runnable = new MultipleSocketServer(connection, count++);
        Thread thread = new Thread(runnable);
        thread.start();  
      }
    }catch (Exception e) {}
  }
}
