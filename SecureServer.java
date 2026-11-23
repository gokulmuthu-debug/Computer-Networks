import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class SecureServer extends JFrame 
{
    private JTextArea logArea;

    public SecureServer() 
    {
        setTitle("Secure File Server");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea));

        JPanel controlPanel = new JPanel(new BorderLayout());
        JLabel statusLabel = new JLabel("Server Running on Port 3333", SwingConstants.CENTER);
        controlPanel.add(statusLabel, BorderLayout.CENTER);
        
        add(controlPanel);
        setVisible(true);
        startServer();
    }

    private void startServer() 
    {
        new Thread(() -> {
            try 
            {
                ServerSocket serverSocket = new ServerSocket(3333);
                logArea.append("Waiting for clients on Port 3333...\n");

                while (true) 
                {
                    Socket socket = serverSocket.accept();
                    logArea.append("Client connected!\n");
                    handleClient(socket);
                }
            } 
            catch (Exception e) 
            {
                logArea.append("Server Error: " + e.getMessage() + "\n");
            }
        }).start();
    }

    private String readLineDirect(InputStream in) throws IOException 
    {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1 && c != '\n') 
        {
            if (c != '\r') sb.append((char) c);
        }
        return sb.toString();
    }

    private void handleClient(Socket socket) 
    {
        new Thread(() -> {
            try 
            {
                InputStream inStream = socket.getInputStream();
                OutputStream outStream = socket.getOutputStream();

                String command = readLineDirect(inStream);
                String filename = readLineDirect(inStream);

                File dir = new File("server_files");
                if (!dir.exists()) dir.mkdir();
                File file = new File(dir, filename);

                if ("UPLOAD".equals(command)) 
                {
                    FileOutputStream fos = new FileOutputStream(file);
                    int c;
                    while ((c = inStream.read()) != -1) 
                    {
                        int decrypted = (c - 3) % 256;
                        if (decrypted < 0) decrypted += 256;
                        fos.write(decrypted);
                    }
                    fos.close();
                    logArea.append("Received and saved (encrypted): " + filename + "\n");
                } 
                else if ("DOWNLOAD".equals(command)) 
                {
                    if (file.exists()) 
                    {
                        outStream.write(1); 
                        FileInputStream fis = new FileInputStream(file);
                        int c;
                        while ((c = fis.read()) != -1) 
                        {
                            outStream.write((c + 3) % 256);
                        }
                        fis.close();
                        logArea.append("Sent (encrypted) file: " + filename + "\n");
                    } 
                    else 
                    {
                        outStream.write(0); 
                        logArea.append("Client requested missing file: " + filename + "\n");
                    }
                }
                socket.close();
            } 
            catch (Exception e) 
            {
                logArea.append("Transfer Error: " + e.getMessage() + "\n");
            }
        }).start();
    }

    public static void main(String[] args) 
    {
        new SecureServer();
    }
}