import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatServer extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;
    private BufferedReader in;

    public ChatServer() {
        // 1. SETUP THE GUI
        setTitle("Chat Server");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the main frame to your preferred layout
        setLayout(new GridLayout(0, 2));

        // Column 1: Chat history area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea));

        // Column 2: A panel to hold the text field and button
        // We use GridLayout(0, 2) here as well!
        JPanel controlPanel = new JPanel(new GridLayout(0, 2));
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        
        controlPanel.add(messageField);
        controlPanel.add(sendButton);
        
        add(controlPanel);

        // What happens when you click Send or press Enter
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        setVisible(true);

        // 2. START THE NETWORK 
        startServer();
    }

    private void sendMessage() {
        String msg = messageField.getText();
        if (!msg.isEmpty() && out != null) {
            out.println(msg); 
            chatArea.append("Server: " + msg + "\n"); 
            messageField.setText(""); 
        }
    }

    private void startServer() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(9806);
                chatArea.append("Waiting for a client on port 9806...\n");
                
                Socket socket = serverSocket.accept(); 
                chatArea.append("Client connected!\n");

                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    chatArea.append("Client: " + message + "\n");
                }
            } catch (Exception e) {
                chatArea.append("Error: " + e.getMessage() + "\n");
            }
        }).start(); 
    }

    public static void main(String[] args) {
        new ChatServer();
    }
}