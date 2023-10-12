import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Opgave_7_a {
    public static void main(String[] args) throws IOException {
        URL url = new URL("https://www.valutakurser.dk");
        InputStreamReader r = new InputStreamReader(url.openStream());
        BufferedReader in = new BufferedReader(r);
        String str;
        while ((str = in.readLine()) != null) {
            if (str.contains("index_singleBanner__3OB4S")){
                String usd = str.substring(279,285);
                System.out.println("100 USD er = " + usd + " DKK");
            }
        }
        in.close();
    }
}
