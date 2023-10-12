package thread_Opgave;

public class Demo {
    public static void main(String[] args) {
        Common cm = new Common();
        Udskriv ud = new Udskriv(cm);
        Indskriv id = new Indskriv(cm);
        ud.start();
        id.start();
    }
}
