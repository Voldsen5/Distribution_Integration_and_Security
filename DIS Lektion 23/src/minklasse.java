import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.*;
public class minklasse {
    /**
     * @param args
     */
    public static void main(String[] args) {
        BufferedReader inLine;
        Connection minConnection;
        try {
            inLine = new BufferedReader(new
                    InputStreamReader(System.in));
            System.out.println("Indtast a eller b for delopgave a eller b");
                    String delopgave = inLine.readLine();
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            minConnection =
                    DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;" +
                            "databaseName=tempdb;user=sa;password=reallyStrongPwd123;");
            Statement stmt = minConnection.createStatement();
            if (delopgave.equals("a")) {
                // denne del bruges til delopgavene 1 - 5
                System.out.println("Indtast brugernavn");
                String s1 =inLine.readLine();
                System.out.println("Indtast password");
                String s2 =inLine.readLine();
                String s = "select * from brugere where brugerid = '" + s1 + "' and passw = '" + s2 + "'";
                System.out.println(s);
                ResultSet res=stmt.executeQuery(s);
                if (res.next()) {
                    System.out.print("Velkommen du er nu logget ind");
                }
                else
                    System.out.print("Ukorrekt logon");
                if (stmt != null)
                    stmt.close();
                if (minConnection != null)
                    minConnection.close();
            }
            else { // denne del anvendes til delopgave 6
                System.out.println("Indtast sÃ ̧gestreng");
                String s3 =inLine.readLine();
                String s = "select produktnavn,lagerantal,pris from produkt " + "where produktnavn like '" + s3 + "%'";
                System.out.println(s);
                Statement stmt2 =
                        minConnection.createStatement();
                ResultSet res2=stmt2.executeQuery(s);
                while (res2.next()) {
                    System.out.println (res2.getString(1) +
                            " " + res2.getInt(2) +
                            " " + res2.getFloat(3));
                }
                if (stmt2 != null)
                    stmt2.close();
                if (minConnection != null)
                    minConnection.close();
            }
        }
        catch (Exception e) {
            System.out.print("fejl: "+e.getMessage());
        }
    }
}