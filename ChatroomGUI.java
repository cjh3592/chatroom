import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.awt.BorderLayout;  
import java.awt.FileDialog;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.File;  
  
import javax.swing.JButton;  
import javax.swing.JFileChooser;  
import javax.swing.JFrame;  
 
public class ChatroomGUI {
    private Client client = new Client();
    private boolean clientLogin = false;
    private boolean findChat = false;
    private String chatPartner;
	private JFrame frame;
    private JFrame regFrame;
    private JFrame searchFrame;
    private JFrame chatFrame;
    /* Login gui */
    private JLabel header;
    private JLabel account;
    private JLabel password;
    private JLabel text;
    private JTextField input;
    private JPasswordField input2;
    private JButton button;
    private JButton button2;
    
    private String _account;
    private String _password;

    /* Register gui */
    private JLabel regheader;
    private JLabel regaccount;
    private JLabel regpassword;
    private JLabel regemail;
    private JLabel text2;
    private JLabel text3;
    private JTextField input3;
    private JPasswordField input4;
    private JTextField input5;
    private JButton button3;

    private String _regAccount;
    private String _regPassword;
    private String _regEmail;

    /* Search gui */
    private JLabel message;
    private JLabel result;
    private JTextField searchId;
    private JButton sButton;

    private String _sid;

    /* Chat gui */
    private JLabel user;
    private JTextArea historyMessage;
    private JTextArea sendMessage;
    private JLabel fileMessage;
    private JButton sendButton;
    private JButton fileButton;

    private String chatMessage;

	public ChatroomGUI() {
		frame = new JFrame(); // login
        regFrame = new JFrame();
        searchFrame = new JFrame();
        chatFrame = new JFrame();
        frame.setResizable(false);
        regFrame.setResizable(false);
        searchFrame.setResizable(false);
        chatFrame.setResizable(false);
	}
    public void chatroom () {
        loginGUI();
        regGUI();
        searchGUI();
        
            Receive r = new Receive();
            r.start();
    }
    public void addBlankRow(int column, int row, JFrame frame)
    {
        JLabel blank = new JLabel("    ");
        GridBagConstraints c5 = new GridBagConstraints();
        c5.gridx = column;
        c5.gridy = row;
        c5.gridwidth = 1;
        c5.gridheight = 1;
        c5.weightx = 0;
        c5.weighty = 0;
        c5.fill = GridBagConstraints.BOTH;
        c5.anchor = GridBagConstraints.CENTER;
        frame.add(blank, c5);
    }
/*======================== TAGL:LOGIN ==========================================*/
	public void loginGUI() {
		frame.setSize(400, 300);
		frame.setLayout(new GridBagLayout());
        frame.setTitle("Chatroom");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        header = new JLabel("Chatroom");
        header.setFont(new Font("Serif", Font.BOLD, 36));
        header.setForeground(new Color(0, 204, 0));
        GridBagConstraints c8 = new GridBagConstraints();
        c8.gridx = 2;
        c8.gridy = 0;
        c8.gridwidth = 3;
        c8.gridheight = 2;
        c8.weightx = 0;
        c8.weighty = 0;
        c8.fill = GridBagConstraints.NONE;
        c8.anchor = GridBagConstraints.CENTER;
        frame.add(header, c8);

        account = new JLabel("Account: ");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        frame.add(account, c);
        
        input = new JTextField();
        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridx = 1;
        c3.gridy = 2;
        c3.gridwidth = 6;
        c3.gridheight = 1;
        c3.weightx = 0;
        c3.weighty = 0;
        c3.fill = GridBagConstraints.BOTH;
        c3.anchor = GridBagConstraints.WEST;
        frame.add(input, c3);

        for(int i = 0; i < 7; i++) {
            addBlankRow(i,3, frame);
        }
        
        password = new JLabel("Password: ");
	    GridBagConstraints c2 = new GridBagConstraints();
		c2.gridx = 0;
        c2.gridy = 4;
        c2.gridwidth = 1;
        c2.gridheight = 1;
        c2.weightx = 0;
        c2.weighty = 0;
        c2.fill = GridBagConstraints.NONE;
        c2.anchor = GridBagConstraints.CENTER;
        frame.add(password, c2);
     
        input2 = new JPasswordField();
        GridBagConstraints c4 = new GridBagConstraints();
        c4.gridx = 1;
        c4.gridy = 4;
        c4.gridwidth = 6;
        c4.gridheight = 1;
        c4.weightx = 0;
        c4.weighty = 0;
        c4.fill = GridBagConstraints.BOTH;
        c4.anchor = GridBagConstraints.WEST;
        frame.add(input2, c4);

        text = new JLabel("");
        text.setForeground(Color.RED);      
        GridBagConstraints c6 = new GridBagConstraints();
        c6.gridx = 0;
        c6.gridy = 5;
        c6.gridwidth = 4;
        c6.gridheight = 1;
        c6.weightx = 0;
        c6.weighty = 0;
        c6.fill = GridBagConstraints.NONE;
        c6.anchor = GridBagConstraints.WEST;
        frame.add(text, c6);
     	
        addBlankRow(0,6,frame);

     	button = new JButton("  Login  ");
        GridBagConstraints c7 = new GridBagConstraints();
        c7.gridx = 4;
        c7.gridy = 7;
        c7.gridwidth = 2;
        c7.gridheight = 1;
        c7.weightx = 0;
        c7.weighty = 0;
        c7.fill = GridBagConstraints.BOTH;
        c7.anchor = GridBagConstraints.CENTER;
        frame.add(button, c7);

        button2 = new JButton("Register");
      //  GridBagConstraints c7 = new GridBagConstraints();
        c7.gridx = 1;
        c7.gridy = 7;
        c7.gridwidth = 2;
        c7.gridheight = 1;
        c7.weightx = 0;
        c7.weighty = 0;
        c7.fill = GridBagConstraints.BOTH;
        c7.anchor = GridBagConstraints.CENTER;
        frame.add(button2, c7);

       // JTextField t = input;
        input.addActionListener(new AccountListener());
        input.addCaretListener(new AccountListener());
        input2.addActionListener(new PasswordListener());
        input2.addCaretListener(new PasswordListener());
        button.addActionListener(new LoginListener());
        button2.addActionListener(new RegListener());

        frame.setVisible(true);
	}
    class AccountListener implements ActionListener, CaretListener {
        public void actionPerformed(ActionEvent event) {
            _account = input.getText();
        }
        public void caretUpdate(CaretEvent event) {
            String temp_account = input.getText();
            if(temp_account!=null)
                _account = temp_account;             
        }  
    }
    class PasswordListener implements ActionListener, CaretListener {
        public void actionPerformed(ActionEvent event) {
            char[] c;    
            c = input2.getPassword();
            if(c != null)
                for(char i:c) {
                    _password = _password+i;
                }
        }
        public void caretUpdate(CaretEvent event) {
            char c[];
            c = input2.getPassword();
            _password = String.valueOf(c);
            System.out.println("p= "+_password);

        }   
    }
    class LoginListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            boolean b = client.login(_account, _password);
            if(b) {
                searchFrame.setVisible(true);
                frame.setVisible(false);
                clientLogin = b;
                System.out.println("login");
            }else {
                text.setText("account or password is wrong!");
            }
        }
    }

    class RegListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            input.setText("");
            input2.setText("");
            frame.setVisible(false);
            regFrame.setVisible(true);
        }
    }

/*====================== TAGR:Register ===============================*/
    public void regGUI() {
        regFrame.setSize(400, 300);
        regFrame.setLayout(new GridBagLayout());
        regFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        regFrame.setTitle("Register");

        regheader = new JLabel(" Register ");
        regheader.setFont(new Font("Serif", Font.BOLD, 36));
        regheader.setForeground(new Color(0, 204, 0));
        GridBagConstraints c8 = new GridBagConstraints();
        c8.gridx = 1;
        c8.gridy = 0;
        c8.gridwidth = 3;
        c8.gridheight = 2;
        c8.weightx = 0;
        c8.weighty = 0;
        c8.fill = GridBagConstraints.NONE;
        c8.anchor = GridBagConstraints.CENTER;
        regFrame.add(regheader, c8);

        regaccount = new JLabel("Account: ");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 0;
        c.weighty = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.WEST;
        regFrame.add(regaccount, c);
            
            input3 = new JTextField();
            GridBagConstraints c3 = new GridBagConstraints();
            c3.gridx = 1;
            c3.gridy = 2;
            c3.gridwidth = 6;
            c3.gridheight = 1;
            c3.weightx = 0;
            c3.weighty = 0;
            c3.fill = GridBagConstraints.BOTH;
            c3.anchor = GridBagConstraints.WEST;
            regFrame.add(input3, c3);

            text2 = new JLabel("");
            text2.setForeground(Color.RED);     
            GridBagConstraints c6 = new GridBagConstraints();
            c6.gridx = 0;
            c6.gridy = 3;
            c6.gridwidth = 4;
            c6.gridheight = 1;
            c6.weightx = 0;
            c6.weighty = 0;
            c6.fill = GridBagConstraints.NONE;
            c6.anchor = GridBagConstraints.WEST;
            regFrame.add(text2, c6);
                
            regpassword = new JLabel("Password: ");
            GridBagConstraints c2 = new GridBagConstraints();
            c2.gridx = 0;
            c2.gridy = 4;
            c2.gridwidth = 1;
            c2.gridheight = 1;
            c2.weightx = 0;
            c2.weighty = 0;
            c2.fill = GridBagConstraints.NONE;
            c2.anchor = GridBagConstraints.WEST;
            regFrame.add(regpassword, c2);
        
            input4 = new JPasswordField();
            GridBagConstraints c4 = new GridBagConstraints();
            c4.gridx = 1;
            c4.gridy = 4;
            c4.gridwidth = 6;
            c4.gridheight = 1;
            c4.weightx = 0;
            c4.weighty = 0;
            c4.fill = GridBagConstraints.BOTH;
            c4.anchor = GridBagConstraints.WEST;
            regFrame.add(input4, c4);

            text3 = new JLabel("");
            c6 = new GridBagConstraints();
            c6.gridx = 0;
            c6.gridy = 5;
            c6.gridwidth = 4;
            c6.gridheight = 1;
            c6.weightx = 0;
            c6.weighty = 0;
            c6.fill = GridBagConstraints.NONE;
            c6.anchor = GridBagConstraints.WEST;
            regFrame.add(text3, c6);

            regemail = new JLabel("Email: ");
            c6 = new GridBagConstraints();
            c6.gridx = 0;
            c6.gridy = 6;
            c6.gridwidth = 1;
            c6.gridheight = 1;
            c6.weightx = 0;
            c6.weighty = 0;
            c6.fill = GridBagConstraints.NONE;
            c6.anchor = GridBagConstraints.WEST;
            regFrame.add(regemail, c6);

            input5 = new JTextField();
            GridBagConstraints c5 = new GridBagConstraints();
            c5.gridx = 1;
            c5.gridy = 6;
            c5.gridwidth = 6;
            c5.gridheight = 1;
            c5.weightx = 0;
            c5.weighty = 0;
            c5.fill = GridBagConstraints.BOTH;
            c5.anchor = GridBagConstraints.WEST;
            regFrame.add(input5, c5);

            addBlankRow(0, 7,regFrame);

            button3 = new JButton(" Confirm ");
            GridBagConstraints c7 = new GridBagConstraints();
            c7.gridx = 2;
            c7.gridy = 8;
            c7.gridwidth = 1;
            c7.gridheight = 1;
            c7.weightx = 0;
            c7.weighty = 0;
            c7.fill = GridBagConstraints.BOTH;
            c7.anchor = GridBagConstraints.CENTER;
            regFrame.add(button3, c7); 

            regFrame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    input3.setText("");
                    input4.setText("");
                    input5.setText("");
                    frame.setVisible(true);
                }
            });

            input3.addActionListener(new RegAccountListener());
            input3.addCaretListener(new RegAccountListener());
            input4.addActionListener(new RegPasswordListener());
            input4.addCaretListener(new RegPasswordListener());
            input5.addActionListener(new RegEmailListener());
            input5.addCaretListener(new RegEmailListener());
            button3.addActionListener(new RegConfirmListener());
        //    button.addActionListener(new LoginListener());
  
            regFrame.setVisible(false);
    }  
    class RegAccountListener implements ActionListener, CaretListener {
        public void actionPerformed(ActionEvent event) {
            _regAccount = input3.getText();
        }
        public void caretUpdate(CaretEvent event) {
            _regAccount = input3.getText();
        }   
    }
    class RegPasswordListener implements ActionListener, CaretListener {
        public void actionPerformed(ActionEvent event) {
            char[] c;
            c = input4.getPassword();
            _regPassword = String.valueOf(c);
        }
        public void caretUpdate(CaretEvent event) {
            char[] c;
            c = input4.getPassword();
            _regPassword = String.valueOf(c);
            System.out.println("p= "+_regPassword);
        }
    }
    class RegEmailListener implements ActionListener, CaretListener {
        public void actionPerformed(ActionEvent event) {
            _regEmail = input5.getText();
        }
        public void caretUpdate(CaretEvent event) {
            _regEmail = input5.getText();
        }
    }
    class RegConfirmListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
       //     boolean isAccountExist = true;
               
            boolean isAccountExist = client.register(_regAccount, _regPassword);
            if(_regAccount==null || _regAccount=="" || _regPassword == "") {
                    isAccountExist = false;
            }
           if(!isAccountExist) {
                if(_regAccount==null || _regAccount=="" || _regPassword == "") 
                    text2.setText("Please choose an account name!");
                else
                    text2.setText("account existed!");
                input4.setText("");
                input5.setText("");
            }else {
                // login into UI
                clientLogin = true;
                _account = _regAccount;
                _password = _regPassword;
                regFrame.setVisible(false);
                searchFrame.setVisible(true);
            }
        }
    }
/*   ====================== search user =================================== */
    public void searchGUI() {
        searchFrame.setSize(400, 100);
        searchFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchFrame.setTitle("Search");
        searchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        searchFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                client.logout();        
            }
        });


        message = new JLabel(" Please enter ID you want to chat:");
        searchId = new JTextField(22);
        sButton = new JButton("Search");
        result = new JLabel("");
        result.setForeground(Color.RED);     

        searchFrame.add(message);
        searchFrame.add(searchId);
        searchFrame.add(sButton);
        searchFrame.add(result);

        searchId.addActionListener(new SearchIdListener());
        searchId.addCaretListener(new SearchIdListener());
        sButton.addActionListener(new SearchButtonListener());

        searchFrame.setVisible(false);
    }
    String temp_id;
    class SearchIdListener implements ActionListener, CaretListener {
        public void actionPerformed(ActionEvent event) {
            _sid = searchId.getText();
            boolean userExist = client.isUserExist(_sid);
            boolean userOnline = client.isUserOnline(_sid);
           // boolean userExist = true;
           // boolean userOnline = false;

            System.out.println("search id = " + _sid);
            if(!userExist) {
                result.setText("User is not exist!");
            }else if (!userOnline){
                result.setText("User is offline!");
            }else {
                chatPartner = _sid;
                chatGUI();
                //do something
            }

            searchId.setText("");
        }
        public void caretUpdate(CaretEvent event) {
            temp_id = searchId.getText();
            if(temp_id != "") 
                _sid = temp_id;
            System.out.println("Chatroom chatPartner: " + chatPartner);
        }
    }

    class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            boolean userExist = client.isUserExist(_sid);
            boolean userOnline = client.isUserOnline(_sid);
           // boolean userExist = true;
           // boolean userOnline = true;

            System.out.println("search id = " + _sid);
            if(!userExist) {
                result.setText("User is not exist!");
            }else if (!userOnline){
                result.setText("User is offline!");
            }else {
                chatPartner = _sid;
                System.out.println("Chatroom " + chatPartner);
                chatGUI();
                findChat = true;
            }

            searchId.setText("");
        }
    }
 /*  ========================== Chat Window ============================= */
    public void chatGUI() {
        chatFrame = new JFrame();

        chatFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chatFrame.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 7));
        chatFrame.setSize(350, 600);
        chatFrame.setVisible(true);

        user = new JLabel(_sid);
        user.setFont(new Font("Serif", Font.BOLD, 20));
        historyMessage = new JTextArea(23, 28);
        historyMessage.setEditable(false);
        fileButton = new JButton("choose file");
        fileMessage = new JLabel("choose a file to send...");
        sendMessage = new JTextArea(5, 28);
        sendMessage.setEditable(true);
        sendButton = new JButton("Send");
        
        chatFrame.add(user);
        chatFrame.add(historyMessage);
        chatFrame.add(fileButton);
        chatFrame.add(fileMessage);
        chatFrame.add(sendMessage);
        chatFrame.add(sendButton);

        fileButton.addActionListener(new FileButtonListener());
        sendMessage.addCaretListener(new SendMessageListener());
        sendButton.addActionListener(new SendButtonListener());

<<<<<<< HEAD
        chatFrame.setVisible(true);
        chatFrame.setResizable(false);
       
=======
        while(true) {
            ArrayList<String> receiveChat;
            ArrayList<String[]> receiveFile; 
            if((receiveChat = client.receive_message())!=null){
                if(clientLogin) {
                    for(int i =0; i < receiveChat.size(); i++)
                        historyMessage.append(receiveChat.get(i)+"\n");
                }
            }
            if((receiveFile = client.receive_file())!=null){
                if(clientLogin) {
                    for(int i = 0; i < receiveFile.size(); i++) {
                        historyMessage.append(receiveFile.get(i)[0]+": \'"+receiveFile.get(i)[1]+"\' has received!");
                    }

                }
            }

        }

>>>>>>> 467370462dbfd13323a8888846fdafc29c55121d
    }
    String filename = null;
    String filepath = null;
    String file = null;
    FileDialog fd = new FileDialog(chatFrame, "FileDialog", FileDialog.LOAD);   

    class FileButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            fd.setVisible(true);
            if(fd!=null){   
                filepath=fd.getDirectory();
                filename = fd.getFile();
                file = filepath + filename;  
            }else {
                fileMessage.setText("choose a file to send...");
            }
            
            if(filename!="")
                fileMessage.setText(filename);
            else
                fileMessage.setText("choose a file to send...");
        }
    }
    class SendMessageListener implements CaretListener {
        public void caretUpdate(CaretEvent event) {
           String temp_message = sendMessage.getText();
           if(temp_message!=null)
            chatMessage = temp_message;
        }
    }
    class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event){
            System.out.println("chatPartner send:" + chatMessage);
            if(chatMessage!=null) {
                client.send_message(chatPartner, chatMessage);
                historyMessage.append(_account+": "+chatMessage+"\n");
                sendMessage.setText("");
                chatMessage = "";
            }
     /*
            if(file != null){
                client.send_file(chatPartner, file);
                historyMessage.append(_account+": "+"\'"+filename+"\'"+" has send"+"\n");
                file=null;
                fileMessage.setText("choose a file to send...");
            }
            */
        }
    }
    class Receive extends Thread {
        public void run() {
            while(true) {
                System.out.println("clientLogin" + clientLogin);
                ArrayList<String> receiveChat;
                ArrayList<String[]> receiveFile; 

                if(findChat) {
                    if((receiveChat = client.receive_message())!=null){
     //                       System.out.println(receiveChat.get(0));        
    //                        for(int i =0; i < receiveChat.size(); i++)
                                historyMessage.append(receiveChat.get(0)+"\n");
                    }
                /*
                     if((receiveFile = client.receive_file())!=null){
                        for(int i = 0; i < receiveFile.size(); i++)
                            historyMessage.append(receiveFile.get(0)[0]+": \'"+receiveFile.get(0)[1]+"\' has received!");                        }                
                    } 
                    */
                } 
            }
        }
    }

}
