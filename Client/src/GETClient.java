import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.Gson;

public class GETClient {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;
    private static String target_path;

    public static void main(String[] args) throws IOException {
        String[] split_addr = args[0].split(":");
        connectAggregator(split_addr[0], Integer.parseInt(split_addr[1]));
        if (args.length == 2){
            target_path = "/" + args[1];
        }
        else {
            target_path = "/";
        }
        String outgoing_http = "GET %s HTTP/1.1"; //TODO make this compliant

        outgoing_http = String.format(outgoing_http, target_path);
        out.println(outgoing_http);

        String res_header = in.readLine();
        if (Objects.equals(res_header, "HTTP/1.1 200 OK")){
            //TODO read and display the return value(s)
        }
        else {
            //TODO retry (or give up)
        }
    }

    public static void connectAggregator(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
    }

}
