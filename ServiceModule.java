import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
import java.sql.*;

class QueryRunner implements Runnable {

    /// database details to login
    static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String DB_URL = "jdbc:postgresql://localhost:5433/postgres";
    static final String USER = "postgres";
    static final String PASS = "iitropar";

    // Declare socket for client access
    protected Socket socketConnection;

    public QueryRunner(Socket clientSocket) {
        this.socketConnection = clientSocket;
    }

    public void run() {
        try {
            // Reading data from client
            InputStreamReader inputStream = new InputStreamReader(socketConnection
                    .getInputStream());
            BufferedReader bufferedInput = new BufferedReader(inputStream);
            OutputStreamWriter outputStream = new OutputStreamWriter(socketConnection
                    .getOutputStream());
            BufferedWriter bufferedOutput = new BufferedWriter(outputStream);
            PrintWriter printWriter = new PrintWriter(bufferedOutput, true);
            String clientCommand = "";
            String responseQuery = "";
            // Read client query from the socket endpoint
            clientCommand = bufferedInput.readLine();
            while (!clientCommand.equals("#")) {

                /////////// received data from the client file is printing from here
                /////////// ///////////////

                // System.out.println("Recieved data <" + clientCommand + "> from client : "
                //         + socketConnection.getRemoteSocketAddress().toString());

                // ******************************************

                StringTokenizer tokenizer = new StringTokenizer(clientCommand);

                String[] temp = new String[10000];
                int i = 0;

                while (tokenizer.hasMoreTokens()) {

                    // System.out.println(tokenizer.nextToken());
                    temp[i] = tokenizer.nextToken();
                    i++;

                }
                /////////////// extracting tokens from the string
                /////////////// ///////////////////////////////

                int no_of_psng = Integer.parseInt(temp[0]);
                String[] names_of = new String[no_of_psng];

                for (int j = 1; j < no_of_psng; j++) {

                    String faltu = temp[j];
                    temp[j] = faltu.substring(0, faltu.length() - 1);
                    // System.out.println(temp[j]);
                }

                String names = "";
                names += "\'";
                names += '{';

                for (int j = 1; j < no_of_psng; j++) {
                    names += "\"";
                    names += temp[j];
                    names += "\"";

                    names += ',';
                    names_of[j - 1] = temp[j];

                }
                names += "\"";
                names += temp[no_of_psng];
                names += "\"";
                names_of[no_of_psng - 1] = temp[no_of_psng];

                names += '}';
                names += "\'";
                // System.out.print(names);

                int x = no_of_psng;

                /////////// making connection to database////////////////
                Connection c = null;
                Statement stmt = null;

                while (true) {
                    int train_count = 0;
                    int occupied_seats = 0;
                    int total_seats = 0;

                    ////////////////////////////////////// Generating queries for database
                    ////////////////////////////////////// ///////////////////////////
                    String train_status1 = "select count(*) from trains where trains.doj=" + "\'" + temp[x + 2] + "\'"
                            + " and trains.train_no=" + temp[x + 1];

                    // System.out.println(train_status1);
                    String train_status2 = "select sum(no_of_psng) from tickets  where doj=" + "\'" + temp[x + 2] + "\'"
                            + " and train_no=" + temp[x + 1] + " and type=" + "\'" + temp[x + 3] + "\'";
                    // System.out.println(train_status2);
                    String train_status3 = "";
                    if (temp[x + 3].equals("AC")) {
                        train_status3 = "select   ac_coaches from trains where trains.doj=" + "\'" + temp[x + 2] + "\'"
                                + " and trains.train_no=" + temp[x + 1];
                    } else {
                        train_status3 = "select sl_coaches from trains where trains.doj=" + "\'" + temp[x + 2] + "\'"
                                + " and trains.train_no=" + temp[x + 1];
                    }

                    // System.out.println(train_status3);

                    //////////////////// calculating train count/////////////////////////

                    while (true) {
                        try {

                            c = DriverManager.getConnection(DB_URL, USER, PASS);
                            stmt = c.createStatement();
                            ResultSet rs = stmt.executeQuery(train_status1);
                            rs.next();
                            train_count = rs.getInt(1);
                            // System.out.println(train_count);

                            stmt.close();
                            c.close();
                            rs.close();
                            break;

                        } catch (Exception e) {
                            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            // System.exit(0);
                        }

                    }

                    /////////////////////// calculating occupied seats ////////////////////

                    while (true) {
                        try {
                            c = DriverManager.getConnection(DB_URL, USER, PASS);
                            stmt = c.createStatement();
                            ResultSet rs = stmt.executeQuery(train_status2);
                            if (rs.next()) { // rs.next();
                                occupied_seats = rs.getInt(1);
                            }

                            else {
                                occupied_seats = 0;
                            }

                            stmt.close();
                            c.close();
                            rs.close();
                            break;

                        } catch (Exception e) {
                            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            // System.exit(0);
                        }
                    }

                    //////////////// calculating total seats trains///////////////////////

                    while (true) {

                        try {

                            c = DriverManager.getConnection(DB_URL, USER, PASS);
                            stmt = c.createStatement();
                            ResultSet rs = stmt.executeQuery(train_status3);

                            if (rs.next()) { // rs.next();
                                total_seats = rs.getInt(1);
                            }

                            else {
                                total_seats = 0;
                            }

                            stmt.close();
                            c.close();
                            rs.close();
                            break;

                        } catch (Exception e) {
                            // System.err.println(e.getClass().getName() + ": " + e.getMessage());
                            // System.exit(0);
                        }
                    }

                    if (temp[x + 3].equals("AC")) {

                        total_seats *= 18;
                    }

                    else {
                        total_seats *= 24;
                    }

                    // System.out.print("\n");
                    // System.out.print(train_count);
                    // System.out.print("\n");
                    // System.out.print(total_seats);
                    // System.out.print("\n");
                    // System.out.println(occupied_seats);

                    /////////// conditions for tickets booking //////////////////

                    if (train_count == 0) {
                        // System.out.println("*******Train Not Availbale ******\n");
                        responseQuery = "*******Train Not Available******";
                        printWriter.println(responseQuery);
                        printWriter.println("\n");
                        break;

                    } else if (total_seats - occupied_seats < no_of_psng) {
                        // System.out.println("*******Seat Not Availbale ****** \n");
                        responseQuery = "*******Seat Not Availbale ******";
                        printWriter.println(responseQuery);
                        printWriter.println("\n");
                        break;

                    } 
                    else {

                        ////////////////// booking tickets ///////////////////



                    //  while(true){

                        try {

                            Connection calal = null;
                            Statement stmtt = null;
                            // System.out.println("conecting to database");
                            calal = DriverManager
                                    .getConnection(DB_URL, USER, PASS);
                            calal.setAutoCommit(false);
                            // c.setTransactionIsolation(8);
                            CallableStatement cst = calal.prepareCall("{CALL bookkrlo(?,?,?,?,?,?)}");
                            cst.registerOutParameter(6, Types.INTEGER);
                            cst.setInt(1, no_of_psng);
                            cst.setInt(2, Integer.parseInt(temp[x + 1]));
                            cst.setString(3, temp[x + 2]);
                            // System.out.println(temp[x+2]);
                            // System.out.println(temp[x+3]);

                            // phone=c.createArrayOf("varchar", names_of);
                            Array names1 = calal.createArrayOf("varchar", names_of);
                            cst.setArray(4, names1);
                            cst.setString(5, temp[x + 3]);
                            // System.out.println("Query executed successfully.");
                            cst.execute();
                            // System.out.println("Function executed successfully");
                            int pnr = cst.getInt(6);
                            // System.out.println("pnr is");
                            // System.out.println(pnr);
                            calal.commit();
                            cst.close();
                            calal.close();

                            /// accesing the seat details for the booked tickets ////////////////

                            if (pnr != 0) {

                                while(true){

                                    try {
                                        
                                        c = DriverManager.getConnection(DB_URL, USER, PASS);
                                        stmt = c.createStatement();
                                        String psng_detail = "select seat from tickets t  where  t.pnr=" + pnr;
                                        // System.out.println(psng_detail);
                                        ResultSet rs = stmt.executeQuery(psng_detail);
                                        rs.next();
                                        String prop_name = rs.getString(1);
                                        // System.out.println(psng_detail);
                                        responseQuery = "*******Tickets booked******";
                                        printWriter.println(responseQuery);
                                        printWriter.println(prop_name);
                                        printWriter.println("\n");
                                        // System.out.println("*******Tickets booked******");
                                        // System.out.println();
        
                                        stmt.close();
                                        c.close();
                                        rs.close();
                                        break;
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }

                                }

                                break;

                              
                            }

                            // break;

                        } catch (Exception e) {
                            // System.err.println(e.getClass().getName() + ": " + e.getMessage());

                            // System.exit(0);
                        }
                    //  }

                    }
                }
                //////////// extracting next line /////////
                clientCommand = bufferedInput.readLine();
            }
            inputStream.close();
            bufferedInput.close();
            outputStream.close();
            bufferedOutput.close();
            printWriter.close();
            socketConnection.close();
        } catch (IOException e) {
            return;
        }
    }
}

/**
 * Main Class to controll the program flow
 */
public class ServiceModule {
    // Server listens to port
    static int serverPort = 7008;
    // Max no of parallel requests the server can process
    static int numServerCores = 32;

    // ------------ Main----------------------
    public static void main(String[] args) throws IOException {
        // Creating a thread pool

        long total_time = 0;

        ExecutorService executorService = Executors.newFixedThreadPool(numServerCores);

        try (// Creating a server socket to listen for clients
                ServerSocket serverSocket = new ServerSocket(serverPort)) {
            Socket socketConnection = null;

            // Always-ON server
            while (true) {

                System.out.println("Listening port : " + serverPort
                        + "\nWaiting for clients...");
                socketConnection = serverSocket.accept(); // Accept a connection from a client
                System.out.println("Accepted client :"
                        + socketConnection.getRemoteSocketAddress().toString()
                        + "\n");

                /// accepted the client file need to calculate the time form here

                // long start = System.currentTimeMillis();
                // Create a runnable task
                Runnable runnableTask = new QueryRunner(socketConnection);
                // Submit task for execution
                executorService.submit(runnableTask);

                // long end = System.currentTimeMillis();
                // total_time += end - start;
                // System.out.println();
                // System.out.println("Time taken to execute all queries " + total_time + "ms");
                // System.out.println();

            }

        } catch (Exception e) {

            System.out.println("Server error");

        }

        // ending time

    }
}