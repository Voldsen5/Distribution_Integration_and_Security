package highTransparancy;
import java.sql.*;
import java.util.*;
import lowTransparancy.*;
public class ViaViewclass {
    public static void main(String[] args) {
        try {
            ArrayList<Person> liste = new ArrayList<Person>();
//	 læser viewet person via native SQL-Server driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Connection minConnection;
            minConnection = DriverManager.getConnection("jdbc:sqlserver://localhost\\SQLEXPRESS;" +
                    "databaseName=ddba;user=sa;password=torben07;");
            String sql= "select * from Person";
            Statement stmt = minConnection.createStatement();
            ResultSet res=stmt.executeQuery(sql);
            while (res.next()) {
                Person p = new Person();
                p.setNavn(res.getString("lname"));
                p.setFornavn(res.getString("fname"));
                p.setKoen(res.getString("sex"));
                p.setLoen(res.getInt("sal"));
                p.setPostnr(res.getString("zip"));
                liste.add(p);
            };
//	 udskriver indholdet af de to tabeller
            for (int i=0;i<liste.size();i++) {
                Person s =liste.get(i);
                System.out.print(s.getNavn()+ "    ");
                System.out.print(s.getFornavn() + "    ");
                System.out.print(s.getKoen() + "     ");
                System.out.print(s.getLoen() + "     ");
                System.out.println(s.getPostnr());
            }
        }
        catch (Exception e) {
            System.out.println("fejl:  "+e.getMessage());
        }
    }
}
