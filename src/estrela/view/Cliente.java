package estrela.view;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
    String HOST;
    int PORTA;
    public Cliente(String HOST, int PORTA){
        this.HOST = HOST;
        this.PORTA = PORTA;
        iniciar();
    }

    public void iniciar(){
        try (Socket socket = new Socket(HOST, PORTA)) {
            System.out.println("Conectado ao servidor");

            Scanner entrada = new Scanner(new InputStreamReader(socket.getInputStream()));
            String identificador = entrada.nextLine();
            System.out.println("Seu identificador é: " + identificador);

            Thread receberThread = new Thread(new ReceberMensagens(socket));
            receberThread.start();

            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String mensagem = "", destinatario = "";
            do {
                System.out.println("Digite ao destinatário (ou '0' para broadcast)");
                destinatario = scanner.nextLine();
                System.out.println("Digite a mensagem (ou 'fim' para sair): ");
                mensagem = scanner.nextLine();
                mensagem = destinatario + ":" + mensagem;
                saida.println(mensagem);
            } while (!mensagem.equalsIgnoreCase("fim"));

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ReceberMensagens implements Runnable {
        private final Socket socket;

        public ReceberMensagens(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                Scanner entrada = new Scanner(new InputStreamReader(socket.getInputStream()));

                while (entrada.hasNextLine()) {
                    String mensagem = entrada.nextLine();
                    System.out.println("Mensagem recebida do servidor: " + mensagem);
                }

                entrada.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
