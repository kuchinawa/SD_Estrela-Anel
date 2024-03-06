package estrela.model;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private final int porta;
    private final List<Socket> clientes;

    public Servidor(int porta) {
        this.porta = porta;
        this.clientes = new ArrayList<>();
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                clientes.add(clienteSocket);
                System.out.println("Novo cliente conectado: " + clienteSocket);

                Thread clienteThread = new Thread(new ClienteHandler(clienteSocket));
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClienteHandler implements Runnable {
        private final Socket clienteSocket;

        public ClienteHandler(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
        }

        @Override
        public void run() {
            try {
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));

                String mensagem;
                while ((mensagem = entrada.readLine()) != null) {
                    System.out.println("Mensagem recebida de " + clienteSocket + ": " + mensagem);
                    encaminharMensagem(mensagem);
                }

                entrada.close();
                clientes.remove(clienteSocket);
                clienteSocket.close();
                System.out.println("Cliente desconectado: " + clienteSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void encaminharMensagem(String mensagem) {
            for (Socket cliente : clientes) {
                try {
                    PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);
                    saida.println(mensagem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor(5000);
        servidor.iniciar();
    }
}

