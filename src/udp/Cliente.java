package udp;

/*
import java.io.*;
import java.net.*;

public class Cliente implements Runnable {
    private ClienteServidor cs;

    public Cliente(ClienteServidor cs) {
        this.cs = cs;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", cs.getPortaServidor());
            System.out.println("Conectado ao servidor em " + cs.getPortaServidor());

            enviarMensagem(socket);

            socket.close();
        } catch (IOException e) {
            System.err.println("Falha na conexão com o servidor. Tentando novamente em 10 segundos...");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void enviarMensagem(Socket socket) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite a mensagem para enviar ao servidor: ");
            String mensagem = reader.readLine();
            out.writeObject(mensagem);
            out.flush(); // Certifique-se de que todos os dados são enviados
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
8/
 */