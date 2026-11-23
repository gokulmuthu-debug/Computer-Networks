import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClient extends JFrame {
    private JTextArea chatArea;
    private JTextField messageField;
    private PrintWriter out;
    private BufferedReader in;

    public ChatClient() {
        // 1. SETUP THE GUI
        setTitle("Chat Client");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set the main frame to your preferred layout
        setLayout(new GridLayout(0, 2));

        // Column 1: Chat history area
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea));

        // Column 2: Panel for text field and button
        JPanel controlPanel = new JPanel(new GridLayout(0, 2));
        messageField = new JTextField();
        JButton sendButton = new JButton("Send");
        
        controlPanel.add(messageField);
        controlPanel.add(sendButton);
        
        add(controlPanel);

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        setVisible(true);

        // 2. START THE NETWORK CONNECTION
        startClient();
    }

    private void sendMessage() {
        String msg = messageField.getText();
        if (!msg.isEmpty() && out != null) {
            out.println(msg); 
            chatArea.append("Client: " + msg + "\n"); 
            messageField.setText(""); 
        }
    }

    private void startClient() {
        new Thread(() -> {
            try {
                // Change "127.0.0.1" to your friend's IP if on different computers
                Socket socket = new Socket("127.0.0.1", 9806);
                chatArea.append("Connected to Server!\n");

                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String message;
                while ((message = in.readLine()) != null) {
                    chatArea.append("Server: " + message + "\n");
                }
            } catch (Exception e) {
                chatArea.append("Error connecting to server.\n");
            }
        }).start();
    }

    public static void main(String[] args) {
        new ChatClient();
    }
}