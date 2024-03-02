package udp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ImplServidor {
    ServerSocket servidor;
    Socket cliente;
    ObjectInputStream entrada;
    ObjectOutputStream saida;
    int porta;
    public ImplServidor(int p) {
        this.porta = p;
        this.rodar();
    }
    private void rodar() {
        try {
            servidor = new ServerSocket(porta);
            System.out.println("Servidor rodando em " +
                    InetAddress.getLocalHost().getHostName() +
                    ":" +
                    servidor.getLocalPort());
            cliente = servidor.accept();
            System.out.println("Nova conexão com " +
                    cliente.getInetAddress().getHostAddress());
            saida = new ObjectOutputStream(cliente.getOutputStream());
            entrada = new ObjectInputStream(cliente.getInputStream());
            Produto p = (Produto) entrada.readObject();
            System.out.println("Produto recebido: " +
                    p.getCodigo() +
                    ":" +
                    p.getNome());
            entrada.close();
            saida.close();
            cliente.close();
            servidor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}