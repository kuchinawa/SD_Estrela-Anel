package anel;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ImplCliente implements Runnable {
    private Socket cliente;
    private boolean conexao = true;
    private PrintStream saida;
    public ImplCliente(Socket c) {
        this.cliente = c;
    }
    public void run() {
        try {
            System.out.println("O cliente conectou ao servidor");
            // Prepara para leitura do teclado
            Scanner teclado = new Scanner(System.in);
            // Cria objeto para enviar a mensagem ao servidor
            saida = new PrintStream(cliente.getOutputStream());
            // Envia mensagem ao servidor
            String mensagem;
            while (conexao) {
                System.out.println("Digite uma mensagem: ");
                mensagem = teclado.nextLine();
                if (mensagem.equalsIgnoreCase("fim"))
                    conexao = false;
                else
                    System.out.println(mensagem);
                saida.println(mensagem);
            }
            saida.close();
            teclado.close();
            cliente.close();
            System.out.println("Cliente finaliza conexão.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}