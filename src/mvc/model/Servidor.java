package mvc.model;

import mvc.view.Cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor implements Runnable {
    private final int porta;
    private boolean conexao = true;
    private ServerSocket serverSocket;
    Cliente cliente;

    public Servidor(int porta, Cliente cliente) {
        this.porta = porta;
        this.cliente = cliente;
    }

    @Override
    public void run() {
        rodarServidor();
    }

    private void rodarServidor() {
        Thread servidorThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(porta);
                System.out.println("Servidor rodando na porta " + serverSocket.getLocalPort());
                System.out.println("Aguardando conexão do cliente...");

                while (true) {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("Conexão com o cliente " + clienteSocket.getInetAddress().getHostAddress() + "/" + clienteSocket.getInetAddress().getHostName());
                    Thread t = new Thread(() -> {
                        try {
                            Scanner s = new Scanner(clienteSocket.getInputStream());
                            // Exibe mensagem no console
                            while (conexao) {
                                String mensagemRecebida = s.nextLine();
                                String[] partesMensagem = mensagemRecebida.split(":");
                                if (mensagemRecebida.equalsIgnoreCase("fim"))
                                    conexao = false;
                                else {
                                    int origem = Integer.parseInt(partesMensagem[0]);
                                    int destino = Integer.parseInt(partesMensagem[1]);
                                    int proximaPorta = cliente.getPortaEuSouCliente();
                                    if(this.porta == destino) {
                                        System.out.println("Mensagem recebida no servidor: " + mensagemRecebida);
                                    } else {
                                        System.out.println("Encaminhando mensagem para frente");
                                        encaminharMensagem(mensagemRecebida);
                                    }
                                }
                            }
                            // Finaliza scanner e socket
                            s.close();
                            System.out.println("Fim do cliente " + clienteSocket.getInetAddress().getHostAddress());
                            clienteSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    t.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
    }

    private void encaminharMensagem(String mensagemRecebida) {
        //int portaDestino = Integer.parseInt(partesMensagem[0]);
        //String mensagem = partesMensagem[1];

        // Implemente aqui o código para encaminhar a mensagem para o destino desejado
        cliente.enviarMensagem(mensagemRecebida);
    }
}
