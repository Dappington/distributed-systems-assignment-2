import java.io.*;
import java.net.*;
import java.util.ArrayList;

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

    public ArrayList<WeatherUpdate> readFile(String file_path) throws IOException {
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
            updates.add(update);
        }
        return updates;
    }
}