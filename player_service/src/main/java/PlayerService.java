import com.google.gson.Gson;

public class PlayerService {

    private Gson gson = new Gson();

    public static void main(String[] args) {
        new PlayerService().run();
    }

    private void run() {
        System.out.println("PlayerService is starting");
    }


}
