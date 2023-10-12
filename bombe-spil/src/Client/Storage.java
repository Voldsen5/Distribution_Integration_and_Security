package Client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Storage {
    private static String userID = "-1";

    public static void setUserID(String userID) {
        Storage.userID = userID;
    }

    public static String getUserID() {
        return userID;
    }

    public static void load() {
        File file = new File("_ZERO/src/Client/Userdata");
        //System.out.println(file.getAbsolutePath());
        try (Scanner scanner = new Scanner(file)) {
            userID = scanner.nextLine();
        } catch (FileNotFoundException | NoSuchElementException e) {
            System.out.println("Could not Read File");
        }
    }

    public static void save() {
        File file = new File("_ZERO/src/Client/Userdata");
        //System.out.println(file.getAbsolutePath());
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(userID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
