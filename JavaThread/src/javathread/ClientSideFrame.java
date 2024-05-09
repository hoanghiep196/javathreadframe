/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package javathread;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ClientSideFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea statusArea;

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;

    public ClientSideFrame() {
        super("Client Login");

        // Create components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        statusArea = new JTextArea(10, 30);
        statusArea.setEditable(false);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                    // Send username and password to server
                    out.println(username);
                    out.println(password);

                    // Receive and display server response
                    String response = in.readLine();
                    statusArea.append(response + "\n");

                    // If login attempts exceeded, close client program
                    if (response.contains("Login attempts exceeded")) {
                        JOptionPane.showMessageDialog(ClientSideFrame.this, "Login attempts exceeded. Exiting...");
                        System.exit(0);
                    }
                } catch (IOException ex) {
                    statusArea.append("Error: " + ex.getMessage() + "\n");
                }
            }
        });

        // Layout components
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);

        // Add components to the frame
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(new JScrollPane(statusArea), BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientSideFrame();
            }
        });
    }
}

