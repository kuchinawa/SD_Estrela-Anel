package mvc.view;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente implements Runnable {
    private final int portaEuSouCliente;
    private final int portaMeuServer;
    private boolean conexao = true;
    private Socket socket;
    private PrintStream saida;

    public Cliente(int portaServidor, int portaMeuServer) {
        this.portaEuSouCliente = portaServidor;
        this.portaMeuServer = portaMeuServer;
    }

    @Override
    public void run() {
        rodarCliente();
    }

    private void rodarCliente() {
        boolean seConectou = false;
        while(!seConectou){
        try {
            socket = new Socket("localhost", portaEuSouCliente);
            System.out.println("O cliente conectou ao servidor");
            seConectou = true;
            // Prepara para leitura do teclado
            Scanner teclado = new Scanner(System.in);
            // Cria objeto para enviar a mensagem ao servidor
            saida = new PrintStream(socket.getOutputStream());
            // Cria objeto para receber a mensagem do servidor
            //Scanner entrada = new Scanner(socket.getInputStream());


            /*
            // Thread para lidar com as mensagens recebidas do servidor
            Thread threadReceberMensagens = new Thread(() -> {
                while (conexao) {
                    if (entrada.hasNextLine()) {
                        String mensagemRecebida = entrada.nextLine();
                        System.out.println("Mensagem do servidor: " + mensagemRecebida);
                    }
                }
            });
           // threadReceberMensagens.start();
            */

            // Envia mensagem ao servidor
            String mensagem;
            while (conexao) {
                System.out.println("Digite a porta de destino (5001 a 5004): ");
                String portaDestino = teclado.nextLine();
                System.out.println("Digite uma mensagem: ");
                mensagem = teclado.nextLine();
                mensagem = portaMeuServer + ":" + portaDestino + ":" + mensagem;
                if (mensagem.equalsIgnoreCase("fim")) {
                    conexao = false;
                } else {
                    System.out.println("Cliente: " + mensagem); // Mostra a mensagem enviada
                    enviarMensagem(mensagem);
                }
            }
            // Finaliza recursos
            teclado.close();
            //entrada.close();
            socket.close();
            System.out.println("Cliente finaliza conexão.");
        } catch (IOException e) {
            System.out.println("Falha ao conectar ao servidor. Tentando novamente em 4 segundos...");
            try {
                Thread.sleep(4000); // Aguarda 10 segundos antes de tentar novamente
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    }
    public int getPortaEuSouCliente() {
        return portaEuSouCliente;
    }

    public void enviarMensagem(String mensagem) {
        saida.println(mensagem);
    }
}
