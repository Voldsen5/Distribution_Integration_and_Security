package Opgave_19;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class testsql {
        public static void main(String[] args) {
        // TODO Auto-generated method stub
            try {

                System.out.println("Program startet");
                Connection minConnection;
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost;" +
                                "databaseName=lektion14;user=sa;password=reallyStrongPwd123;");

                Statement stmt = minConnection.createStatement();
                BufferedReader indFraBruger = new BufferedReader(new InputStreamReader(System.in));

                stmt.execute("set transaction isolation level snapshot");
                minConnection.setAutoCommit(false);

                System.out.println("Skriv kontnr:");
                String kontoNr = indFraBruger.readLine();
                ResultSet res = stmt.executeQuery("select * from konto where kontonr =" + kontoNr);
                if (!res.next()){
                    throw new RuntimeException("Konto-nr findes ikke");
                }
                System.out.println("Konto " + res.getInt(1) + " har saldo " + res.getInt(2) + " og ejer " + res.getString(3) );

                System.out.println("Skriv ny Saldo på konto:");
                String beloeb = indFraBruger.readLine();

                Thread.sleep(5000);

                stmt.execute("update konto\n" + "set saldo =" + beloeb + "\n" + "where kontonr = " + kontoNr);

                res = stmt.executeQuery("select * from konto");
                while (res.next()) {
                    System.out.println("Konto " + res.getInt(1) +
                            " har saldo " + res.getInt(2) + " og ejer " + res.getString(3) );
                }

                minConnection.commit();

                if (stmt != null)
                    stmt.close();
                if (minConnection != null)
                    minConnection.close();
            }
            catch (Exception e) {
                System.out.print("fejl: "+e.getMessage());
            }
        }
    }
