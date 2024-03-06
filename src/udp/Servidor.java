package udp;
/*
import java.io.*;
import java.net.*;

public class Servidor implements Runnable {
    private ClienteServidor cs;
    private static boolean ativo = false;

    public Servidor(ClienteServidor cs) {
        this.cs = cs;
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(cs.getPortaServidor());
            ativo = true;
            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Conexão aceita em " + cs.getPortaServidor());

                // Lê a mensagem do cliente
                BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                String mensagem = in.readLine();
                System.out.println("Mensagem recebida do cliente: " + mensagem);

                // Verifica se a mensagem é para este servidor
                if (cs.getPortaServidor() == cs.getProximaPortaServidor()) {
                    System.out.println("O servidor " + cs.getProximaPortaServidor() + " te mandou uma mensagem: " + mensagem);
                } else {
                    System.out.println("Encaminhando mensagem para o próximo servidor...");
                    encaminharMensagem(mensagem);
                }

                cliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void encaminharMensagem(String mensagem) {
        try {
            Socket socket = new Socket("localhost", cs.getProximaPortaServidor());
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(mensagem);
            out.flush();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isAtivo() {
        return ativo;
    }
}
*/