import java.io.*;
import java.net.*;

public class ContentServer {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) {

    }

    public void connectAggregator(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
    }

    public void readFile(String file_path){

    }
}