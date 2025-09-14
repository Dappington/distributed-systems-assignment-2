import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.google.gson.Gson;

public class ContentServer {
    private static Socket socket;
    private static BufferedReader in;
    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        ArrayList<WeatherUpdate> updates_list = readFile(args[1]);
        // assuming for now that the input will take the form ip:port and that ip will be localhost
        String[] split_addr = args[0].split(":");
        connectAggregator(split_addr[0], Integer.parseInt(split_addr[1]));

        Gson gson = new Gson();
        for (WeatherUpdate update : updates_list) {
            String update_string = gson.toJson(update);
            String http_put_request = "PUT /weather.json HTTP/1.1\r\nUser-Agent: ATOMClient/1/0\r\nContent-Type: application/json\r\nContent-Length: %d\r\n\r\n" + update_string;
            http_put_request = String.format(http_put_request, update_string.getBytes(StandardCharsets.UTF_8).length);
            out.println(http_put_request);
            String res = in.readLine();
            System.out.println(res);
        }
    }

    public static void connectAggregator(String host, int port) throws IOException {
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true);
    }

    public static ArrayList<WeatherUpdate> readFile(String file_path) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(file_path));
        String line;
        ArrayList<WeatherUpdate> updates = new ArrayList<WeatherUpdate>();

        // this monstrosity should parse text files into WeatherUpdate objects so long as the source file follows the format exactly.
        while ((line = file.readLine()) != null) {
            WeatherUpdate update = new WeatherUpdate();
            String[] split_line = line.split(":");
            update.id = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.name = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.state = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.time_zone = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.lat = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.lon = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.local_date_time = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.local_date_time_full = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.air_temp = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.apparent_t = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.cloud = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.dewpt = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.press = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.rel_hum = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.wind_dir = split_line[1];
            line = file.readLine();
            split_line = line.split(":");
            update.wind_spd_kmh = Double.parseDouble(split_line[1]);
            line = file.readLine();
            split_line = line.split(":");
            update.wind_spd_kt = Double.parseDouble(split_line[1]);
//            System.out.println(update);
            updates.add(update);
        }
        return updates;
    }
}