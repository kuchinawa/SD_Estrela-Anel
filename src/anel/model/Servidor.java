package anel.model;

import anel.view.Cliente;

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
            try {
                serverSocket = new ServerSocket(porta);
                System.out.println("Servidor rodando na porta " + serverSocket.getLocalPort());
                System.out.println("Aguardando conexão do cliente...");

                while (conexao) {
                    Socket clienteSocket = serverSocket.accept();
                    System.out.println("Conexão com o cliente " + clienteSocket.getInetAddress().getHostAddress() + "/" + clienteSocket.getInetAddress().getHostName());
                    Thread t = new Thread(() -> {
                        try {
                            Scanner s = new Scanner(clienteSocket.getInputStream());
                            while (conexao) {
                                String mensagemRecebida = s.nextLine();
                                String[] partesMensagem = mensagemRecebida.split(":");
                                if (mensagemRecebida.equalsIgnoreCase("fim")) {
                                    conexao = false;
                                } else {
                                    int origem = Integer.parseInt(partesMensagem[0]);
                                    int destino = Integer.parseInt(partesMensagem[1]);
                                    int proximoDestino = cliente.getPortaEuSouCliente();
                                    if (destino == 0){
                                        if (proximoDestino == origem) {
                                            System.out.println("Broadcast recebido do cliente "  + partesMensagem[0] + " -> " + partesMensagem[2]);
                                        }
                                        else{
                                            System.out.println("Broadcast recebido do cliente "  + partesMensagem[0] + " -> " + partesMensagem[2]);
                                            encaminharMensagem(mensagemRecebida);
                                        }
                                    } else {
                                        if (this.porta == destino) {
                                            System.out.println("Mensagem recebida de " + partesMensagem[0] + " -> " + partesMensagem[2]);
                                        }else if(this.porta != origem){
                                            System.out.println("Encaminhando mensagem para frente");
                                            encaminharMensagem(mensagemRecebida);
                                        }else {
                                            System.out.println("“Erro na entrada de dados. Tente outra vez!");
                                        }
                                    }
                                }
                            }
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
    }

    private void encaminharMensagem(String mensagemRecebida) {
        cliente.enviarMensagem(mensagemRecebida);
    }
}
