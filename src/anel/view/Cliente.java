package anel.view;

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

    public Cliente(int portaCliente, int portaMeuServer) {
        this.portaEuSouCliente = portaCliente;
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
            Scanner teclado = new Scanner(System.in);
            saida = new PrintStream(socket.getOutputStream());


            String mensagem;
            while (conexao) {
                System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("Digite a porta de destino ou 0 para broadcast: ");
                String portaDestino = teclado.nextLine();
                System.out.println("Digite uma mensagem: ");
                mensagem = teclado.nextLine();
                mensagem = portaMeuServer + ":" + portaDestino + ":" + mensagem;
                if (mensagem.equalsIgnoreCase("fim")) {
                    conexao = false;
                } else {
                    System.out.println("Mensagem: " + mensagem);
                    enviarMensagem(mensagem);

                    System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                }
            }
            teclado.close();
            socket.close();
            System.out.println("Cliente finaliza conexão.");
        } catch (IOException e) {
            System.out.println("Falha ao conectar ao servidor. Tentando novamente em 4 segundos...");
            try {
                Thread.sleep(4000);
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
