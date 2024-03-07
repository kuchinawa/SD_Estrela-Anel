package estrela.view;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORTA = 5000;

        try (Socket socket = new Socket(HOST, PORTA)) {
            System.out.println("Conectado ao servidor");

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String identificador = entrada.readLine(); // Receber identificador atribuído pelo servidor
            System.out.println("Seu identificador é: " + identificador);

            Thread receberThread = new Thread(new ReceberMensagens(socket));
            receberThread.start();

            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String mensagem="", destinatario="";
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
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String mensagem;
                while ((mensagem = entrada.readLine()) != null) {
                    System.out.println("Mensagem recebida do servidor: " + mensagem);
                }

                entrada.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

