package anel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    ServerSocket socketServidor;
    Socket cliente;
    int porta;
    public Servidor(int porta) {
        this.porta = porta;
        this.rodar();
    }
    private void rodar() {
        try {
            socketServidor = new ServerSocket(porta);
            System.out.println("Servidor rodando na porta " +
                    socketServidor.getLocalPort());
            System.out.println("HostAddress = " +
                    InetAddress.getLocalHost().getHostAddress());
            System.out.println("HostName = " +
                    InetAddress.getLocalHost().getHostName());
            System.out.println("Aguardando conexão do cliente...");
            while (true) {
                cliente = socketServidor.accept();
                ImplServidor servidor = new ImplServidor(cliente);
                Thread t = new Thread(servidor);
                ImplServidor.cont++;
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception {
        new Servidor(54321);
    }
}
