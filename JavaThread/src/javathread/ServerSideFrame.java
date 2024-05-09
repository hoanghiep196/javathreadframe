/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javathread;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSide {
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    private static int loginAttempts = 0;

    public static void main(String[] args) {
        int portNumber = 5555;

        try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
            System.out.println("Server is running and waiting for client connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);
                ;
                // Handle client connection in a new thread
                new ClientHandler(clientSocket).start();
            }
            
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                // Handle login attempts
                while (loginAttempts < MAX_LOGIN_ATTEMPTS) {
                    String username = in.readLine();
                    String password = in.readLine();

                    // Simulate authentication logic (replace with your actual authentication logic)
                    if (isValidCredentials(username, password)) {
                        out.println("Login successful");
                        break; // Exit loop on successful login
                    } else {
                        out.println("Invalid credentials. Please try again.");
                        loginAttempts++;
                    }
                }

                // After 5 unsuccessful login attempts
                if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
                    out.println("Login attempts exceeded. Refused connection.");
                    System.out.println("Client exceeded login attempts. Closing connection...");
                }

                // Close client connection
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error handling client: " + e.getMessage());
            }
        }

        private boolean isValidCredentials(String username, String password) throws IOException {
            // Replace this with your actual authentication logic (e.g., check against a database)
            BufferedReader fileReader = new BufferedReader(new FileReader("/Users/user/lab1_asm/asm3_ntw201x_ver3/src/asm3_ntw201x_ver3/account.txt"));
            String inputLine;
            boolean isAuthenticated = false;

            // Read client input (username and password)
            String clientInput = in.readLine();
            String[] credentials = clientInput.split(" ");
            String clientUsername = credentials[0];
            String clientPassword = credentials[1];

            // Read account.txt to check credentials
            while ((inputLine = fileReader.readLine()) != null) {
                String[] parts = inputLine.split(" ");
                String storedUsername = parts[0];
                String storedPassword = parts[1];

            // Check if credentials match
            if (clientUsername.equals(storedUsername) && clientPassword.equals(storedPassword)) {
                isAuthenticated = true;
                break;
                }
            }

            // For demonstration purposes, accept any non-empty username and password
            return !username.isEmpty() && !password.isEmpty() && isAuthenticated == true;
        }
    }
}
