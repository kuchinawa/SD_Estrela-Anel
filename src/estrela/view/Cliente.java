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

            Thread receberThread = new Thread(new ReceberMensagens(socket));
            receberThread.start();

            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            String mensagem;
            do {
                System.out.print("Digite a mensagem: ");
                mensagem = scanner.nextLine();
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

