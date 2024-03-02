package udp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ImplCliente {
    Socket cliente;
    ObjectInputStream entrada;
    ObjectOutputStream saida;
    String ip;
    int porta;
    public ImplCliente(String i, int p) {
        this.ip = i;
        this.porta = p;
        this.rodar();
    }
    private void rodar() {
        Produto p = null;
        try {
            cliente = new Socket(ip, porta);
            System.out.println("Conectado!");
            saida = new ObjectOutputStream(cliente.getOutputStream());
            entrada = new ObjectInputStream(cliente.getInputStream());
            p = new Produto(123, "TV Smart");
            saida.writeObject(p);
            saida.flush();
       //     entrada.close();
        //    saida.close();
         //   cliente.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
