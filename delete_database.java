import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import javax.sound.sampled.SourceDataLine;


public class delete_database {

 static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5433/postgres";

    static final String USER = "postgres";
    static final String PASS = "iitropar";

    public static void main(String[] args) {

        String query1 = "drop table trains";
        String query2 = "drop table ac_coach_comp";
        String query3 = "drop table sl_coach_comp";
        String query4 = " drop table tickets";
        String query5 = "drop table train_avail";
       
        Connection c = null;
        Statement stmt = null;

        try {

            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query1);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query2);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query3);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query4);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
       

        try {
            c = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = c.createStatement();

            stmt.executeUpdate(query5);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Databse deleted Successfully.");

    }

}

    

