package anel.control;

import anel.model.Servidor;
import anel.view.Cliente;

public class ClienteServidor {
    public static void main(String[] args) {
        int porta1 = 5001;
        int porta2 = 5002;
        int porta3 = 5003;
        int porta4 = 5004;

     // iniciarServidorCliente(porta1, porta2);
     //   iniciarServidorCliente(porta2, porta3);
       //   iniciarServidorCliente(porta3, porta4);
          iniciarServidorCliente(porta4, porta1);
    }

    private static void iniciarServidorCliente(int portaServidor, int portaCliente) {
            Cliente cliente = new Cliente(portaCliente, portaServidor);
            Thread thcliente = new Thread(cliente);
            thcliente.start();

            Servidor servidor = new Servidor(portaServidor, cliente);
            Thread thserver = new Thread(servidor);
            thserver.start();

    }
}
