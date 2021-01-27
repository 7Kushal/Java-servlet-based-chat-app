import java.net.*;

import java.io.*;

class Server {

    ServerSocket server; // our server app object
    Socket socket;// client app object

    BufferedReader br;
    PrintWriter out;

    public Server() {
        try {
            server = new ServerSocket(8888);
            System.out.println("Connection esatablished \nWaiting for Client to connect");
            socket = server.accept();
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
            startWriting();
            startReading();
        } catch (Exception e) {

            System.out.println(e);
        }

    }

    public void startReading() {
        Runnable r1 = () -> {// making a thread to asynchronously read data from ServerSocket
            System.out.println("Reader started...");

            try {
                while (true) {
                    if (socket.isClosed())
                        break;
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Client exiting...");
                        socket.close();
                        break;
                    }
                    System.out.println("Client:  " + msg);
                }
            } catch (Exception e) {
                // System.out.println("Connection closed by client...");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        Runnable r2 = () -> {
            System.out.println("Writer started...");

            try {
                while (true) {
                    BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
                    // the message we want send to the socket(Client)
                    String content = br2.readLine();
                    out.println(content);
                    out.flush();
                    if (socket.isClosed() || content.equals("exit")) {
                        System.out.println("Connection closed...bye");
                        break;
                    }

                }
            } catch (Exception e) {

            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("Server is running");
        new Server();
    }
}