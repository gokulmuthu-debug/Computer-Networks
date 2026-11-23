import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class SecureClient extends JFrame 
{
    private JTextArea logArea;
    private JTextField fileField;

    public SecureClient() 
    {
        setTitle("Secure File Client");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(0, 2));

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea));

        JPanel controlPanel = new JPanel(new GridLayout(0, 2));
        fileField = new JTextField();
        JButton uploadBtn = new JButton("Upload");
        JButton downloadBtn = new JButton("Download");

        controlPanel.add(new JLabel(" Enter Filename:", SwingConstants.CENTER));
        controlPanel.add(fileField);
        controlPanel.add(uploadBtn);
        controlPanel.add(downloadBtn);

        add(controlPanel);

        uploadBtn.addActionListener(e -> transferFile("UPLOAD"));
        downloadBtn.addActionListener(e -> transferFile("DOWNLOAD"));

        setVisible(true);
    }

    private void transferFile(String action) 
    {
        String filename = fileField.getText().trim();
        if (filename.isEmpty()) 
        {
            logArea.append("Please enter a filename.\n");
            return;
        }

        new Thread(() -> {
            try 
            {
                // Create a folder to keep client files organized
                File dir = new File("client_files");
                if (!dir.exists()) dir.mkdir();
                File file = new File(dir, filename);

                if (action.equals("UPLOAD") && !file.exists()) 
                {
                    logArea.append("File not found to upload: " + filename + "\n");
                    return;
                }

                Socket socket = new Socket("127.0.0.1", 3333);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                OutputStream outStream = socket.getOutputStream();
                InputStream inStream = socket.getInputStream();

                // Tell the server what we want to do, and the filename
                out.println(action);
                out.println(filename);

                if (action.equals("UPLOAD")) 
                {
                    FileInputStream fis = new FileInputStream(file);
                    int c;
                    while ((c = fis.read()) != -1) 
                    {
                        outStream.write((c + 3) % 256); // Encrypt using Caesar Cipher (+3)
                    }
                    fis.close();
                    logArea.append("Uploaded successfully (encrypted): " + filename + "\n");
                } 
                else if (action.equals("DOWNLOAD")) 
                {
                    int exists = inStream.read(); // Read server's status check
                    if (exists == 1) 
                    {
                        FileOutputStream fos = new FileOutputStream(file);
                        int c;
                        while ((c = inStream.read()) != -1) 
                        {
                            int decrypted = (c - 3) % 256;
                            if (decrypted < 0) decrypted += 256; // Handle negative modulo in Java
                            fos.write(decrypted); // Decrypt using Caesar Cipher (-3)
                        }
                        fos.close();
                        logArea.append("Downloaded & decrypted successfully.\n");
                    } 
                    else 
                    {
                        logArea.append("Server says: File not found.\n");
                    }
                }
                socket.close();
            } 
            catch (Exception e) 
            {
                logArea.append("Error: " + e.getMessage() + "\n");
            }
        }).start();
    }

    public static void main(String[] args) 
    {
        new SecureClient();
    }
}