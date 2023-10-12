package Opgave_16;

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

                minConnection.setAutoCommit(false);

                System.out.println("Skriv kontnr, på fra-konto:");
                String fraKonto = indFraBruger.readLine();
                ResultSet res = stmt.executeQuery("select * from konto where kontonr =" + fraKonto);
                if (!res.next()){
                    throw new RuntimeException("Konto-nr findes ikke");
                }

                System.out.println("Skriv beløb, der skal overføres:");
                String beloeb = indFraBruger.readLine();
                if (res.getInt(2) < Integer.parseInt(beloeb)){
                    throw new RuntimeException("Beløb overskrider grænse");
                }

                int value1 = res.getInt(2) - Integer.parseInt(beloeb);

                System.out.println("Skriv kontnr, på til-konto:");
                String tilKonto = indFraBruger.readLine();
                ResultSet res2 = stmt.executeQuery("select * from konto where kontonr =" + tilKonto);
                if (!res2.next()){
                    throw new RuntimeException("Konto-nr findes ikke");
                }



                int value2 = res2.getInt(2) + Integer.parseInt(beloeb);

                Thread.sleep(10000);

                stmt.execute("update konto\n" + "set saldo =" + value1 + "\n" + "where kontonr = " + fraKonto);
                stmt.execute("update konto\n" + "set saldo =" + value2 + "\n" + "where kontonr = " + tilKonto);

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
