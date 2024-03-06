package mvc.control;

import mvc.model.Servidor;
import mvc.view.Cliente;

import java.util.Scanner;

public class ClienteServidor {
    public static void main(String[] args) {
        int porta1 = 5001;
        int porta2 = 5002;
        int porta3 = 5003;
        int porta4 = 5004;

        // Inicia servidor e cliente em threads separadas
       // iniciarServidorCliente(porta1, porta2);
       // iniciarServidorCliente(porta2, porta3);
        iniciarServidorCliente(porta3, porta4);
        //iniciarServidorCliente(porta4, porta1);

        // Thread para lidar com entrada de mensagens do usuário
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Digite a porta de destino (5001 a 5004): ");
            int portaDestino = scanner.nextInt();
            scanner.nextLine(); // Limpa o buffer

            System.out.println("Digite a mensagem: ");
            String mensagem = scanner.nextLine();
            scanner.nextLine();

            encaminharMensagem(portaDestino, mensagem);
        }).start();
    }

    private static void iniciarServidorCliente(int portaServidor, int portaCliente) {
        Thread servidorThread = new Thread(() -> {
            Cliente cliente = new Cliente(portaCliente);
            cliente.run();

            Servidor servidor = new Servidor(portaServidor, cliente);
            servidor.run(); // Inicia o servidor
        });
        servidorThread.start();
    }

    private static void encaminharMensagem(int portaDestino, String mensagem) {
        switch (portaDestino) {
            case 5001:
                enviarMensagem(portaDestino, mensagem);
                break;
            case 5002:
                enviarMensagem(portaDestino, mensagem);
                break;
            case 5003:
                enviarMensagem(portaDestino, mensagem);
                break;
            case 5004:
                enviarMensagem(portaDestino, mensagem);
                break;
            default:
                System.out.println("Porta de destino inválida.");
        }
    }

    private static void enviarMensagem(int portaDestino, String mensagem) {
        // Implemente aqui o código para enviar a mensagem para o destino desejado
        System.out.println("Mensagem enviada para a porta " + portaDestino + ": " + mensagem);
    }
}
