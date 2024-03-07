package estrela.model;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private final int porta;
    private final Map<String, Socket> clientes; // Usando Map para associar identificadores aos clientes

    private int contadorClientes; // Para contar o número de clientes conectados

    public Servidor(int porta) {
        this.porta = porta;
        this.clientes = new HashMap<>();
        this.contadorClientes = 0;
    }

    public void iniciar() {
        try (ServerSocket serverSocket = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                Socket clienteSocket = serverSocket.accept();
                contadorClientes++;
                String identificador = "P" + contadorClientes;
                clientes.put(identificador, clienteSocket);
                System.out.println("Novo cliente conectado: " + identificador);

                // Envia o identificador para o cliente
                PrintWriter saida = new PrintWriter(clienteSocket.getOutputStream(), true);
                saida.println(identificador);

                Thread clienteThread = new Thread(new ClienteHandler(clienteSocket, identificador));
                clienteThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClienteHandler implements Runnable {
        private final Socket clienteSocket;
        private final String identificador;

        public ClienteHandler(Socket clienteSocket, String identificador) {
            this.clienteSocket = clienteSocket;
            this.identificador = identificador;
        }

        @Override
        public void run() {
            try {
                boolean conexao = true;
                BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
                while (conexao) {


                    String mensagem = entrada.readLine();
                    String partes[] = mensagem.split(":");
                    if (partes[1].equals("fim")) {
                        conexao=false;
                    }
                    if(partes[0].equals("0")) {
                        System.out.println("Mensagem recebida de " + identificador + ": " + partes[1]);
                        encaminharMensagemBroadcast(identificador, partes[1], partes[0]);
                    } else {
                        System.out.println("Mensagem recebida de " + identificador + ": " + partes[1]);
                        encaminharMensagem(identificador, partes[1], partes[0]);
                    }
                }

                entrada.close();
                clientes.remove(identificador);
                clienteSocket.close();
                System.out.println("Cliente desconectado: " + identificador);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void encaminharMensagem(String remetente, String mensagem, String destinatarioMsg) {
            boolean encontrou = false;
            for (Map.Entry<String, Socket> entry : clientes.entrySet()) {
                String destinatario = entry.getKey();
                if (!destinatario.equals(remetente)) {
                    if(destinatario.equals(destinatarioMsg)) {
                        try {
                            PrintWriter saida = new PrintWriter(entry.getValue().getOutputStream(), true);
                            saida.println(remetente + ": " + mensagem);
                            encontrou = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(encontrou == false){
                System.out.println("Destinatario não encontrade");
            }
        }
        private void encaminharMensagemBroadcast(String remetente, String mensagem, String destinatarioMsg) {
            for (Map.Entry<String, Socket> entry : clientes.entrySet()) {
                String destinatario = entry.getKey();
                if (!destinatario.equals(remetente)) { // Evita enviar a mensagem de volta para o remetente
                    try {
                        PrintWriter saida = new PrintWriter(entry.getValue().getOutputStream(), true);
                        saida.println(remetente + ": " + mensagem);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Servidor servidor = new Servidor(5000);
        servidor.iniciar();
    }
}
