import java.util.StringTokenizer;
import java.util.*;
import java.io.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.sql.*;

// Main class
public class admin {


    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5433/postgres";

    static final String USER = "postgres";
    static final String PASS = "iitropar";

    
    //////////////////////////////////////////////////////////////////
 
    // Main driver method
    public static void main(String[] args)
            throws FileNotFoundException {

        System.out.println("Enter 1 to add train with checking  or 2  for without checking  and 3 for train data ");
        Scanner scn = new Scanner(System.in); // System.in is a standard input stream
        int a = scn.nextInt();

        if (a == 1) {

            // Declaring and initializing the string with
            // custom path of a file

            String path = "./Trainschedule.txt";

            // Creating an instance of Inputstream
            InputStream is = new FileInputStream(path);

            // Try block to check for exceptions
            try (Scanner sc = new Scanner(
                    is, StandardCharsets.UTF_8.name())) {

                // It holds true till there is single element
                // left in the object with usage of hasNext()
                // method
                while (true) {

                    String sss = sc.nextLine();
                    if (sss.equals("#")) {
                        return;
                    }
                    String[] temp = new String[4];
                    int i = 0;

                    StringTokenizer st = new StringTokenizer(sss);
                    while (st.hasMoreTokens()) {
                        temp[i] = st.nextToken();
                        // System.out.println(temp[i]);
                        i++;

                    }

                    int t_no = Integer.parseInt(temp[0]);
                    // System.out.print(temp[0]);
                    int ac = Integer.parseInt(temp[2]);
                    // System.out.println(temp[1]);
                    int sl = Integer.parseInt(temp[3]);
                    // System.out.print(temp[2]);
                    String dt = temp[1];
                    // System.out.println(temp[3]);
                    try {
                        Connection c = null;
                        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                        CallableStatement cst = con.prepareCall("{CALL release_train(?,?,?,?,?)}");
                        cst.registerOutParameter(5, Types.VARCHAR);
                        cst.setInt(1, t_no);
                        cst.setString(2, dt);
                        cst.setInt(3, ac);
                        cst.setInt(4, sl);
                        cst.execute();
                        String res = cst.getString(5);

                        // System.out.println(res);
                        if (res.equals("yes")){
                            System.out.println("Train released successfully ");
                        }
                        else{
                            System.out.println("Train cannot be released.");
                        }

                        

                        cst.close();
                    } catch (Exception e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.exit(0);
                    }

                }

            }

        }
        
        else if (a==2){




            String path = "./Trainschedule.txt";

            // Creating an instance of Inputstream
            InputStream is = new FileInputStream(path);

            // Try block to check for exceptions
            try (Scanner sc = new Scanner(
                    is, StandardCharsets.UTF_8.name())) {

                // It holds true till there is single element
                // left in the object with usage of hasNext()
                // method
                while (true) {

                    String sss = sc.nextLine();
                    if (sss.equals("#")) {
                        return;
                    }
                    String[] temp = new String[4];
                    int i = 0;

                    StringTokenizer st = new StringTokenizer(sss);
                    while (st.hasMoreTokens()) {
                        temp[i] = st.nextToken();
                        // System.out.println(temp[i]);
                        i++;

                    }

                    int t_no = Integer.parseInt(temp[0]);
                    // System.out.print(temp[0]);
                    int ac = Integer.parseInt(temp[2]);
                    // System.out.println(temp[1]);
                    int sl = Integer.parseInt(temp[3]);
                    // System.out.print(temp[2]);
                    String dt = temp[1];
                    // System.out.println(dt);
                    // System.out.println(temp[3]);
                    Connection c = null;
                    Statement stmt = null;
            
                     String    train_status3 = "insert into  trains values ("  +t_no + ','  + '\'' + dt  + '\'' + ',' +  ac +','  + sl + ")";  
                     try {

                        c = DriverManager.getConnection(DB_URL, USER, PASS);
                        stmt = c.createStatement();
                        System.out.println(train_status3);
                        stmt.executeUpdate(train_status3);
                        stmt.close();
                        c.close();
                    } catch (Exception e) {
                        System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.exit(0);
                    }
                     
                }
            }

            
            
            
        }else if (a==3) {

            String path = "/home/oem/Desktop/RAILWAY_RESERVATION_SYSTEM_PROJECT/RAILWAY_RESERVATION_SYSTEM(2020CSB1060,2020CSB1061)/Multi-Thread_ClientServer/train_data.txt";

            // Creating an instance of Inputstream
            InputStream is = new FileInputStream(path);

            // Try block to check for exceptions
            try (Scanner sc = new Scanner(
                    is, StandardCharsets.UTF_8.name())) {

                // It holds true till there is single element
                // left in the object with usage of hasNext()
                // method
                while (true) {

                    String sss = sc.nextLine();
                    if (sss.equals("#")) {
                        return;
                    }
                    String[] temp = new String[2];
                    int i = 0;

                    StringTokenizer st = new StringTokenizer(sss);
                    while (st.hasMoreTokens()) {
                        temp[i] = st.nextToken();
                        // System.out.println(temp[i]);
                        i++;

                    }

                    int t_no = Integer.parseInt(temp[0]);

                    try {
                        Connection c = null;
                        Connection con = DriverManager.getConnection(DB_URL, USER, PASS);
                        CallableStatement cst = con.prepareCall("{CALL enter_train(?)}");
                        cst.setInt(1, t_no);
                        cst.execute();
                        cst.close();
                        System.out.println("train added successfully");
                    } catch (Exception e) {
                        // System.err.println(e.getClass().getName() + ": " + e.getMessage());
                        System.out.println("Train already exists");


                        // System.exit(0);
                    }

                }

            }
          

        }

    }
}
