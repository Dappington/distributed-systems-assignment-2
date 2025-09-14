import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ArrayList;
import com.google.gson.Gson;

public class AggregationServer {
    private static ServerSocket serverSocket;
    private static ArrayList<WeatherUpdate> updates_list = new ArrayList<>(); //TODO make thread safe

    public static void main(String[] args) throws IOException {
        if (args.length != 0){
            serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        }
        else {
            serverSocket = new ServerSocket(4567);
        }
        while (true){
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    public static class ClientHandler extends Thread {
        private final Socket socket;
        private PrintWriter out;
        private  BufferedReader in;

        public ClientHandler(Socket new_socket){
            System.out.println("thread spawned");
            this.socket = new_socket;
            System.out.println("socket assigned");
        }

        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String input_line;
            Gson gson = new Gson();

            input_line = in.readLine();
            if (Objects.equals(input_line, "PUT /weather.json HTTP/1.1")){
                handlePut();
            } else if (Objects.equals(input_line, "get reqeust")) { //TODO check for get requests
                handleGet(input_line);
            }
            else {
                out.println("400"); //TODO send 400 response
            }

            } catch (IOException e) {
//                e.printStackTrace();
//                this.socket.close();
                throw new RuntimeException(e);
            }
        }

        private void handlePut() throws IOException {
            String input_line;
            Gson gson = new Gson();
            while (!Objects.equals(input_line = in.readLine(), "")) {
                System.out.println(input_line);
                continue;
            }
            input_line = in.readLine();
            if (input_line == null) {
                //TODO send a 500 code
            }
            else {
                WeatherUpdate update = gson.fromJson(input_line, WeatherUpdate.class);
                updates_list.add(update); // TODO make thread safe
                out.println("200"); //TODO send 200 response
            }

        }

        private void handleGet(String request_line) throws IOException {
            Gson gson = new Gson();
            String URL = request_line.split(" ")[1];
            String http_res;
            if (updates_list.isEmpty()){
                // TODO send some code to indicate no data?
                http_res = "500";
                out.println(http_res);
            }
            else if (!Objects.equals(URL, "/")){
               String target_id = URL.replace("/", "");
                ArrayList<WeatherUpdate> list_id_subset = new ArrayList<>();
               for (WeatherUpdate update : updates_list){//TODO make thread safe
                   if (Objects.equals(update.id, target_id)){
                       list_id_subset.add(update);
                   }
               }
               String outgoing_update = gson.toJson(list_id_subset);
                http_res = "HTTP/1.1 200 OK\r\nUser-Agent: ATOMClient/1/0\r\nContent-Type: application/json\r\nContent-Length: %d\r\n\r\n" + outgoing_update;
                http_res = String.format(http_res, outgoing_update.getBytes(StandardCharsets.UTF_8).length);
                out.println(http_res);
            }
            else {
                String outgoing_update = gson.toJson(updates_list);//TODO make thread safe
                http_res = "HTTP/1.1 200 OK\r\nUser-Agent: ATOMClient/1/0\r\nContent-Type: application/json\r\nContent-Length: %d\r\n\r\n" + outgoing_update;
                http_res = String.format(http_res, outgoing_update.getBytes(StandardCharsets.UTF_8).length);
                out.println(http_res);
            }
            socket.close();
        }

    }
}

// Things I haven't implemented yet:        Lamport clocks, Thread safety, The Client
// Things I've given up on implementing:    Server persistance/crash recovery
