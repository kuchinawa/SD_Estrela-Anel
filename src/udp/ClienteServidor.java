package udp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClienteServidor implements Runnable {
    private int serverPort;
    private int clientPort;
    private int nextPort;

    public ClienteServidor(int clientPort, int serverPort) {
        this.clientPort = clientPort;
        this.serverPort = serverPort;
        this.nextPort = clientPort + 1;
    }

    @Override
    public void run() {
        startServer();
    }

    private void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Servidor na porta " + serverPort + " iniciado.");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão recebida de " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClienteHandler(clientSocket, nextPort)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o número da porta do cliente: ");
        int clientPort = scanner.nextInt();

        System.out.print("Digite o número da porta do servidor: ");
        int serverPort = scanner.nextInt();

        // Iniciar a execução
        new Thread(new ClienteServidor(clientPort, serverPort)).start();
    }

    static class ClienteHandler implements Runnable {
        private Socket clientSocket;
        private int nextPort;

        public ClienteHandler(Socket clientSocket, int nextPort) {
            this.clientSocket = clientSocket;
            this.nextPort = nextPort;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

                while (true) {
                    String message = reader.readLine();
                    if (message == null || message.equals("exit")) break;
                    System.out.println("Mensagem recebida: " + message);
                    Thread.sleep(1000); // Simular algum processamento
                    sendMessage(nextPort, message);
                }

                clientSocket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void sendMessage(int port, String message) {
            try {
                Socket socket = new Socket("localhost", port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println(message);
                socket.close();
            } catch (IOException e) {
                System.out.println("Porta " + port + " não está ativa. Tentando novamente em 10 segundos...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                sendMessage(port, message);
            }
        }
    }
}
