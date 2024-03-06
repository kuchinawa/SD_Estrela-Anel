package teste;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ClienteServidor implements Runnable {
    private final int portaCliente;
    private final int portaServidor;
    private boolean conexao = true;
    private ServerSocket serverSocket;
    private Socket socket;
    PrintStream saida;

    public ClienteServidor(int portaCliente, int portaServidor) {
        this.portaCliente = portaCliente;
        this.portaServidor = portaServidor;
    }

    public void run() {
        rodarServidor();
        try {
            rodarCliente();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void rodarServidor() {
        Thread servidorThread = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(portaServidor);
                System.out.println("Servidor rodando na porta " + serverSocket.getLocalPort());
                System.out.println("Aguardando conexão do cliente...");

                // Atraso de 2000 milissegundos (2 segundos)
                Thread.sleep(2000);

                while (true) {
                    Socket cliente = serverSocket.accept();
                    System.out.println("Conexão com o cliente " + cliente.getInetAddress().getHostAddress() + "/" + cliente.getInetAddress().getHostName());
                    Thread t = new Thread(() -> {
                        try {
                            Scanner s = new Scanner(cliente.getInputStream());
                            // Exibe mensagem no console
                            while (conexao) {
                                String mensagemRecebida = s.nextLine();
                                if (mensagemRecebida.equalsIgnoreCase("fim"))
                                    conexao = false;
                                else
                                    System.out.println("Cliente " + " -> " + mensagemRecebida); // Mostra o contador

                                saida.println(mensagemRecebida);
                            }
                            // Finaliza scanner e socket
                            s.close();
                            System.out.println("Fim do cliente " + cliente.getInetAddress().getHostAddress());
                            cliente.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    t.start();
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        servidorThread.start();
    }

    private void rodarCliente() throws IOException {
        saida = new PrintStream(socket.getOutputStream());
        Thread clienteThread = new Thread(() -> {
            while (true) {
                try {
                    socket = new Socket("127.0.0.1", portaCliente);
                    System.out.println("O cliente conectou ao servidor");
                    // Prepara para leitura do teclado
                    Scanner teclado = new Scanner(System.in);
                    // Cria objeto para enviar a mensagem ao servidor

                    // Envia mensagem ao servidor
                    String mensagem;
                    while (conexao) {
                        System.out.println("Digite uma mensagem: ");
                        mensagem = teclado.nextLine();
                        if (mensagem.equalsIgnoreCase("fim"))
                            conexao = false;
                        else
                            System.out.println("Cliente "+ " -> " + mensagem); // Mostra o contador
                        saida.println(mensagem);
                    }
                    saida.close();
                    teclado.close();
                    socket.close();
                    System.out.println("Cliente finaliza conexão.");
                    break;
                } catch (IOException e) {
                    System.out.println("Falha ao conectar ao servidor. Tentando novamente em 10 segundos...");
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        clienteThread.start();
    }

    public static void main(String[] args) {
        int portaCliente = 5001 ; // Incrementa a porta do cliente
        int portaServidor = 5002 ; // Incrementa a porta do servidor

        ClienteServidor clienteServidor = new ClienteServidor(portaCliente, portaServidor);
        clienteServidor.run();
    }
}
