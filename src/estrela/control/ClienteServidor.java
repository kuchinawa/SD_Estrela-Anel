package estrela.control;

import estrela.model.Servidor;
import estrela.view.Cliente;

public class ClienteServidor {
    public static void main(String[] args) {
     //  Servidor servidor = new Servidor(5000);

        Cliente cliente = new Cliente("localhost", 5000);
    }
}
