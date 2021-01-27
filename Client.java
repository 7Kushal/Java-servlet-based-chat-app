import java.net.Socket;
import java.io.*;

public class Client {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending Request....");
            socket = new Socket("127.0.0.1", 8888);
            System.out.println("Connection Eastablished...");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());
            startWriting();
            startReading();
        } catch (Exception e) {
            System.out.println("Server connection is in off state");
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
                        System.out.println("Connection closed by Server");
                        socket.close();
                        break;
                    }
                    System.out.println("Server:  " + msg);
                }
            } catch (Exception e) {

                // System.out.println("Connection closed by server");
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
                        System.out.println("Connection closed....bye");
                        break;
                    }

                }
            } catch (Exception e) {

                // e.printStackTrace();

            }
        };
        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("This is a client client side running");
        new Client();
    }
}
